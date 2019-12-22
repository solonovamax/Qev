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

package com.solostudios.qev.commands.statistics;

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import com.solostudios.qev.framework.main.MongoDBInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.solostudios.qev.framework.utility.Sort.sortByValue;


public class Leaderboards extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Leaderboards() {
		super("leaderboards");
		this.setAliases("leaderboard", "levels", "top", "leaderboards");
		this.setCategory("Utility");
		this.setDescription("Gets the leaderboards for this server");
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> leaderboard =
				(LinkedHashMap<String, Integer>) MongoDBInterface.get((guild, ignore, ex) -> {
					LinkedHashMap<String, Integer> leaderBoard = new LinkedHashMap<>();
					logger.info("1");
					for (Map.Entry<String, Object> entry : guild.entrySet()) {
						if (!(entry.getValue() instanceof Document)) {
							continue;
						}
						logger.info(new JSONObject(((Document) entry.getValue()).toJson()).toString(11));
						Document entryValue = (Document) entry.getValue();
						
						leaderBoard.put(entryValue.getString("userIDString"), entryValue.getInteger("xp"));
					}
					
					return sortByValue(leaderBoard);
					
				}, event.getGuild().getIdLong(), 0L);
		
		assert leaderboard != null;
		EmbedBuilder topTen = new EmbedBuilder()
				.setColor(Color.YELLOW)
				.setTitle("Top 10 Users");
		int nOfUsers = 0;
		for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
			nOfUsers++;
			try {
				if (nOfUsers <= 10) {
					topTen.addField("Number " + nOfUsers + ": " + Objects.requireNonNull(
							event.getGuild().getJDA().getUserById(entry.getKey())).getName(),
									"XP:" + entry.getValue().toString(), false);
				}
			} catch (NullPointerException ignored) {
				nOfUsers--;
			}
		}
		
		event.getChannel().sendMessage(topTen.build()).queue();
	}
}
