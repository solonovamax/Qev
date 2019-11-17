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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.OffsetDateTime;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;


public class Poll extends AbstractCommand {
	
	private static final String[] reactionNumbers = new String[]{
			"\u0030\u20E3",
			"\u0031\u20E3",
			"\u0032\u20E3",
			"\u0033\u20E3",
			"\u0034\u20E3",
			"\u0035\u20E3",
			"\u0036\u20E3",
			"\u0037\u20E3",
			"\u0038\u20E3",
			"\u0039\u20E3",
			"\uD83D\uDD1F"
	};
	
	public Poll() {
		super("poll");
		this.setCategory("Utility");
		this.setDescription("Generates a poll.\n" +
							"You may have up to 10 answers.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "question")
											   .put("type", String.class)
											   .put("error", "Invalid question!"))
								  .put(new JSONObject()
											   .put("key", "answers")
											   .put("type", String.class)
											   .put("error", "Invalid answers!")));
		this.setClientPermissions(Permission.MESSAGE_ADD_REACTION);
		this.setUsage("poll \"{question}\" \"{answer1}\" \"{answer2}\" . . . \"{answer10}\"");
		this.setExample("poll \"Do you go to school?\" \"Yes!\" \"No!\"");
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		
		String[] items = Pattern.compile("\"(.*?)\"")
								.matcher(args.getString("answers"))
								.results()
								.map(MatchResult::group)
								.toArray(String[]::new);
		
		for (int i = 0; i < items.length; i++) {
			items[i] = items[i].replace("\"", "");
		}
		
		if (items.length < 2 || items.length >= 10) {
			throw new IllegalInputException("poll not in right format");
		}
		
		EmbedBuilder poll = new EmbedBuilder()
				.setTitle("Poll by: " + event.getAuthor().getAsTag())
				.setThumbnail(event.getAuthor().getAvatarUrl())
				.setTimestamp(OffsetDateTime.now());
		
		StringBuilder p = new StringBuilder();
		for (int x = 0; x < items.length; x++) {
			p.append("\n").append(reactionNumbers[x]).append(": ").append(items[x]);
		}
		
		poll.addField(args.getString("question").replace("\"", ""), p.toString(), false);
		
		
		event.getChannel().sendMessage(poll.build()).queue(pollMessage -> {
			for (int x = 0; x < items.length; x++) {
				System.out.println(items.length);
				pollMessage.addReaction(reactionNumbers[x]).queue();
			}
		});
	}
}
