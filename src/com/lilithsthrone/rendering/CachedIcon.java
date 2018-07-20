package com.lilithsthrone.rendering;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.Base64;
import java.util.LinkedList;

/**
 * @since 0.2.8.2
 * @version 0.2.8.2
 * @author Addi
 */
public class CachedIcon {
    LinkedList<String> usedInContext = new LinkedList<>();
    CachedIcon chainedIcon = null;

    String imageString;
    float width;
    float height;

    public void load(String context, String svg, float width, float height) {
        // Store context and size
        usedInContext.add(context);
        this.width = width;
        this.height = height;

        // Setup SVG to PNG render
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PNGTranscoder transcoder = new PNGTranscoder();
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_WIDTH, width);
        transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_HEIGHT, height);

        try {
            // Convert SVG to PNG and then to output string
            transcoder.transcode(new TranscoderInput(new StringReader(svg)), new TranscoderOutput(byteStream));
            imageString = "<img src='data:image/png;base64," + Base64.getEncoder().encodeToString(byteStream.toByteArray()) + "'/>";
        } catch (TranscoderException e) {
            // Conversion failed, use the SVG string instead
            String id = svg.substring(svg.indexOf("id="), svg.indexOf("version=\"1.1\"")).trim();
            System.out.println("Error converting SVG with " + id);
            e.printStackTrace();
            imageString = svg;
        }
    }

    /**
     * Retrieve the icon in base 64 encoded string format. The string must start with '<img src='data:image/png;base64,'.
     * @return The icon as base64 string, including the HTML <img> tag
     */
    public String getImageString() {
        return imageString;
    }

    /**
     * Removes the given context from any icon in the chain. An icon which does not have any other contexts is deleted.
     * @param context The context to remove
     * @return True if the icon is to be deleted, false otherwise
     */
    public boolean removeContext(String context) {
        usedInContext.remove(context);
        if (usedInContext.isEmpty()) return true; // Also deletes chained icons
        if (chainedIcon != null) {
            if (chainedIcon.removeContext(context)) {
                chainedIcon = null;
            }
        }
        return false;
    }

    /**
     * Adds an icon to the chain. The new icon must have the same resource path, but different context.
     * @param icon The icon to be added
     */
    public void addChainedIcon(CachedIcon icon) {
        if (chainedIcon == null)
            chainedIcon = icon;
        else
            chainedIcon.addChainedIcon(icon);
    }

    /**
     * Checks this and all chained icons for the requested context. If it is found, the associated icon is returned.
     * @param context The requested context
     * @return A CachedIcon from the chain if the context is found, null otherwise
     */
    CachedIcon getIconWithContext(String context) {
        if (usedInContext.contains(context)) return this;
        return chainedIcon == null ? null : chainedIcon.getIconWithContext(context);
    }

    /**
     * Checks this and all chained icons for the given size. If an icon with equal dimensions is found, the context is
     * added to that icon's contexts. This ignores existing context information, use {@link CachedIcon#getIconWithContext(String)} to check.
     * @param context The requested context
     * @param width The requested width
     * @param height The requested height
     * @return True if an icon with equal size is found, false otherwise
     */
    boolean addIfEqualSize(String context, float width, float height) {
        if (this.width == width && this.height == height) {
            usedInContext.add(context);
            return true;
        }
        return chainedIcon != null && chainedIcon.addIfEqualSize(context, width, height);
    }
}
