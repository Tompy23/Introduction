package com.tompy.introduction;


import com.tompy.adventure.api.Adventure;
import com.tompy.attribute.internal.AttributeManagerFactoryImpl;
import com.tompy.entity.api.EntityService;
import com.tompy.entity.event.internal.EventManagerFactoryImpl;
import com.tompy.entity.internal.EntityFacadeBuilderFactoryImpl;
import com.tompy.entity.internal.EntityServiceImpl;
import com.tompy.exit.internal.ExitBuilderFactoryImpl;
import com.tompy.io.UserInput;
import com.tompy.io.UserInputTextImpl;
import com.tompy.player.api.Player;
import com.tompy.player.internal.PlayerImpl;
import com.tompy.state.api.AdventureStateFactory;
import com.tompy.state.internal.AdventureStateFactoryImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Hello world!
 */
public class App {
    private final static Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        App a = new App();
        System.exit(a.go(args));
    }

    public int go(String[] args) {
        InputStream inStream = System.in;
        PrintStream outStream = System.out;
        EntityService entityService =
            new EntityServiceImpl(new AttributeManagerFactoryImpl(), new EventManagerFactoryImpl());
        UserInput ui = new UserInputTextImpl(inStream, outStream, entityService);
        Player player = new PlayerImpl(ui.getResponse("Player name?"), null);
        Adventure adventure = new Introduction(player, entityService, new EntityFacadeBuilderFactoryImpl(entityService),
            new ExitBuilderFactoryImpl(), ui, outStream);

        //Journey journey = new JourneyImpl(player, adventure, entityService);
        AdventureStateFactory stateFactory = new AdventureStateFactoryImpl(player, adventure, ui, outStream);

        LOGGER.info("Player [{}] enters the adventure", player.getName());

        outStream.println(String
            .format("%s, you are about to enter a world of adventure... you find yourself in an area...",
                player.getName()));

        adventure.create(stateFactory);

        adventure.start(stateFactory.createExploreState());

        outStream.println(String.format("%s has left the adventure.", player.getName()));

        return 0;
    }
}
