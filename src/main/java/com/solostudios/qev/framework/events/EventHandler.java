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

package com.solostudios.qev.framework.events;

import com.solostudios.qev.framework.main.MongoDBInterface;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;


public class EventHandler extends ListenerAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(EventHandler.class);
	
	@Override
	public void onReady(@Nonnull ReadyEvent event) {
		logger.info("Bot has logged in.");
	}
	
	@Override
	public void onResume(@Nonnull ResumedEvent event) {
		logger.info("Bot has resumed connection");
	}
	
	@Override
	public void onReconnect(@Nonnull ReconnectedEvent event) {
		logger.info("Bot has reconnected.");
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		MongoDBInterface.messageEvent(event); //Forward message receive event to the Level Handler.
	}
	
	@Override
	public void onDisconnect(@Nonnull DisconnectEvent event) {
		logger.info("Bot has been disconnected from the discord servers.");
	}
	
	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent e) {
		MongoDBInterface.guildJoinEvent(e); //Forward guild join event to the Level Handler.
	}
	
	@Override
	public void onGuildReady(@NotNull GuildReadyEvent event) {
		MongoDBInterface.guildReadyEvent(event); //Forward guild join event to the Level Handler.
	}
	
}
