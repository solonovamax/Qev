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
import com.solostudios.solobot.framework.commands.ArgumentContainer;
import com.solostudios.solobot.framework.commands.errors.IllegalInputException;
import com.solostudios.solobot.framework.utility.WebUtils;
import com.solostudios.solobot.soloBOT;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Youtube extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(Youtube.class);

    public Youtube() {
        super("youtube");
        this.withCategory("Search");
        this.withDescription("Searches (youtube)[https://youtube.com] for a specific video.");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "search")
                        .put("type", String.class)
                        .put("error", "Invalid search!")));
        this.withUsage("youtube {search}");
    }

    @Override
    public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {

        //https://www.urlencoder.io/learn/

        String urlStart = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&order=viewCount&q=";
        String urlSearch = args.getString("search");
        String urlEnd = "&type=video&videoDefinition=high&key=" + (soloBOT.settings != null ? soloBOT.settings.getString("youtube") : null);

        String url;
        url = urlStart + URLEncoder.encode(urlSearch, StandardCharsets.UTF_8) + urlEnd;


        JSONObject youtubeJSON = WebUtils.readJSONObjectFromUrl(url);
        logger.debug(youtubeJSON != null ? youtubeJSON.toString(11) : null);
        if ((youtubeJSON != null ? youtubeJSON.getJSONArray("items").length() : 0) == 0)
            event.getChannel().sendMessage("No results found for search " + args.getString("search")).queue();
        else
            event.getChannel().sendMessage("https://youtube.com/watch?v=" + youtubeJSON.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId")).queue();

    }

}