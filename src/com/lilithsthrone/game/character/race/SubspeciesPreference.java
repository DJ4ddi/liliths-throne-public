package com.lilithsthrone.game.character.race;

import com.lilithsthrone.rendering.IconCache;
import com.lilithsthrone.utils.Colour;

/**
 * @since 0.1.99
 * @version 0.1.99
 * @author tukaima
 */
public enum SubspeciesPreference {

	ZERO_NONE("off", 0) {
		@Override
		public String getIcon(String context, boolean disabled) {
			return IconCache.INSTANCE.getIcon(context, "UIElements/scale_zero.svg",
					disabled ? Colour.BASE_GREY : Colour.BASE_MAGENTA);
		}
	},
	ONE_LOW("low", 25),
	TWO_AVERAGE("average", 50),
	THREE_HIGH("high", 75),
	FOUR_ABUNDANT("abundant", 100);

	private String name;
	private int value;
	
	private SubspeciesPreference(String name, int value) {
		this.name=name;
		this.value=value;
	}
	
	public String getIcon(String context, boolean disabled) {
		return IconCache.INSTANCE.getIcon(context, "UIElements/scale_" + name().split("_")[0].toLowerCase() + ".svg",
				disabled ? Colour.BASE_GREY : Colour.BASE_GREEN);
	}
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
}