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

package com.solostudios.qev.commands.administrative;

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Ban extends AbstractCommand {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Permission BAN_MEMBERS = Permission.BAN_MEMBERS;
	
	public Ban() {
		super("ban");
		this.setCategory("Moderation");
		this.setDescription("Bans a user.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "user")
											   .put("type", Member.class)
											   .put("error", "Invalid user!"))
								  .put(new JSONObject()
											   .put("key", "reason")
											   .put("type", String.class)
											   .put("error", "Invalid reason")
											   .put("default", "No reason specified")));
		this.setUsage("ban {user} <reason>");
		this.setClientPermissions(Permission.BAN_MEMBERS);
		this.setUserPermissions(Permission.BAN_MEMBERS);
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		String response;
		User   author = event.getAuthor();
		User   userToBan;
		
		userToBan = args.getUser("user");
		
		if (userToBan.getIdLong() == author.getIdLong()) {
			event.getChannel().sendMessage("You cannot ban yourself!").queue();
			return;
		}
		
		if (userToBan.getIdLong() == event.getJDA().getSelfUser().getIdLong()) {
			event.getChannel().sendMessage("You cannot ban me!").queue();
			return;
		}
		
		event.getGuild().ban(userToBan, 7, args.getString("reason")).queue(); //Ban member.
		response = "Banned user " + userToBan.getAsMention() + "!"; //Ban message
		
		event.getChannel().sendMessage(response).queue();
	}
}
