package com.tompy.introduction;

import com.tompy.adventure.Adventure;
import com.tompy.adventure.AdventureImpl;
import com.tompy.entity.EntityFacadeBuilderFactory;
import com.tompy.entity.EntityService;
import com.tompy.entity.area.Area;
import com.tompy.entity.encounter.Encounter;
import com.tompy.entity.event.Event;
import com.tompy.entity.feature.Feature;
import com.tompy.entity.item.Item;
import com.tompy.exit.Exit;
import com.tompy.exit.ExitBuilderFactory;
import com.tompy.io.UserInput;
import com.tompy.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tompy.attribute.Attribute.*;
import static com.tompy.directive.ActionType.*;
import static com.tompy.directive.Direction.*;
import static com.tompy.directive.EncounterType.ENCOUNTER_ENVIRONMENT;
import static com.tompy.directive.EncounterType.ENCOUNTER_MERCHANT;
import static com.tompy.directive.EventType.*;
import static com.tompy.directive.FeatureType.*;
import static com.tompy.directive.ItemType.*;
import static com.tompy.directive.TriggerType.*;

public class Introduction extends AdventureImpl implements Adventure {
    private static final Logger LOGGER = LogManager.getLogger(Introduction.class);

    public Introduction(Player player, EntityService entityService,
            EntityFacadeBuilderFactory entityFacadeBuilderFactory, ExitBuilderFactory exitBuilderFactory,
            UserInput userInput, PrintStream outStream) {
        super(player, entityService, entityFacadeBuilderFactory, exitBuilderFactory, userInput, outStream);
    }

    @Override
    public void create() {
        setThisAdventure(this);
        LOGGER.info("Creating adventure...");

        // First map out the adventure with areas and exits
        // Areas
        Area room1 = buildArea("StartRoom");
        Area room2 = buildArea("room2");
        Area room3 = buildArea("room3");
        Area room4 = buildArea("room4");
        Area room5 = buildArea("room5");
        Area room6 = buildArea("room6");
        Area room7 = buildArea("room7");

        // Exits
        Exit exit1 = buildExit(room1, DIRECTION_NORTH, room2, DIRECTION_SOUTH, false);
        Exit exit2 = buildExit(room2, DIRECTION_EAST, room3, DIRECTION_WEST, false);
        Exit exit3 = buildExit(room3, DIRECTION_NORTH, room4, DIRECTION_SOUTH, false);
        Exit exit4 = buildExit(room4, DIRECTION_EAST, room5, DIRECTION_WEST, true);
        Exit exit5 = buildExit(room4, DIRECTION_NORTH, room6, DIRECTION_SOUTH, true);
        Exit exit6 = buildExit(room2, DIRECTION_NORTH, room7, DIRECTION_SOUTH, false);


        // Detail each area
        // Start Room
        Feature room1Chest = buildFeature(FEATURE_CHEST, "room1.chest", "room1Chest");
        add(room1Chest, LOCKED);

        describeAlways(room1Chest, EVENT_FEATURE_SEARCH, "room1.chest.search");
        Event room1ChestOpen1 =
                eventBuilder(ACTION_DESCRIBE, TRIGGER_ALWAYS_DELAY, room1Chest, "room1.chest.open.2").delay(1).build();
        Event room1ChestOpen2 = eventBuilder(ACTION_DESCRIBE, TRIGGER_ONCE, room1Chest, "room1.chest.open.1").build();
        addEvent(room1Chest, EVENT_FEATURE_OPEN, room1ChestOpen1);
        addEvent(room1Chest, EVENT_FEATURE_OPEN, room1ChestOpen2);
        describeAlways(room1Chest, EVENT_FEATURE_CLOSE, "room1.chest.close");
        describeAlways(room1Chest, EVENT_FEATURE_OPEN_BUT_LOCKED, "room1.chest.open.locked");

        Feature room1NorthDoor =
                featureBuilder(FEATURE_DOOR, "room1.door.north").name("room1NorthDoor").exit(exit1).build();

        describeAlways(room1NorthDoor, EVENT_FEATURE_SEARCH, "room1.door.north.search");
        describeAlways(room1NorthDoor, EVENT_FEATURE_OPEN, "room1.door.north.open");
        describeAlways(room1NorthDoor, EVENT_FEATURE_CLOSE, "room1.door.north.close");
        describeAlways(room1, EVENT_AREA_ENTER, "room1.enter");
        describeAlways(room1, EVENT_AREA_ENTER_SOUTH, "room1.enter.south");
        describeAlways(room1, EVENT_AREA_SEARCH, "room1.search");
        describeAlways(room1, EVENT_AREA_SOUTH_SEARCH, "room1.search.south");

        room1.installFeature(room1Chest, null);
        room1.installFeature(room1NorthDoor, DIRECTION_NORTH);

        Item gem1 = buildItem(ITEM_GEM, "gem1");
        add(gem1, VALUE, 5);
        room1Chest.addItem(gem1);


        // Room 2
        Feature room2NorthDoor =
                featureBuilder(FEATURE_DOOR, "room2.door.north").name("room2NorthDoor").exit(exit6).build();
        remove(room2NorthDoor, VISIBLE);

        Event room2NorthDoorOpen1 =
                eventBuilder(ACTION_DESCRIBE, TRIGGER_ONCE, room2NorthDoor, "room2.door.north.open.1").build();
        addEvent(room2NorthDoor, EVENT_FEATURE_OPEN, room2NorthDoorOpen1);
        Event room2NorthDoorOpen2 =
                eventBuilder(ACTION_DESCRIBE, TRIGGER_ALWAYS_DELAY, room2NorthDoor, "room2.door.north.open.2").delay(1)
                        .build();
        addEvent(room2NorthDoor, EVENT_FEATURE_OPEN, room2NorthDoorOpen2);

        Feature room2EastDoor =
                featureBuilder(FEATURE_DOOR, "room2.door.east").name("room2EastDoor").exit(exit2).build();
        add(room2EastDoor, LOCKED);

        describeAlways(room2EastDoor, EVENT_FEATURE_OPEN, "room2.door.east.open");
        describeAlways(room2EastDoor, EVENT_FEATURE_OPEN_BUT_LOCKED, "room2.door.east.open.locked");

        describeAlways(room2, EVENT_AREA_ENTER, "room2.enter");
        describeAlways(room2, EVENT_AREA_ENTER_SOUTH, "room2.enter.south");
        describeAlways(room2, EVENT_AREA_ENTER_EAST, "room2.enter.east");
        describeAlways(room2, EVENT_AREA_SEARCH, "room2.search");
        describeAlways(room2, EVENT_AREA_SOUTH_SEARCH, "room2.search.south");
        describeAlways(room2, EVENT_AREA_EAST_SEARCH, "room2.search.east");
        describeAlways(room2EastDoor, EVENT_FEATURE_SEARCH, "room2.door.east.search");

        describeAlways(room2, EVENT_AREA_NORTH_SEARCH, "room2.search.north.1");
        Event room2SearchNorth2 =
                eventBuilder(ACTION_MAKE_VISIBLE, TRIGGER_ONCE_DELAY, room2NorthDoor, "room2.search.north.2").delay(1)
                        .build();
        addEvent(room2, EVENT_AREA_NORTH_SEARCH, room2SearchNorth2);
        Event room2SearchNorth3 =
                eventBuilder(ACTION_MAKE_VISIBLE, TRIGGER_ALWAYS_DELAY, room2NorthDoor, "room2.search.north.3").delay(2)
                        .build();
        addEvent(room2, EVENT_AREA_NORTH_SEARCH, room2SearchNorth3);

        // Room 2 Portal Encounter
        Encounter room2WestPortal = buildEncounter(ENCOUNTER_ENVIRONMENT);

        Event room2WestPortalJumpIn =
                eventBuilder(ACTION_END_ADVENTURE, TRIGGER_ALWAYS, room2, "room2.portal.jump.1", "room2.portal.jump.2")
                        .memo(messages.get("room2.portal.jump.choice")).build();
        addEvent(room2WestPortal, EVENT_INTERACTION, room2WestPortalJumpIn);

        Event room2WestPortalExplore =
                eventBuilder(ACTION_EXPLORE, TRIGGER_ALWAYS, room2, "room2.portal.explore.1", "room2.portal.explore.2")
                        .memo(messages.get("room2.portal.explore.choice")).build();
        addEvent(room2WestPortal, EVENT_INTERACTION, room2WestPortalExplore);

        describeAlways(room2, EVENT_AREA_WEST_SEARCH, "room2.search.west.0");
        Event room2SearchWest1 =
                eventBuilder(ACTION_DESCRIBE, TRIGGER_ONCE_DELAY, room2, "room2.search.west.1").delay(1).build();
        addEvent(room2, EVENT_AREA_WEST_SEARCH, room2SearchWest1);
        Event room2SearchWest2 =
                eventBuilder(ACTION_ENCOUNTER, TRIGGER_ONCE_DELAY, room2, "room2.search.west.2").delay(2)
                        .encounter(room2WestPortal).build();
        addEvent(room2, EVENT_AREA_WEST_SEARCH, room2SearchWest2);

        room2.installFeature(room2EastDoor, DIRECTION_EAST);
        room2.installFeature(room1NorthDoor, DIRECTION_SOUTH);
        room2.installFeature(room2NorthDoor, DIRECTION_NORTH);

        // Key for east door is in room 1
        Item key1 = itemBuilder(ITEM_KEY, "key1").targetFeature(room2EastDoor).build();
        add(key1, VISIBLE);
        room1.addItem(key1);


        // Room 3
        Feature room3NorthDoor =
                featureBuilder(FEATURE_DOOR, "room3.door.north").name("room3NorthDoor").exit(exit3).build();

        describeAlways(room3NorthDoor, EVENT_FEATURE_SEARCH, "room3.door.north.search");
        describeAlways(room3, EVENT_AREA_ENTER, "room3.enter");
        describeAlways(room3, EVENT_AREA_SEARCH, "room3.search");
        describeAlways(room3, EVENT_AREA_WEST_SEARCH, "room3.search.west");
        describeAlways(room3, EVENT_AREA_NORTH_SEARCH, "room3.search.north");
        describeAlways(room3NorthDoor, EVENT_FEATURE_OPEN, "room3.door.north.open");
        describeAlways(room3NorthDoor, EVENT_FEATURE_CLOSE, "room3.door.north.close");

        room3.installFeature(room2EastDoor, DIRECTION_WEST);
        room3.installFeature(room3NorthDoor, DIRECTION_NORTH);

        Item key2 = itemBuilder(ITEM_KEY, "key2").targetFeature(room1Chest).build();
        add(key2, VISIBLE);
        room3.addItem(key2);


        // Room 4
        describeAlways(room4, EVENT_AREA_ENTER, "room4.enter.1", "room4.enter.2", "room4.enter.3");
        describeAlways(room4, EVENT_AREA_SEARCH, "room4.enter.1", "room4.enter.2", "room4.enter.3");
        describeAlways(room4, EVENT_AREA_ENTER_WEST, "room4.enter.west");

        // Room 5 merchant
        // Prerequisite from room 6
        Feature room6Monster = buildFeature(FEATURE_MONSTER, "room6.monster", "room6Monster");
        add(room6Monster, VALUE, 9);

        Item simpleDagger = itemBuilder(ITEM_WEAPON, "simple.dagger").targetFeature(room6Monster).build();
        add(simpleDagger, VALUE, 3);

        Item simpleSword = itemBuilder(ITEM_WEAPON, "simple.sword").targetFeature(room6Monster).build();
        add(simpleSword, VALUE, 12);

        Item simpleShield = itemBuilder(ITEM_WEAPON, "simple.shield").targetFeature(room6Monster).build();
        add(simpleShield, VALUE, 8);

        Encounter room5Merchant =
                encounterBuilder(ENCOUNTER_MERCHANT).items(new Item[]{simpleDagger, simpleSword, simpleShield})
                        .sellRate(.8).buyRate(1.2).build();

        Event room5MerchantChat = eventBuilder(ACTION_DESCRIBE, TRIGGER_ALWAYS, room5Merchant, "room5.merchant.chat")
                .memo(messages.get("room5.merchant.chat.choice")).build();
        addEvent(room5Merchant, EVENT_INTERACTION, room5MerchantChat);

        Event room5MerchantGoodbye =
                eventBuilder(ACTION_EXPLORE, TRIGGER_ALWAYS, room5Merchant, "room5.merchant.goodbye")
                        .memo(messages.get("room5.merchant.goodbye.choice")).build();
        addEvent(room5Merchant, EVENT_INTERACTION, room5MerchantGoodbye);

        Event room5MerchantEncounterEnd =
                eventBuilder(ACTION_SEND_TO_AREA, TRIGGER_ALWAYS, room5Merchant, "room5.merchant.end").area(room4)
                        .direction(DIRECTION_WEST).build();
        addEvent(room5Merchant, EVENT_ENCOUNTER_END, room5MerchantEncounterEnd);

        Event room5Enter =
                eventBuilder(ACTION_ENCOUNTER, TRIGGER_ALWAYS, room5Merchant, "room5.enter").encounter(room5Merchant)
                        .build();
        addEvent(room5, EVENT_AREA_ENTER, room5Enter);


        // Room 6
        Event room6MonsterAttack =
                eventBuilder(ACTION_END_ADVENTURE, TRIGGER_ALWAYS_DELAY, room6Monster, "room6.monster.attack").delay(1)
                        .build();

        describeAlways(room6, EVENT_AREA_ENTER, "room6.enter");

        Event room6ExitSouth = eventBuilder(ACTION_END_ADVENTURE, TRIGGER_ALWAYS, room6, "room6.exit.south").build();
        addEvent(room6, EVENT_AREA_EXIT_SOUTH, room6ExitSouth);

        Event room6Enter2 = eventBuilder(ACTION_ADD_EVENT, TRIGGER_ONCE, room6)
                .events(Collections.singletonList(room6MonsterAttack)).eventType(EVENT_EXPLORING).build();
        addEvent(room6, EVENT_AREA_ENTER, room6Enter2);

        Event swordSuccess =
                eventBuilder(ACTION_END_ADVENTURE, TRIGGER_ALWAYS, simpleSword, "simple.sword.success").build();
        addEvent(simpleSword, EVENT_WEAPON_ATTACK_SUCCESS, swordSuccess);

        Event swordSuccess2 = eventBuilder(ACTION_REMOVE_EVENT, TRIGGER_ONCE, room6)
                .events(Collections.singletonList(room6MonsterAttack)).eventType(EVENT_EXPLORING).build();
        addEvent(simpleSword, EVENT_WEAPON_ATTACK_SUCCESS, swordSuccess2);

        room6.installFeature(room6Monster, null);


        // Room 7
        Feature room7ChestRed = buildFeature(FEATURE_CHEST, "room7.chest.red", "room7ChestRed");
        room7.installFeature(room7ChestRed, null);
        add(room7ChestRed, LOCKED);

        Feature room7ChestWhite = buildFeature(FEATURE_CHEST, "room7.chest.white", "room7ChestWhite");
        room7.installFeature(room7ChestWhite, null);
        add(room7ChestWhite, LOCKED);

        Feature room7ChestBlue = buildFeature(FEATURE_CHEST, "room7.chest.blue", "room7ChestBlue");
        room7.installFeature(room7ChestBlue, null);
        add(room7ChestBlue, LOCKED);

        Feature room7Table = buildFeature(FEATURE_TABLE, "room7.table", "room7Table");
        room7.installFeature(room7Table, null);

        describeAlways(room7, EVENT_AREA_ENTER, "room7.enter");
        describeAlways(room7, EVENT_AREA_SEARCH, "room7.search");
        describeAlways(room7Table, EVENT_FEATURE_SEARCH, "room7.table.search");
        describeAlways(room7ChestRed, EVENT_FEATURE_SEARCH, "room7.chest.red.search");
        describeAlways(room7ChestWhite, EVENT_FEATURE_SEARCH, "room7.chest.white.search");
        describeAlways(room7ChestBlue, EVENT_FEATURE_SEARCH, "room7.chest.blue.search");
        describeAlways(room7ChestRed, EVENT_FEATURE_UNLOCK, "room7.chest.unlock");
        describeAlways(room7ChestRed, EVENT_FEATURE_OPEN, "room7.chest.open");
        describeAlways(room7ChestWhite, EVENT_FEATURE_UNLOCK, "room7.chest.unlock");
        describeAlways(room7ChestWhite, EVENT_FEATURE_OPEN, "room7.chest.open");
        describeAlways(room7ChestBlue, EVENT_FEATURE_UNLOCK, "room7.chest.unlock");
        describeAlways(room7ChestBlue, EVENT_FEATURE_OPEN, "room7.chest.open");

        Event room7ChestTrap2 =
                eventBuilder(ACTION_DESCRIBE, TRIGGER_ONCE_DELAY, room7, "room7.poison.2").delay(2).build();
        Event room7ChestTrap4 =
                eventBuilder(ACTION_END_ADVENTURE, TRIGGER_ONCE_DELAY, room7, "room7.poison.4").delay(4).build();

        List<Event> room7ChestTraps = new ArrayList<>();
        room7ChestTraps.add(room7ChestTrap2);
        room7ChestTraps.add(room7ChestTrap4);

        Event room7ChestRedTrap1 =
                eventBuilder(ACTION_ADD_EVENT, TRIGGER_ONCE, room7, "room7.chest.trap").events(room7ChestTraps)
                        .eventType(EVENT_EXPLORING).build();
        addEvent(room7ChestRed, EVENT_FEATURE_TRAP, room7ChestRedTrap1);

        Event room7ChestWhiteTrap1 =
                eventBuilder(ACTION_ADD_EVENT, TRIGGER_ONCE, room7, "room7.chest.trap").events(room7ChestTraps)
                        .eventType(EVENT_EXPLORING).build();
        addEvent(room7ChestWhite, EVENT_FEATURE_TRAP, room7ChestWhiteTrap1);

        Event room7ChestBlueTrap1 =
                eventBuilder(ACTION_ADD_EVENT, TRIGGER_ONCE, room7, "room7.chest.trap").events(room7ChestTraps)
                        .eventType(EVENT_EXPLORING).build();
        addEvent(room7ChestBlue, EVENT_FEATURE_TRAP, room7ChestBlueTrap1);

        Event curePoison = eventBuilder(ACTION_REMOVE_EVENT, TRIGGER_ONCE, room7, "cure.poison.1", "cure.poison.2")
                .events(room7ChestTraps).eventType(EVENT_EXPLORING).build();

        Item potionCurePoison = itemBuilder(ITEM_POTION, "potion.cure.poison").event(curePoison).build();

        Item gem2 = buildItem(ITEM_GEM, "gem2");
        add(gem2, VALUE, 20);

        Item keyGold = itemBuilder(ITEM_KEY, "key.gold").targetFeature(room7ChestBlue).build();
        Item keySilver = itemBuilder(ITEM_KEY, "key.silver").targetFeature(room7ChestRed).build();
        Item keyBronze = itemBuilder(ITEM_KEY, "key.bronze").targetFeature(room7ChestWhite).build();

        room7Table.addItem(keyGold);
        room7Table.addItem(keySilver);
        room7Table.addItem(keyBronze);

        room7ChestWhite.addItem(potionCurePoison);
        room7ChestRed.addItem(gem2);

        room7.installFeature(room2NorthDoor, DIRECTION_SOUTH);

        LOGGER.info("...complete.");
    }
}
