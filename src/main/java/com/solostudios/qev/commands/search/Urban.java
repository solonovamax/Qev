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

package com.solostudios.qev.commands.search;

import com.solostudios.qev.framework.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.framework.command.handler.old.ArgumentContainer;
import com.solostudios.qev.framework.old.exceptions.IllegalInputException;
import com.solostudios.qev.framework.old.utility.WebUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;


public class Urban extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Urban() {
		super("urban");
		this.setCategory("Search");
		this.setDescription("Searches urban dictionary for a word.");
		this.setIsNSFW(true);
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "search")
											   .put("type", String.class)
											   .put("error", "Invalid search")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		String urlStart  = "https://api.urbandictionary.com/v0/define?term=";
		String urlSearch = args.getString("search");
		
		String url;
		url = urlStart + URLEncoder.encode(urlSearch, StandardCharsets.UTF_8);
		
		
		JSONObject urbanJSON = WebUtilities.readJSONObjectFromUrl(url);
		if ((urbanJSON != null ? urbanJSON.getJSONArray("list").length() : 0) == 0) {
			event.getChannel().sendMessage("No results found for search " + args.getString("search") + ".").queue();
		} else {
			JSONObject urbanDefinition = urbanJSON.getJSONArray("list").getJSONObject(0);
			
			EmbedBuilder em = new EmbedBuilder()
					.setTitle(args.getString("search"))
					.addField(
							"TOP DEFINITION: :thumbsup:" + urbanDefinition.getInt("thumbs_up") + " :thumbsdown:" + urbanDefinition.getInt("thumbs_down"),
							urbanDefinition.getString(
									"definition"),
							false)
					.setTimestamp(Instant.now())
					.setColor(new Color(54, 255, 248));
			
			event.getChannel().sendMessage(em.build()).queue();
		}
	}
}