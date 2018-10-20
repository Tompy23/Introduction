package com.tompy.maze;

import com.tompy.adventure.Adventure;
import com.tompy.adventure.AdventureImpl;
import com.tompy.directive.ActionType;
import com.tompy.directive.TriggerType;
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

import static com.tompy.directive.Direction.*;
import static com.tompy.directive.EventType.EVENT_AREA_ENTER;

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

        Event win = eventBuilder(ActionType.ACTION_END_ADVENTURE, TriggerType.TRIGGER_ALWAYS, winRoom, "maze.room.win")
                .build();
        addEvent(winRoom, EVENT_AREA_ENTER, win);
    }

    private Area[] createRooms() {
        Area[] rooms = new Area[size * size];
        for (int i = 0; i < size * size; i++) {
            rooms[i] = buildArea(String.format("Room-%d", i));
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
                        exits[a + b] = buildExit(rooms[a + b], DIRECTION_EAST, rooms[a + b + 1], DIRECTION_WEST, true, 1);
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
                        exits[a + b] = buildExit(rooms[a + b], DIRECTION_WEST, rooms[a + b - 1], DIRECTION_EAST, true, 1);
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



