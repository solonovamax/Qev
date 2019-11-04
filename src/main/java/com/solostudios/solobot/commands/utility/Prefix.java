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

package com.solostudios.solobot.commands.utility;

import com.solostudios.solobot.framework.commands.AbstractCommand;
import com.solostudios.solobot.framework.commands.ArgumentContainer;
import com.solostudios.solobot.framework.commands.errors.IllegalInputException;
import com.solostudios.solobot.framework.main.MongoDBInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

public class Prefix extends AbstractCommand {
    public Prefix() {
        super("prefix");
        this.withCategory("Utility");
        this.withDescription("Changes the prefix of the bot.");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "prefix")
                        .put("type", String.class)
                        .put("error", "Invalid prefix!")
                        .put("optional", true)));
        this.withUsage("prefix <prefix>");
    }

    @Override
    public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
        if (args.has("prefix")) {
            MongoDBInterface.setPrefix(event.getGuild().getIdLong(), args.getString("prefix"));
        } else {
            event.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(event.getGuild().getName())
                    .addField("Prefix", MongoDBInterface.getPrefix(event.getGuild().getIdLong()), false)
                    .build()).queue();
        }
    }
}