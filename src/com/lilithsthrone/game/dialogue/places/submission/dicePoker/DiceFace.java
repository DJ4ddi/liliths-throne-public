package com.lilithsthrone.game.dialogue.places.submission.dicePoker;

import com.lilithsthrone.rendering.IconCache;
import com.lilithsthrone.utils.Colour;

/**
 * @since 0.2.6
 * @version 0.2.6
 * @author Innoxia
 */
public enum DiceFace {
	
	ONE(1, "one", "&#9856;"),
	TWO(2, "two", "&#9857;"),
	THREE(3, "three", "&#9858;"),
	FOUR(4, "four", "&#9859;"),
	FIVE(5, "five", "&#9860;"),
	SIX(6, "six", "&#9861;");

	private int value;
	private String name;
	private String htmlDisplay;
	
	private DiceFace(int value, String name, String htmlDisplay) {
		this.value = value;
		this.name = name;
		this.htmlDisplay = htmlDisplay;
	}
	
	public String getIcon(String context) {
		return IconCache.INSTANCE.getIcon(context, "UIElements/dice" + value + ".svg", Colour.BASE_WHITE);
	}
	
	public int getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHtmlDisplay() {
		return htmlDisplay;
	}
}
