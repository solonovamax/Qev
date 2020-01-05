/*
 * Copyright (c) 2020 solonovamax <solonovamax@12oclockpoint.com>
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
 */

package com.solostudios.qev;

import com.solostudios.qev.framework.commands.CommandHandler;
import com.solostudios.qev.framework.commands.CommandListener;
import com.solostudios.qev.framework.config.AppProperties;
import com.solostudios.qev.framework.events.EventHandler;
import com.solostudios.qev.framework.main.MongoDBInterface;
import com.solostudios.qev.framework.utility.GameSwitcher;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("WeakerAccess")
public class Qev {
	/**
	 * This will store all the settings for the bot.
	 * <p>
	 * In the future, I may migrate it over to a custom settings store, but i'm currently holding it in a JSON object.
	 */
	@Nullable
	public static final  AppProperties            settings          = AppProperties.getProperties();
	/**
	 * Stores the start time of the bot, to get uptime.
	 */
	public static final  long                     START_TIME        = System.currentTimeMillis();
	/**
	 * Stores the amount of shards will be created at runtime.
	 */
	public static final  int                      shardCount        = 3;
	/**
	 * This executor is used to schedule threads for a GC every 30 minutes and an activity switcher service.
	 */
	public static final  ScheduledExecutorService executor          = Executors.newScheduledThreadPool(1 + shardCount);
	/**
	 * List of shards that can be used to get things like user count.
	 */
	public static final  LinkedList<JDA>          shardList         = new LinkedList<>();
	/**
	 * Logger for the Qev main class. Build using SLF4J libraries
	 */
	private final static Logger                   logger            = LoggerFactory.getLogger(Qev.class);
	/**
	 * Thread pool used for the execution of commands.
	 */
	@NotNull
	public static        ExecutorService          commandThreadPool = Executors.newCachedThreadPool();
	/**
	 * Stores the default prefix of Qev.
	 */
	public static        String                   PREFIX;
	/**
	 * Stores if debug mode is enabled.
	 * <p>
	 * I'm going to eventually switch much of the logging over to requiring debug to be true.
	 */
	public static        boolean                  DEBUG;
	/**
	 * Stores the ID of the bot owner
	 */
	public static        String                   BOT_OWNER;
	public static        List<String>             BOT_ADMINS;
	/**
	 * Stores the support server invite url.
	 */
	public static        String                   SUPPORT_SERVER;
	/**
	 * Stores the current version of the bot.
	 */
	public static        String                   VERSION;
	/**
	 * This is the JDABuilder that is used to create all the shards of the bot.
	 */
	@SuppressWarnings("FieldCanBeLocal")
	private static       JDABuilder               shardBuilder;
	
	/**
	 * Main class where everything is run.
	 * <p>
	 * The JDA objects are initialized here. The
	 *
	 * @param args
	 * 		input args from command line. Completely disregarded.
	 */
	public static void main(String[] args) {
		
		logger.info("Initializing level handler.");
		//Initializes the level handler/database interface. All functions are static, so there is no need to store it.
		new MongoDBInterface();
		
		//Loads settings from the file.
		//noinspection ConstantConditions
		PREFIX = settings.defaultPrefix;
		BOT_OWNER = settings.botOwner;
		DEBUG = settings.debug;
		SUPPORT_SERVER = settings.supportServer;
		VERSION = settings.version;
		
		
		logger.debug("Validating Token.");
		//Check if token exists.
		if (settings.botToken == null || settings.botToken.equals("")) {
			logger.error("Please input a valid token!", new IllegalArgumentException());
			return;
		}
		
		logger.info("Initializing Bot");
		logger.info("Constructing JDABuilder");
		//Build bot using token from settings.
		shardBuilder = new JDABuilder(settings.botToken);
		
		
		logger.info("Initializing Command Handler");
		//Initialize Command Handler. No need to store it since all the methods are static
		new CommandHandler();
		
		
		logger.info("Attaching Listeners");
		//Add required listeners to the shards
		shardBuilder.addEventListeners(new CommandListener(), new EventHandler());
		
		/*
		Begin the sharding process for the bot.
		
		The bot will be sharded into $shardCount shards.
		 */
		logger.info("--- Sharding Bot ---");
		for (int i = 0; i < shardCount; i++) {
			try {
				logger.info("Constructing Shard " + (i + 1) + "/" + shardCount);
				//Construct a new JDA object with the shard id $i.
				JDA shard = shardBuilder.useSharding(i, shardCount)
										.build();
				shardList.add(shard);
				//Schedules a new game switcher object to run. This will change the discord presence every 60 seconds.
				executor.scheduleAtFixedRate(new GameSwitcher(shard), 5L, 60L, TimeUnit.SECONDS);
				logger.info("Constructed Shard " + (i + 1) + "/" + shardCount);
			} catch (LoginException e) {
				logger.warn("Error while constructing shard {}/{}", i, shardCount);
				logger.error("You token is not valid!", e);
			}
		}
		
		
		/*
		Schedule a GC every 30 minutes.
		
		This is so that the GC doesn't run only when the memory is close to being fully used.
		 */
		executor.scheduleAtFixedRate(System::gc, 30L, 30L, TimeUnit.MINUTES);
		
		
	}
}
