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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class Anime extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Anime() {
		super("anime");
		this.setAliases("mal", "MyAnimeList");
		this.setCategory("Search");
		this.setDescription("Searches (MyAnimeList)[https://myanimelist.net] for an anime.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "anime")
											   .put("type", String.class)
											   .put("error", "Invalid anime!")));
		this.setUsage("anime {anime}");
	}
	
	@Override
	public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
        /*
        https://api.jikan.moe/v3/search/anime/?q=no%20game%20%no%life&limit=1
         */
		
		//https://www.urlencoder.io/learn/
		
		String urlStart  = "https://api.jikan.moe/v3/search/anime/?q=";
		String urlSearch = args.getString("anime");
		String urlEnd    = "&limit=1";
		
		String url;
		url = urlStart + URLEncoder.encode(urlSearch, StandardCharsets.UTF_8) + urlEnd;
		
		
		JSONObject malJSON = WebUtilities.readJSONObjectFromUrl(url);
		//logger.info(malJSON != null ? malJSON.toString(11) : null);
		
		if ((malJSON != null ? malJSON.getJSONArray("results").length() : 0) == 0) {
			event.getChannel().sendMessage("No results found for search " + args.getString("anime")).queue();
		} else {
			url = "https://api.jikan.moe/v3/anime/" +
				  malJSON.getJSONArray("results").getJSONObject(0).getInt("mal_id") + "/";
			
			JSONObject aniJSON = WebUtilities.readJSONObjectFromUrl(url);
			
			
			if (aniJSON != null) {
				EmbedBuilder malEmbed = new EmbedBuilder()
						.setThumbnail(aniJSON.getString("image_url"))
						.setColor(new Random(aniJSON.getInt("mal_id")).nextInt(16777215))
						.addField("Title", aniJSON.getString("title"), true)
						.addField("Japanese Title", aniJSON.getString("title_japanese"), true)
						.addField("Source", aniJSON.getString("source"), true)
						.addField("Link", "[Link](" + aniJSON.getString("url") + ")", true)
						.addField("Rating", aniJSON.getString("rating"), true)
						.addField("Status", aniJSON.getString("status"), true)
						.addField("Score", Double.toString(aniJSON.getDouble("score")), true)
						.addField("Rank", Integer.toString(aniJSON.getInt("rank")), true)
						.addField("Synopsis", malJSON.getJSONArray("results").getJSONObject(0).getString("synopsis"),
								  false);
				
				event.getChannel().sendMessage(malEmbed.build()).queue();
			}
			
		}
	}
}