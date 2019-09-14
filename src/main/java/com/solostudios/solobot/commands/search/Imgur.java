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

import com.solostudios.solobot.abstracts.AbstractCommand;
import com.solostudios.solobot.framework.utility.WebUtils;
import com.solostudios.solobot.soloBOT;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Imgur extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Imgur() {
        super("imgur",
                "Search",
                "Searches imgur for an image",
                "imgur {search}",
                true);
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {
        // https://api.imgur.com/3/

        StringBuilder search = new StringBuilder();
        for (String arg : args) {
            if (args[0].equals(arg))
                continue;
            search.append(" ").append(arg);
        }

        //https://www.urlencoder.io/learn/

        String urlStart = "https://api.imgur.com/3/gallery/search/?perPage=1&q=";
        String urlSearch = search.toString();
        String urlEnd = "";

        String url;
        url = urlStart + URLEncoder.encode(urlSearch, StandardCharsets.UTF_8) + urlEnd;


        JSONObject imgurJSON = WebUtils.readJSONObjectFromUrl(url, soloBOT.settings != null ? soloBOT.settings.getString("imgur") : null);

        logger.info(imgurJSON.toString(11));

        logger.debug(imgurJSON != null ? imgurJSON.toString(11) : null);
        if ((imgurJSON != null ? imgurJSON.getJSONArray("data").length() : 0) == 0)
            message.getChannel().sendMessage("No results found for search " + search.toString()).queue();
        else
            message.getChannel().sendMessage(imgurJSON.getJSONArray("data").getJSONObject(0).getString("link")).queue();

    }
}