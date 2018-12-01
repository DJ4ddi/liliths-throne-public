package com.lilithsthrone.game.dialogue.places.submission.impFortress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lilithsthrone.game.PropertyValue;
import com.lilithsthrone.game.character.CharacterUtils;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.attributes.Attribute;
import com.lilithsthrone.game.character.effects.Perk;
import com.lilithsthrone.game.character.fetishes.Fetish;
import com.lilithsthrone.game.character.gender.Gender;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.game.character.npc.misc.Elemental;
import com.lilithsthrone.game.character.npc.submission.ImpAttacker;
import com.lilithsthrone.game.character.quests.Quest;
import com.lilithsthrone.game.character.quests.QuestLine;
import com.lilithsthrone.game.character.race.Race;
import com.lilithsthrone.game.character.race.Subspecies;
import com.lilithsthrone.game.combat.DamageType;
import com.lilithsthrone.game.combat.Spell;
import com.lilithsthrone.game.dialogue.DialogueFlagValue;
import com.lilithsthrone.game.dialogue.DialogueNodeOld;
import com.lilithsthrone.game.dialogue.responses.Response;
import com.lilithsthrone.game.dialogue.responses.ResponseCombat;
import com.lilithsthrone.game.dialogue.responses.ResponseEffectsOnly;
import com.lilithsthrone.game.dialogue.responses.ResponseSex;
import com.lilithsthrone.game.dialogue.responses.ResponseTag;
import com.lilithsthrone.game.dialogue.utils.BodyChanging;
import com.lilithsthrone.game.dialogue.utils.InventoryInteraction;
import com.lilithsthrone.game.dialogue.utils.UtilText;
import com.lilithsthrone.game.inventory.clothing.AbstractClothingType;
import com.lilithsthrone.game.inventory.clothing.ClothingType;
import com.lilithsthrone.game.inventory.item.AbstractItem;
import com.lilithsthrone.game.inventory.item.ItemType;
import com.lilithsthrone.game.inventory.weapon.AbstractWeaponType;
import com.lilithsthrone.game.inventory.weapon.WeaponType;
import com.lilithsthrone.game.sex.Sex;
import com.lilithsthrone.game.sex.SexPace;
import com.lilithsthrone.game.sex.SexPositionSlot;
import com.lilithsthrone.game.sex.managers.SexManagerInterface;
import com.lilithsthrone.game.sex.managers.universal.SMKneeling;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Colour;
import com.lilithsthrone.utils.Util;
import com.lilithsthrone.utils.Util.Value;
import com.lilithsthrone.world.Cell;
import com.lilithsthrone.world.WorldType;
import com.lilithsthrone.world.places.PlaceType;

/**
 * @since 0.2.11
 * @version 0.2.12
 * @author Innoxia
 */
public class ImpCitadelDialogue {
	
	public static boolean isImpsDefeated() {
		return Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impFortressDemonImpsDefeated);
	}
	
	public static boolean isDefeated() {
		return Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impFortressDemonDefeated);
	}
	
	private static boolean isBossEncountered() {
		return Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impFortressDemonBossEncountered);
	}
	
	public static void clearFortress() {
		
		for(GameCharacter character : getBossGroup(false)) {
			character.setLocation(WorldType.EMPTY, PlaceType.GENERIC_HOLDING_CELL);
		}
		
		// Sort out boss:
		getBoss().setLocation(WorldType.LYSSIETH_PALACE, PlaceType.LYSSIETH_PALACE_THRONE_ROOM);
		Main.game.getLyssieth().addSlave((NPC) getBoss());
		
		// Increment quest:
		if(Main.game.getPlayer().getQuest(QuestLine.MAIN) == Quest.MAIN_2_B_SIRENS_CALL) {
			Main.game.getTextEndStringBuilder().append(Main.game.getPlayer().setQuestProgress(QuestLine.MAIN, Quest.MAIN_2_C_SIRENS_FALL));
		}
		
		Main.game.getDialogueFlags().impFortressDemonDefeatedTime = Main.game.getMinutesPassed();
		Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impFortressDemonDefeated, true);

		Main.game.getDialogueFlags().impFortressAlphaDefeatedTime = Main.game.getMinutesPassed();
		Main.game.getDialogueFlags().impFortressFemalesDefeatedTime = Main.game.getMinutesPassed();
		Main.game.getDialogueFlags().impFortressMalesDefeatedTime = Main.game.getMinutesPassed();

		// Move NPCs out of hiding:
		for(GameCharacter character : Main.game.getCharactersPresent(WorldType.EMPTY, PlaceType.GENERIC_HOLDING_CELL)) {
			if(character.getHomeLocationPlace().getPlaceType()==PlaceType.SUBMISSION_IMP_TUNNELS_DEMON) {
				character.returnToHome();
			}
		}
	}
	
	private static void banishImps() {
		Cell[][] grid =  Main.game.getWorlds().get(WorldType.IMP_FORTRESS_DEMON).getGrid();
		for(int i=0;i<grid.length;i++) {
			for(int j=0;j<grid[0].length;j++) {
				for(GameCharacter character : Main.game.getCharactersPresent(grid[i][j])) {
					if(character instanceof ImpAttacker && !character.isSlave()) {
						Main.game.banishNPC((NPC) character);
					}
				}
			}
		}
	}
	
	public static void spawnImps() {
		banishImps();
		
		List<GameCharacter> impGroup = new ArrayList<>();
		try {
			
			impGroup = new ArrayList<>();
			List<String> impAdjectives = new ArrayList<>();
			
			for(int i=0; i<6; i++) {
				Subspecies subspecies = i<3?Subspecies.IMP_ALPHA:Subspecies.IMP;
				
				ImpAttacker imp = new ImpAttacker(subspecies, Gender.getGenderFromUserPreferences(false, false), false);
				imp.setLevel(12-(i*2)+Util.random.nextInt(3));
				Main.game.addNPC(imp, false);
				if(i==0) {
					imp.setGenericName("alpha-imp leader");
					imp.equipMainWeaponFromNowhere(AbstractWeaponType.generateWeapon(WeaponType.getWeaponTypeFromId("innoxia_pipe_pipe")));
					imp.equipOffhandWeaponFromNowhere(AbstractWeaponType.generateWeapon(WeaponType.getWeaponTypeFromId("innoxia_crudeShield_crude_shield")));
					
				} else if(i==1) {
					imp.setGenericName("alpha-imp archer");
					imp.equipMainWeaponFromNowhere(AbstractWeaponType.generateWeapon(WeaponType.OFFHAND_BOW_AND_ARROW));
					
				} else if(i==2) {
					imp.setGenericName("alpha-imp arcanist");
					imp.setAttribute(Attribute.MAJOR_ARCANE, 50);
					imp.addSpell(Spell.ARCANE_AROUSAL);
					imp.addSpell(Spell.FIREBALL);
					imp.addSpell(Spell.ICE_SHARD);
					imp.addSpell(Spell.TELEKENETIC_SHOWER);
					
				} else {
					impAdjectives.add(CharacterUtils.setGenericName(imp, impAdjectives));
					imp.equipMainWeaponFromNowhere(AbstractWeaponType.generateWeapon(WeaponType.getWeaponTypeFromId("innoxia_pipe_pipe")));
				}
				impGroup.add(imp);
			}
			
			for(GameCharacter impCharacter : impGroup) {
				impCharacter.setLocation(Main.game.getPlayer().getWorldLocation(), Main.game.getPlayer().getLocation(), true);
				((NPC)impCharacter).equipClothing(true, true, true, true);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<GameCharacter> getImpGroup() {
		List<GameCharacter> impGuards = new ArrayList<>();

		for(GameCharacter character : Main.game.getCharactersPresent()) {
			if(character instanceof ImpAttacker && character.getPartyLeader()==null && !character.isSlave()) {
				impGuards.add(character);
			}
		}
		
		impGuards.sort((imp1, imp2) -> imp2.getLevel()-imp1.getLevel());
		return impGuards;
	}
	
	public static GameCharacter getImpGroupLeader() {
		return getImpGroup().get(0);
	}
	
	/**
	 * Only to be used in Game.importGame() for versions prior to 0.2.12.5
	 */
	public static void resetFortress() {
		
		Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impFortressDemonDefeated, false);
		
		banishImps();
		
		// Move boss back to fortress:
		Main.game.getFortressDemonLeader().setLocation(WorldType.IMP_FORTRESS_DEMON, PlaceType.FORTRESS_DEMON_KEEP);

		// Move defeated leaders into fortress:
		if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impFortressAlphaDefeated)) {
			Main.game.getFortressAlphaLeader().setLocation(WorldType.IMP_FORTRESS_DEMON, PlaceType.FORTRESS_DEMON_KEEP);
		}
		if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impFortressFemalesDefeated)) {
			Main.game.getFortressFemalesLeader().setLocation(WorldType.IMP_FORTRESS_DEMON, PlaceType.FORTRESS_DEMON_KEEP);
		}
		if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impFortressMalesDefeated)) {
			Main.game.getFortressMalesLeader().setLocation(WorldType.IMP_FORTRESS_DEMON, PlaceType.FORTRESS_DEMON_KEEP);
		}
		
		// Move NPCs into hiding:
		Cell[][] cells = Main.game.getWorlds().get(WorldType.SUBMISSION).getCellGrid();
		for(int i=0; i< cells.length;i++) {
			for(int j=0; j< cells[i].length;j++) {
				Cell cell = cells[j][i];
				if(cell.getPlace().getPlaceType()==PlaceType.SUBMISSION_IMP_TUNNELS_DEMON) {
					for(GameCharacter character : Main.game.getCharactersPresent(cell)) {
						if(!Main.game.getPlayer().getCompanions().contains(character)) {
							character.setHomeLocation(WorldType.SUBMISSION, character.getLocation());
							character.setLocation(WorldType.EMPTY, PlaceType.GENERIC_HOLDING_CELL);
						}
					}
				}
			}
		}
	}
	
	public static List<GameCharacter> getBossGroup(boolean includeBoss) {
		List<GameCharacter> bossGroup = new ArrayList<>();
		
		if(includeBoss) {
			bossGroup.add(getBoss());
		}
		bossGroup.add(Main.game.getFortressMalesLeader());
		bossGroup.add(Main.game.getFortressAlphaLeader());
		bossGroup.add(Main.game.getFortressFemalesLeader());
		
		return bossGroup;
	}

	public static NPC getDemonLeader() {
		return Main.game.getFortressMalesLeader();
	}

	public static GameCharacter getBoss() {
		return Main.game.getFortressDemonLeader();
	}
	
	public static GameCharacter getArcanist() {
		return Main.game.getSubmissionCitadelArcanist();
	}

	private static GameCharacter getMainCompanion() {
		return Main.game.getPlayer().getMainCompanion();
	}
	
	private static boolean isCompanionDialogue() {
		return !Main.game.getPlayer().getCompanions().isEmpty();
	}
	
	public static List<GameCharacter> getAllCharacters() {
		// There's a reason I can't just add all from getCharactersPresent(), but I forgot. Maybe it was because the Elemental companion gets added?
		List<GameCharacter> allCharacters = new ArrayList<>();
		
		if(isCompanionDialogue()) {
			allCharacters.add(getMainCompanion());
		}
		
		if(Main.game.getPlayer().getLocationPlace().getPlaceType()==PlaceType.FORTRESS_DEMON_KEEP) {
			allCharacters.add(getBoss());
			allCharacters.add(Main.game.getFortressMalesLeader());
			allCharacters.add(Main.game.getFortressAlphaLeader());
			allCharacters.add(Main.game.getFortressFemalesLeader());
		}

		// For the arcanist:
		for(NPC character : Main.game.getCharactersPresent()) {
			if(!allCharacters.contains(character)) {
				allCharacters.add(character);
			}
		}
		
		return allCharacters;
	}
	
	public static String getDialogueEncounterId() {
		if(isCompanionDialogue()) {
			return "Companions";
		}
		return "";
	}
	
	// Dialogues:
	
	private static Response getImpChallengeResponse() {
		return new Response("Challenge",
				"Declare that you've come to defeat all of the inhabitants of this citadel!<br/>"
						+ "<i>You think that there are about [style.boldBad(thirty imps)] in the citadel, so be prepared to fight this many as they arrive, in waves, to the aid of their allies!</i>",
				IMP_CHALLENGE) {
			@Override
			public boolean isCombatHighlight() {
				return true;
			}
			@Override
			public void effects() {
				Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_CHALLENGE", getAllCharacters()));
				Main.game.getDialogueFlags().impCitadelImpWave = 0;
				spawnImps();
				getArcanist().setLocation(WorldType.EMPTY, PlaceType.GENERIC_HOLDING_CELL);
			}
		};
	}
	
	public static final DialogueNodeOld ENTRANCE = new DialogueNodeOld("Gateway", "", false) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public int getMinutesPassed() {
			return 1;
		}
		
		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);

			if(isDefeated()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ENTRANCE_RUINS", getAllCharacters()));
				
			} else {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ENTRANCE", getAllCharacters()));
				
				if(isImpsDefeated()) {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "GENERIC_IMPS_DEFEATED", getAllCharacters()));
				} else {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "GENERIC_IMP_GUARDS", getAllCharacters()));
				}
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new Response("Leave", "Head back out into the tunnels.", PlaceType.SUBMISSION_IMP_FORTRESS_DEMON.getDialogue(false)) {
					@Override
					public void effects() {
						Main.game.getPlayer().setLocation(WorldType.SUBMISSION, PlaceType.SUBMISSION_IMP_FORTRESS_DEMON);
						Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LEAVE_FORTRESS", getAllCharacters()));
					}
				};

			} else if(index==5 && !isImpsDefeated()) {
				return getImpChallengeResponse();
			}
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_CHALLENGE = new DialogueNodeOld("", "", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			return "";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				try {
					return new ResponseCombat("Fight", "It's time to put these imps in their place!", (NPC) getImpGroupLeader(), getImpGroup(), null);
				} catch(Exception ex) {
				}
			}
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_CHALLENGE_CONTINUE = new DialogueNodeOld("", "", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);
			
			UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_CHALLENGE_CONTINUE", getAllCharacters()));
			
			switch(Main.game.getDialogueFlags().impCitadelImpWave) {
				case 1:
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_CHALLENGE_CONTINUE_WAVE_2", getAllCharacters()));
					break;
				case 2:
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_CHALLENGE_CONTINUE_WAVE_3", getAllCharacters()));
					break;
				case 3:
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_CHALLENGE_CONTINUE_WAVE_4", getAllCharacters()));
					break;
				case 4:
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_CHALLENGE_CONTINUE_WAVE_5", getAllCharacters()));
					break;
				case 5:
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_CHALLENGE_CONTINUE_WAVE_6", getAllCharacters()));
					break;
				default:
					break;
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				return new ResponseCombat("More imps", "Yet more imps arrive to save their fallen comrades!", (NPC) getImpGroup().get(0), getImpGroup(), null);
			}
			return null;
		}
	};
	
	//TODO Don't forget IMP_FIGHT_AFTER_COMBAT_VICTORY_ATTRIBUTE_BOOST
	public static final DialogueNodeOld IMP_FIGHT_AFTER_COMBAT_VICTORY = new DialogueNodeOld("Victory", ".", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public String getDescription() {
			return "You have defeated the imps!";
		}

		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_FIGHT_AFTER_COMBAT_VICTORY", getAllCharacters());
		}

		@Override
		public String getResponseTabTitle(int index) {
			if(index==0) {
				return "Standard";
				
			} else if(index==1) {
				return "Inventories";
				
			} else if(index==2) {
				return "Transformations";
				
			}
 			return null;
		}
		
		@Override
		public Response getResponse(int responseTab, int index) {
			if(!isCompanionDialogue()) {
				if(responseTab == 0) {
					if (index == 1) {
						return new Response("Scare off", "Tell the imps to get out of here while they still can...", Main.game.getDefaultDialogueNoEncounter()) {
							@Override
							public void effects() {
								Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_COMBAT_VICTORY_SCARE_OFF", getAllCharacters()));
								banishImps();
							}
						};
						
					} else if (index == 2) {
						return new ResponseSex("Sex",
								"Well, they <i>are</i> asking for it!",
								true,
								false,
								Main.game.getPlayer().getParty(),
								getImpGroup(),
								null,
								null,
								IMP_AFTER_SEX_VICTORY, UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_COMBAT_VICTORY_SEX", getAllCharacters()));
						
					} else if (index == 3) {
						return new ResponseSex("Gentle Sex",
								"Well, they <i>are</i> asking for it! (Start the sex scene in the 'gentle' pace.)",
								true,
								false,
								Main.game.getPlayer().getParty(),
								getImpGroup(),
								null,
								null,
								IMP_AFTER_SEX_VICTORY,
								UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_COMBAT_VICTORY_SEX_GENTLE", getAllCharacters()), ResponseTag.START_PACE_PLAYER_DOM_GENTLE);
						
					} else if (index == 4) {
						return new ResponseSex("Rough Sex",
								"Well, they <i>are</i> asking for it! (Start the sex scene in the 'rough' pace.)",
								true,
								false,
								Main.game.getPlayer().getParty(),
								getImpGroup(),
								null,
								null,
								IMP_AFTER_SEX_VICTORY,
								UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_COMBAT_VICTORY_SEX_ROUGH", getAllCharacters()), ResponseTag.START_PACE_PLAYER_DOM_ROUGH);
						
					} else if (index == 5) {
						return new ResponseSex("Submit",
								"You're not really sure what to do now... Perhaps it would be best to let the imps choose what to do next...",
								Util.newArrayListOfValues(Fetish.FETISH_SUBMISSIVE),
								null,
								Fetish.FETISH_SUBMISSIVE.getAssociatedCorruptionLevel(),
								null,
								null,
								null,
								true,
								false,
								getImpGroup(),
								Main.game.getPlayer().getParty(),
								null,
								null,
								IMP_AFTER_SEX_VICTORY, UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_COMBAT_VICTORY_SEX_SUBMIT", getAllCharacters()));
					}
					
				} else if(responseTab == 1) {
					for(int i=1; i<=getImpGroup().size(); i++) {
						if(index==i) {
							NPC imp = (NPC) getImpGroup().get(i-1);
							return new ResponseEffectsOnly(UtilText.parse(imp, "[npc.Name]"),
									UtilText.parse(imp, "Now that you've defeated [npc.name], there's nothing stopping you from helping yourself to [npc.her] clothing and items...")) {
								@Override
								public void effects() {
									Main.mainController.openInventory(imp, InventoryInteraction.FULL_MANAGEMENT);
								}
							};
						}
					}
					
				} else if(responseTab == 2) {
					for(int i=1; i<=getImpGroup().size(); i++) {
						if(index==i) {
							NPC imp = (NPC) getImpGroup().get(i-1);
							return new Response(UtilText.parse(imp, "[npc.Name]"),
									UtilText.parse(imp, "Take a very detailed look at what [npc.name] can transform [npc.herself] into..."),
									BodyChanging.BODY_CHANGING_CORE){
								@Override
								public void effects() {
									Main.game.saveDialogueNode();
									BodyChanging.setTarget(imp);
								}
							};
						}
					}
				}
				
				return null;
			
			} else {
				if(responseTab == 0) { //TODO Companion variations
					if (index == 1) {
						return new Response("Scare off", "Tell the imps to get out of here while they still can...", Main.game.getDefaultDialogueNoEncounter()) {
							@Override
							public void effects() {
								Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_COMBAT_VICTORY_SCARE_OFF", getAllCharacters()));
								banishImps();
							}
						};
						
					} else if (index == 2) {
						return new ResponseSex("Solo sex",
								UtilText.parse(getMainCompanion(), "Tell [npc.name] to stand to one side and watch as you have sex with the imps."),
								true,
								false,
								Util.newArrayListOfValues(Main.game.getPlayer()),
								getImpGroup(),
								null,
								Util.newArrayListOfValues(getMainCompanion()),
								IMP_AFTER_SEX_VICTORY, UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_SEX", getAllCharacters()));
						
					} else if (index == 3) {
						return new ResponseSex("Solo sex (Gentle)",
								UtilText.parse(getMainCompanion(), "Tell [npc.name] to stand to one side and watch as you have sex with the imps. (Start sex in the gentle pace.)"),
								true,
								false,
								Util.newArrayListOfValues(Main.game.getPlayer()),
								getImpGroup(),
								null,
								Util.newArrayListOfValues(getMainCompanion()),
								IMP_AFTER_SEX_VICTORY,
								UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_SEX_GENTLE", getAllCharacters()), ResponseTag.START_PACE_PLAYER_DOM_GENTLE);
						
					} else if (index == 4) {
						return new ResponseSex("Solo sex (Rough)",
								UtilText.parse(getMainCompanion(), "Tell [npc.name] to stand to one side and watch as you have sex with the imps. (Start sex in the rough pace.)"),
								true,
								false,
								Util.newArrayListOfValues(Main.game.getPlayer()),
								getImpGroup(),
								null,
								Util.newArrayListOfValues(getMainCompanion()),
								IMP_AFTER_SEX_VICTORY,
								UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_SEX_ROUGH", getAllCharacters()), ResponseTag.START_PACE_PLAYER_DOM_ROUGH);
						
					} else if (index == 5) {
						return new ResponseSex("Solo submission",
								UtilText.parse(getMainCompanion(), "Tell [npc.name] to stand to one side and watch as you submit to the imps, allowing them to have dominant sex with you."),
								Util.newArrayListOfValues(Fetish.FETISH_SUBMISSIVE),
								null,
								Fetish.FETISH_SUBMISSIVE.getAssociatedCorruptionLevel(),
								null,
								null,
								null,
								true,
								false,
								getImpGroup(),
								Util.newArrayListOfValues(Main.game.getPlayer()),
								null,
								Util.newArrayListOfValues(getMainCompanion()),
								IMP_AFTER_SEX_DEFEAT, UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_SEX_SUBMIT", getAllCharacters()));
						
					} else if (index == 6) {
						GameCharacter companion = getMainCompanion();

						if(!companion.isAttractedTo(getImpGroupLeader()) && !companion.isSlave() && !(companion instanceof Elemental)) {
							return new Response("Group sex",
									UtilText.parse(companion, "[npc.Name] is not interested in having sex with the imps, and as [npc.sheIs] not a slave, you can't force [npc.herHim] to do so..."), null);
							
						} else {
							return new ResponseSex(UtilText.parse(companion, "Group sex"),
									UtilText.parse(companion, "Have dominant sex with the imps, and get [npc.name] to join in with the fun."),
									true,
									false,
									Main.game.getPlayer().getParty(),
									getImpGroup(),
									null,
									null,
									IMP_AFTER_SEX_VICTORY, UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_GROUP_SEX", getAllCharacters()));
						}
						
					} else if (index == 7) {
						GameCharacter companion = getMainCompanion();

						if(!companion.isAttractedTo(getImpGroupLeader()) && !companion.isSlave() && !(companion instanceof Elemental)) {
							return new Response("Group submission",
									UtilText.parse(companion, "[npc.Name] is not interested in having sex with the imps, and as [npc.sheIs] not a slave, you can't force [npc.herHim] to do so..."), null);
							
						} else {
							return new ResponseSex(UtilText.parse(companion, "Group submission"),
									UtilText.parse(companion, "Get [npc.name] to join you in submitting to the imps, allowing them to have dominant sex with the two of you."),
									true,
									false,
									getImpGroup(),
									Main.game.getPlayer().getParty(),
									null,
									null,
									IMP_AFTER_SEX_VICTORY, UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_GROUP_SEX_SUBMISSION", getAllCharacters()));
						}
						
					} else if (index == 8) {
						GameCharacter companion = getMainCompanion();

						if(!companion.isAttractedTo(getImpGroupLeader()) && !companion.isSlave() && !(companion instanceof Elemental)) {
							return new Response(UtilText.parse(companion, "Give to [npc.name]"),
									UtilText.parse(companion, "[npc.Name] is not interested in having sex with the imps, and as [npc.sheIs] not a slave, you can't force [npc.herHim] to do so..."), null);
							
						} else {
							return new ResponseSex(UtilText.parse(companion, "Give to [npc.name]"),
									UtilText.parse(companion, "Tell [npc.name] that [npc.she] can have some fun with the imps while you watch."),
									false,
									false,
									Util.newArrayListOfValues(getMainCompanion()),
									getImpGroup(),
									null,
									Util.newArrayListOfValues(Main.game.getPlayer()),
									IMP_AFTER_SEX_VICTORY, UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_GIVE_TO_COMPANION", getAllCharacters()));
						}
						
					} else if (index == 9 && Main.getProperties().hasValue(PropertyValue.voluntaryNTR)) {
						GameCharacter companion = getMainCompanion();
						
						if(!companion.isAttractedTo(getImpGroupLeader()) && !companion.isSlave() && !(companion instanceof Elemental)) {
							return new Response(UtilText.parse(companion, "Offer [npc.name]"),
									UtilText.parse(companion, "You can tell that [npc.name] isn't at all interested in having sex with the imps, and as [npc.sheIs] not your slave, you can't force [npc.herHim] to do so..."),
									null);
							
						} else {
							return new ResponseSex(UtilText.parse(companion, "Offer [npc.name]"),
									UtilText.parse(companion, "Hand [npc.name] over to the imps, and watch as they have sex with [npc.herHim]."),
									true,
									false,
									getImpGroup(),
									Util.newArrayListOfValues(getMainCompanion()),
									null,
									Util.newArrayListOfValues(Main.game.getPlayer()),
									IMP_AFTER_SEX_VICTORY, UtilText.parseFromXMLFile("places/submission/impCitadelDemonCompanions", "IMP_COMBAT_VICTORY_OFFER_COMPANION", getAllCharacters())) {
								@Override
								public void effects() {
									if(!companion.isAttractedTo(getImpGroupLeader()) && Main.game.isNonConEnabled()) {
										Main.game.getTextEndStringBuilder().append(companion.incrementAffection(Main.game.getPlayer(), -50));
									}
								}
							};
						}
						
					} else {
						return null;
					}
					
				} else if(responseTab == 1) {
					for(int i=1; i<=getImpGroup().size(); i++) {
						if(index==i) {
							NPC imp = (NPC) getImpGroup().get(i-1);
							return new ResponseEffectsOnly(UtilText.parse(imp, "[npc.Name]"),
									UtilText.parse(imp, "Now that you've defeated [npc.name], there's nothing stopping you from helping yourself to [npc.her] clothing and items...")) {
								@Override
								public void effects() {
									Main.mainController.openInventory(imp, InventoryInteraction.FULL_MANAGEMENT);
								}
							};
						}
					}
					
				} else if(responseTab == 2) {
					for(int i=1; i<=getImpGroup().size(); i++) {
						if(index==i) {
							NPC imp = (NPC) getImpGroup().get(i-1);
							return new Response(UtilText.parse(imp, "[npc.Name]"),
									UtilText.parse(imp, "Take a very detailed look at what [npc.name] can transform [npc.herself] into..."),
									BodyChanging.BODY_CHANGING_CORE){
								@Override
								public void effects() {
									Main.game.saveDialogueNode();
									BodyChanging.setTarget(imp);
								}
							};
						}
					}
				}
				
				return null;
			}
		}
	};

	public static final DialogueNodeOld IMP_FIGHT_AFTER_COMBAT_DEFEAT = new DialogueNodeOld("Defeat", ".", true) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getDescription() {
			return "You have been defeated by the imps!";
		}

		@Override
		public String getContent() {
			if(Main.game.isNonConEnabled()) {
				if(Main.game.getPlayerCell().getPlace().getPlaceType()==PlaceType.FORTRESS_LAB) { //TODO remember Need variations for companion and player feminine
					return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_FIGHT_AFTER_COMBAT_DEFEAT_IN_LAB", getAllCharacters());
				}
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_FIGHT_AFTER_COMBAT_DEFEAT", getAllCharacters());
				
			} else {
				if(Main.game.getPlayerCell().getPlace().getPlaceType()==PlaceType.FORTRESS_LAB) {
					return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_FIGHT_AFTER_COMBAT_DEFEAT_IN_LAB_NO_NON_CON", getAllCharacters());
				}
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_FIGHT_AFTER_COMBAT_DEFEAT_NO_NON_CON", getAllCharacters());
			}
		}
		
		@Override
		public Response getResponse(int responseTab, int index) {
			if(!Main.game.isNonConEnabled()) {
				if(index==1) {
					return new Response("Recover", "Take a moment to catch your breath, and then continue on your way.", Main.game.getDefaultDialogue()) {
						@Override
						public void effects() {
							banishImps();
							Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_FIGHT_AFTER_COMBAT_DEFEAT_RECOVER", getAllCharacters()));
						}
					};
				}
				return null;
			}
			
			if(Main.game.getPlayerCell().getPlace().getPlaceType()==PlaceType.FORTRESS_LAB
					&& (Main.game.getPlayer().isFeminine() || (isCompanionDialogue() && getMainCompanion().isFeminine() && Main.getProperties().hasValue(PropertyValue.involuntaryNTR)))) {
				
				Map<GameCharacter, SexPositionSlot> subSlots;
				List<GameCharacter> spectators = new ArrayList<>();
				
				if(!Main.game.getPlayer().isFeminine()) {
					spectators.add(Main.game.getPlayer());
					subSlots = Util.newHashMapOfValues(new Value<>(getMainCompanion(), SexPositionSlot.KNEELING_PERFORMING_ORAL));
					
				} else if(isCompanionDialogue () && !getMainCompanion().isFeminine()) {
					spectators.add(getMainCompanion());
					subSlots = Util.newHashMapOfValues(new Value<>(Main.game.getPlayer(), SexPositionSlot.KNEELING_PERFORMING_ORAL));
					
				} else {
					if(isCompanionDialogue()) {
						subSlots = Util.newHashMapOfValues(
								new Value<>(Main.game.getPlayer(), SexPositionSlot.KNEELING_PERFORMING_ORAL),
								new Value<>(getMainCompanion(), SexPositionSlot.KNEELING_PERFORMING_ORAL_TWO));
					} else {
						subSlots = Util.newHashMapOfValues(new Value<>(Main.game.getPlayer(), SexPositionSlot.KNEELING_PERFORMING_ORAL));
					}
				}

				SexManagerInterface manager = new SMKneeling(
						Util.newHashMapOfValues(new Value<>(getArcanist(), SexPositionSlot.KNEELING_RECEIVING_ORAL)),
						subSlots) {
					@Override
					public boolean isPositionChangingAllowed(GameCharacter character) {
						return false;
					}
					@Override
					public SexPace getStartingSexPaceModifier(GameCharacter character) {
						if(character.isPlayer()) {
							if(index==2) {
								return SexPace.SUB_EAGER;
							} else if(index==3) {
								return SexPace.SUB_RESISTING;
							}
						}
						return null;
					}
				};
				
				if(index==1) {
					return new ResponseSex(
							"Sex",
							isCompanionDialogue()
								?UtilText.parse(getArcanist(), getMainCompanion(), "Accept the price of your defeat, and, alongside [pc2.name], prepare to perform oral on [npc.name].")
								:UtilText.parse(getArcanist(), "Accept the price of your defeat, and prepare to perform oral on [npc.name]."),
							false,
							false,
							manager,
							null,
							null,
							IMP_AFTER_SEX_DEFEAT,
							UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_AFTER_COMBAT_DEFEAT_SEX_ARCANIST", getAllCharacters()));
					
				} else if(index==2) {
					return new ResponseSex(
							"Eager sex",
							isCompanionDialogue()
								?UtilText.parse(getArcanist(), getMainCompanion(), "Happily accept what's being demanded of you, and, alongside [pc2.name], eagerly prepare to perform oral on [npc.name].")
								:UtilText.parse(getArcanist(), "Happily accept what's being demanded of you, and eagerly prepare to perform oral on [npc.name]."),
							false,
							false,
							manager,
							null,
							null,
							IMP_AFTER_SEX_DEFEAT,
							UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_AFTER_COMBAT_DEFEAT_SEX_ARCANIST_EAGER", getAllCharacters()));

				} else if (index == 3 && Main.game.isNonConEnabled()) {
					return new ResponseSex(
							"Resist sex",
							UtilText.parse(getArcanist(), "Struggle against [npc.name] and try to push [npc.herHim] away from you."),
							false,
							false,
							manager,
							null,
							null,
							IMP_AFTER_SEX_DEFEAT,
							UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_AFTER_COMBAT_DEFEAT_SEX_ARCANIST_RESIST", getAllCharacters()));
				}
				
			} else {
				if (index == 1) {
					return new ResponseSex("Sex",
							"Allow the imps to move you into position...",
							false,
							false,
							getImpGroup(),
							Main.game.getPlayer().getParty(),
							null,
							null,
							IMP_AFTER_SEX_DEFEAT,
							UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_AFTER_COMBAT_DEFEAT_SEX", getAllCharacters()));
					
				} else if (index == 2) {
					return new ResponseSex("Eager sex",
							"Eagerly allow yourself to be moved into position by the gang of imps...",
							false,
							false,
							getImpGroup(),
							Main.game.getPlayer().getParty(),
							null,
							null,
							IMP_AFTER_SEX_DEFEAT,
							UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_AFTER_COMBAT_DEFEAT_SEX_EAGER", getAllCharacters()),
							ResponseTag.START_PACE_PLAYER_SUB_EAGER);
					
				} else if (index == 3 && Main.game.isNonConEnabled()) {
					return new ResponseSex("Resist sex",
							"Try to resist as the gang of imps move you into position...",
							false,
							false,
							getImpGroup(),
							Main.game.getPlayer().getParty(),
							null,
							null,
							IMP_AFTER_SEX_DEFEAT,
							UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_AFTER_COMBAT_DEFEAT_SEX_RESIST", getAllCharacters()),
							ResponseTag.START_PACE_PLAYER_SUB_RESIST);
				}
			}
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_AFTER_SEX_VICTORY = new DialogueNodeOld("Step back", "", true) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getDescription(){
			return "Now that you've had your fun, you can step back and leave the imps to recover and disperse.";
		}

		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "IMP_AFTER_SEX_VICTORY", getAllCharacters());
		}

		@Override
		public String getResponseTabTitle(int index) {
			if(index == 0 || index == 1) {
				return IMP_FIGHT_AFTER_COMBAT_VICTORY.getResponseTabTitle(index);
			}
			return null;
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(responseTab==0) {
				if (index == 1) {
					return new Response("Scare off", "Scare the imps off and continue on your way.", Main.game.getDefaultDialogueNoEncounter()) {
						@Override
						public void effects() {
							banishImps();
						}
					};
				}
				
			} else if(responseTab==1) {
				return IMP_FIGHT_AFTER_COMBAT_VICTORY.getResponse(responseTab, index);
			}
			
			return null;
		}
	};
	
	public static final DialogueNodeOld IMP_AFTER_SEX_DEFEAT = new DialogueNodeOld("Collapse", "", true) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public int getMinutesPassed(){
			return 15;
		}
		
		@Override
		public String getDescription(){
			return "You're completely worn out from [npc.namePos] dominant treatment, and need a while to recover.";
		}

		@Override
		public String getContent() {
			if(Sex.getAllParticipants().contains(getArcanist())) { //TODO from here
				if(Sex.getAllParticipants().contains(getMainCompanion())) {
					return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "AFTER_IMP_DEFEAT_ARCANIST_SEX_WITH_COMPANION", getAllCharacters());
				} else {
					return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "AFTER_IMP_DEFEAT_ARCANIST_SEX", getAllCharacters());
				}
			}
			if(Sex.getAllParticipants().contains(getMainCompanion())) {
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "AFTER_IMP_DEFEAT_SEX_WITH_COMPANION", getAllCharacters());
			} else {
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "AFTER_IMP_DEFEAT_SEX", getAllCharacters());
			}
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new Response("Continue", "Carry on your way.", Main.game.getDefaultDialogueNoEncounter());
				
			} else {
				return null;
			}
		}
	};

	public static final DialogueNodeOld COURTYARD = new DialogueNodeOld("Courtyard", ".", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);

			if(isDefeated()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "COURTYARD_RUINS", getAllCharacters()));
				
			} else {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "COURTYARD", getAllCharacters()));
				
				if(isImpsDefeated()) {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "GENERIC_IMPS_DEFEATED", getAllCharacters()));
				} else {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "GENERIC_IMP_GUARDS", getAllCharacters()));
				}
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==5 && !isImpsDefeated()) {
				return getImpChallengeResponse();
			}
			return null;
		}
	};

	public static final DialogueNodeOld WELL = new DialogueNodeOld("Well", ".", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);

			if(isDefeated()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "WELL_RUINS", getAllCharacters()));
				
			} else {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "WELL", getAllCharacters()));
				
				if(isImpsDefeated()) {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "GENERIC_IMPS_DEFEATED", getAllCharacters()));
				} else {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "GENERIC_IMP_GUARDS", getAllCharacters()));
				}
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==5 && !isImpsDefeated()) {
				return getImpChallengeResponse();
			}
			
			//TODO escape method
			return null;
		}
	};

	public static final DialogueNodeOld LABORATORY = new DialogueNodeOld("Laboratory", ".", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);

			if(isDefeated()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_RUINS", getAllCharacters()));
				
			} else {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY", getAllCharacters()));
				
				if(isImpsDefeated()) {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_IMPS_DEFEATED", getAllCharacters()));
				} else {
					if(getArcanist().isPlayerKnowsName()) {
						UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_IMPS_ARCANIST_MET", getAllCharacters()));
					} else {
						UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_IMPS", getAllCharacters()));
					}
				}
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(!isDefeated() && !isImpsDefeated()) {
				if(index==1) {
					return new Response("Arcanist", "Approach the imp arcanist who's overseeing the production of transformation potions.", LABORATORY_ARCANIST) {
						@Override
						public void effects() {
							getArcanist().setPlayerKnowsName(true);
						}
					};
				}
			}
			return null;
		}
	};

	public static final DialogueNodeOld LABORATORY_ARCANIST = new DialogueNodeOld("", ".", true, true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			// TODO Go over all of this and check for companion variations
				// She offers to eat you out if she can use TF potion on you (doesn't mention denying you)
				// If imps defeated, change option into 'loot' - gain two fox-girl potions
			UtilText.nodeContentSB.setLength(0);
			if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelArcanistEncountered)) {
				if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelArcanistAcceptedTF)) {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_REPEAT_NO_TF", getAllCharacters()));
				} else {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_REPEAT", getAllCharacters()));
				}
			} else {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST", getAllCharacters()));
			}
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				return new Response("Decline", UtilText.parse(getArcanist(), "Say no to [npc.name], and leave [npc.herHim] to [npc.her] work."), LABORATORY_ARCANIST_EXIT) {
					@Override
					public void effects() {
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistEncountered, true);
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_DECLINE", getAllCharacters()));
					}
				};
				
			// Action 2: Player accepts TF potion.
			// If already drank TF potion, or is already a fox-morph, action is for performing oral sex.
			} else if(index==2) {
				if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelArcanistAcceptedTF) || Main.game.getPlayer().getRace()==Race.FOX_MORPH) {
					if(!Main.game.getPlayer().isFeminine()) {
						return new Response(isCompanionDialogue()?"Oral (solo)":"Oral",
								UtilText.parse(getArcanist(), "As [npc.sheIsFull] gynephilic, [npc.name] is not interested in having you perform oral on her."),
								null);
					} else {
						return new ResponseSex(isCompanionDialogue()?"Oral (solo)":"Oral",
								UtilText.parse(getArcanist(), "Agree to do as [npc.name] says, and get down on your knees to perform oral on her."),
								true,
								false,
								new SMKneeling(
										Util.newHashMapOfValues(new Value<>(getArcanist(), SexPositionSlot.KNEELING_RECEIVING_ORAL)),
										Util.newHashMapOfValues(new Value<>(Main.game.getPlayer(), SexPositionSlot.KNEELING_PERFORMING_ORAL))){
									@Override
									public boolean isPositionChangingAllowed(GameCharacter character) {
										return false;
									}
								},
								null,
								Util.newArrayListOfValues(getMainCompanion()),
								LABORATORY_ARCANIST_POST_SEX,
								UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ARCANIST_SOLO_START_SEX", getAllCharacters()));
					}
				}
				return new Response(isCompanionDialogue()?"Accept (solo)":"Accept",
						UtilText.parse(getArcanist(), "Agree to drink [npc.namePos] potion.<br/>"
								+ "This will [style.italicsTfGeneric(transform you into a fox-girl)], and is likely to apply some additional, non-racial, transformative effects!"),
						LABORATORY_ARCANIST_SOLO_TF) {
					@Override
					public void effects() {
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistEncountered, true);
						Value<String, AbstractItem> potion = ((NPC)getArcanist()).getTransformativePotion(Main.game.getPlayer(), true);
						Main.game.getTextEndStringBuilder().append(getArcanist().useItem(potion.getValue(), Main.game.getPlayer(), false));
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_SOLO_TF_OFFER_SEX", getAllCharacters()));
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistAcceptedTF, true);
					}
				};

			// Action 3: Companion accepts TF potion.
			// If already drank TF potion, or is already a fox-morph, action is for getting companion to perform oral sex.
			} else if(index==3 && isCompanionDialogue()) {
				if((Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelArcanistAcceptedTF) || getMainCompanion().getRace()==Race.FOX_MORPH) && Main.getProperties().hasValue(PropertyValue.voluntaryNTR)) {
					if(!getMainCompanion().isAttractedTo(getArcanist()) && !getMainCompanion().isSlave() && !(getMainCompanion() instanceof Elemental)) {
						return new Response(
								UtilText.parse(getMainCompanion(), "Oral ([npc.name])"),
								UtilText.parse(getArcanist(), getMainCompanion(),
									"As [npc2.name] is not attracted to [npc.name], [npc2.she] will refuse to have sex with [npc.herHim]. As [npc2.she] is not your slave, you can't force [npc2.herHim] to do it, either..."),
								null);
						
					} else if(!getMainCompanion().isFeminine()) {
						return new Response(
								UtilText.parse(getMainCompanion(), "Oral ([npc.name])"),
								UtilText.parse(getArcanist(), getMainCompanion(), "As [npc.sheIsFull] gynephilic, [npc.name] is not interested in having [npc2.name] perform oral on her."),
								null);
						
					} else {
						return new ResponseSex(
								UtilText.parse(getMainCompanion(), "Oral ([npc.name])"),
								UtilText.parse(getArcanist(), getMainCompanion(), "Tell [npc2.name] to get down on [npc2.her] knees and perform oral on [npc.name] while you watch."),
								true,
								false,
								new SMKneeling(
										Util.newHashMapOfValues(new Value<>(getArcanist(), SexPositionSlot.KNEELING_RECEIVING_ORAL)),
										Util.newHashMapOfValues(new Value<>(getMainCompanion(), SexPositionSlot.KNEELING_PERFORMING_ORAL))) {
									@Override
									public boolean isPositionChangingAllowed(GameCharacter character) {
										return false;
									}
								},
								null,
								Util.newArrayListOfValues(Main.game.getPlayer()),
								LABORATORY_ARCANIST_POST_SEX,
								UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ARCANIST_COMPANION_START_SEX", getAllCharacters()));
					}
				}
				if(!getMainCompanion().getFetishDesire(Fetish.FETISH_TRANSFORMATION_RECEIVING).isPositive() && !getMainCompanion().isSlave() && !(getMainCompanion() instanceof Elemental)) {
					return new Response(
							UtilText.parse(getMainCompanion(), "Accept ([npc.name])"),
							UtilText.parse(getArcanist(), getMainCompanion(),
								"As [npc2.name] doesn't like being transformed, [npc2.she] will refuse to drink [npc.namePos] potion. As [npc2.she] is not your slave, you can't force [npc2.herHim] to do it, either..."),
							null);
				}
				return new Response(
						UtilText.parse(getMainCompanion(), "Accept ([npc.name])"),
						UtilText.parse(getArcanist(), getMainCompanion(),
								"Tell [npc2.name] to drink [npc.namePos] potion.<br/>"
								+ "This will [style.italicsTfGeneric(transform [npc2.name] into a fox-girl)], and is likely to apply some additional, non-racial, transformative effects!"),
						LABORATORY_ARCANIST_COMPANION_TF) {
					@Override
					public void effects() {
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistEncountered, true);
						Value<String, AbstractItem> potion = ((NPC)getArcanist()).getTransformativePotion(getMainCompanion(), true);
						Main.game.getTextEndStringBuilder().append(getArcanist().useItem(potion.getValue(), getMainCompanion(), false));
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_COMPANION_TF_OFFER_SEX", getAllCharacters()));
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistAcceptedTF, true);
					}
				};

			// Action 4: Both player and companion accept TF potion.
			// If already drank TF potion, or both are already fox-morphs, action is for performing oral sex.
			} else if(index==4 && isCompanionDialogue()) {
				if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelArcanistAcceptedTF) || (Main.game.getPlayer().getRace()==Race.FOX_MORPH && getMainCompanion().getRace()==Race.FOX_MORPH)) {
					if(!getMainCompanion().isAttractedTo(getArcanist()) && !getMainCompanion().isSlave() && !(getMainCompanion() instanceof Elemental)) {
						return new Response(
								"Oral (both)",
								UtilText.parse(getArcanist(), getMainCompanion(),
									"As [npc2.name] is not attracted to [npc.name], [npc2.she] will refuse to have sex with [npc.herHim]. As [npc2.she] is not your slave, you can't force [npc2.herHim] to do it, either..."),
								null);
						
					} else if(!Main.game.getPlayer().isFeminine()) {
						return new Response(
								"Oral (both)",
								UtilText.parse(getArcanist(), "As [npc.sheIsFull] gynephilic, [npc.name] is not interested in having you perform oral on her."),
								null);
						
					} else if(!getMainCompanion().isFeminine()) {
						return new Response(
								"Oral (both)",
								UtilText.parse(getArcanist(), getMainCompanion(), "As [npc.sheIsFull] gynephilic, [npc.name] is not interested in having [npc2.name] perform oral on her."),
								null);
						
					} else {
						return new ResponseSex(
								UtilText.parse(getMainCompanion(), "Oral (both)"),
								UtilText.parse(getArcanist(), getMainCompanion(), "Tell [npc2.name] to get down on [npc2.her] knees and join you in performing oral on [npc.name]."),
								true,
								false,
								new SMKneeling(
										Util.newHashMapOfValues(new Value<>(getArcanist(), SexPositionSlot.KNEELING_RECEIVING_ORAL)),
										Util.newHashMapOfValues(
												new Value<>(Main.game.getPlayer(), SexPositionSlot.KNEELING_PERFORMING_ORAL),
												new Value<>(getMainCompanion(), SexPositionSlot.KNEELING_PERFORMING_ORAL_TWO))) {
									@Override
									public boolean isPositionChangingAllowed(GameCharacter character) {
										return false;
									}
								},
								null,
								null,
								LABORATORY_ARCANIST_POST_SEX,
								UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ARCANIST_BOTH_START_SEX", getAllCharacters()));
					}
				}
				if(getMainCompanion().getRace()==Race.FOX_MORPH) {
					return new Response(UtilText.parse(
							getMainCompanion(), "Accept (both)"),
							UtilText.parse(getArcanist(), getMainCompanion(),
								"As [npc2.name] is already a fox-morph, [npc.name] is unwilling to use a potion on [npc2.herHim]..."),
							null);
				}
				if(Main.game.getPlayer().getRace()==Race.FOX_MORPH) {
					return new Response(UtilText.parse(
							getMainCompanion(), "Accept (both)"),
							UtilText.parse(getArcanist(),
								"As you are already a fox-morph, [npc.name] is unwilling to use a potion on you..."),
							null);
				}
				if(!getMainCompanion().getFetishDesire(Fetish.FETISH_TRANSFORMATION_RECEIVING).isPositive() && !getMainCompanion().isSlave() && !(getMainCompanion() instanceof Elemental)) {
					return new Response(UtilText.parse(
							getMainCompanion(), "Accept (both)"),
							UtilText.parse(getArcanist(), getMainCompanion(),
								"As [npc2.name] doesn't like being transformed, [npc2.she] will refuse to drink [npc.namePos] potion. As [npc2.she] is not your slave, you can't force [npc2.herHim] to do it, either..."),
							null);
				}
				return new Response("Accept (both)", 
						UtilText.parse(getArcanist(), getMainCompanion(),
								"Agree to share [npc.namePos] potion between you and [npc2.name].<br/>"
								+ "This will [style.italicsTfGeneric(transform both you and [npc2.name] into fox-girls)], and is likely to apply some additional, non-racial, transformative effects!"),
						LABORATORY_ARCANIST_BOTH_TF) {
					@Override
					public void effects() {
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistEncountered, true);
						Value<String, AbstractItem> potion = ((NPC)getArcanist()).getTransformativePotion(Main.game.getPlayer(), true);
						Main.game.getTextEndStringBuilder().append(getArcanist().useItem(potion.getValue(), Main.game.getPlayer(), false));
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ARCANIST_BOTH_TF_COMPANION_TF", getAllCharacters()));
						potion = ((NPC)getArcanist()).getTransformativePotion(getMainCompanion(), true);
						Main.game.getTextEndStringBuilder().append(getArcanist().useItem(potion.getValue(), getMainCompanion(), false));
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ARCANIST_BOTH_TF_COMPANION_TF_OFFER_SEX", getAllCharacters()));
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistAcceptedTF, true);
					}
				};
				
			// Action 5: Combat
			} else if(index==5) {
				return new Response("Attack",
						"There's no way you're going to let this sneaky fox get away with insulting you in this manner!<br/>"
							+ "<i>You think that there are about [style.boldBad(thirty imps)] in the citadel, so be prepared to fight this many as they arrive, in waves, to the aid of their allies!</i>",
						IMP_CHALLENGE) {
					@Override
					public boolean isCombatHighlight() {
						return true;
					}
					@Override
					public void effects() {
						Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "ARCANIST_FIGHT_START", getAllCharacters()));
						Main.game.getDialogueFlags().impCitadelImpWave = 0;
						spawnImps();
						getArcanist().setLocation(WorldType.EMPTY, PlaceType.GENERIC_HOLDING_CELL);
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelArcanistEncountered, true);
					}
				};
			}
			return null;
		}
	};

	public static final DialogueNodeOld LABORATORY_ARCANIST_EXIT = new DialogueNodeOld("", ".", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public String getContent() {
			return "";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			return LABORATORY.getResponse(responseTab, index);
		}
	};

	public static final DialogueNodeOld LABORATORY_ARCANIST_SOLO_TF = new DialogueNodeOld("", ".", true, true) {
		private static final long serialVersionUID = 1L;

		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_SOLO_TF", getAllCharacters());
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				return new Response("Decline",
						UtilText.parse(getArcanist(),
								"Tell [npc.name] that you have no interest in having sex with [npc.herHim]."),
						LABORATORY_ARCANIST_EXIT) {
					@Override
					public void effects() {
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_SOLO_TF_SEX_DECLINED", getAllCharacters()));
					}
				};
				
			} else if(index==2) {
				return new ResponseSex("Oral",
						UtilText.parse(getArcanist(), "Agree to do as [npc.name] says, and get down on your knees to perform oral on [npc.herHim]."),
						true,
						false,
						new SMKneeling(
								Util.newHashMapOfValues(new Value<>(getArcanist(), SexPositionSlot.KNEELING_RECEIVING_ORAL)),
								Util.newHashMapOfValues(new Value<>(Main.game.getPlayer(), SexPositionSlot.KNEELING_PERFORMING_ORAL))){
							@Override
							public boolean isPositionChangingAllowed(GameCharacter character) {
								return false;
							}
						},
						null,
						Util.newArrayListOfValues(getMainCompanion()),
						LABORATORY_ARCANIST_POST_SEX,
						UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_SOLO_TF_SEX_ACCEPTED", getAllCharacters()));
				
			}
			return null;
		}
	};

	public static final DialogueNodeOld LABORATORY_ARCANIST_COMPANION_TF = new DialogueNodeOld("", ".", true, true) {
		private static final long serialVersionUID = 1L;

		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_COMPANION_TF", getAllCharacters());
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				return new Response("Decline",
						UtilText.parse(getArcanist(), getMainCompanion(),
								"Tell [npc.name] that you have no interest in getting [npc2.name] to perform oral on [npc.herHim]."),
						LABORATORY_ARCANIST_EXIT) {
					@Override
					public void effects() {
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_COMPANION_TF_SEX_DECLINED", getAllCharacters()));
					}
				};
				
			} else if(index==2 && Main.getProperties().hasValue(PropertyValue.voluntaryNTR)) {
				if(!getMainCompanion().isAttractedTo(getArcanist()) && !getMainCompanion().isSlave() && !(getMainCompanion() instanceof Elemental)) {
					return new Response(
							UtilText.parse(getMainCompanion(), "Agree"),
							UtilText.parse(getArcanist(), getMainCompanion(),
								"As [npc2.name] is not attracted to [npc.name], [npc2.she] will refuse to have sex with [npc.herHim]. As [npc2.she] is not your slave, you can't force [npc2.herHim] to do it, either..."),
							null);
				} 
				return new ResponseSex("Agree",
						UtilText.parse(getArcanist(), getMainCompanion(), "Agree to do as [npc.name] says, and tell [npc2.name] to get down on [npc2.her] knees to perform oral on [npc.herHim] while you watch."),
						true,
						false,
						new SMKneeling(
								Util.newHashMapOfValues(new Value<>(getArcanist(), SexPositionSlot.KNEELING_RECEIVING_ORAL)),
								Util.newHashMapOfValues(new Value<>(getMainCompanion(), SexPositionSlot.KNEELING_PERFORMING_ORAL))){
							@Override
							public boolean isPositionChangingAllowed(GameCharacter character) {
								return false;
							}
						},
						null,
						Util.newArrayListOfValues(Main.game.getPlayer()),
						LABORATORY_ARCANIST_POST_SEX,
						UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_COMPANION_TF_SEX_ACCEPTED", getAllCharacters()));
			}
			
			return null;
		}
	};

	public static final DialogueNodeOld LABORATORY_ARCANIST_BOTH_TF = new DialogueNodeOld("", ".", true, true) {
		private static final long serialVersionUID = 1L;

		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_BOTH_TF", getAllCharacters());
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				return new Response("Decline",
						UtilText.parse(getArcanist(),
								"Tell [npc.name] that you have no interest in having sex with [npc.herHim]."),
						LABORATORY_ARCANIST_EXIT) {
					@Override
					public void effects() {
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_BOTH_TF_SEX_DECLINED", getAllCharacters()));
					}
				};
				
			} else if(index==2) {
				if(!getMainCompanion().isAttractedTo(getArcanist()) && !getMainCompanion().isSlave() && !(getMainCompanion() instanceof Elemental)) {
					return new Response(
							UtilText.parse(getMainCompanion(), "Agree"),
							UtilText.parse(getArcanist(), getMainCompanion(),
								"As [npc2.name] is not attracted to [npc.name], [npc2.she] will refuse to have sex with [npc.herHim]. As [npc2.she] is not your slave, you can't force [npc2.herHim] to do it, either..."),
							null);
				} 
				return new ResponseSex("Agree",
						UtilText.parse(getArcanist(), getMainCompanion(), "Agree to do as [npc.name] says, and tell [npc2.name] to join you in getting down on your knees to perform oral on [npc.herHim]."),
						true,
						false,
						new SMKneeling(
								Util.newHashMapOfValues(new Value<>(getArcanist(), SexPositionSlot.KNEELING_RECEIVING_ORAL)),
								Util.newHashMapOfValues(
										new Value<>(Main.game.getPlayer(), SexPositionSlot.KNEELING_PERFORMING_ORAL),
										new Value<>(getMainCompanion(), SexPositionSlot.KNEELING_PERFORMING_ORAL_TWO))){
							@Override
							public boolean isPositionChangingAllowed(GameCharacter character) {
								return false;
							}
						},
						null,
						null,
						LABORATORY_ARCANIST_POST_SEX,
						UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_BOTH_TF_SEX_ACCEPTED", getAllCharacters()));
			}
			
			return null;
		}
	};

	public static final DialogueNodeOld LABORATORY_ARCANIST_POST_SEX = new DialogueNodeOld("Finished", ".", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public String getContent() {
			if(Sex.getAllParticipants().contains(Main.game.getPlayer())) {
				if(Sex.getAllParticipants().contains(getMainCompanion())) {
					return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_POST_SEX_BOTH", getAllCharacters());
				} else {
					return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_POST_SEX_SOLO", getAllCharacters());
				}
			} else {
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "LABORATORY_ARCANIST_POST_SEX_COMPANION", getAllCharacters());
			}
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				return new Response("Continue", "", Main.game.getDefaultDialogueNoEncounter());
			}
			return null;
		}
	};

	public static final DialogueNodeOld TREASURY = new DialogueNodeOld("Treasury", ".", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelTreasurySearched)) {
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "TREASURY_SEARCHED", getAllCharacters());
			} else {
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "TREASURY", getAllCharacters());
			}
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(index==1) {
				if(Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelTreasurySearched)) {
					return new Response("Search", "You've already searched the treasury. There's nothing left to find!", null);
				}
				return new Response("Search", "Search through the rubble to see if you can find anything of value.", TREASURY) {
					@Override
					public void effects() {
						Main.game.getDialogueFlags().setFlag(DialogueFlagValue.impCitadelTreasurySearched, true);

						Main.game.getTextEndStringBuilder().append(Main.game.getPlayer().incrementMoney(15000));
						
						Main.game.getTextEndStringBuilder().append(Main.game.getPlayer().addClothing(
										AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_darkSiren_siren_amulet"), Colour.CLOTHING_RED_VERY_DARK, Colour.CLOTHING_BLACK_STEEL, Colour.CLOTHING_PURPLE_DARK, false),
										false));
						Main.game.getTextEndStringBuilder().append(Main.game.getPlayer().addClothing(
								AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_darkSiren_siren_cloak"), Colour.CLOTHING_BLACK, Colour.CLOTHING_RED_VERY_DARK, Colour.CLOTHING_STEEL, false),
								false));
						Main.game.getTextEndStringBuilder().append(Main.game.getPlayer().addClothing(
								AbstractClothingType.generateClothing(ClothingType.getClothingTypeFromId("innoxia_darkSiren_siren_seal"), Colour.CLOTHING_BLACK, Colour.CLOTHING_BLACK, Colour.CLOTHING_BLACK, false),
								false));

						Main.game.getTextEndStringBuilder().append(Main.game.getPlayer().addWeapon(
								AbstractWeaponType.generateWeapon(WeaponType.getWeaponTypeFromId("innoxia_scythe_scythe"), DamageType.PHYSICAL, Colour.CLOTHING_BLACK_STEEL, Colour.CLOTHING_RED_DARK),
								false));
					}
				};
			}
			return null;
		}
	};
	
	public static final DialogueNodeOld TREASURY_SEARCH = new DialogueNodeOld("Treasury", ".", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 5;
		}

		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "TREASURY_SEARCH", getAllCharacters());
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			return TREASURY.getResponse(responseTab, index);
		}
	};

	public static final DialogueNodeOld KEEP = new DialogueNodeOld("Keep", ".", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}
		
		@Override
		public boolean isTravelDisabled() {
			return !isDefeated();
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);

			if(isDefeated()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_DEFEATED", getAllCharacters()));
			} else {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP", getAllCharacters()));
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if(!isDefeated()) {
				if(index==1) {
					return new Response("Enter", "Push open the doors of the keep and step inside.", KEEP_ENTRY) {
						@Override
						public void effects() {
							getBoss().setPlayerKnowsName(true);
						}
					};
					
				} else if(index==2) {
					return new Response("Leave", "Step back from the doors of the keep.", COURTYARD) {
						@Override
						public void effects() {
							Main.game.getPlayer().moveToAdjacentMatchingCellType(false, PlaceType.FORTRESS_DEMON_COURTYARD);
							Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_BACK_OFF", getAllCharacters()));
						}
					};
				}
			}
			return null;
		}
	};
	
	public static final DialogueNodeOld KEEP_ENTRY = new DialogueNodeOld("Keep", ".", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);
			
			if(isBossEncountered()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_ENTRY_RETURN", getAllCharacters()));
				
			} else {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_ENTRY", getAllCharacters()));
			}
			if(Main.game.getPlayer().hasItemType(ItemType.LYSSIETHS_RING)) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_ENTRY_RING", getAllCharacters()));
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				if(Main.game.getPlayer().hasTraitActivated(Perk.CHUUNI)) {
					return new Response("Challenge", UtilText.parse(getBoss(), "Challenge [npc.name] to a duel between the two greatest arcane-users in the world!"), KEEP_CHALLENGE) {
						@Override
						public void effects() {
							Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_CHALLENGE", getAllCharacters()));
						}
					};
					
				} else {
					return new Response("Challenge", "You can't bring yourself to engage in the same level of dialogue as this embarrassing succubus. Perhaps if you were a chuuni as well, things would be different...", null);
				}
				
			} else if(index==2) {
				return new ResponseCombat("Fight", UtilText.parse(getBoss(), "Defend yourself against the three demons!"), getDemonLeader(), getBossGroup(false), null);
				
			} else {
				return null;
			}
		}
	};

	// Used for post-demon player victory as well:
	public static final DialogueNodeOld KEEP_CHALLENGE = new DialogueNodeOld("Keep", ".", true) { // TODO make dialogue continue for non-post-combat scene
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			 // Males demon leader combat defeat appends: KEEP_CHALLENGE_LEADER_VICTORY
			
			if(Main.game.getPlayer().hasItemType(ItemType.LYSSIETHS_RING)) {
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_CHALLENGE_RING", getAllCharacters());
			} else {
				return "";
			}
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new ResponseCombat("Duel", UtilText.parse(getBoss(), "Accept [npc.namePos] offer, and fight [npc.herHim] one-on-one!"), (NPC) getBoss(), null);

			} else if(index==2 && Main.game.getPlayer().hasItemType(ItemType.LYSSIETHS_RING)) {
				return new Response("Use ring",
						UtilText.parse(getBoss(), "Show [npc.name] that you have [npc.her] mother's ring, and trick her into taking it from you and putting it on!<br/>"
								+ "[style.italicsExcellent(You can tell that [npc.sheWill] instantly put it on, thus enslaving [npc.herHim] without having to fight!)]"),
						KEEP_CHALLENGE_RING_TRICK) {
					@Override
					public void effects() {
						clearFortress();
					}
				};
				
			}
			return null;
		}
	};

	public static final DialogueNodeOld KEEP_CHALLENGE_RING_TRICK = new DialogueNodeOld("Keep", ".", true, true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);

			UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_CHALLENGE_RING_TRICK", getAllCharacters()));
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new Response("Escape",
						"The citadel is collapsing! Rush out to the safety of Submission before you're crushed!",
						KEEP_COLLAPSE_ESCAPE) {
					@Override
					public void effects() {
						Main.game.getPlayer().setLocation(WorldType.SUBMISSION, PlaceType.SUBMISSION_IMP_FORTRESS_DEMON);
						Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_CHALLENGE_RING_TRICK_ESCAPE", getAllCharacters()));
						Main.game.getTextStartStringBuilder().append(getBoss().incrementAffection(Main.game.getPlayer(), -50));
					}
				};

			}
			return null;
		}
	};

	public static final DialogueNodeOld KEEP_COLLAPSE_ESCAPE = new DialogueNodeOld("", "", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			return "";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				if(Main.game.getCharactersPresent().contains(getBoss())) {
					return new Response("Explain",
							UtilText.parse(getBoss(), "Explain to Lyssieth's guards what happened."),
							KEEP_COLLAPSE_ESCAPE_END) {
						@Override
						public void effects() {
							Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_COLLAPSE_ESCAPE_GUARDS_ENSLAVE", getAllCharacters()));
							clearFortress();
						}
					};
				}
				return new Response("Explain",
						UtilText.parse(getBoss(), "Explain to Lyssieth's guards what happened."),
						KEEP_COLLAPSE_ESCAPE_END) {
					@Override
					public void effects() {
						Main.game.getTextEndStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_COLLAPSE_ESCAPE_GUARDS_EXPLAIN", getAllCharacters()));
					}
				};

			}
			return null;
		}
	};

	public static final DialogueNodeOld KEEP_COLLAPSE_ESCAPE_END = new DialogueNodeOld("", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			return "";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld KEEP_AFTER_COMBAT_VICTORY = new DialogueNodeOld("Victory", "", true) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getDescription() {
			return UtilText.parse(getBoss(), "[npc.Name] is finally on the verge of defeat!");
		}

		@Override
		public String getContent() {
			return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_AFTER_COMBAT_VICTORY", getAllCharacters());
		}
		
		@Override
		public Response getResponse(int responseTab, int index) {
			if (index == 1) {
				return new Response("Run",
						UtilText.parse(getBoss(), "The citadel is collapsing! Abandon [npc.name] and rush out to the safety of Submission before you're crushed!"),
						KEEP_COLLAPSE_ESCAPE) {
					@Override
					public void effects() {
						Main.game.getPlayer().incrementKarma(-25); // Really? You'd just leave her to die? ;_;
						Main.game.getPlayer().setLocation(WorldType.SUBMISSION, PlaceType.SUBMISSION_IMP_FORTRESS_DEMON);
						getBoss().setLocation(WorldType.SUBMISSION, PlaceType.SUBMISSION_IMP_FORTRESS_DEMON);
						Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_COLLAPSE_ESCAPE_COMBAT", getAllCharacters()));
						Main.game.getTextStartStringBuilder().append(getBoss().incrementAffection(Main.game.getPlayer(), -100));
					}
				};

			} else if (index == 2) {
				return new Response(UtilText.parse(getBoss(), "Save [npc.name]"),
						UtilText.parse(getBoss(), "The citadel is collapsing! Save [npc.name] by carrying [npc.herHim] out to the safety of Submission before the two of you are crushed!"),
						KEEP_COLLAPSE_ESCAPE) {
					@Override
					public void effects() {
						Main.game.getPlayer().setLocation(WorldType.SUBMISSION, PlaceType.SUBMISSION_IMP_FORTRESS_DEMON);
						getBoss().setLocation(WorldType.SUBMISSION, PlaceType.SUBMISSION_IMP_FORTRESS_DEMON);
						Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_COLLAPSE_ESCAPE_COMBAT_HERO", getAllCharacters()));
						Main.game.getTextStartStringBuilder().append(getBoss().incrementAffection(Main.game.getPlayer(), 50));
						Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_COLLAPSE_ESCAPE_COMBAT_HERO_END", getAllCharacters()));
					}
				};

			}
			return null;
		}
	};

	
	public static final DialogueNodeOld KEEP_AFTER_COMBAT_DEFEAT = new DialogueNodeOld("Keep", ".", true) {
		private static final long serialVersionUID = 1L;
		
		@Override
		public String getDescription() {
			return "You have been defeated!";
		}

		@Override
		public String getContent() {
			// KEEP_CHALLENGE_LEADER_DEFEAT or KEEP_CHALLENGE_BOSS_DEFEAT are appended to the start of this content (from DS's or Male leader's endCombat() methods)
			
//			if(Main.game.isNonConEnabled()) { TODO prisoner stuff
//				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_AFTER_COMBAT_DEFEAT_CHOICES", getAllCharacters());
//			} else {
				return UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_AFTER_COMBAT_DEFEAT_NO_NON_CON", getAllCharacters());
//			}
		}
		
		@Override
		public Response getResponse(int responseTab, int index) {
//			if(!Main.game.isNonConEnabled()) {
				if(index==1) {
					return new Response("Recover", "Take a moment to catch your breath, and then continue on your way.", THROWN_OUT) {
						@Override
						public void effects() {
							Main.game.getPlayer().setLocation(WorldType.SUBMISSION, PlaceType.SUBMISSION_IMP_FORTRESS_DEMON);
							Main.game.getTextStartStringBuilder().append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "KEEP_AFTER_COMBAT_DEFEAT_NO_NON_CON_THROWN_OUT", getAllCharacters()));
						}
					};
				}
				return null;
//			}
			
			/* TODO
				Mark which one selected
				DS removes all your clothing - uses her arcane power to remove jinxed clothing. (Put clothing in treasury)
				She tells the one you chose to fuck you in front of her
				Then you are put in cells.
				Wake up
					Youko feeds you potion to TF into chosen type.
					Demon puts bitch collar on you, then fucks you
			 */
			
//			if (index == 1) {
//				return new Response(UtilText.parse(Main.game.getFortressMalesLeader(), "[npc.Name]"),
//						UtilText.parse(Main.game.getFortressMalesLeader(), getBoss(),
//								"Tell [npc2.name] that you submit to [npc.name]...<br/><i>It's obvious from what [npc.sheHas] just said that [npc.she] wants to turn you into an imp broodmother!</i>"),
//						null);
//				
//			} else if (index == 2) {
//				return new Response(UtilText.parse(Main.game.getFortressAlphaLeader(), "[npc.Name]"),
//						UtilText.parse(Main.game.getFortressAlphaLeader(), getBoss(),
//								"Tell [npc2.name] that you submit to [npc.name]...<br/><i>It's obvious from what [npc.sheHas] just said that [npc.she] wants to abuse you and turn you into [npc.her] worthless cum-dump!</i>"),
//						null);
//				
//			} else if (index == 3) {
//				return new Response(UtilText.parse(Main.game.getFortressFemalesLeader(), "[npc.Name]"),
//						UtilText.parse(Main.game.getFortressFemalesLeader(), getBoss(),
//								"Tell [npc2.name] that you submit to [npc.name]...<br/><i>It's obvious from what [npc.sheHas] just said that [npc.she] wants to give you a big cock and put you to work breeding imps!</i>"),
//						null);
//				
//			} else if (index == 4) {
//				return new Response("Refuse",
//						UtilText.parse(getBoss(),
//								"Tell [npc.name] that you refuse to submit to anyone!<br/><i>It's obvious from what [npc.sheHas] just said that [npc.she] will let the demons decide amongst themselves as to who gets possession of you!</i>"),
//						null);
//				
//			} else {
//				return null;
//			}
		}
	};
	

	public static final DialogueNodeOld THROWN_OUT = new DialogueNodeOld("", "", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 5;
		}

		@Override
		public String getContent() {
			return "";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			return PlaceType.SUBMISSION_IMP_FORTRESS_DEMON.getDialogue(false).getResponse(responseTab, index);
		}
	};
	
	
	// Kept as prisoner dialogue:

	private static boolean isPrisonerMale() {
		return Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelPrisonerMale);
	}

	private static boolean isPrisonerFemale() {
		return Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelPrisonerFemale);
	}
	
	private static boolean isPrisonerAlpha() {
		return Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.impCitadelPrisonerAlpha);
	}

	private static boolean isPrisoner() {
		return isPrisonerMale() || isPrisonerFemale() || isPrisonerAlpha();
	}
	
	public static final DialogueNodeOld PRISONER_INITIAL_SCENE = new DialogueNodeOld("", "", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 60;
		}

		@Override
		public String getContent() {
			return "";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};

	
	public static final DialogueNodeOld PRISONER_CELLS_TF = new DialogueNodeOld("", "", true) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 60;
		}

		@Override
		public String getContent() {
			return "";
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			return null;
		}
	};
	
	public static final DialogueNodeOld CELLS = new DialogueNodeOld("Cells", ".", false) {
		private static final long serialVersionUID = 1L;

		@Override
		public int getMinutesPassed() {
			return 1;
		}

		@Override
		public String getContent() {
			UtilText.nodeContentSB.setLength(0);
			
			if(isPrisonerAlpha()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "PRISONER_ALPHA_CELL", getAllCharacters()));
				
			} else if(isPrisonerMale()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "PRISONER_MALE_CELL", getAllCharacters()));
				
			} else if(isPrisonerFemale()) {
				UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "PRISONER_FEMALE_CELL", getAllCharacters()));
				
			} else {
				if(isDefeated()) {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "CELLS_RUINS", getAllCharacters()));
				} else {
					UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "CELLS", getAllCharacters()));
					if(isImpsDefeated()) {
						UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "CELLS_IMPS_DEFEATED", getAllCharacters()));
					} else {
						UtilText.nodeContentSB.append(UtilText.parseFromXMLFile("places/submission/impCitadel"+getDialogueEncounterId(), "CELLS_IMP_GUARDS", getAllCharacters()));
					}
				}
			}
			
			return UtilText.nodeContentSB.toString();
		}

		@Override
		public Response getResponse(int responseTab, int index) {
			/* It's ok if some are randomised. I don't mind players save/loading to try to get different events.
			(Maximum times/day)Events:
				(1) Fucked by demon
				(-) Fucked by imps
				(1) Use as trophy foot-rest
				(-) Milked/cum milked
				(1) DS watches imps fuck you as amusement
				(1) If female owner, gets you to breed trespasser she caught
				(1) If alpha owner, she tattoos you
			*/
			if(isPrisoner()) {
				if(Main.game.getHourOfDay()<7) { // Wake up event
					// Imp arrives to give breakfast. Imp is generated based on player's SO.
					
				} else if(Main.game.getHourOfDay()<11) { // Morning event
					
				} else if(Main.game.getHourOfDay()<14) { // Lunch event
					// Imp feeds you. If player has cum addict, the imp cums on food.
					
				} else if(Main.game.getHourOfDay()<18) { // Afternoon event
					
				} else if(Main.game.getHourOfDay()<21) { // Dinner event
					
				} else if(Main.game.getHourOfDay()<=24) { // Night event
					
				}
			}
			
			return null;
		}
	};
}
