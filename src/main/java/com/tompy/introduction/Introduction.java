package com.tompy.introduction;

import com.tompy.adventure.api.Adventure;
import com.tompy.adventure.internal.AdventureImpl;
import com.tompy.directive.*;
import com.tompy.entity.EntityUtil;
import com.tompy.entity.api.EntityFacade;
import com.tompy.entity.api.EntityFacadeBuilderFactory;
import com.tompy.entity.api.EntityService;
import com.tompy.entity.area.api.Area;
import com.tompy.entity.encounter.api.Encounter;
import com.tompy.entity.event.api.Event;
import com.tompy.entity.feature.api.Feature;
import com.tompy.entity.item.api.Item;
import com.tompy.exit.api.Exit;
import com.tompy.exit.api.ExitBuilderFactory;
import com.tompy.io.UserInput;
import com.tompy.player.api.Player;
import com.tompy.state.api.AdventureStateFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static com.tompy.attribute.api.Attribute.*;
import static com.tompy.directive.Direction.*;
import static com.tompy.directive.EventType.*;
import static com.tompy.directive.FeatureType.*;
import static com.tompy.directive.ItemType.*;

public class Introduction extends AdventureImpl implements Adventure {
    private static final Logger LOGGER = LogManager.getLogger(Introduction.class);

    public Introduction(Player player, EntityService entityService,
            EntityFacadeBuilderFactory entityFacadeBuilderFactory, ExitBuilderFactory exitBuilderFactory,
            UserInput userInput, PrintStream outStream) {
        super(player, entityService, entityFacadeBuilderFactory, exitBuilderFactory, userInput, outStream);
    }

    @Override
    public void create(AdventureStateFactory stateFactory) {
        LOGGER.info("Creating adventure...");
        // Areas
        Area room1 = entityService.createAreaBuilder().name("Room1").build();

        Area room2 = entityService.createAreaBuilder().name("Room2").build();

        Area room3 = entityService.createAreaBuilder().name("Room3").build();

        Area room4 = entityService.createAreaBuilder().name("Room4").build();

        Area room5 = entityService.createAreaBuilder().name("Room5").build();

        Area room6 = entityService.createAreaBuilder().name("Room6").build();

        Area room7 = entityService.createAreaBuilder().name("Room7").build();


        // Events
        Event room1Enter =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses(
                                "Well lit empty room with bright white walls and dark blue carpet.  In the center of the room " +
                                        "sits ${room1Chest|open} large wooden box.").entity(room1).build();

        Event room1Search =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses(
                                "There is nothing special about this room.  There is ${room2SouthDoor|open|an open|a closed} " +
                                        "door to the north.").entity(room1).build();

        entityService.add(room1, EventType.AREA_ENTER, room1Enter);
        entityService.add(room1, EventType.AREA_SEARCH, room1Search);

        Event room2Enter =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses(
                                "A hallway that bends to the right.  It is well let with white walls and a dark blue well worn carpet" +
                                        ".").entity(room2).build();

        Event room2Search =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("Along both sides of the hallway are portraits of previous tenants, some quite old.")
                        .entity(room2).build();

        Event room2SearchSouth =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("A hallway leading south to ${room2SouthDoor|open} door.").entity(room2).build();

        Event room2SearchEast =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("A hallway leading east to ${room2EastDoor|open} door").entity(room2).build();


        // INTERACTION EXAMPLE
        Encounter room2WestPortal =
                entityService.createEncounterBuilder(player, this).type(EncounterType.ENVIRONMENT).build();

        Event room2WestPortalJumpIn =
                entityService.createEventBuilder().actionType(ActionType.HORRIBLE_DEATH).triggerType(TriggerType.ALWAYS)
                        .entity(room2).memo("Jump into the portal").responses("Chaos engulfs you.", "You die.").build();
        Event room2WestPortalExplore =
                entityService.createEventBuilder().actionType(ActionType.EXPLORE).triggerType(TriggerType.ALWAYS)
                        .entity(room2).memo("Back away from the portal").stateFactory(stateFactory)
                        .responses("There is a bright flash of light.", "The portal closes.").build();

        entityService.add(room2WestPortal, INTERACTION, room2WestPortalJumpIn);
        entityService.add(room2WestPortal, INTERACTION, room2WestPortalExplore);

        Event room2SearchWest0 =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ONCE_DELAY)
                        .delay(1).entity(room2).responses("You see a crack forming in the wall").build();

        Event room2SearchWest1 =
                entityService.createEventBuilder().actionType(ActionType.ENCOUNTER).stateFactory(stateFactory)
                        .triggerType(TriggerType.ONCE_DELAY).delay(2).entity(room2).encounter(room2WestPortal)
                        .responses("A dark writhing portal opens before you.").build();

        entityService.add(room2, EventType.AREA_WEST_SEARCH, room2SearchWest0);
        entityService.add(room2, EventType.AREA_WEST_SEARCH, room2SearchWest1);
        // END INTERACTION EXAMPLE


        Event room2SearchNorth1 =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS_DELAY)
                        .entity(room2).delay(1)
                        .responses("You find a door that was not there which is ${room2NorthDoor|open|open|closed}.")
                        .build();

        entityService.add(room2, EventType.AREA_ENTER, room2Enter);
        entityService.add(room2, EventType.AREA_SEARCH, room2Search);
        entityService.add(room2, EventType.AREA_SOUTH_SEARCH, room2SearchSouth);
        entityService.add(room2, EventType.AREA_EAST_SEARCH, room2SearchEast);


        Event room3Enter =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("This room has a single torch, making it smoky and dark.").entity(room3).build();

        Event room3Search =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("There is nothing to see, but the smoke seems to be building up.").entity(room3)
                        .build();

        Event room3SearchWest =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("${room2EastDoor|open|An open|A closed} door").entity(room3).build();

        Event room3SearchNorth =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("${room3NorthDoor|open|An open|A closed} curtain seems to cover something.")
                        .entity(room3).build();

        entityService.add(room3, EventType.AREA_ENTER, room3Enter);
        entityService.add(room3, EventType.AREA_SEARCH, room3Search);
        entityService.add(room3, EventType.AREA_WEST_SEARCH, room3SearchWest);
        entityService.add(room3, EventType.AREA_NORTH_SEARCH, room3SearchNorth);

        Event room4Enter = entityService.createEventBuilder().name("room4Enter").actionType(ActionType.DESCRIBE)
                .triggerType(TriggerType.ALWAYS).responses("You have made it outside", "To the north is a cave.",
                        "To the east is a merchant's tent with a beautiful women standing behind a counter.")
                .entity(room4).build();

        entityService.add(room4, EventType.AREA_ENTER, room4Enter);

        Feature room6Monster = entityService.createFeatureBuilder().type(FEATURE_MONSTER).name("room6Monster")
                .description("mean bloodthirsty orc").build();

        // Room 5 merchant
        Item simpleDagger =
                entityService.createItemBuilder().name("dagger1").description("sharp dagger").type(ItemType.ITEM_WEAPON)
                        .targetFeature(room6Monster).build();
        Item simpleSword = entityService.createItemBuilder().name("sword1").description("long sword").type(ITEM_WEAPON)
                .targetFeature(room6Monster).build();
        Item simpleShield =
                entityService.createItemBuilder().name("shield1").description("round shield").type(ITEM_WEAPON).build();

        entityService.add(simpleDagger, VALUE, 3);
        entityService.add(simpleSword, VALUE, 12);
        entityService.add(simpleShield, VALUE, 8);

        Encounter room5Merchant = entityService.createEncounterBuilder(player, this).type(EncounterType.MERCHANT)
                .items(new Item[]{simpleDagger, simpleSword, simpleShield}).sellRate(.8).buyRate(1.2).build();

        Event room5MerchantChat =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .memo("Want to know how to get more money?").entity(room5Merchant)
                        .responses("Search opposite the door in the hallway with the pictures.").build();

        Event room5MerchantGoodbye =
                entityService.createEventBuilder().actionType(ActionType.EXPLORE).triggerType(TriggerType.ALWAYS)
                        .entity(room5Merchant).stateFactory(stateFactory).responses("Goodbye").memo("Goodbye").build();

        entityService.add(room5Merchant, INTERACTION, room5MerchantChat);
        entityService.add(room5Merchant, INTERACTION, room5MerchantGoodbye);

        Event room5Enter =
                entityService.createEventBuilder().actionType(ActionType.ENCOUNTER).triggerType(TriggerType.ALWAYS)
                        .encounter(room5Merchant).responses("You see a lovely lady standing behind a counter.")
                        .entity(room5Merchant).stateFactory(stateFactory).build();

        entityService.add(room5, EventType.AREA_ENTER, room5Enter);
        // END room 5 merchant

        // Room6
        Event room6MonsterAttack = entityService.createEventBuilder().actionType(ActionType.HORRIBLE_DEATH)
                .triggerType(TriggerType.ALWAYS_DELAY).delay(1).entity(room6Monster)
                .responses("The orc swings his axe visciously at your head!").memo("Ouch!").build();
        entityService.add(room6Monster, EventType.START_ROUND, room6MonsterAttack);
        entityService.add(room6Monster, VALUE, 10);
        // Room6 - END


        // Exits
        Exit exit1 = exitBuilderFactory.builder().area(room2).area(room1).state(false).build();
        Exit exit2 = exitBuilderFactory.builder().area(room3).area(room2).state(false).build();
        Exit exit3 = exitBuilderFactory.builder().area(room3).area(room4).state(false).build();
        Exit exit4 = exitBuilderFactory.builder().area(room4).area(room5).state(true).build();
        Exit exit5 = exitBuilderFactory.builder().area(room4).area(room6).state(true).build();
        Exit exit6 = exitBuilderFactory.builder().area(room2).area(room7).state(false).build();

        room1.installExit(DIRECTION_NORTH, exit1);
        room2.installExit(DIRECTION_SOUTH, exit1);
        room2.installExit(DIRECTION_EAST, exit2);
        room2.installExit(DIRECTION_NORTH, exit6);
        room3.installExit(DIRECTION_WEST, exit2);
        room3.installExit(DIRECTION_NORTH, exit3);
        room4.installExit(DIRECTION_SOUTH, exit3);
        room4.installExit(DIRECTION_EAST, exit4);
        room5.installExit(DIRECTION_WEST, exit4);
        room4.installExit(DIRECTION_NORTH, exit5);
        room6.installExit(DIRECTION_SOUTH, exit5);


        // Features
        Feature room2NorthDoor =
                entityService.createFeatureBuilder().name("room2NorthDoor").description("secret door behind a picture")
                        .type(FEATURE_DOOR).exit(exit6).build();

        Feature room1Chest =
                entityService.createFeatureBuilder().type(FEATURE_CHEST).name("room1Chest").description("dusty chest")
                        .build();
        Feature room2EastDoor =
                entityService.createFeatureBuilder().type(FEATURE_DOOR).name("room2EastDoor").description("iron door")
                        .exit(exit2).build();
        Feature room2SouthDoor =
                entityService.createFeatureBuilder().type(FEATURE_DOOR).name("room2SouthDoor").description("oak door")
                        .exit(exit1).build();
        Feature room3NorthDoor = entityService.createFeatureBuilder().type(FEATURE_DOOR).name("room3NorthDoor")
                .description("dark curtain").exit(exit3).build();

        Feature room7ChestRed =
                entityService.createFeatureBuilder().type(FEATURE_CHEST).name("room7ChestRed").description("red chest")
                        .build();

        Feature room7ChestWhite = entityService.createFeatureBuilder().type(FEATURE_CHEST).name("room7ChestWhite")
                .description("white chest").build();

        Feature room7ChestBlue = entityService.createFeatureBuilder().type(FEATURE_CHEST).name("room7ChestBlue")
                .description("blue chest").build();

        Feature room7Table =
                entityService.createFeatureBuilder().description("large granite pedestal").type(FEATURE_TABLE)
                        .name("room7Table").build();

        Event room7Search =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS_DELAY)
                        .entity(room7).responses("You see 3 chests aligned to the right and a pedestal with 3 keys.")
                        .build();
        Event room7TableSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .entity(room7Table).responses("You see a round smooth 3 foot tall granite pedestal.").build();
        Event room7ChestRedSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .entity(room7ChestRed).responses(
                        "${room7ChestRed|open|An open|A closed} and ${room7ChestRed|locked} red chest on the floor.")
                        .build();
        Event room7ChestWhiteSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .entity(room7ChestWhite).responses(
                        "${room7ChestWhite|open|An open|A closed} and ${room7ChestWhite|locked} white chest on the floor.")
                        .build();
        Event room7ChestBlueSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .entity(room7ChestBlue).responses(
                        "${room7ChestBlue|open|An open|A closed} and ${room7ChestBlue|locked} blue chest on the floor.")
                        .build();

        Event room7ChestTrap2 =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ONCE_DELAY)
                        .delay(2).entity(room7ChestRed)
                        .responses("The poison courses through your veins, you feel weak.").build();
        Event room7ChestTrap3 = entityService.createEventBuilder().actionType(ActionType.HORRIBLE_DEATH)
                .triggerType(TriggerType.ONCE_DELAY).delay(4).entity(room7ChestRed)
                .responses("The poison enters your brain, your body becomes paralyzed... you can't breath... you die.")
                .build();

        List<Event> room7ChestTraps = new ArrayList<>();
        room7ChestTraps.add(room7ChestTrap2);
        room7ChestTraps.add(room7ChestTrap3);

        Event room7ChestRedTrap1 =
                entityService.createEventBuilder().actionType(ActionType.ADD_EVENT).triggerType(TriggerType.ONCE)
                        .eventType(EXPLORING).events(room7ChestTraps).entity(room7ChestRed)
                        .responses("You feel a small prick in your finger as you turn the key.").build();
        Event room7ChestWhiteTrap1 =
                entityService.createEventBuilder().actionType(ActionType.ADD_EVENT).triggerType(TriggerType.ONCE)
                        .eventType(EXPLORING).events(room7ChestTraps).entity(room7ChestWhite)
                        .responses("You feel a small prick in your finger as you turn the key.").build();
        Event room7ChestBlueTrap1 =
                entityService.createEventBuilder().actionType(ActionType.ADD_EVENT).triggerType(TriggerType.ONCE)
                        .eventType(EXPLORING).events(room7ChestTraps).entity(room7ChestBlue)
                        .responses("You feel a small prick in your finger as you turn the key.").build();

        entityService.add(room7, AREA_SEARCH, room7Search);
        entityService.add(room7, AREA_ENTER, room7Search);
        entityService.add(room7Table, FEATURE_SEARCH, room7TableSearch);
        entityService.add(room7ChestRed, FEATURE_SEARCH, room7ChestRedSearch);
        entityService.add(room7ChestWhite, FEATURE_SEARCH, room7ChestWhiteSearch);
        entityService.add(room7ChestBlue, FEATURE_SEARCH, room7ChestBlueSearch);

        entityService.add(room7ChestRed, FEATURE_TRAP, room7ChestRedTrap1);
        entityService.add(room7ChestWhite, FEATURE_TRAP, room7ChestWhiteTrap1);
        entityService.add(room7ChestBlue, FEATURE_TRAP, room7ChestBlueTrap1);


        Event room2SearchNorth2 = entityService.createEventBuilder().actionType(ActionType.MAKE_VISIBLE)
                .triggerType(TriggerType.ONCE_DELAY).entity(room2NorthDoor).delay(1)
                .responses("A door suddenly appears.").build();

        entityService.add(room2, AREA_NORTH_SEARCH, room2SearchNorth1);
        entityService.add(room2, AREA_NORTH_SEARCH, room2SearchNorth2);


        Event room1ChestSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("an old and dusty ${room1Chest|open|open|closed} chest").entity(room1Chest).build();
        entityService.add(room1Chest, FEATURE_SEARCH, room1ChestSearch);

        Event room2EastDoorSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("You see ${room2EastDoor|open} iron door").entity(room2EastDoor).build();
        entityService.add(room2EastDoor, FEATURE_SEARCH, room2EastDoorSearch);

        Event room2SouthDoorSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("You see ${room2SouthDoor|open} old wooden oak door.").entity(room2SouthDoor)
                        .build();
        entityService.add(room2SouthDoor, FEATURE_SEARCH, room2SouthDoorSearch);

        Event room3NorthDoorSearch =
                entityService.createEventBuilder().actionType(ActionType.DESCRIBE).triggerType(TriggerType.ALWAYS)
                        .responses("${room3NorthDoor|open|An open|A closed} dark and dusty curtain")
                        .entity(room3NorthDoor).build();
        entityService.add(room3NorthDoor, FEATURE_SEARCH, room3NorthDoorSearch);


        EntityFacade room1ChestLock = entityFacadeBuilderFactory.builder().entity(room1Chest).attribute(LOCKED).build();
        EntityFacade room2EastDoorLock =
                entityFacadeBuilderFactory.builder().entity(room2EastDoor).attribute(LOCKED).build();

        EntityUtil.add(room1ChestLock);
        EntityUtil.add(room2EastDoorLock);

        room1.installFeature(room1Chest, null);
        room1.installFeature(room2SouthDoor, DIRECTION_NORTH);
        room2.installFeature(room2EastDoor, DIRECTION_EAST);
        room2.installFeature(room2SouthDoor, DIRECTION_SOUTH);
        room2.installFeature(room2NorthDoor, DIRECTION_NORTH);
        room3.installFeature(room2EastDoor, DIRECTION_WEST);
        room3.installFeature(room3NorthDoor, DIRECTION_NORTH);
        room6.installFeature(room6Monster, null);
        room7.installFeature(room7Table, null);
        room7.installFeature(room7ChestBlue, null);
        room7.installFeature(room7ChestRed, null);
        room7.installFeature(room7ChestWhite, null);


        // Items.
        Item key1 = entityService.createItemBuilder().type(ITEM_KEY).name("key1").description("shiny blue key")
                .targetFeature(room2EastDoor).build();
        Item key2 = entityService.createItemBuilder().type(ITEM_KEY).name("key2").description("dull iron key")
                .targetFeature(room1Chest).build();
        Item gem1 =
                entityService.createItemBuilder().type(ITEM_GEM).name("gem1").description("sparkling red ruby").build();
        Item gem2 = entityService.createItemBuilder().type(ITEM_GEM).name("gem2").description("bright shiny diamond")
                .build();
        Item potion1 =
                entityService.createItemBuilder().type(ITEM_POTION).name("potion1").description("cure poison potion")
                        .build();
        Item keyGold = entityService.createItemBuilder().type(ITEM_KEY).name("keyGold").description("gold key")
                .targetFeature(room7ChestBlue).build();
        Item keySilver = entityService.createItemBuilder().type(ITEM_KEY).name("keySilver").description("silver key")
                .targetFeature(room7ChestRed).build();
        Item keyBronze = entityService.createItemBuilder().type(ITEM_KEY).name("keyBronze").description("bronze key")
                .targetFeature(room7ChestWhite).build();

        entityService.add(gem1, VALUE, 5);
        entityService.add(gem2, VALUE, 20);

        room1Chest.addItem(gem1);
        room1.addItem(key1);
        room3.addItem(key2);

        entityService.add(keyGold, VISIBLE);
        entityService.add(keySilver, VISIBLE);
        entityService.add(keyBronze, VISIBLE);

        room7Table.addItem(keyGold);
        room7Table.addItem(keySilver);
        room7Table.addItem(keyBronze);


        room7ChestWhite.addItem(potion1);
        room7ChestRed.addItem(gem2);


        entityService.add(room7ChestBlue, LOCKED);
        entityService.add(room7ChestRed, LOCKED);
        entityService.add(room7ChestWhite, LOCKED);

        // Summary
        entityService.addArea(room1);
        entityService.addArea(room2);
        entityService.addArea(room3);
        entityService.addArea(room4);
        entityService.addArea(room5);
        entityService.addArea(room6);
        entityService.addArea(room7);

        LOGGER.info("...complete.");
    }
}
