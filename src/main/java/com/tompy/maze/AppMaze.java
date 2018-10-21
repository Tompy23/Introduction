package com.tompy.maze;


import com.tompy.adventure.Adventure;
import com.tompy.attribute.AttributeManagerFactoryImpl;
import com.tompy.entity.Actor.MoveStrategyFactory;
import com.tompy.entity.Actor.MoveStrategyFactoryImpl;
import com.tompy.entity.EntityFacadeBuilderFactoryImpl;
import com.tompy.entity.EntityService;
import com.tompy.entity.EntityServiceImpl;
import com.tompy.entity.event.EventManagerFactoryImpl;
import com.tompy.exit.ExitBuilderFactoryImpl;
import com.tompy.io.UserInput;
import com.tompy.io.UserInputTextImpl;
import com.tompy.player.Player;
import com.tompy.player.PlayerImpl;
import com.tompy.state.AdventureStateFactory;
import com.tompy.state.AdventureStateFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.PrintStream;

import static com.tompy.directive.Direction.DIRECTION_NORTH;

/**
 * Hello world!
 */
public class AppMaze {
    private final static Logger LOGGER = LogManager.getLogger(AppMaze.class);

    public static void main(String[] args) {
        AppMaze a = new AppMaze();
        System.exit(a.go(args));
    }

    public int go(String[] args) {
        InputStream inStream = System.in;
        PrintStream outStream = System.out;
        EntityService entityService =
                new EntityServiceImpl(new AttributeManagerFactoryImpl(), new EventManagerFactoryImpl());
        UserInput ui = new UserInputTextImpl(inStream, outStream, entityService);
        Player player = new PlayerImpl(ui.getResponse("Player name?"), null);
        outStream.println();
        Adventure adventure = new Maze(player, entityService, new EntityFacadeBuilderFactoryImpl(entityService),
                new ExitBuilderFactoryImpl(), ui, outStream);

        AdventureStateFactory stateFactory =
                new AdventureStateFactoryImpl(player, adventure, ui, outStream, entityService);

        MoveStrategyFactory moveStrategyFactory = new MoveStrategyFactoryImpl(player, adventure, entityService);

        LOGGER.info("Player [{}] enters the adventure", player.getName());

        outStream.println(String.format("%s, you are about to enter a world of adventure... ", player.getName()));
        outStream.println();

        adventure.create();

        adventure.start(stateFactory.getExploreState(), "Room-0", DIRECTION_NORTH);

        outStream.println(String.format("%s has left the adventure.", player.getName()));

        return 0;
    }
}
