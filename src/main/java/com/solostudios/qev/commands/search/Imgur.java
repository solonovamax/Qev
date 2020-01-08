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

import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import com.solostudios.qev.core.main.Qev;
import com.solostudios.qev.core.utility.WebUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class Imgur extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Imgur() {
		super("imgur");
		this.setCategory("Search");
		this.setDescription("Searches [imgur](https://imgur.com) for a specific image.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "search")
											   .put("type", String.class)
											   .put("error", "Invalid search!")));
		this.setUsage("imgur {search}");
	}
	
	@Override
	public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		// https://api.imgur.com/3/
		//https://www.urlencoder.io/learn/
		
		String urlStart  = "https://api.imgur.com/3/gallery/search/?perPage=1&q=";
		String urlSearch = args.getString("search");
		String urlEnd    = "";
		
		String url;
		url = urlStart + URLEncoder.encode(urlSearch, StandardCharsets.UTF_8) + urlEnd;
		
		
		JSONObject imgurJSON =
				WebUtilities.readJSONObjectFromUrl(url, Qev.settings.tokenList.get("imgur"));
		
		if ((imgurJSON != null ? imgurJSON.getJSONArray("data").length() : 0) == 0) {
			event.getChannel().sendMessage("No results found for search " + args.getString("search")).queue();
		} else {
			event.getChannel().sendMessage(new EmbedBuilder().setImage(
					imgurJSON.getJSONArray("data").getJSONObject(0).getJSONArray("images").getJSONObject(0).getString(
							"link")).build()).queue();
		}
		
	}
}