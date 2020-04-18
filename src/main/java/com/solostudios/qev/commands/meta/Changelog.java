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

package com.solostudios.qev.commands.meta;

import com.solostudios.qev.framework.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.framework.command.handler.old.ArgumentContainer;
import com.solostudios.qev.framework.old.exceptions.IllegalInputException;
import com.solostudios.qev.framework.old.utility.WebUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;


public class Changelog extends AbstractCommand {
	public Changelog() {
		super("changelog");
		this.setCategory("Utility");
		this.setDescription("Gets most recent changelog.");
		//this.setIsEnabled(false);
	}
	
	@Override
	public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		JSONArray gitInfo = WebUtilities.readJSONArrayFromUrl("https://api.github.com/repos/solonovamax/Qev/commits");
		try {
			assert gitInfo != null;
		} catch (NullPointerException e) {
			event.getChannel().sendMessage("Error while accessing github api. Please try again later.").queue();
			return;
		}
		JSONArray commits = new JSONArray();
		
		//text.length > maxLen ? `${text.substr(0, maxLen - 3)}...` : text;
		//(text.length > 50 ? text.substring(0, 50 - 3) : text)
		EmbedBuilder em = new EmbedBuilder()
				.setAuthor("Qev:master Latest Commits", "https://github.com/solonovamax/Qev/commits/master")
				.setColor(new Color(40, 150, 255));
		
		for (int i = 0; i < 6 && i < gitInfo.length(); i++) {
			JSONObject obj    = gitInfo.getJSONObject(i);
			JSONObject commit = obj.getJSONObject("commit");
			em.addField("Commit " + (i + 1) + " by " + commit.getJSONObject("committer").getString("name"),
						"[Commit](" + obj.getString("html_url") + "): " + commit.getString("message"), true);
		}
		
		event.getChannel().sendMessage(em.build()).queue();
	}
}
