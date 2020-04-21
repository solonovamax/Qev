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

package com.solostudios.qev.framework.command.handler.old;

import com.solostudios.qev.framework.old.main.Qev;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommandListener extends ListenerAdapter {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (!event.isFromGuild()) {
			return;
		}
		Message message = event.getMessage();
		
		if (message.getAuthor().isBot()) {
			return;
		}
		if ((!message.getContentRaw().startsWith("<@" + message.getGuild().getSelfMember().getId() + ">") &&
			 !message.getContentDisplay().startsWith(Qev.databaseInterface.getPrefix(event.getGuild().getIdLong())))) {
			return;
		}
		
		
		if (!event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
			event.getAuthor().openPrivateChannel().queue(
					pms -> pms.sendMessage("I do not have permission to post messages in chat.\n" +
										   "Please contact the server owner, and request them to give me this permission, if you wish to use any of the functionality of this bot.").queue());
			return;
		}
		
		
		JDA.ShardInfo shardInfo = event.getJDA().getShardInfo();
		
		logger.debug("Received message from shard {}/{}. Attempting to parse.", (shardInfo.getShardId() + 1),
					 shardInfo.getShardTotal());
		
		String[] args;
		if (message.getContentRaw().startsWith("<@" + message.getGuild().getSelfMember().getId() + "> ")) {
			args = message.getContentRaw().toLowerCase().replaceFirst(
					"<@" + message.getGuild().getSelfMember().getId() + "> ", "").split(" ");
			args[0] = args[0].replace(Qev.databaseInterface.getPrefix(event.getGuild().getIdLong()), "").trim();
			if (args[0].equals("info")) {
				CommandHandler.parseMessage(event, message, args);
			} else {
				event.getChannel().sendMessage(
						"If you wish to find out the prefix of the bot, mention with the word info after it.").queue();
			}
		} else {
			args = message.getContentRaw().toLowerCase().split(" ");
		}
		
		args[0] = args[0].replace(Qev.databaseInterface.getPrefix(event.getGuild().getIdLong()), "");
		
		CommandHandler.parseMessage(event, message, args);
	}
}
