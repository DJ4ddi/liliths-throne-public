package com.lilithsthrone.world.places;

import com.lilithsthrone.game.Weather;
import com.lilithsthrone.game.character.quests.Quest;
import com.lilithsthrone.game.character.quests.QuestLine;
import com.lilithsthrone.game.character.race.Subspecies;
import com.lilithsthrone.game.dialogue.DialogueFlagValue;
import com.lilithsthrone.game.dialogue.DialogueNodeOld;
import com.lilithsthrone.game.dialogue.encounters.Encounter;
import com.lilithsthrone.game.dialogue.places.JunglePlaces;
import com.lilithsthrone.game.dialogue.places.dominion.*;
import com.lilithsthrone.game.dialogue.places.dominion.harpyNests.*;
import com.lilithsthrone.game.dialogue.places.dominion.lilayashome.*;
import com.lilithsthrone.game.dialogue.places.dominion.shoppingArcade.*;
import com.lilithsthrone.game.dialogue.places.dominion.slaverAlley.ScarlettsShop;
import com.lilithsthrone.game.dialogue.places.dominion.slaverAlley.SlaverAlleyDialogue;
import com.lilithsthrone.game.dialogue.places.dominion.zaranixHome.ZaranixHomeFirstFloor;
import com.lilithsthrone.game.dialogue.places.dominion.zaranixHome.ZaranixHomeFirstFloorRepeat;
import com.lilithsthrone.game.dialogue.places.dominion.zaranixHome.ZaranixHomeGroundFloor;
import com.lilithsthrone.game.dialogue.places.dominion.zaranixHome.ZaranixHomeGroundFloorRepeat;
import com.lilithsthrone.game.dialogue.places.submission.BatCaverns;
import com.lilithsthrone.game.dialogue.places.submission.GamblingDenDialogue;
import com.lilithsthrone.game.dialogue.places.submission.SlimeQueensLair;
import com.lilithsthrone.game.dialogue.places.submission.SubmissionGenericPlaces;
import com.lilithsthrone.game.inventory.CharacterInventory;
import com.lilithsthrone.game.inventory.clothing.AbstractClothingType;
import com.lilithsthrone.game.inventory.clothing.ClothingType;
import com.lilithsthrone.game.inventory.item.AbstractItemType;
import com.lilithsthrone.game.inventory.item.ItemType;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.rendering.IconCache;
import com.lilithsthrone.utils.Bearing;
import com.lilithsthrone.utils.Colour;
import com.lilithsthrone.utils.Util;
import com.lilithsthrone.world.EntranceType;
import com.lilithsthrone.world.WorldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @since 0.1.0
 * @version 0.2.8
 * @author Innoxia
 */
public enum PlaceType {
	
	
	GENERIC_IMPASSABLE(null, null, null, Colour.MAP_BACKGROUND, null, null, false, false, true, ""),
	
	GENERIC_EMPTY_TILE("Empty", "dominion/slaverAlleyIcon",  Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, null, null, false, false, true, ""),

	GENERIC_HOLDING_CELL("Holding cell", "dominion/slaverAlleyIcon",  Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, null, null, false, false, true, ""),
	
	GENERIC_MUSEUM("Museum", "dominion/slaverAlleyIcon",  Colour.BASE_TAN, Colour.MAP_BACKGROUND, null, null, false, true, false, "in Lily's Museum"),
	
	DOMINION_PLAZA("Lilith's Plaza", "dominion/statue",  Colour.BASE_PINK_DEEP, Colour.MAP_BACKGROUND_PINK, CityPlaces.DOMINION_PLAZA, null, false, false, true, "in Dominion's central plaza") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			if(Main.game.getCurrentWeather() == Weather.MAGIC_STORM) {
				return Subspecies.getDominionStormImmuneSpecies();
			} else {
				return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
			}
		}
	},
	
	DOMINION_STREET("Dominion Streets", null, null, Colour.MAP_BACKGROUND, CityPlaces.STREET, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_BOULEVARD("Dominion Boulevard", null, null, Colour.MAP_BACKGROUND_PINK, CityPlaces.BOULEVARD, Encounter.DOMINION_BOULEVARD, false, false, true, "in the streets of Dominion") {

		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_LILITHS_TOWER("Lilith's Tower", "dominion/lilithsTowerIcon", Colour.BASE_PURPLE, Colour.MAP_BACKGROUND_PINK, LilithsTower.OUTSIDE, null, false, false, true, "in the streets of Dominion") {

		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_ENFORCER_HQ("Enforcer HQ", "dominion/enforcerHQIcon", Colour.BASE_BLUE, Colour.MAP_BACKGROUND, EnforcerHQDialogue.EXTERIOR, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_DEMON_HOME_GATE("Demon Home Gates", "dominion/gate", Colour.BASE_PINK_LIGHT, Colour.MAP_BACKGROUND_PINK, DemonHome.DEMON_HOME_GATE, null, false, false, true, "in the streets of Demon Home") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_DEMON_HOME("Demon Home", null, null, Colour.MAP_BACKGROUND_PINK, DemonHome.DEMON_HOME_STREET, null, false, false, true, "in the streets of Demon Home") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_DEMON_HOME_ARTHUR("Demon Home", "dominion/demonHomeIcon", Colour.BASE_PINK, Colour.MAP_BACKGROUND_PINK, DemonHome.DEMON_HOME_STREET_ARTHUR, null, false, false, true, "in the streets of Demon Home") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_SHOPPING_ARCADE("Shopping Arcade", "dominion/shoppingArcadeIcon", Colour.BASE_GOLD, Colour.MAP_BACKGROUND, ShoppingArcadeDialogue.OUTSIDE, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_STREET_HARPY_NESTS("Dominion Streets", null, null, Colour.MAP_BACKGROUND_DARK, CityPlaces.STREET_SHADED, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_HARPY_NESTS_ENTRANCE("Harpy Nests Entrance", "dominion/harpyNestIcon", Colour.BASE_MAGENTA, Colour.MAP_BACKGROUND_DARK, HarpyNestsDialogue.OUTSIDE, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_NIGHTLIFE_DISTRICT("Nightlife District", "dominion/nightlifeIcon", Colour.BASE_PINK_LIGHT, Colour.MAP_BACKGROUND, NightlifeDistrict.OUTSIDE, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_CITY_HALL("City Hall", "dominion/townHallIcon",  Colour.BASE_LILAC, Colour.MAP_BACKGROUND, CityHall.OUTSIDE, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_AUNTS_HOME("Lilaya's Home", "dominion/homeIcon", Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, LilayaHomeGeneric.OUTSIDE, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	DOMINION_SLAVER_ALLEY("Slaver Alley", "dominion/slaverAlleyIcon",  Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, SlaverAlleyDialogue.OUTSIDE, null, false, false, true, "in the alleyways near Slaver's Alley") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},

	DOMINION_RED_LIGHT_DISTRICT("Red Light District", "dominion/brothel", Colour.BASE_MAGENTA, Colour.MAP_BACKGROUND_DARK, RedLightDistrict.OUTSIDE, Encounter.DOMINION_STREET, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},

	DOMINION_PARK("Park", "dominion/park", Colour.BASE_GREEN, Colour.MAP_BACKGROUND, CityPlaces.PARK, Encounter.DOMINION_STREET, false, false, true, "in one of Dominion's parks") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
	},
	
	// Alleyways:
	
	DOMINION_BACK_ALLEYS("Dominion Alleyways", "dominion/alleysIcon",  Colour.BASE_BLACK, Colour.MAP_BACKGROUND, CityPlaces.BACK_ALLEYS, Encounter.DOMINION_ALLEY, true, false, true, "in one of Dominion's backalleys"),

	DOMINION_DARK_ALLEYS("Dark Alleyways", "dominion/alleysDarkIcon",  Colour.BASE_PURPLE, Colour.MAP_BACKGROUND, CityPlaces.DARK_ALLEYS, Encounter.DOMINION_DARK_ALLEY, true, false, true, "in one of Dominion's dark alleyways"),
	
	DOMINION_ALLEYS_CANAL_CROSSING("Canal Crossing", "dominion/bridge",  Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, CityPlaces.BACK_ALLEYS_CANAL, Encounter.DOMINION_ALLEY, true, false, true, "in one of Dominion's backalleys"),
	
	// Canals:
	
	DOMINION_CANAL("Dominion Canal", "dominion/canalIcon",  Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, CityPlaces.CANAL, Encounter.DOMINION_CANAL, true, false, true, "beside one of Dominion's canals"),
	
	DOMINION_CANAL_END("Dominion Canal", "dominion/canalEndIcon",  Colour.BASE_BLUE, Colour.MAP_BACKGROUND, CityPlaces.CANAL_END, Encounter.DOMINION_CANAL, true, false, true, "beside one of Dominion's canals"),
	
	// Exits & entrances:
	
	DOMINION_EXIT_TO_SUBMISSION("Submission Entrance", "dominion/submissionExit",  Colour.BASE_TEAL, Colour.MAP_BACKGROUND, CityPlaces.CITY_EXIT_SEWERS, null, false, false, true, "in the streets of Dominion") {
		@Override
		public boolean isDangerous() {
			return Main.game.getCurrentWeather() == Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
		@Override
		public Bearing getBearing() {
			return Bearing.RANDOM;
		}
	},
	
	DOMINION_EXIT_TO_JUNGLE("Jungle Entrance", "dominion/JungleExit",  Colour.BASE_GREEN_LIME, Colour.MAP_BACKGROUND_PINK, CityPlaces.CITY_EXIT_JUNGLE, null, false, false, true, "in the streets of Dominion") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
		@Override
		public Bearing getBearing() {
			return Bearing.NORTH;
		}
	},
	
	DOMINION_EXIT_TO_FIELDS("Fields Entrance", "dominion/fieldsExit",  Colour.BASE_GREEN_LIGHT, Colour.MAP_BACKGROUND_PINK, CityPlaces.CITY_EXIT_FIELDS, null, false, false, true, "in the streets of Dominion") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
		@Override
		public Bearing getBearing() {
			return Bearing.WEST;
		}
	},
	
	DOMINION_EXIT_TO_SEA("Endless Sea Entrance", "dominion/endlessSeaExit",  Colour.BASE_TEAL, Colour.MAP_BACKGROUND_PINK, CityPlaces.CITY_EXIT_SEA, null, false, false, true, "in the streets of Dominion") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
		@Override
		public Bearing getBearing() {
			return Bearing.EAST;
		}
	},
	
	DOMINION_EXIT_TO_DESERT("Desert Entrance", "dominion/desertExit", Colour.BASE_YELLOW, Colour.MAP_BACKGROUND_PINK, CityPlaces.CITY_EXIT_DESERT, null, false, false, true, "in the streets of Dominion") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return DOMINION_PLAZA.getSpeciesPopulatingArea();
		}
		@Override
		public Bearing getBearing() {
			return Bearing.SOUTH;
		}
	},
	
	
	
	
	ENFORCER_HQ_CORRIDOR("Corridor", null, Colour.BASE_BLACK, Colour.MAP_BACKGROUND, EnforcerHQDialogue.CORRIDOR, null, false, true, true, "in the Enforcer HQ"),

	ENFORCER_HQ_WAITING_AREA("Waiting area", "dominion/enforcerHQ/waitingRoom", Colour.BASE_BROWN, Colour.MAP_BACKGROUND, EnforcerHQDialogue.WAITING_AREA, null, false, true, true, "in the Enforcer HQ"),
	
	ENFORCER_HQ_RECEPTION_DESK("Reception desk", "dominion/enforcerHQ/receptionDesk", Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, EnforcerHQDialogue.RECEPTION_DESK, null, false, true, true, "in Candi's office"),
	
	ENFORCER_HQ_GUARDED_DOOR("Guarded door", "dominion/enforcerHQ/guardedDoor", Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, EnforcerHQDialogue.GUARDED_DOOR, null, false, true, true, "in the Enforcer HQ"),
	
	ENFORCER_HQ_BRAXS_OFFICE("Brax's Office", "dominion/enforcerHQ/braxsOffice", Colour.BASE_BLUE_STEEL, Colour.MAP_BACKGROUND, EnforcerHQDialogue.INTERIOR_BRAX, null, false, true, true, "in his office") {
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.braxEncountered)) {
				return EnforcerHQDialogue.INTERIOR_BRAX_REPEAT;
				
			} else {
				return EnforcerHQDialogue.INTERIOR_BRAX;
			}
		}
	},

	ENFORCER_HQ_ENTRANCE("Entranceway", "dominion/enforcerHQ/exit", Colour.BASE_RED, Colour.MAP_BACKGROUND, EnforcerHQDialogue.ENTRANCE, null, false, true, true, ""),
	
	
	
	
	// Standard tiles:
	HARPY_NESTS_WALKWAYS("Walkway", null, Colour.BASE_BLACK, Colour.MAP_BACKGROUND, HarpyNestsDialogue.WALKWAY, Encounter.HARPY_NEST_WALKWAYS, true, false, true, "in the Harpy Nests") {
		@Override
		public boolean isDangerous() {
			return !Main.game.getPlayer().isQuestCompleted(QuestLine.SIDE_HARPY_PACIFICATION) || Main.game.getCurrentWeather()==Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			if(Main.game.getCurrentWeather() == Weather.MAGIC_STORM) {
				return super.getSpeciesPopulatingArea();
			} else {
				return Subspecies.getWorldSpecies().get(WorldType.HARPY_NEST);
			}
		}
	},
	
	HARPY_NESTS_WALKWAYS_BRIDGE("Walkway Bridge", "dominion/harpyNests/bridge", Colour.BASE_GREY, Colour.MAP_BACKGROUND, HarpyNestsDialogue.WALKWAY_BRIDGE, Encounter.HARPY_NEST_WALKWAYS, true, false, true, "in the Harpy Nests") {
		@Override
		public boolean isDangerous() {
			return !Main.game.getPlayer().isQuestCompleted(QuestLine.SIDE_HARPY_PACIFICATION) || Main.game.getCurrentWeather()==Weather.MAGIC_STORM;
		}
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return HARPY_NESTS_WALKWAYS.getSpeciesPopulatingArea();
		}
	},

	// Places:
	HARPY_NESTS_ENTRANCE_ENFORCER_POST("Enforcer post", "dominion/harpyNests/exit", Colour.BASE_RED, Colour.MAP_BACKGROUND, HarpyNestsDialogue.ENTRANCE_ENFORCER_POST, null, false, true, true, "in the Harpy Nests") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},
	
	HARPY_NESTS_ALEXAS_NEST("Alexa's nest", "dominion/harpyNests/nestAlexa", Colour.BASE_GOLD, Colour.MAP_BACKGROUND, HarpyNestAlexa.ALEXAS_NEST_EXTERIOR, null, false, false, true, "in Alexa's nest"){
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return HARPY_NESTS_WALKWAYS.getSpeciesPopulatingArea();
		}
	},
	
	HARPY_NESTS_HARPY_NEST_RED("Harpy nest", "dominion/harpyNests/nestRed", Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, HarpyNestDominant.HARPY_NEST_DOMINANT, null, false, false, true, "in Diana's nest"){
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return HARPY_NESTS_WALKWAYS.getSpeciesPopulatingArea();
		}
	},
	
	HARPY_NESTS_HARPY_NEST_PINK("Harpy nest", "dominion/harpyNests/nestPink", Colour.BASE_PINK_LIGHT, Colour.MAP_BACKGROUND, HarpyNestNympho.HARPY_NEST_NYMPHO, null, false, false, true, "in Lexi's nest"){
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return HARPY_NESTS_WALKWAYS.getSpeciesPopulatingArea();
		}
	},
	
	HARPY_NESTS_HARPY_NEST_YELLOW("Harpy nest", "dominion/harpyNests/nestYellow", Colour.BASE_YELLOW_LIGHT, Colour.MAP_BACKGROUND, HarpyNestBimbo.HARPY_NEST_BIMBO, null, false, false, true, "in Brittany's nest"){
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return HARPY_NESTS_WALKWAYS.getSpeciesPopulatingArea();
		}
	},
		
		
	
	
	
	// Standard tiles:
	JUNGLE_PATH("Jungle Path", null, Colour.BASE_GREEN, Colour.MAP_BACKGROUND, JunglePlaces.PATH, null, false, false, true, "in the jungle"),
	
	JUNGLE_DENSE_JUNGLE("Dense Jungle", null, Colour.BASE_GREEN, Colour.MAP_BACKGROUND, JunglePlaces.DENSE_JUNGLE, null, true, false, true, "in the jungle"),

	// Safe places:
	JUNGLE_CLUB("Club", null, Colour.BASE_GREEN, Colour.MAP_BACKGROUND, JunglePlaces.CLUB, null, false, false, true, "in the jungle"),
	
	JUNGLE_BROTHEL("Brothel", null, Colour.BASE_GREEN, Colour.MAP_BACKGROUND, JunglePlaces.BROTHEL, null, false, false, true, "in the jungle"),

	// Dangerous places:
	JUNGLE_TENTACLE_QUEENS_LAIR("Tentacle Queen's Lair", null, Colour.BASE_GREEN, Colour.MAP_BACKGROUND, JunglePlaces.TENTACLE_QUEENS_LAIR, null, false, false, true, "in the jungle"),

	// Exits & entrances:
	JUNGLE_ENTRANCE("Jungle Entrance", null, Colour.BASE_GREEN, Colour.MAP_BACKGROUND, JunglePlaces.JUNGLE_ENTRANCE, null, false, false, true, "in the jungle"){
//		@Override
//		public WorldType getParentWorldType() {
//			return WorldType.DOMINION;
//		}
//		@Override
//		public PlaceType getParentPlaceType() {
//			return PlaceType.DOMINION_EXIT_TO_JUNGLE;
//		}
//		@Override
//		public EntranceType getParentAlignment() {
//			return EntranceType.ALIGNED_FLIP_VERTICAL;
//		}
	},
	
	
	
	// Ground floor:
	
	LILAYA_HOME_CORRIDOR("Corridor", null, Colour.BASE_GREY, Colour.MAP_BACKGROUND, LilayaHomeGeneric.CORRIDOR, Encounter.LILAYAS_HOME_CORRIDOR, false, true, false, "in Lilaya's Home"),
	
	LILAYA_HOME_ROOM_WINDOW_GROUND_FLOOR("Room", "dominion/lilayasHome/room", Colour.BASE_GREY, Colour.MAP_BACKGROUND, LilayaHomeGeneric.ROOM_WINDOW, null, false, true, false, "in Lilaya's Home") {
		@Override
		public ArrayList<PlaceUpgrade> getStartingPlaceUpgrades() {
			return Util.newArrayListOfValues(PlaceUpgrade.LILAYA_EMPTY_ROOM);
		}
		@Override
		public ArrayList<PlaceUpgrade> getAvailablePlaceUpgrades(Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesSingle();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesDouble();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return PlaceUpgrade.getMilkingUpgrades();
			}
			
			return PlaceUpgrade.getCoreRoomUpgrades();
		}
		@Override
		public String getIcon(String context, Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlave.svg", Colour.BASE_CRIMSON);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomMilking.svg", Colour.BASE_ORANGE);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlaveDouble.svg", Colour.BASE_MAGENTA);
				
			} else {
				return super.getIcon(context, upgrades);
			}
		}
		@Override
		public boolean isAbleToBeUpgraded() {
			return true;
		}
		@Override
		public String getPlaceNameAppendFormat(int count) {
			return " G-"+String.format("%02d", count);
		}
	},
	
	LILAYA_HOME_ROOM_GARDEN_GROUND_FLOOR("Garden Room", "dominion/lilayasHome/room", Colour.BASE_GREY, Colour.MAP_BACKGROUND, LilayaHomeGeneric.ROOM_GARDEN_GROUND_FLOOR, null, false, true, false, "in Lilaya's Home") {
		@Override
		public ArrayList<PlaceUpgrade> getStartingPlaceUpgrades() {
			return Util.newArrayListOfValues(PlaceUpgrade.LILAYA_EMPTY_ROOM);
		}
		@Override
		public ArrayList<PlaceUpgrade> getAvailablePlaceUpgrades(Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesSingle();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesDouble();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return PlaceUpgrade.getMilkingUpgrades();
			}
			
			return PlaceUpgrade.getCoreRoomUpgrades();
		}
		@Override
		public String getIcon(String context, Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlave.svg", Colour.BASE_CRIMSON);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomMilking.svg", Colour.BASE_ORANGE);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlaveDouble.svg", Colour.BASE_MAGENTA);
				
			} else {
				return super.getIcon(context, upgrades);
			}
		}
		@Override
		public boolean isAbleToBeUpgraded() {
			return true;
		}
		@Override
		public String getPlaceNameAppendFormat(int count) {
			return " GG-"+String.format("%02d", count);
		}
	},
	
	LILAYA_HOME_ROOM_WINDOW_FIRST_FLOOR("Room", "dominion/lilayasHome/room", Colour.BASE_GREY, Colour.MAP_BACKGROUND, LilayaHomeGeneric.ROOM_WINDOW, null, false, true, false, "in Lilaya's Home") {
		@Override
		public ArrayList<PlaceUpgrade> getStartingPlaceUpgrades() {
			return Util.newArrayListOfValues(PlaceUpgrade.LILAYA_EMPTY_ROOM);
		}
		@Override
		public ArrayList<PlaceUpgrade> getAvailablePlaceUpgrades(Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesSingle();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesDouble();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return PlaceUpgrade.getMilkingUpgrades();
			}
			
			return PlaceUpgrade.getCoreRoomUpgrades();
		}
		@Override
		public String getIcon(String context, Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlave.svg", Colour.BASE_CRIMSON);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomMilking.svg", Colour.BASE_ORANGE);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlaveDouble.svg", Colour.BASE_MAGENTA);
				
			} else {
				return super.getIcon(context, upgrades);
			}
		}
		@Override
		public boolean isAbleToBeUpgraded() {
			return true;
		}
		@Override
		public String getPlaceNameAppendFormat(int count) {
			return " F-"+String.format("%02d", count);
		}
	},
	
	LILAYA_HOME_ROOM_GARDEN_FIRST_FLOOR("Garden Room", "dominion/lilayasHome/room", Colour.BASE_GREY, Colour.MAP_BACKGROUND, LilayaHomeGeneric.ROOM_GARDEN, null, false, true, false, "in Lilaya's Home") {
		@Override
		public ArrayList<PlaceUpgrade> getStartingPlaceUpgrades() {
			return Util.newArrayListOfValues(PlaceUpgrade.LILAYA_EMPTY_ROOM);
		}
		@Override
		public ArrayList<PlaceUpgrade> getAvailablePlaceUpgrades(Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesSingle();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return PlaceUpgrade.getSlaveQuartersUpgradesDouble();
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return PlaceUpgrade.getMilkingUpgrades();
			}
			
			return PlaceUpgrade.getCoreRoomUpgrades();
		}
		@Override
		public String getIcon(String context, Set<PlaceUpgrade> upgrades) {
			if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlave.svg", Colour.BASE_CRIMSON);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_MILKING_ROOM)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomMilking.svg", Colour.BASE_ORANGE);
				
			} else if(upgrades.contains(PlaceUpgrade.LILAYA_SLAVE_ROOM_DOUBLE)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/lilayasHome/roomSlaveDouble.svg", Colour.BASE_MAGENTA);
				
			} else {
				return super.getIcon(context, upgrades);
			}
		}
		@Override
		public boolean isAbleToBeUpgraded() {
			return true;
		}
		@Override
		public String getPlaceNameAppendFormat(int count) {
			return " FG-"+String.format("%02d", count);
		}
	},
	
	LILAYA_HOME_ARTHUR_ROOM("Arthur's Room", "dominion/lilayasHome/roomArthur", Colour.BASE_BLUE_STEEL, Colour.MAP_BACKGROUND, LilayaHomeGeneric.ROOM_ARTHUR, null, false, true, false, "in Arthur's Room"),
	
	LILAYA_HOME_BIRTHING_ROOM("Room", "dominion/lilayasHome/roomBirthing", Colour.BASE_PINK, Colour.MAP_BACKGROUND, LilayaHomeGeneric.BIRTHING_ROOM, null, false, true, false, "in Lilaya's Home"),
	
	LILAYA_HOME_KITCHEN("Kitchen", "dominion/lilayasHome/kitchen", Colour.BASE_TAN, Colour.MAP_BACKGROUND, LilayaHomeGeneric.KITCHEN, null, false, true, false, "in Lilaya's kitchen"),
	
	LILAYA_HOME_LIBRARY("Library", "dominion/lilayasHome/library", Colour.BASE_TEAL, Colour.MAP_BACKGROUND, Library.LIBRARY, null, false, true, false, "in Lilaya's library"),
	
	LILAYA_HOME_STAIR_UP("Staircase", "dominion/lilayasHome/stairsUp", Colour.BASE_GREEN_LIGHT, Colour.MAP_BACKGROUND, LilayaHomeGeneric.STAIRCASE_UP, null, false, true, false, "in Lilaya's Home"),
	
	LILAYA_HOME_ENTRANCE_HALL("Entrance Hall", "dominion/lilayasHome/entranceHall", Colour.BASE_RED, Colour.MAP_BACKGROUND, LilayaHomeGeneric.ENTRANCE_HALL, null, false, true, false, "in Lilaya's Home"),
	
	LILAYA_HOME_LAB("Lilaya's Lab", "dominion/lilayasHome/lab", Colour.BASE_ORANGE, Colour.MAP_BACKGROUND, Lab.LAB, null, false, true, false, "in Lilaya's lab") {
		@Override
		public void applyInventoryInit(CharacterInventory inventory) {
			inventory.addClothing(AbstractClothingType.generateClothing(ClothingType.SCIENTIST_EYES_SAFETY_GOGGLES, Colour.CLOTHING_BLACK, false));
		}
	},
	
	LILAYA_HOME_GARDEN("Garden", "dominion/lilayasHome/garden", Colour.BASE_GREEN, Colour.MAP_BACKGROUND, LilayaHomeGeneric.GARDEN, null, false, false, false, "in Lilaya's garden"),
	
	LILAYA_HOME_FOUNTAIN("Fountain", "dominion/lilayasHome/fountain", Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, LilayaHomeGeneric.FOUNTAIN, null, false, false, false, "in Lilaya's garden"),
	

	// First floor:

	LILAYA_HOME_ROOM_LILAYA("Lilaya's Room", "dominion/lilayasHome/roomLilaya", Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, LilayasRoom.ROOM_LILAYA, null, false, true, false, "in Lilaya's Home"),
	
	LILAYA_HOME_ROOM_ROSE("Rose's Room", "dominion/lilayasHome/roomRose", Colour.BASE_PINK, Colour.MAP_BACKGROUND, LilayaHomeGeneric.ROOM_ROSE, null, false, true, false, "in Lilaya's Home"),
	
	LILAYA_HOME_ROOM_PLAYER("Your Room", "dominion/lilayasHome/roomPlayer", Colour.BASE_AQUA, Colour.MAP_BACKGROUND, RoomPlayer.ROOM, null, false, true, false, "in your room"),
	
	LILAYA_HOME_STAIR_DOWN("Staircase", "dominion/lilayasHome/stairsDown", Colour.BASE_RED, Colour.MAP_BACKGROUND, LilayaHomeGeneric.STAIRCASE_DOWN, null, false, true, false, "in Lilaya's Home"),
	
	

	// Zaranix:
	// Ground floor:
	
	ZARANIX_GF_CORRIDOR("Corridor", null, Colour.BASE_GREY, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.CORRIDOR, null, false, true, false, "in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.CORRIDOR;
				
			} else {
				return ZaranixHomeGroundFloor.CORRIDOR;
			}
		}
	},
	
	ZARANIX_GF_STAIRS("Staircase", "dominion/zaranixHome/stairsDown", Colour.BASE_GREEN_LIGHT, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.STAIRS, null, false, true, false, "in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.STAIRS;
				
			} else {
				return ZaranixHomeGroundFloor.STAIRS;
			}
		}
	},
	
	ZARANIX_GF_ENTRANCE("Entrance", "dominion/zaranixHome/entranceHall", Colour.BASE_RED, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.ENTRANCE, null, false, true, false, "in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.ENTRANCE;
				
			} else {
				return ZaranixHomeGroundFloor.ENTRANCE;
			}
		}
	},
	
	ZARANIX_GF_LOUNGE("Lounge", "dominion/zaranixHome/lounge", Colour.BASE_ORANGE, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.LOUNGE, null, false, true, false, "in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.LOUNGE;
				
			} else {
				return ZaranixHomeGroundFloor.LOUNGE;
			}
		}
	},
	
	ZARANIX_GF_ROOM("Room", "dominion/zaranixHome/room", Colour.BASE_GREY, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.ROOM, null, false, true, false, "in a room in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.ROOM;
				
			} else {
				return ZaranixHomeGroundFloor.ROOM;
			}
		}
	},
	
	ZARANIX_GF_MAID("Corridor", null, Colour.BASE_GREY, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.CORRIDOR_MAID, null, true, true, false, "in Zaranix's Home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.CORRIDOR;
				
			} else {
				return ZaranixHomeGroundFloor.CORRIDOR_MAID;
			}
		}
		@Override
		public boolean isDangerous() {
			return !Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE);
		}
	},
	
	ZARANIX_GF_GARDEN_ROOM("Room", "dominion/zaranixHome/room", Colour.BASE_GREY, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.GARDEN_ROOM, null, false, true, false, "in a room in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.GARDEN_ROOM;
				
			} else {
				return ZaranixHomeGroundFloor.GARDEN_ROOM;
			}
		}
	},
	
	ZARANIX_GF_GARDEN("Garden", "dominion/zaranixHome/garden", Colour.BASE_GREEN, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.GARDEN, null, false, true, false, "in Zaranix's garden"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.GARDEN;
				
			} else {
				return ZaranixHomeGroundFloor.GARDEN;
			}
		}
	},
	
	ZARANIX_GF_GARDEN_ENTRY("Garden", "dominion/zaranixHome/entranceHall", Colour.BASE_GREEN, Colour.MAP_BACKGROUND, ZaranixHomeGroundFloor.GARDEN_ENTRY, null, false, true, false, "in Zaranix's garden"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeGroundFloorRepeat.GARDEN_ENTRY;
				
			} else {
				return ZaranixHomeGroundFloor.GARDEN_ENTRY;
			}
		}
	},
	
	// First floor:
	
	ZARANIX_FF_CORRIDOR("Corridor", null, Colour.BASE_GREY, Colour.MAP_BACKGROUND, ZaranixHomeFirstFloor.CORRIDOR, null, false, true, false, "in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeFirstFloorRepeat.CORRIDOR;
				
			} else {
				return ZaranixHomeFirstFloor.CORRIDOR;
			}
		}
	},
	
	ZARANIX_FF_STAIRS("Staircase", "dominion/zaranixHome/stairsDown", Colour.BASE_RED, Colour.MAP_BACKGROUND, ZaranixHomeFirstFloor.STAIRS, null, false, true, false, "in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeFirstFloorRepeat.STAIRS;
				
			} else {
				return ZaranixHomeFirstFloor.STAIRS;
			}
		}
	},
	
	ZARANIX_FF_OFFICE("Zaranix's Room", "dominion/zaranixHome/roomZaranix", Colour.BASE_PINK_DEEP, Colour.MAP_BACKGROUND, ZaranixHomeFirstFloor.ZARANIX_ROOM, null, true, true, false, "in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeFirstFloorRepeat.ZARANIX_ROOM;
				
			} else {
				return ZaranixHomeFirstFloor.ZARANIX_ROOM;
			}
		}
		@Override
		public boolean isDangerous() {
			return !Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE);
		}
	},
	
	ZARANIX_FF_ROOM("Room", "dominion/zaranixHome/room", Colour.BASE_GREY, Colour.MAP_BACKGROUND, ZaranixHomeFirstFloor.ROOM, null, false, true, false, "in a room in Zaranix's home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeFirstFloorRepeat.ROOM;
				
			} else {
				return ZaranixHomeFirstFloor.ROOM;
			}
		}
	},
	
	ZARANIX_FF_MAID("Corridor", null, Colour.BASE_RED, Colour.MAP_BACKGROUND, ZaranixHomeFirstFloor.CORRIDOR_MAID, null, true, true, false, "in Zaranix's Home"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE)) {
				return ZaranixHomeFirstFloorRepeat.CORRIDOR;
				
			} else {
				return ZaranixHomeFirstFloor.CORRIDOR_MAID;
			}
		}
		@Override
		public boolean isDangerous() {
			return !Main.game.getPlayer().isQuestProgressGreaterThan(QuestLine.MAIN, Quest.MAIN_1_H_THE_GREAT_ESCAPE);
		}
	},
	
	
	// Angel's Kiss:

	ANGELS_KISS_CORRIDOR("Corridor", null, Colour.BASE_GREY, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_CORRIDOR, null, false, true, false, "in Angel's Kiss"),
	ANGELS_KISS_ENTRANCE("Entrance Hall", "dominion/angelsKiss/entrance", Colour.BASE_RED, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_ENTRANCE, null, false, true, false, "in Angel's Kiss"),
	ANGELS_KISS_STAIRCASE_UP("Entrance Hall", "dominion/angelsKiss/stairsUp", Colour.BASE_GREEN_LIGHT, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_STAIRS_UP, null, false, true, false, "in Angel's Kiss"),
	ANGELS_KISS_STAIRCASE_DOWN("Entrance Hall", "dominion/angelsKiss/stairsDown", Colour.BASE_RED, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_STAIRS_DOWN, null, false, true, false, "in Angel's Kiss"),
	ANGELS_KISS_BEDROOM("Bedroom", "dominion/angelsKiss/bedroom", Colour.BASE_PINK, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_BEDROOM, null, false, true, false, "in Angel's Kiss"),
	ANGELS_KISS_BEDROOM_BUNNY("Bunny's Bedroom", "dominion/angelsKiss/bedroomBunny", Colour.BASE_PINK_LIGHT, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_BEDROOM_BUNNY, null, false, true, false, "in Bunny's Bedroom"),
	ANGELS_KISS_BEDROOM_LOPPY("Loppy's Bedroom", "dominion/angelsKiss/bedroomLoppy", Colour.BASE_PURPLE, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_BEDROOM_LOPPY, null, false, true, false, "in Loppy's Bedroom"),
	ANGELS_KISS_OFFICE("Angel's Office", "dominion/angelsKiss/office", Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, RedLightDistrict.ANGELS_KISS_OFFICE, null, false, true, false, "in Angel's Office"),
	
	
	// Shopping arcade:
	
	SHOPPING_ARCADE_PATH("Arcade", null, Colour.BASE_BLACK, Colour.MAP_BACKGROUND, ShoppingArcadeDialogue.ARCADE, null, false, true, true, "in the Shopping Arcade") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},

	SHOPPING_ARCADE_GENERIC_SHOP("Shop", "dominion/shoppingArcade/genericShop", Colour.BASE_BLACK, Colour.MAP_BACKGROUND, ShoppingArcadeDialogue.GENERIC_SHOP, null, false, true, true, "in the Shopping Arcade") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},
	
	SHOPPING_ARCADE_RALPHS_SHOP("Ralph's Snacks", "dominion/shoppingArcade/ralphShop", Colour.BASE_TEAL, Colour.MAP_BACKGROUND, RalphsSnacks.EXTERIOR, null, false, true, true, "in his store"),
	
	SHOPPING_ARCADE_NYANS_SHOP("Nyan's Clothing Emporium", "dominion/shoppingArcade/nyanShop", Colour.BASE_ROSE, Colour.MAP_BACKGROUND, ClothingEmporium.EXTERIOR, null, false, true, true, "in her store"),
	
	SHOPPING_ARCADE_VICKYS_SHOP("Arcane Arts", "dominion/shoppingArcade/vickyShop", Colour.BASE_MAGENTA, Colour.MAP_BACKGROUND, ArcaneArts.EXTERIOR, null, false, true, true, "in her store"),

	SHOPPING_ARCADE_KATES_SHOP("Succubi's Secrets", "dominion/shoppingArcade/kateShop", Colour.BASE_PINK, Colour.MAP_BACKGROUND, SuccubisSecrets.EXTERIOR, null, false, true, true, "in her beauty salon"),

	SHOPPING_ARCADE_ASHLEYS_SHOP("Dream Lover", "dominion/shoppingArcade/ashleyShop", Colour.BASE_LILAC_LIGHT, Colour.MAP_BACKGROUND, DreamLover.EXTERIOR, null, false, true, true, "in their store"),
	
	SHOPPING_ARCADE_SUPPLIER_DEPOT("Supplier Depot", "dominion/shoppingArcade/supplierDepot", Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, SupplierDepot.EXTERIOR, null, false, true, true, "in the supplier depot") {
		@Override
		public Colour getColour() {
			if(Main.game.getPlayer().isQuestCompleted(QuestLine.RELATIONSHIP_NYAN_HELP)) {
				return Colour.BASE_GREEN;
			} else {
				return Colour.BASE_CRIMSON;
			}
		}
		@Override
		public String getIcon(String context, Set<PlaceUpgrade> upgrades) {
			if(Main.game.getPlayer().isQuestCompleted(QuestLine.RELATIONSHIP_NYAN_HELP)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/shoppingArcade/supplierDepot.svg", Colour.BASE_GREEN);
			} else {
				return super.getIcon(context, upgrades);
			}
		}
	},
	
	SHOPPING_ARCADE_PIXS_GYM("Pix's Playground", "dominion/shoppingArcade/gym", Colour.BASE_GOLD, Colour.MAP_BACKGROUND, PixsPlayground.GYM_EXTERIOR, null, false, true, true, "in her gym"),

	// Exits & entrances:
	SHOPPING_ARCADE_ENTRANCE("Exit", "dominion/shoppingArcade/exit", Colour.BASE_RED, Colour.MAP_BACKGROUND, ShoppingArcadeDialogue.ENTRY, null, false, true, true, "in the Shopping Arcade"),
	
	
	// Supplier Depot:
	
	SUPPLIER_DEPOT_CORRIDOR("Corridor", null, Colour.BASE_BLACK, Colour.MAP_BACKGROUND, SupplierDepot.SUPPLIER_DEPOT_CORRIDOR, null, false, true, false, "in the supplier depot"),
	
	SUPPLIER_DEPOT_ENTRANCE("Reception Area", "dominion/shoppingArcade/exit", Colour.BASE_GREY, Colour.MAP_BACKGROUND, SupplierDepot.SUPPLIER_DEPOT_RECEPTION, null, false, true, false, "in the supplier depot") {
		@Override
		public boolean isPopulated() {
			return Main.game.getPlayer().isQuestCompleted(QuestLine.RELATIONSHIP_NYAN_HELP);
		}
		@Override
		public String getIcon(String context, Set<PlaceUpgrade> upgrades) {
			if(Main.game.getPlayer().isQuestCompleted(QuestLine.RELATIONSHIP_NYAN_HELP)) {
				return IconCache.INSTANCE.getIcon(context, "map/dominion/shoppingArcade/exit.svg", Colour.BASE_GREEN);
			} else {
				return super.getIcon(context, upgrades);
			}
		}
	},
	
	SUPPLIER_DEPOT_STORAGE_ROOM("Storage Room", "dominion/shoppingArcade/supplierStorageRoom", Colour.BASE_ORANGE, Colour.MAP_BACKGROUND, SupplierDepot.SUPPLIER_DEPOT_STORAGE_ROOM, null, false, true, false, "in the supplier depot"),
	
	SUPPLIER_DEPOT_OFFICE("Office", "dominion/shoppingArcade/supplierOffice", Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, SupplierDepot.SUPPLIER_DEPOT_OFFICE, null, true, true, false, "in the supplier depot") {
		@Override
		public boolean isDangerous() {
			return !Main.game.getPlayer().isQuestCompleted(QuestLine.RELATIONSHIP_NYAN_HELP);
		}
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.suppliersEncountered)) {
				return SupplierDepot.SUPPLIER_DEPOT_OFFICE_REPEAT;
				
			} else {
				return SupplierDepot.SUPPLIER_DEPOT_OFFICE;
			}
		}
	},
	
	
	// Slaver Alley:
	
	SLAVER_ALLEY_PATH("Alleyway", null, Colour.BASE_BLACK, Colour.MAP_BACKGROUND, SlaverAlleyDialogue.ALLEYWAY, null, false, true, true, "in Slaver's Alley") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},

	SLAVER_ALLEY_MARKET_STALL("Slaver's Shop", "dominion/slaverAlley/marketStall", Colour.BASE_BLACK, Colour.MAP_BACKGROUND, SlaverAlleyDialogue.MARKET_STALL, null, false, true, true, "in Slaver's Alley") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},
	
	SLAVER_ALLEY_AUCTIONING_BLOCK("Auctioning Block", "dominion/slaverAlley/auctionBlock", Colour.BASE_GOLD, Colour.MAP_BACKGROUND, SlaverAlleyDialogue.AUCTION_BLOCK, null, false, true, true, "in Slaver's Alley") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},

	SLAVER_ALLEY_PUBLIC_STOCKS("Public Stocks", "dominion/slaverAlley/stocks", Colour.BASE_TAN, Colour.MAP_BACKGROUND, SlaverAlleyDialogue.PUBLIC_STOCKS, null, false, true, true, "in the stocks at Slaver's Alley") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},

	SLAVER_ALLEY_SLAVERY_ADMINISTRATION("Slavery Administration", "dominion/slaverAlley/slaveryAdministration", Colour.BASE_PURPLE, Colour.MAP_BACKGROUND, SlaverAlleyDialogue.SLAVERY_ADMINISTRATION_EXTERIOR, null, false, true, true, "in Slaver's Alley"){
		@Override
		public ArrayList<PlaceUpgrade> getStartingPlaceUpgrades() {
			return Util.newArrayListOfValues(PlaceUpgrade.SLAVERY_ADMINISTRATION_CELLS);
		}
	},
	
	SLAVER_ALLEY_SCARLETTS_SHOP("Scarlett's Shop", "dominion/slaverAlley/scarlettsStall", Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, ScarlettsShop.SCARLETTS_SHOP_EXTERIOR, null, false, true, true, "in Slaver's Alley"){
		@Override
		public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
			if(Main.game.getPlayer().isQuestProgressLessThan(QuestLine.MAIN, Quest.MAIN_1_F_SCARLETTS_FATE)) { // Scarlett owns the shop:
				return ScarlettsShop.SCARLETTS_SHOP_EXTERIOR;
				
			} else { // Alexa owns the shop:
				return ScarlettsShop.ALEXAS_SHOP_EXTERIOR;
			}
		}	
	},
	
	SLAVER_ALLEY_ENTRANCE("Gateway", "dominion/slaverAlley/exit", Colour.BASE_RED, Colour.MAP_BACKGROUND, SlaverAlleyDialogue.GATEWAY, null, false, true, true, "in Slaver's Alley") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.DOMINION);
		}
	},
	
	// Nightlife:

	
	WATERING_HOLE_ENTRANCE("Entrance", "dominion/nightLife/exit", Colour.BASE_RED, Colour.MAP_BACKGROUND, NightlifeDistrict.WATERING_HOLE_ENTRANCE, null, false, true, true, "in 'The Watering Hole'") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.NIGHTLIFE_CLUB);
		}
	},

	WATERING_HOLE_MAIN_AREA("The Watering Hole", null, Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, NightlifeDistrict.WATERING_HOLE_MAIN, null, false, true, true, "in 'The Watering Hole'") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.NIGHTLIFE_CLUB);
		}
	},

	WATERING_HOLE_SEATING_AREA("Seating Area", "dominion/nightLife/seatingArea", Colour.BASE_BLUE, Colour.MAP_BACKGROUND, NightlifeDistrict.WATERING_HOLE_SEATING, null, false, true, true, "in 'The Watering Hole'") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.NIGHTLIFE_CLUB);
		}
	},

	WATERING_HOLE_VIP_AREA("VIP Area", "dominion/nightLife/vipArea", Colour.BASE_PURPLE, Colour.MAP_BACKGROUND, NightlifeDistrict.WATERING_HOLE_VIP, null, false, true, true, "in 'The Watering Hole'") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Util.newArrayListOfValues(
					Subspecies.CAT_MORPH_LION,
					Subspecies.HORSE_MORPH_ZEBRA);
		}
	},

	WATERING_HOLE_BAR("VIP Area", "dominion/nightLife/bar", Colour.BASE_ORANGE, Colour.MAP_BACKGROUND, NightlifeDistrict.WATERING_HOLE_BAR, null, false, true, true, "in 'The Watering Hole'") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.NIGHTLIFE_CLUB);
		}
	},

	WATERING_HOLE_DANCE_FLOOR("Dance Floor", "dominion/nightLife/danceFloor", Colour.BASE_PINK_DEEP, Colour.MAP_BACKGROUND, NightlifeDistrict.WATERING_HOLE_DANCE_FLOOR, null, false, true, true, "in 'The Watering Hole'") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.NIGHTLIFE_CLUB);
		}
	},

	WATERING_HOLE_TOILETS("Toilets", "dominion/nightLife/toilets", Colour.BASE_GREEN_DARK, Colour.MAP_BACKGROUND, NightlifeDistrict.WATERING_HOLE_TOILETS, null, false, true, true, "in the toilets of 'The Watering Hole'") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.NIGHTLIFE_CLUB);
		}
	},
	
	
	// Submission:
	
	SUBMISSION_WALKWAYS("Walkways", null, Colour.BASE_BLACK, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.WALKWAYS, null, false, true, true, "in Submission") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.SUBMISSION);
		}
	},

	SUBMISSION_TUNNELS("Tunnels", "submission/tunnelsIcon", Colour.BASE_BLACK, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.TUNNEL, Encounter.SUBMISSION_TUNNELS, true, true, true, "in Submission"),
	
	SUBMISSION_BAT_CAVERNS("Bat Caverns", "submission/batCaverns", Colour.BASE_BLUE, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.BAT_CAVERNS, null, false, true, true, "in Submission"), // Insert batman reference here.
	
	SUBMISSION_RAT_WARREN("Rat Warren", "submission/ratWarren", Colour.BASE_BROWN_DARK, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.RAT_WARREN, null, false, true, true, "in Submission"),
	
	SUBMISSION_GAMBLING_DEN("Gambling Den", "submission/gamblingDen", Colour.BASE_GOLD, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.GAMBLING_DEN, null, false, true, true, "in Submission"),
	
	SUBMISSION_LILIN_PALACE("Lyssieth's Palace", "submission/lilinPalace", Colour.BASE_PURPLE, Colour.MAP_BACKGROUND_DARK, SubmissionGenericPlaces.LILIN_PALACE, null, false, true, true, "in Submission"),
	SUBMISSION_LILIN_PALACE_GATE("Lyssieth's Palace Gate", "submission/gate", Colour.BASE_PURPLE_LIGHT, Colour.MAP_BACKGROUND_DARK, SubmissionGenericPlaces.LILIN_PALACE_GATE, null, false, true, true, "in Submission"),
	SUBMISSION_LILIN_PALACE_CAVERN("Cavern", null, null, Colour.MAP_BACKGROUND_DARK, SubmissionGenericPlaces.LILIN_PALACE_CAVERN, null, false, true, true, "in Submission"),

	SUBMISSION_IMP_FORTRESS_1("Imp Fortress", "submission/impFortress1", Colour.BASE_CRIMSON, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.IMP_FORTRESS_1, null, false, true, true, "in Submission"),
	SUBMISSION_IMP_FORTRESS_2("Imp Fortress", "submission/impFortress2", Colour.BASE_MAGENTA, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.IMP_FORTRESS_2, null, false, true, true, "in Submission"),
	SUBMISSION_IMP_FORTRESS_3("Imp Fortress", "submission/impFortress3", Colour.BASE_PINK, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.IMP_FORTRESS_3, null, false, true, true, "in Submission"),
	SUBMISSION_IMP_FORTRESS_4("Imp Fortress", "submission/impFortress4", Colour.BASE_PINK_LIGHT, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.IMP_FORTRESS_4, null, false, true, true, "in Submission"),
	SUBMISSION_IMP_FORTRESS_5("Imp Fortress", "submission/impFortress5", Colour.BASE_PURPLE, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.IMP_FORTRESS_5, null, false, true, true, "in Submission"),
	SUBMISSION_IMP_FORTRESS_6("Imp Fortress", "submission/impFortress6", Colour.BASE_PURPLE_LIGHT, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.IMP_FORTRESS_6, null, false, true, true, "in Submission"),

	SUBMISSION_ENTRANCE("Enforcer Checkpoint", "submission/submissionExit", Colour.BASE_BROWN, Colour.MAP_BACKGROUND, SubmissionGenericPlaces.SEWER_ENTRANCE, null, false, true, true, "in Submission") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.SUBMISSION);
		}
	},
	
	// Bat caverns:

	BAT_CAVERN_ENTRANCE("Winding Staircase", "submission/batCaverns/cavernStaircase", Colour.BASE_GREEN, Colour.MAP_BACKGROUND, BatCaverns.STAIRCASE, null, false, true, true, "in the Bat Caverns"),
	BAT_CAVERN_DARK("Dark Cavern", null, null, Colour.MAP_BACKGROUND, BatCaverns.CAVERN_DARK, Encounter.BAT_CAVERN, true, true, true, "in the Bat Caverns"),
	BAT_CAVERN_LIGHT("Bioluminescent Cavern", "submission/batCaverns/cavernBioluminescent", Colour.BASE_AQUA, Colour.MAP_BACKGROUND, BatCaverns.CAVERN_LIGHT, Encounter.BAT_CAVERN, true, true, true, "in the Bat Caverns"),
	BAT_CAVERN_RIVER("Underground River", "submission/batCaverns/cavernRiver", Colour.BASE_BLUE, Colour.MAP_BACKGROUND, BatCaverns.RIVER, Encounter.BAT_CAVERN, true, true, true, "in the Bat Caverns"),
	BAT_CAVERN_RIVER_CROSSING("Mushroom Bridge", "submission/batCaverns/cavernBridge", Colour.BASE_TEAL, Colour.MAP_BACKGROUND, BatCaverns.RIVER_BRIDGE, Encounter.BAT_CAVERN, true, true, true, "in the Bat Caverns"),
	BAT_CAVERN_RIVER_END("Underground River End", "submission/batCaverns/cavernRiverEnd", Colour.BASE_BLUE_DARK, Colour.MAP_BACKGROUND, BatCaverns.RIVER_END, Encounter.BAT_CAVERN, true, true, true, "in the Bat Caverns"),
	BAT_CAVERN_SLIME_QUEEN_LAIR("Slime Lake", "submission/batCaverns/cavernLake", Colour.BASE_PINK_LIGHT, Colour.MAP_BACKGROUND, BatCaverns.SLIME_LAKE, Encounter.BAT_CAVERN, true, true, true, "beside Slime Lake"),
	

	SLIME_QUEENS_LAIR_CORRIDOR("Corridor", null, null, Colour.MAP_BACKGROUND, SlimeQueensLair.CORRIDOR, null, false, true, true, "in the Slime Queen's tower"),
	SLIME_QUEENS_LAIR_ENTRANCE("Entrance Hall", "submission/slimeQueensLair/entranceHall", Colour.BASE_RED, Colour.MAP_BACKGROUND, SlimeQueensLair.ENTRANCE, null, false, true, true, "in the Slime Queen's tower"),
	SLIME_QUEENS_LAIR_STAIRS_UP("Spiral Staircase", "submission/slimeQueensLair/staircase", Colour.BASE_GREEN, Colour.MAP_BACKGROUND, SlimeQueensLair.STAIRCASE_UP, null, false, true, true, "in the Slime Queen's tower"),
	SLIME_QUEENS_LAIR_STAIRS_DOWN("Spiral Staircase", "submission/slimeQueensLair/staircase", Colour.BASE_RED, Colour.MAP_BACKGROUND, SlimeQueensLair.STAIRCASE_DOWN, null, false, true, true, "in the Slime Queen's tower"),
	SLIME_QUEENS_LAIR_ROOM("Bedroom", "submission/slimeQueensLair/room", Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, SlimeQueensLair.ROOM, null, false, true, true, "in the Slime Queen's tower"),
	SLIME_QUEENS_LAIR_STORAGE_VATS("Distillery", "submission/slimeQueensLair/storageVats", Colour.BASE_ORANGE, Colour.MAP_BACKGROUND, SlimeQueensLair.STORAGE_VATS, null, false, true, false, "in the Slime Queen's tower") {
		@Override
		public void applyInventoryInit(CharacterInventory inventory) {
			for(int i=0; i<15; i++) {
				inventory.addItem(AbstractItemType.generateItem(ItemType.SEX_INGREDIENT_SLIME_QUENCHER));
			}
			for(int i=0; i<5; i++) {
				inventory.addItem(AbstractItemType.generateItem(ItemType.RACE_INGREDIENT_SLIME));
			}
		}
	},
	SLIME_QUEENS_LAIR_ENTRANCE_GUARDS("Guard Post", "submission/slimeQueensLair/guards", Colour.BASE_RED, Colour.MAP_BACKGROUND, SlimeQueensLair.GUARD_POST, null, true, true, true, "in the Slime Queen's tower"),
	SLIME_QUEENS_LAIR_ROYAL_GUARD("Royal Guard Post", "submission/slimeQueensLair/royalGuards", Colour.BASE_PURPLE, Colour.MAP_BACKGROUND, SlimeQueensLair.ROYAL_GUARD_POST, null, true, true, true, "in the Slime Queen's tower"),
	SLIME_QUEENS_LAIR_SLIME_QUEEN("Bed Chamber", "submission/slimeQueensLair/bedChamber", Colour.BASE_PINK, Colour.MAP_BACKGROUND, SlimeQueensLair.BED_CHAMBER, null, false, true, true, "in the Slime Queen's tower"),
	
	
	// Gambling Den:
	
	GAMBLING_DEN_CORRIDOR("Gambling Den", null, Colour.BASE_BLACK, Colour.MAP_BACKGROUND, GamblingDenDialogue.CORRIDOR, null, false, true, true, "in the Gambling Den") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.SUBMISSION);
		}
	},

	GAMBLING_DEN_ENTRANCE("Entrance", "submission/gamblingDen/entrance", Colour.BASE_GREEN, Colour.MAP_BACKGROUND, GamblingDenDialogue.ENTRANCE, null, false, true, true, "in the Gambling Den") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.SUBMISSION);
		}
	},
	
	GAMBLING_DEN_TRADER("Trader", "submission/gamblingDen/trader", Colour.BASE_TEAL, Colour.MAP_BACKGROUND, GamblingDenDialogue.TRADER, null, false, true, true, "in the Gambling Den"),
	GAMBLING_DEN_GAMBLING("Dice Poker Tables", "submission/gamblingDen/gambling", Colour.BASE_GOLD, Colour.MAP_BACKGROUND, GamblingDenDialogue.GAMBLING, null, false, true, true, "in the Gambling Den") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.SUBMISSION);
		}
	},
	GAMBLING_DEN_PREGNANCY_ROULETTE("Pregnancy Roulette", "submission/gamblingDen/referee", Colour.BASE_PINK, Colour.MAP_BACKGROUND, GamblingDenDialogue.PREGNANCY_ROULETTE, null, false, true, true, "in the Gambling Den"),
	GAMBLING_DEN_PREGNANCY("Breeding Stalls", "submission/gamblingDen/normalPregnancy", Colour.BASE_BLUE_LIGHT, Colour.MAP_BACKGROUND, GamblingDenDialogue.PREGNANCY_ROULETTE_MALE_STALLS, null, false, true, true, "in the Gambling Den") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.SUBMISSION);
		}
	},
	GAMBLING_DEN_FUTA_PREGNANCY("Futa Breeding Stalls", "submission/gamblingDen/futaPregnancy", Colour.BASE_PINK_LIGHT, Colour.MAP_BACKGROUND, GamblingDenDialogue.PREGNANCY_ROULETTE_FUTA_STALLS, null, false, true, true, "in the Gambling Den") {
		@Override
		public List<Subspecies> getSpeciesPopulatingArea() {
			return Subspecies.getWorldSpecies().get(WorldType.SUBMISSION);
		}
	},
	
	
	;

	
	private String name;
	protected String iconPath;
	private Colour iconColour;
	private Colour backgroundColour;
	protected DialogueNodeOld dialogue;
	private Encounter encounterType;
	private boolean dangerous, stormImmune, itemsDisappear;
	private String virgintyLossDescription;
	
	private PlaceType(String name,
			String pathName,
			Colour colour,
			Colour backgroundColour,
			DialogueNodeOld dialogue,
			Encounter encounterType,
			boolean dangerous,
			boolean stormImmune,
			boolean itemsDisappear,
			String virgintyLossDescription) {
		
		this.name = name;
		this.backgroundColour = backgroundColour;
		this.dialogue = dialogue;
		this.encounterType = encounterType;
		this.dangerous = dangerous;
		this.stormImmune = stormImmune;
		this.itemsDisappear = itemsDisappear;
		this.virgintyLossDescription = virgintyLossDescription;

		iconColour = colour;
		iconPath = pathName == null ? "" : "map/" + pathName + ".svg";
	}

	public String getName() {
		return name;
	}

	public Colour getColour() {
		return iconColour;
	}

	public Colour getBackgroundColour() {
		return backgroundColour;
	}

	public Encounter getEncounterType() {
		return encounterType;
	}

	public DialogueNodeOld getDialogue(boolean withRandomEncounter) {
		return getDialogue(withRandomEncounter, false);
	}
	
	public DialogueNodeOld getDialogue(boolean withRandomEncounter, boolean forceEncounter) {
		if (getEncounterType() != null && withRandomEncounter) {
			DialogueNodeOld dn = getEncounterType().getRandomEncounter(forceEncounter);
			if (dn != null) {
				return dn;
			}
		}

		return dialogue;
	}
	
	public List<Subspecies> getSpeciesPopulatingArea() {
		return null;
	}
	
	public boolean isPopulated() {
		return getSpeciesPopulatingArea()!=null && !getSpeciesPopulatingArea().isEmpty();
	}

	public boolean isDangerous() {
		return dangerous;
	}

	public boolean isStormImmune() {
		return stormImmune;
	}
	
	public boolean isItemsDisappear() {
		return itemsDisappear;
	}
	
	public String getIcon(String context, Set<PlaceUpgrade> upgrades) {
		return iconPath.isEmpty() ? "" : IconCache.INSTANCE.getIcon(context, iconPath, iconColour);
	}
	
	public void applyInventoryInit(CharacterInventory inventory) {
		
	}
	
	// For determining where this place should be placed:
	
	public Bearing getBearing() {
		return null;
	}
	
	public WorldType getParentWorldType() {
		return null;
	}
	
	public PlaceType getParentPlaceType() {
		return null;
	}
	
	public EntranceType getParentAlignment() {
		return null;
	}
	
	public String getPlaceNameAppendFormat(int count) {
		return "";
	}
	
	public boolean isAbleToBeUpgraded() {
		return false;
	}
	
	public ArrayList<PlaceUpgrade> getStartingPlaceUpgrades() {
		return new ArrayList<>();
	}
	
	public ArrayList<PlaceUpgrade> getAvailablePlaceUpgrades(Set<PlaceUpgrade> upgrades) {
		return new ArrayList<>();
	}

	public String getVirgintyLossDescription() {
		return virgintyLossDescription;
	}
}
