package com.lilithsthrone.game.character.race;

import com.lilithsthrone.rendering.IconCache;
import com.lilithsthrone.utils.Colour;

/**
 * @since 0.1.78
 * @version 0.1.99
 * @author Innoxia
 */
public enum FurryPreference {

	/**No furry parts at all. (NPCs will spawn as regular humans.)*/
	HUMAN("Disabled") {
		@Override
		public String getDescriptionFeminine(Subspecies r) {
			return "Feminine "+r.getNamePlural()+" will be completely disabled in random encounters. If all feminine preferences are set to 'Disabled', random encounters will default to feminine humans.";
		}
		@Override
		public String getDescriptionMasculine(Subspecies r) {
			return "Masculine "+r.getNamePlural()+" will be completely disabled in random encounters. If all masculine preferences are set to 'Disabled', random encounters will default to masculine humans.";
		}
		@Override
		public String getIcon(String context, boolean disabled) {
			return IconCache.INSTANCE.getIcon(context, "UIElements/scale_zero.svg",
					disabled ? Colour.BASE_GREY : Colour.BASE_MAGENTA);
		}
	},
	
	/**NPCs will spawn with furry ears and eyes, and where applicable, furry tails, horns, antenna, and wings. They will <b>not</b> spawn with furry breasts or genitalia.*/
	MINIMUM("Minimum") {
		@Override
		public String getDescriptionFeminine(Subspecies r) {
			return "Feminine "+r.getNamePlural()+" will spawn with furry ears and eyes, and where applicable, furry tails, horns, antenna, and wings. They will <b>not</b> spawn with furry breasts or genitalia.";
		}

		@Override
		public String getDescriptionMasculine(Subspecies r) {
			return "Masculine "+r.getNamePlural()+" will spawn with furry ears and eyes, and where applicable, furry tails, horns, antenna, and wings. They will <b>not</b> spawn with furry breasts or genitalia.";
		}

		@Override
		public String getIcon(String context, boolean disabled) {
			return IconCache.INSTANCE.getIcon(context, "UIElements/scale_one.svg",
					disabled ? Colour.BASE_GREY : Colour.BASE_GREEN);
		}
	},
	
	/**NPCs will spawn with all of the furry parts that the 'Minimum' setting enables (ears, eyes, tails, horns, antenna, and wings). They also have the chance to spawn with furry breasts, genitalia, arms, and legs.*/
	REDUCED("Lesser") {
		@Override
		public String getDescriptionFeminine(Subspecies r) {
			return "Feminine "+r.getNamePlural()+" will spawn with all of the furry parts that the 'Minimum' setting enables (ears, eyes, tails, horns, antenna, and wings)."
						+ " They also have the chance to spawn with furry breasts, genitalia, arms, and legs.";
		}

		@Override
		public String getDescriptionMasculine(Subspecies r) {
			return "Masculine "+r.getNamePlural()+" will spawn with all of the furry parts that the 'Minimum' setting enables (ears, eyes, tails, horns, antenna, and wings)."
					+ " They also have the chance to spawn with furry breasts, genitalia, arms, and legs.";
		}

		@Override
		public String getIcon(String context, boolean disabled) {
			return IconCache.INSTANCE.getIcon(context, "UIElements/scale_two.svg",
					disabled ? Colour.BASE_GREY : Colour.BASE_GREEN);
		}
	},
	
	/**NPCs will spawn with all of the furry parts that the 'Minimum' setting enables (ears, eyes, tails, horns, antenna, and wings). They also have the chance to spawn with furry breasts, genitalia, arms, legs, skin/fur, and faces.*/
	NORMAL("Greater") {
		@Override
		public String getDescriptionFeminine(Subspecies r) {
			return "Feminine "+r.getNamePlural()+" will spawn with all of the furry parts that the 'Minimum' setting enables (ears, eyes, tails, horns, antenna, and wings)."
					+ " They also have the chance to spawn with furry breasts, genitalia, arms, legs, skin/fur, and faces.";
		}

		@Override
		public String getDescriptionMasculine(Subspecies r) {
			return "Masculine "+r.getNamePlural()+" will spawn with all of the furry parts that the 'Minimum' setting enables (ears, eyes, tails, horns, antenna, and wings)."
					+ " They also have the chance to spawn with furry breasts, genitalia, arms, legs, skin/fur, and faces.";
		}

		@Override
		public String getIcon(String context, boolean disabled) {
			return IconCache.INSTANCE.getIcon(context, "UIElements/scale_three.svg",
					disabled ? Colour.BASE_GREY : Colour.BASE_GREEN);
		}
	},
	
	/**NPCs will <b>always</b> spawn with as many furry parts as is possible (ears, eyes, tails, horns, antenna, wings, breasts, genitalia, arms, legs, skin/fur, and face).*/
	MAXIMUM("Maximum") {
		@Override
		public String getDescriptionFeminine(Subspecies r) {
			return "Feminine "+r.getNamePlural()+" will <b>always</b> spawn with as many furry parts as is possible (ears, eyes, tails, horns, antenna, wings, breasts, genitalia, arms, legs, skin/fur, and face).";
		}

		@Override
		public String getDescriptionMasculine(Subspecies r) {
			return "Masculine "+r.getNamePlural()+" will <b>always</b> spawn with as many furry parts as is possible (ears, eyes, tails, horns, antenna, wings, breasts, genitalia, arms, legs, skin/fur, and face).";
		}

		@Override
		public String getIcon(String context, boolean disabled) {
			return IconCache.INSTANCE.getIcon(context, "UIElements/scale_four.svg",
					disabled ? Colour.BASE_GREY : Colour.BASE_GREEN);
		}
	};
	
	private String name;
	private FurryPreference(String name) {
		this.name=name;
	}

	public abstract String getIcon(String context, boolean disabled);
	
	public String getName() {
		return name;
	}
	
	public abstract String getDescriptionFeminine(Subspecies r);
	public abstract String getDescriptionMasculine(Subspecies r);
}
