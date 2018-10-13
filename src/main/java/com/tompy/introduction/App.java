package com.tompy.introduction;


import com.tompy.adventure.Adventure;
import com.tompy.attribute.AttributeManagerFactoryImpl;
import com.tompy.entity.EntityService;
import com.tompy.entity.event.EventManagerFactoryImpl;
import com.tompy.entity.EntityFacadeBuilderFactoryImpl;
import com.tompy.entity.EntityServiceImpl;
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

        AdventureStateFactory stateFactory =
                new AdventureStateFactoryImpl(player, adventure, ui, outStream, entityService);

        LOGGER.info("Player [{}] enters the adventure", player.getName());

        outStream.println(
                String.format("%s, you are about to enter a world of adventure... you find yourself in a room...",
                        player.getName()));
        outStream.println("You have been tasked with defeating the orc to the north.");
        outStream.println(
                "You will find him in a cave.  First, you must acquire the necessary weapon to defeat the nasty orc and then venture into the cave and kill it.");
        outStream.println(
                "You are in a small room, which you have just entered from the south.  You cannot return... you must complete your quest.");

        adventure.create(stateFactory);

        adventure.start(stateFactory.getExploreState(), "StartRoom");

        outStream.println(String.format("%s has left the adventure.", player.getName()));

        return 0;
    }
}
