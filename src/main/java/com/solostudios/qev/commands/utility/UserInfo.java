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

package com.solostudios.qev.commands.utility;

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


public class UserInfo extends AbstractCommand {
	public UserInfo() {
		super("userinfo");
		this.setCategory("Utility");
		this.setDescription("Gets info for a certain user");
		setIsGuildOnly(true);
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "member")
											   .put("type", Member.class)
											   .put("optional", true)
											   .put("error", "Invalid user!")));
	}
	
	@Override
	public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		try {
			if (event.isWebhookMessage()) {
				event.getChannel().sendMessage(
						"Well, you managed to run a command via a webhook. How? Heck if I know. Sadly, doing this will break this command.").queue();
				return;
			}
			Member author = event.getMember();
			assert author != null;
			Member member;
			User   user;
			if (args.has("member")) {
				member = args.getMember("member");
				user = member.getUser();
			} else {
				member = author;
				user = author.getUser();
			}
			
			Activity   activity      = member.getActivities().get(0);
			Date       discJoinDateD = new Date(member.getTimeCreated().toInstant().toEpochMilli());
			Date       servJoinDateD = new Date(member.getTimeJoined().toInstant().toEpochMilli());
			DateFormat formatter     = new SimpleDateFormat("YYYY-L-dd HH:mm");
			
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			String discJoinDate = formatter.format(discJoinDateD);
			String servJoinDate = formatter.format(servJoinDateD);
			
			String activityName = activity.getType().name();
			EmbedBuilder info = new EmbedBuilder()
					.setTitle("User Information")
					.setThumbnail(Objects.requireNonNull(user.getAvatarUrl()).replace(".png", ".webp?size=256"))
					.addField("Username", user.getName(), true)
					.addField("Nickname", (member.getNickname() == null ? "None" : member.getNickname()), true)
					.addField("Full Name", user.getAsTag(), true)
					.addField("User ID", member.getId(), true)
					.addField((activityName.equals("DEFAULT") ? "Playing" : activityName.substring(0, 1).toUpperCase() +
																			activityName.substring(1)),
							  activity.getName(), true)
					.addField("Profile Picture",
							  "[Link](" + user.getAvatarUrl().replace(".png", ".webp?size=256") + ")", true)
					.addField("Account Created Date", discJoinDate + " UTC", true)
					.addField("Server Joined Date", servJoinDate + " UTC", true);
			
			event.getChannel().sendMessage(info.build()).queue();
		} catch (NullPointerException e) {
			NullPointerException npe = new NullPointerException("The member you mentioned is not a user.");
			npe.setStackTrace(e.getStackTrace());
			npe.addSuppressed(e);
			throw npe;
		}
	}
}