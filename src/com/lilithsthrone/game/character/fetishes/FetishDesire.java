package com.lilithsthrone.game.character.fetishes;

import com.lilithsthrone.game.character.attributes.Attribute;
import com.lilithsthrone.rendering.IconCache;
import com.lilithsthrone.utils.Colour;
import com.lilithsthrone.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 0.1.99
 * @version 0.2.0
 * @author Innoxia
 */
public enum FetishDesire {
	
	ZERO_HATE(0, "hate", "hate", "hates", "fondness1", -25f, Colour.BASE_CRIMSON),
	
	ONE_DISLIKE(1, "dislike", "dislike", "dislikes", "fondness2", -10f, Colour.BASE_RED),
	
	TWO_NEUTRAL(2, "indifferent", "are indifferent to", "is indifferent to", "fondness3", 0, Colour.BASE_BLUE_STEEL),
	
	THREE_LIKE(3, "like", "like", "likes", "fondness4", 5f, Colour.BASE_PINK_LIGHT),
	
	FOUR_LOVE(4, "love", "love", "loves", "fondness5", 10f, Colour.BASE_PINK);
	
	private int value;
	private String name;
	private String nameAsPlayerVerb;
	private String nameAsVerb;
	private String iconPath;
	private Colour iconColour;
	private float lustIncrement;
	private List<String> modifiersList;
	
	private FetishDesire(int value, String name, String nameAsPlayerVerb, String nameAsVerb, String pathName, float lustIncrement, Colour colour) {
		this.value = value;
		this.name = name;
		this.nameAsPlayerVerb = nameAsPlayerVerb;
		this.nameAsVerb = nameAsVerb;
		this.lustIncrement = lustIncrement;
		
		modifiersList = new ArrayList<>();
		modifiersList.add((lustIncrement >= 0 ? "[style.boldSex(+" + lustIncrement : "[style.boldBad(" + lustIncrement) + ")] [style.boldLust("+ Util.capitaliseSentence(Attribute.LUST.getAbbreviatedName())+ ")] from related sex actions");

		iconPath = "fetishes/" + pathName + ".svg";
		iconColour = colour;
	}
	
	public static int getCostToChange() {
		return 0;
	}
	
	public static FetishDesire getDesireFromValue(int value) {
		for(FetishDesire desire : FetishDesire.values()) {
			if(desire.getValue() == value) {
				return desire;
			}
		}
		
		if(value<=ZERO_HATE.getValue()) {
			return ZERO_HATE;
		} else {
			return FOUR_LOVE;
		}
	}
	
	public FetishDesire getPreviousDesire() {
		switch(this) {
			case ZERO_HATE:
				return ZERO_HATE;
			case ONE_DISLIKE:
				return ZERO_HATE;
			case TWO_NEUTRAL:
				return ONE_DISLIKE;
			case THREE_LIKE:
				return TWO_NEUTRAL;
			case FOUR_LOVE:
				return THREE_LIKE;
		}
		return TWO_NEUTRAL;
	}
	
	public FetishDesire getNextDesire() {
		switch(this) {
			case ZERO_HATE:
				return ONE_DISLIKE;
			case ONE_DISLIKE:
				return TWO_NEUTRAL;
			case TWO_NEUTRAL:
				return THREE_LIKE;
			case THREE_LIKE:
				return FOUR_LOVE;
			case FOUR_LOVE:
				return FOUR_LOVE;
		}
		return TWO_NEUTRAL;
	}
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getNameAsPlayerVerb() {
		return nameAsPlayerVerb;
	}
	
	public String getNameAsVerb() {
		return nameAsVerb;
	}

	public float getLustIncrement() {
		return lustIncrement;
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
	
	public List<String> getModifiersAsStringList() {
		return modifiersList;
	}

}
