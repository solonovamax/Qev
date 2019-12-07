package com.solostudios.qev.commands.search;

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import com.solostudios.qev.framework.utility.WebUtils;
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
		
		
		JSONObject urbanJSON = WebUtils.readJSONObjectFromUrl(url);
		if ((urbanJSON != null ? urbanJSON.getJSONArray("list").length() : 0) == 0) {
			event.getChannel().sendMessage("No results found for search " + args.getString("search")).queue();
		} else {
			urbanJSON = urbanJSON.getJSONArray("list").getJSONObject(0);
			
			EmbedBuilder em = new EmbedBuilder()
					.setTitle(args.getString("search"))
					.addField(":thumbsup:" + urbanJSON.getInt("thumbs_up") + " :thumbsdown:" + urbanJSON.getInt("thumbs_down"), urbanJSON.getString("definition"), false)
					.setTimestamp(Instant.now())
					.setColor(new Color(54, 255, 248));
			
			event.getChannel().sendMessage(em.build()).queue();
		}
	}
}