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

import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;


public class Kick extends AbstractCommand {
	private Permission KICK_MEMBERS = Permission.KICK_MEMBERS;
	
	public Kick() {
		super("kick");
		this.setCategory("Moderation");
		this.setDescription("Kicks a user.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "user")
											   .put("type", Member.class)
											   .put("error", "Invalid user!"))
								  .put(new JSONObject()
											   .put("key", "reason")
											   .put("type", String.class)
											   .put("default", "No reason specified")
											   .put("error", "Invalid reason!")));
		this.setClientPermissions(Permission.KICK_MEMBERS);
		this.setUserPermissions(Permission.KICK_MEMBERS);
	}
	
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		
		String response;
		User   author = event.getAuthor();
		
		Member memberToKick = ((Member) args.get("user"));
		
		
		if (!(author.getIdLong() ==
			  memberToKick.getUser().getIdLong())) { //Check if user mentioned user is the same as themselves.
			if (!(memberToKick.getUser().getIdLong() ==
				  event.getJDA().getSelfUser().getIdLong())) { //Check if mentioned user is equal to the bot
				event.getGuild().kick(memberToKick, args.getString("reason")).queue(); //Kick member.
				response = "Kicked user " + memberToKick.getAsMention() + "!"; //Kick message
			} else {
				response = "You cannot kick me! " + author.getAsMention();
			}
		} else {
			response = "You cannon ban yourself! " + author.getAsMention();
		}
		event.getChannel().sendMessage(response).queue();
	}
}
