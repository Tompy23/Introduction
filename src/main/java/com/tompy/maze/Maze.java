package com.tompy.maze;

import com.tompy.adventure.Adventure;
import com.tompy.adventure.AdventureImpl;
import com.tompy.common.Coordinates;
import com.tompy.common.Coordinates2DImpl;
import com.tompy.directive.ActionType;
import com.tompy.directive.TriggerType;
import com.tompy.entity.Actor.Actor;
import com.tompy.entity.EntityFacadeBuilderFactory;
import com.tompy.entity.EntityService;
import com.tompy.entity.area.Area;
import com.tompy.entity.event.Event;
import com.tompy.exit.Exit;
import com.tompy.exit.ExitBuilderFactory;
import com.tompy.io.UserInput;
import com.tompy.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.Random;

import static com.tompy.directive.ActionType.ACTION_ACTOR_MOVE;
import static com.tompy.directive.ActionType.ACTION_END_ADVENTURE;
import static com.tompy.directive.Direction.*;
import static com.tompy.directive.EventType.EVENT_ACTOR_PROGRAM;
import static com.tompy.directive.EventType.EVENT_AREA_ENTER;
import static com.tompy.directive.MoveStrategyType.MOVE_RANDOM;
import static com.tompy.directive.TriggerType.TRIGGER_ALWAYS;

public class Maze extends AdventureImpl implements Adventure {
    private static final Logger LOGGER = LogManager.getLogger(Maze.class);
    private static final int size = 4;
    private static final double exitFactor = 1.0;

    public Maze(Player player, EntityService entityService, EntityFacadeBuilderFactory entityFacadeBuilderFactory,
            ExitBuilderFactory exitBuilderFactory, UserInput userInput, PrintStream outStream) {
        super(player, entityService, entityFacadeBuilderFactory, exitBuilderFactory, userInput, outStream);
    }

    @Override
    public void create() {
        setThisAdventure(this);
        Area[] rooms = createRooms();
        createExits(rooms);
        enterRooms(rooms);

        Area winRoom = rooms[(size * size) - 2];

        Event win = eventBuilder(ACTION_END_ADVENTURE, TRIGGER_ALWAYS, winRoom, "maze.room.win")
                .build();
        addEvent(winRoom, EVENT_AREA_ENTER, win);

        // Actors testing
        for (int i = 0; i < 10; i++) {
            Actor actor = buildActor("actor-" + i, "burly bearded man", MOVE_RANDOM);
            Event actorMove =
                    eventBuilder(ACTION_ACTOR_MOVE, TRIGGER_ALWAYS, actor, "maze.actor1.move").actor(actor).build();
            programActor(actor, actorMove);
            actor.setArea(rooms[i + 1]);
        }

        // TODO put in an event that ends the adventure if the actor1 wins the game by getting to the room first.

        //programActor(actor1, <some event that triggers when he wins>);
        // In order for an Actor to interact, an event needs to be created, mostly this seems like it would be an
        // encounter event
        // so programming an actor is just adding to his takeAction list, but other events are possible like all
        // other entities.
        // Some triggers will be expanded to be called "TRIGGER_ALWAYS_WITH_PLAYER", meaning it triggers only if
        // the entity is in the same area as the player
        //
        // Remember that explore goes... actor take action, player command, and ending with EXPLORE event.  So actors
        // could in fact take an action before and after the command by applying actions to EXPLORE events.  Be
        // careful as this may change what the text says about the scene.
        // Perhaps there is a way for a character to enter a room, see that there is an item they want, stay in the room
        // and take that item on the next take action.  Maybe some funky complex actions can be combined to create
        // this effect
    }

    private Area[] createRooms() {
        Area[] rooms = new Area[size * size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int i = (y * size) + x;
                Coordinates coordinates = new Coordinates2DImpl(x, y);
                rooms[i] = buildArea(String.format("Room-%d", i), coordinates);
            }
        }

        return rooms;
    }

    private Exit[] createExits(Area[] rooms) {
        Random r = new Random();
        Exit[] exits = new Exit[size * size];
        int i = 0;
        while (i < size * size * exitFactor) {
            int a = r.nextInt(size - 1);
            int b = r.nextInt(size - 1) * size;
            int c = r.nextInt(3);

            switch (c) {
                case 0:
                    if (b != 0 && rooms[a + b].getExitForDirection(DIRECTION_NORTH) == null) {
                        i++;
                        exits[a + b] =
                                buildExit(rooms[a + b], DIRECTION_NORTH, rooms[a + b - size], DIRECTION_SOUTH, true, 1);
                    }
                    break;
                case 1:
                    if (a != size - 1 && rooms[a + b].getExitForDirection(DIRECTION_EAST) == null) {
                        i++;
                        exits[a + b] =
                                buildExit(rooms[a + b], DIRECTION_EAST, rooms[a + b + 1], DIRECTION_WEST, true, 1);
                    }
                    break;
                case 2:
                    if (b != (size - 1) * size && rooms[a + b].getExitForDirection(DIRECTION_SOUTH) == null) {
                        i++;
                        exits[a + b] =
                                buildExit(rooms[a + b], DIRECTION_SOUTH, rooms[a + b + size], DIRECTION_NORTH, true, 1);
                    }
                    break;
                case 3:
                    if (a != 0 && rooms[a + b].getExitForDirection(DIRECTION_WEST) == null) {
                        i++;
                        exits[a + b] =
                                buildExit(rooms[a + b], DIRECTION_WEST, rooms[a + b - 1], DIRECTION_EAST, true, 1);
                    }
                    break;
            }
        }

        return exits;
    }

    private void enterRooms(Area[] rooms) {
        Random r = new Random();
        for (int i = 0; i < size * size; i++) {
            r.nextInt(7);
            Area room = rooms[i];
            describeAlways(room, EVENT_AREA_ENTER, String.format("maze.room.%d", r.nextInt(7)));
        }
    }
}



