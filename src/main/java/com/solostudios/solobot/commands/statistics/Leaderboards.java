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

package com.solostudios.solobot.commands.statistics;

import com.solostudios.solobot.abstracts.AbstractCommand;
import com.solostudios.solobot.framework.main.MongoDBInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.solostudios.solobot.framework.utility.Sort.sortByValue;

public class Leaderboards extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public Leaderboards() {
        super("leaderboard",
                "Statistics",
                "Gets the top 10 people with the most xp on the server.",
                "leaderboard",
                true,
                "leaderboards", "levels");
    }

    @Override
    public void run(@NotNull MessageReceivedEvent event, @NotNull Message message, String[] args) throws IllegalArgumentException {

        LinkedHashMap<String, Integer> leaderboard = (LinkedHashMap<String, Integer>) MongoDBInterface.get((guild, ignore, ex) -> {
            LinkedHashMap<String, Integer> leaderBoard = new LinkedHashMap<>();
            logger.info("1");
            for (Map.Entry<String, Object> entry : guild.entrySet()) {
                if (!(entry.getValue() instanceof Document))
                    continue;
                logger.info(new JSONObject(((Document) entry.getValue()).toJson()).toString(11));
                Document entryValue = (Document) entry.getValue();

                leaderBoard.put(entryValue.getString("userIDString"), entryValue.getInteger("xp"));
            }

            return sortByValue(leaderBoard);

        }, message.getGuild().getIdLong(), 0L);

        assert leaderboard != null;
        EmbedBuilder topTen = new EmbedBuilder()
                .setColor(Color.YELLOW)
                .setTitle("Top 10 Users");
        int nOfUsers = 0;
        for (Map.Entry<String, Integer> entry : leaderboard.entrySet()) {
            nOfUsers++;

            if (nOfUsers <= 10) {
                topTen.addField("Number " + nOfUsers + ": " + message.getGuild().getJDA().getUserById(entry.getKey()).getName(), "XP:" + entry.getValue().toString(), false);
            }
        }

        message.getChannel().sendMessage(topTen.build()).queue();
    }
}
