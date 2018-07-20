package com.lilithsthrone.game.character.fetishes;

import java.io.IOException;
import java.io.InputStream;

import com.lilithsthrone.rendering.IconCache;
import com.lilithsthrone.utils.Colour;
import com.lilithsthrone.utils.Util;

/**
 * @since 0.1.99
 * @version 0.1.99
 * @author Innoxia
 */
public enum FetishLevel {
	
	ZERO_NO_EXPERIENCE("inexperienced", "I", "", "overlay1", 0, 0, 10, Colour.DESIRE_STAGE_ZERO),
	
	ONE_AMATEUR("amateur", "II", "", "overlay2", 0.5f, 10, 50, Colour.DESIRE_STAGE_ONE),
	
	TWO_EXPERIENCED("experienced", "III", "", "overlay3", 1f, 50, 100, Colour.DESIRE_STAGE_TWO),
	
	THREE_EXPERT("expert", "IV", "", "overlay4", 2f, 100, 200, Colour.DESIRE_STAGE_THREE),
	
	FOUR_MASTERFUL("masterful", "V", "", "overlay5", 2.5f, 200, 200, Colour.DESIRE_STAGE_FOUR);
	
	private String name;
	private String numeral;
	private String description;
	private float bonusArousalIncrease;
	private int minimumExperience;
	private int maximumExperience;
	private Colour colour;
	private String iconPath;
	
	private FetishLevel(String name, String numeral, String description, String pathName, float bonusArousalIncrease, int minimumExperience, int maximumExperience, Colour colour) {
		this.name = name;
		this.numeral = numeral;
		this.description = description;
		this.bonusArousalIncrease = bonusArousalIncrease;
		this.minimumExperience = minimumExperience;
		this.maximumExperience = maximumExperience;
		this.colour = colour;

		iconPath = "fetishes/" + pathName + ".svg";
	}

	public String getName() {
		return name;
	}
	
	public String getNumeral() {
		return numeral;
	}
	
	public String getDescription() {
		return description;
	}

	public String getIcon(String context) {
		return IconCache.INSTANCE.getIcon(context, iconPath, Colour.BASE_PINK);
	}

	public float getBonusArousalIncrease() {
		return bonusArousalIncrease;
	}

	public float getBonusArousalIncreasePartner() {
		return Math.round((bonusArousalIncrease/2f)*100)/100f;
	}

	public int getMinimumExperience() {
		return minimumExperience;
	}

	public int getMaximumExperience() {
		return maximumExperience;
	}

	public Colour getColour() {
		return colour;
	}
	
	public static FetishLevel getFetishLevelFromValue(int value){
		for(FetishLevel fl : FetishLevel.values()) {
			if(value>=fl.getMinimumExperience() && value<fl.getMaximumExperience())
				return fl;
		}
		return FOUR_MASTERFUL;
	}
	
}
