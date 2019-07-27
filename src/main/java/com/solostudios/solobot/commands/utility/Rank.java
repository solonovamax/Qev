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

import com.solostudios.solobot.abstracts.AbstractCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bson.Document;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.solostudios.solobot.framework.utility.MongoDBtoJSON.toJSONObject;
import static com.solostudios.solobot.framework.utility.Sort.sortByValue;
import static com.solostudios.solobot.main.StatsHandler.getGuild;

public class Rank extends AbstractCommand {
    public Rank() {
        super("rank",
                "Statistics",
                "Gets the rank of a given user.",
                "rank \n" +
                        "rank {@user}",
                true,
                "r");
    }

    @Override
    public void run(MessageReceivedEvent event, Message message, String[] args) throws IllegalArgumentException {
        HashMap<String, Integer> leaderBoard = new HashMap<>();

        for (Map.Entry<String, Object> entry : getGuild(message.getGuild()).toMap().entrySet()) {
            if (!(entry.getValue() instanceof Document))
                continue;
            JSONObject entryValue = toJSONObject((Document) entry.getValue());
            leaderBoard.put(event.getJDA().getUserById(entryValue.getString("userIDString")).getId(), entryValue.getInt("xp"));
        }

        leaderBoard = sortByValue(leaderBoard);


        int nOfUsers = 0;
        for (Map.Entry<String, Integer> entry : leaderBoard.entrySet()) {
            switch (nOfUsers++) {
            }

            if (entry.getKey().equals(message.getAuthor().getId())) {
                message.getChannel().sendMessage(
                        new EmbedBuilder()
                                .setTitle(message.getAuthor().getName())
                                .addField("Position:", nOfUsers + "", false)
                                .addField("XP:", entry.getValue().toString(), false)
                                .build()
                ).queue();
            }
        }
    }
}
