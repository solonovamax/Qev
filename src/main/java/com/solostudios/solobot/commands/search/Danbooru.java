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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Danbooru extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Danbooru() {
        super("danbooru");
        this.withCategory("Search");
        this.withDescription("Searches (danbooru)[https://danboorudonmai.us] for a specific tag. [NSFW]");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "tag")
                        .put("type", String.class)
                        .put("error", "Invalid tag!")));
        this.withNSFW(true);
        this.withUsage("danbooru {tag}");
        this.withEnabled(false);
    }

    @Override
    public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
        event.getChannel().sendMessage("This channel is not NSFW. \nThis command can only be used in channels with an NSFW tag.").queue();


    }
}