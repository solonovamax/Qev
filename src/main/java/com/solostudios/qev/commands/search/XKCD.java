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
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class XKCD extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public XKCD() {
		super("xkcd");
		this.setCategory("Search");
		this.setDescription("Gets the specified comic from [xkcd](https://xkcd.com). Use 0 for latest comic.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "ComicNumber")
											   .put("type", int.class)
											   .put("error", "Illegal number!")
											   .put("optional", true)));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		int comicNumber = 0;
		try {
			comicNumber = args.getInt("ComicNumber");
		} catch (NullPointerException ignored) {
		}
		
		EmbedBuilder em = new EmbedBuilder()
				.setAuthor("XKCD");
		try {
			if (comicNumber == 0) {
				JSONObject obj = WebUtilities.readJSONObjectFromUrl("https://xkcd.com/info.0.json");
				em.setTitle(Objects.requireNonNull(obj).getString("safe_title"));
				em.setImage(obj.getString("img"));
				em.addField("Alt-Text", obj.getString("alt"), false);
			} else {
				JSONObject obj = WebUtilities.readJSONObjectFromUrl("https://xkcd.com/" + comicNumber + "/info.0.json");
				em.setTitle(Objects.requireNonNull(obj).getString("safe_title"));
				em.setImage(obj.getString("img"));
				em.addField("Alt-Text", obj.getString("alt"), false);
			}
			event.getChannel().sendMessage(em.build()).queue();
		} catch (JSONException e) {
			event.getChannel().sendMessage("Error in getting comics. Please try again later.").queue();
		} catch (NullPointerException e) {
			event.getChannel().sendMessage("The number `" + comicNumber + "` is not the number of a comic.").queue();
		}
	}
}