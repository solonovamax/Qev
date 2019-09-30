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

package com.solostudios.solobot.commands.search;

import com.solostudios.solobot.framework.commands.AbstractCommand;
import com.solostudios.solobot.framework.utility.WebUtils;
import com.solostudios.solobot.soloBOT;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Youtube extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(Youtube.class);

    public Youtube() {
        super("youtube",
                "Search",
                "Searches for a youtube video.",
                "youtube {query}",
                true,
                "y");
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {
        StringBuilder search = new StringBuilder();
        for (String arg : args) {
            if (args[0].equals(arg))
                continue;
            search.append(" ").append(arg);
        }

        //https://www.urlencoder.io/learn/

        String urlStart = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=viewCount&q=";
        String urlSearch = search.toString();
        String urlEnd = "&type=video&videoDefinition=high&key=" + (soloBOT.settings != null ? soloBOT.settings.getString("youtube") : null);

        String url;
        url = urlStart + URLEncoder.encode(urlSearch, StandardCharsets.UTF_8) + urlEnd;


        JSONObject youtubeJSON = WebUtils.readJSONObjectFromUrl(url);
        logger.debug(youtubeJSON != null ? youtubeJSON.toString(11) : null);
        if ((youtubeJSON != null ? youtubeJSON.getJSONArray("items").length() : 0) == 0)
            message.getChannel().sendMessage("No results found for search " + search.toString()).queue();
        else
            message.getChannel().sendMessage("https://youtube.com/watch?v=" + youtubeJSON.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId")).queue();

    }

}