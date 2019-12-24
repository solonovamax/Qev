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

import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.database.MongoDBInterface;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import com.solostudios.qev.core.main.LevelCard;
import com.solostudios.qev.core.main.UserStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.solostudios.qev.core.utility.Sort.sortByValue;


public class Rank extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(Rank.class);
	
	public Rank() {
		super("rank");
		this.setAliases("r", "level", "l");
		this.setCategory("Statistics");
		this.setDescription("Gets the rank of a given user");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "user")
											   .put("type", Member.class)
											   .put("optional", true)
											   .put("error", "Invalid user!")));
		this.setUsage("rank <user>");
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		User author = event.getAuthor();
		User user;
		if (args.has("user")) {
			user = args.getUser("user");
		} else {
			user = author;
		}
		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Integer> leaderboard = (LinkedHashMap) MongoDBInterface.get((guild, ignore, ex) -> {
			LinkedHashMap<String, Integer> leaderBoard = new LinkedHashMap<>();
			for (Map.Entry<String, Object> entry : guild.entrySet()) {
				if (!(entry.getValue() instanceof Document)) {
					continue;
				}
				
				Document entryValue = (Document) entry.getValue();
				
				leaderBoard.put(entryValue.getString("userIDString"), entryValue.getInteger("xp"));
			}
			
			return sortByValue(leaderBoard);
			
		}, event.getGuild().getIdLong(), 0L);
		
		assert leaderboard != null;
		EmbedBuilder pos = new EmbedBuilder()
				.setColor(Color.YELLOW)
				.setTitle("Could not find user");
		
		logger.debug(leaderboard.size() + "");
		
		BufferedImage levelCard;
		
		int nOfUsers = 0;
		for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
			nOfUsers++;
			logger.debug(entry.getKey() + " + " + entry.getValue());
			if (!entry.getKey().equals(user.getId())) {
				continue;
			}
			
			
			UserStats u = new UserStats(MongoDBInterface.getGuildDocument(event.getGuild().getIdLong()), user);
			
			try {
				levelCard = LevelCard.makeLevelCard(user, u.getLevelXP(), u.getXpToNextLevel(), u.getLevel(), nOfUsers,
													leaderboard.size());
				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				ImageIO.write(levelCard, "png", bytes);
				bytes.flush();
				event.getChannel().sendFile(bytes.toByteArray(), "src/levelCard.png").queue();
				return;
			} catch (IOException ignored) {
			}
			
		}
		
		
		event.getChannel().sendMessage("Could not find specified user.").queue();
	}
}
