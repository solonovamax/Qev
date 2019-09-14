/*
 *
 * Copyright 2016 2019 solonovamax <solonovamax@12oclockpoint.com>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.solostudios.solobot;

import com.solostudios.solobot.framework.commands.CommandHandler;
import com.solostudios.solobot.framework.commands.CommandListener;
import com.solostudios.solobot.framework.events.EventHandler;
import com.solostudios.solobot.framework.main.MongoDBInterface;
import com.solostudios.solobot.framework.main.Settings;
import com.solostudios.solobot.framework.utility.GameSwitcher;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class soloBOT {

    private final static Logger logger = LoggerFactory.getLogger(soloBOT.class);

    @Nullable
    public static final JSONObject settings = Settings.get(); //Can be accessed by other classes to allow for global settings check, Cannot be changed though.
    public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    public static final long START_TIME = System.currentTimeMillis(); //Might use this for an uptime command.
    public static final int shardCount = 3;
    @NotNull
    public static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    public static String DEFAULT_STATUS;
    public static String PREFIX;
    public static boolean DEBUG;
    public static String BOT_OWNER;
    public static String SUPPORT_SERVER;
    private static JDABuilder shardBuilder;

    //public static DiscordBotListAPI dblapi;

    public static void main(String[] args) {

        logger.info("Initializing level handler.");
        new MongoDBInterface();

        //Loads settings from the file.
        PREFIX = settings.getString("prefix");
        BOT_OWNER = settings.getString("botOwner");
        DEBUG = settings.getBoolean("debug");
        SUPPORT_SERVER = settings.getString("supportServer");

        //logger.info("Registering on DiscordBotList");

        /*
        dblapi = new DiscordBotListAPI.Builder()
                .token("")
                .botId("")
                .build();

         */

        logger.debug("Validating Token.");
        //Check if token exists.
        if (settings.getString("token").equals("YOUR-TOKEN-HERE")) {
            logger.error("Please input a valid token!", new IllegalArgumentException());
            return;
        }

        logger.info("Initializing Bot");
        logger.info("Constructing JDABuilder");
        //Build bot using token.
        shardBuilder = new JDABuilder(settings.getString("token"));


        //Initialize Command Handler.
        logger.info("Initializing Command Handler");
        new CommandHandler();

        //Add listeners for commands and assorted events, respectively.
        logger.info("Attaching Listeners");
        shardBuilder.addEventListeners(new CommandListener(), new EventHandler());

        logger.info("--- Sharding Bot ---");
        for (int i = 0; i < shardCount; i++) {
            try {
                logger.info("Constructing Shard " + (i + 1) + "/" + shardCount);
                JDA shard = shardBuilder.useSharding(i, shardCount)
                        .build();
                executor.scheduleAtFixedRate(new GameSwitcher(shard), 0L, 60L, TimeUnit.SECONDS);
                logger.info("Constructed Shard " + (i + 1) + "/" + shardCount);
            } catch (LoginException e) {
                logger.warn("Error while constructing shard {}/{}", i, shardCount);
                logger.error("You token is not valid!", e);
            }
        }

        executor.scheduleAtFixedRate(System::gc, 10L, 10L, TimeUnit.MINUTES);


    }
}
