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

package com.solostudios.qev.core.main;

import com.solostudios.qev.core.command.handler.CommandHandler;
import com.solostudios.qev.core.command.handler.CommandListener;
import com.solostudios.qev.core.config.AppProperties;
import com.solostudios.qev.core.database.MongoDBInterface;
import com.solostudios.qev.core.events.EventHandler;
import com.solostudios.qev.core.presence.GameSwitcher;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("WeakerAccess")
public class Qev {
	
	public static final  AppProperties            settings              = AppProperties.getProperties();
	public static final  long                     START_TIME            = System.currentTimeMillis();
	public static final  int                      shardCount            = 3;
	public static final  ScheduledExecutorService shardPresenceExecutor = Executors.newScheduledThreadPool(shardCount);
	public static final  ArrayList<JDA>           shardList             = new ArrayList<>();
	private final static Logger                   logger                = LoggerFactory.getLogger(Qev.class);
	@NotNull
	public static        ExecutorService          commandThreadPool     = Executors.newCachedThreadPool();
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
	public static        String                   SUPPORT_SERVER;
	public static        String                   VERSION;
	public static        List<String>             BOT_ADMINS;
	@SuppressWarnings("FieldCanBeLocal")
	private static       JDABuilder               shardBuilder;
	
	public Qev() {
		
		logger.info("Initializing level handler.");
		new MongoDBInterface();
		
		//Loads settings from the file.
		PREFIX = settings.defaultPrefix;
		BOT_OWNER = settings.botOwner;
		DEBUG = settings.debug;
		SUPPORT_SERVER = settings.supportServer;
		VERSION = settings.version;
		BOT_ADMINS = settings.botAdminList;
		
		
		logger.debug("Validating Token.");
		//Check if token exists.
		if (settings.botToken == null || settings.botToken.equals("")) {
			logger.error("Please input a valid token!", new IllegalArgumentException());
			return;
		}
		logger.info("Valid Token!");
		
		logger.info("Initializing Bot");
		logger.info("Constructing JDABuilder");
		shardBuilder = new JDABuilder(settings.botToken);
		
		
		logger.info("Initializing Command Handler");
		new CommandHandler();
		
		
		logger.info("Attaching Listeners");
		shardBuilder.addEventListeners(new CommandListener(), new EventHandler());
		
		logger.info("--- Sharding Bot ---");
		for (int i = 0; i < shardCount; i++) {
			try {
				logger.info("Constructing Shard " + (i + 1) + "/" + shardCount);
				JDA shard = shardBuilder.useSharding(i, shardCount)
										.build();
				shardList.add(shard);
				shardPresenceExecutor.scheduleAtFixedRate(new GameSwitcher(shard), 5L, 60L, TimeUnit.SECONDS);
				logger.info("Constructed Shard " + (i + 1) + "/" + shardCount);
			} catch (LoginException e) {
				logger.warn("Error while constructing shard {}/{}", i, shardCount);
				logger.error("You token is not valid!", e);
			}
		}
		
	}
}
