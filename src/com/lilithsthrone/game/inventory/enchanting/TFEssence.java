package com.lilithsthrone.game.inventory.enchanting;

import com.lilithsthrone.game.inventory.Rarity;
import com.lilithsthrone.game.inventory.item.AbstractItemType;
import com.lilithsthrone.game.inventory.item.ItemType;
import com.lilithsthrone.rendering.IconCache;
import com.lilithsthrone.utils.Colour;

/**
 * @since 0.1.7
 * @version 0.1.83
 * @author Innoxia
 */
public enum TFEssence {
	
	ARCANE("Arcane",
			"An exceedingly rare pure arcane essence.",
			"arcane",
			"essenceArcane",
			Colour.GENERIC_ARCANE,
			Rarity.LEGENDARY);
	

	private String name, description, descriptor, iconPath;
	private Colour iconColour;
	private Rarity rarity;

	private TFEssence(String name, String description, String descriptor, String pathName, Colour colour, Rarity rarity) {
		this.name = name;
		this.description = description;
		this.descriptor = descriptor;
		this.rarity = rarity;

		iconColour = colour;
		iconPath = "crafting/" + pathName + ".svg";
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public String getIcon(String context) {
		return IconCache.INSTANCE.getIcon(context, iconPath, iconColour);
	}
	
	public String getIconDesaturated(String context) {
		return IconCache.INSTANCE.getIcon(context, iconPath, Colour.BASE_GREY);
	}

	public Colour getColour() {
		return iconColour;
	}

	public Rarity getRarity() {
		return rarity;
	}
	
	public static AbstractItemType essenceToItem(TFEssence essence) {
		return ItemType.BOTTLED_ESSENCE_ARCANE;
	}
}
