package com.lilithsthrone.rendering;

import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Colour;
import com.lilithsthrone.utils.Util;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @since 0.2.8.2
 * @version 0.2.8.2
 * @author Addi
 */
public enum IconCache {
    INSTANCE;

    ConcurrentHashMap<String, CachedIcon> iconMap = new ConcurrentHashMap<>();
    HashMap<WebEngine, List<String>> contextMap = new HashMap<>();

    ExecutorService loadingQueue = Executors.newSingleThreadExecutor(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    /**
     * Checks if an icon with the given id, context and dimensions is already in the cache.
     * @param context The context to search for
     * @param id The resource path to check
     * @param width The width to search for
     * @param height The height to search for
     * @return
     */
    public boolean isCached(String context, String id, float width, float height) {
        CachedIcon icon = iconMap.get(id);
        if (icon != null) {
            CachedIcon contextIcon = icon.getIconWithContext(context);
            if (contextIcon != null || icon.addIfEqualSize(context, width, height)) return true;
        }
        return false;
    }

    /**
     * Caches an SVG icon with the given dimensions and context. This is recommended for icons that never change their
     * size during runtime.
     * @param context The context of the icon usage, which is required to check for the same icon with different sizes
     * @param id The resource path of the icon (e.g. "clothing/ankle_bracelet.svg")
     * @param width The width of the icon
     * @param height The height of the icon
     */
    public void prepareIcon(String context, String id, float width, float height) {
        prepareIcon(context, id, (Colour[]) null, width, height);
    }

    /**
     * Caches a given SVG icon. Otherwise equal to {@link IconCache#prepareIcon(String, String, float, float)}.
     * @param svg The SVG string to load
     */
    public void prepareIcon(String context, String id, String svg, float width, float height) {
        if (svg.isEmpty()) throw new IllegalArgumentException("The given SVG string is empty.");
        if (!isCached(context, id, width, height)) {
            cacheIcon(context, id, width, height, svg);
        }
    }

    /**
     * Caches an SVG icon with the given colors. Otherwise equal to {@link IconCache#prepareIcon(String, String, float, float)}.
     * @param colors An array of up to three colors that are to be replaced in the SVG
     */
    public void prepareIcon(String context, String id, Colour[] colors, float width, float height) {
        String idWithColors = id + getColorId(colors);
        if (!isCached(context, idWithColors, width, height)) {
            String svg = loadIcon(id);
            svg = applyColors(svg, colors);
            cacheIcon(context, idWithColors, width, height, svg);
        }
    }

    protected void cacheIcon(String context, String id, float width, float height, String svg) {
        loadingQueue.execute(() -> {
            if (!isCached(context, id, width, height)) {
                // Create and load PNG icon
                CachedIcon icon = new CachedIcon();
                icon.load(context, svg, width, height);

                // Insert new icon at the start of the chain
                CachedIcon entry = iconMap.get(id);
                if (entry != null) {
                    icon.addChainedIcon(entry);
                }
                iconMap.put(id, icon);
            }
        });
    }

    /**
     * Attempts to access a cached icon. If it is cached within the given context, it will be returned. Otherwise, the
     * SVG string is loaded and returned. A task is then queued to check the dimensions of the SVG after it is rendered
     * and to load the icon into the cache as a PNG image.
     * @param context The context of the icon usage, which is required to check for the same icon with different sizes
     * @param id The resource path of the icon (e.g. "clothing/ankle_bracelet.svg")
     * @return An HTML <img> tag with the PNG image if cached, an SVG string otherwise, an empty string for invalid ids
     */
    public String getIcon(String context, String id) {
        return getIcon(context, id, (Colour[]) null);
    }

    /**
     * Attempts to access a cached icon. If it is not cached, the given SVG string will be returned and used for
     * rendering the PNG. Otherwise equal to {@link IconCache#getIcon(String, String)}.
     * @param svg The SVG string to use for caching
     */
    public String getIcon(String context, String id, String svg) {
        CachedIcon icon = retrieveIcon(context, id);
        if (icon != null) return icon.getImageString();

        if (svg.isEmpty()) throw new IllegalArgumentException("The given SVG string is empty.");
        return prepareIcon(context, id, svg);
    }

    /**
     * Attempts to access a cached icon with one given color. Otherwise equal to {@link IconCache#getIcon(String, String)}.
     * @param color A color that is to be replaced in the SVG
     * @return
     */
    public String getIcon(String context, String id, Colour color) {
        return getIcon(context, id, new Colour[]{color});
    }

    /**
     * Attempts to access a cached icon with up to three given colors. Otherwise equal to {@link IconCache#getIcon(String, String)}.
     * @param colors An array of colors that are to be replaced in the SVG
     */
    public String getIcon(String context, String id, Colour[] colors) {
        String idWithColors = id + getColorId(colors);
        CachedIcon icon = retrieveIcon(context, idWithColors);
        if (icon != null) return icon.getImageString();

        String svg = loadIcon(id);
        svg = applyColors(svg, colors);
        return prepareIcon(context, idWithColors, svg);
    }

    protected CachedIcon retrieveIcon(String context, String id) {
        CachedIcon icon = iconMap.get(id);
        if (icon != null) return icon.getIconWithContext(context);
        return null;
    }

    protected String loadIcon(String path) {
        if (path.isEmpty() || !path.endsWith(".svg"))
            throw new IllegalArgumentException("The given path " + path + " does not contain an SVG file.");

        // Try in the classpath first
        String svg = Util.inputStreamToString(getClass().getResourceAsStream("/com/lilithsthrone/res/" + path));

        // If the classpath doesn't have it, try the resource folder instead
        if (svg.isEmpty()) {
            try {
                svg = new String(Files.readAllBytes(Paths.get("res/" + path)));
            } catch (IOException e) {
                throw new IllegalArgumentException("The path res/" + path + " does not contain a valid SVG file.");
            }
        }

        return svg;
    }

    protected String prepareIcon(String context, String id, String svg) {
        // Abort if no SVG information is available
        if (svg.isEmpty()) return svg;

        // Replace original id with a unique id for later lookup
        String searchId = id.replaceAll("[/.]", "_") + '-' + ((int) (Math.random() * 9000) + 1000);
        String uniqueSvg = svg.replaceFirst("(?<=id=\")svg\\d+", searchId);

        // Cache SVG as PNG for subsequent calls
        Platform.runLater(() -> {
            if (Main.mainController == null) return; // We don't even have WebEngines yet, go away

            // Check in all available web engines
            WebEngine[] engines = {Main.mainController.getWebEngine(),
                    Main.mainController.getWebEngineAttributes(),
                    Main.mainController.getWebEngineButtons(),
                    Main.mainController.getWebEngineRight(),
                    Main.mainController.getWebEngineTooltip()};
            for (WebEngine e : engines) {
                try {
                    // Determine dimensions
                    JSObject bounds = (JSObject) e.executeScript("document.getElementById('" + searchId + "').getBoundingClientRect();");
                    float width = ((Number) bounds.getMember("width")).floatValue();
                    float height = ((Number) bounds.getMember("height")).floatValue();

                    // Load it if it doesn't exist at all or in this size
                    if (!isCached(context, id, width, height)) {
                        cacheIcon(context, id, width, height, uniqueSvg);
                        bindContext(e, context);
                    }
                    break;
                } catch (JSException ex) {
                    // Icon wasn't found within that engine
                    continue;
                }
            }
        });

        return uniqueSvg;
    }

    /**
     * Removes every icon belonging to a given context, except when the same icon is also used within a different context.
     * @param context The context to reset
     */
    public void resetContext(String context) {
        iconMap.entrySet().removeIf((entry) -> entry.getValue().removeContext(context));
    }

    /**
     * Notifies the cache that a given WebEngine has been resized. Every context used within that engine will be reset.
     * @param engine The WebEngine that has been resized.
     */
    public void resize(WebEngine engine) {
        if (contextMap.containsKey(engine))
            for (String context : contextMap.get(engine))
                resetContext(context);
    }

    protected void bindContext(WebEngine engine, String context) {
        List<String> contextList = contextMap.get(engine);
        if (contextList == null) {
            contextList = new LinkedList<>();
            contextList.add(context);
            contextMap.put(engine, contextList);
        } else if (!contextList.contains(context)) {
            contextList.add(context);
        }
    }

    public static String getColorId(Colour[] colors) {
        String id = "";
        if (colors != null) {
            switch (colors.length) {
                case 3:
                    id += '-' + colors[2].name();
                case 2:
                    id += '-' + colors[1].name();
                case 1:
                    id += '-' + colors[0].name();
            }
        }
        return id;
    }

    public static String applyColors(String svg, Colour[] colors) {
        if (colors != null) {
            String rv = svg;
            // TODO use Util.colourReplacement instead?
            // TODO add gradient
            switch(colors.length) {
                case 3:
                    rv = rv.replaceAll("#ffd42a", colors[2].getShades()[0])
                            .replaceAll("#ffdd55", colors[2].getShades()[1])
                            .replaceAll("#ffe680", colors[2].getShades()[2])
                            .replaceAll("#ffeeaa", colors[2].getShades()[3])
                            .replaceAll("#fff6d5", colors[2].getShades()[4]);
                case 2:
                    rv = rv.replaceAll("#ff7f2a", colors[1].getShades()[0])
                            .replaceAll("#ff9955", colors[1].getShades()[1])
                            .replaceAll("#ffb380", colors[1].getShades()[2])
                            .replaceAll("#ffccaa", colors[1].getShades()[3])
                            .replaceAll("#ffe6d5", colors[1].getShades()[4]);
                case 1:
                    rv = rv.replaceAll("#ff2a2a", colors[0].getShades()[0])
                            .replaceAll("#ff5555", colors[0].getShades()[1])
                            .replaceAll("#ff8080", colors[0].getShades()[2])
                            .replaceAll("#ffaaaa", colors[0].getShades()[3])
                            .replaceAll("#ffd5d5", colors[0].getShades()[4]);
                    return rv;
            }
        }
        return svg;
    }
}