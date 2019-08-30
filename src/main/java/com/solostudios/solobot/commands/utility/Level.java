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
import com.solostudios.solobot.framework.main.MongoDBInterface;
import com.solostudios.solobot.framework.main.UserStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.concurrent.Exchanger;

public class Level extends AbstractCommand {

    public Level() {
        super("level",
                "Statistics",
                "Gets the level of a given user.",
                "level \n" +
                        "level {user}",
                true,
                "l");
    }

    @Override
    public void run(@NotNull MessageReceivedEvent event, @NotNull Message message, @NotNull String[] args) throws IllegalArgumentException {
        User author = event.getAuthor();
        User user;
        if (args.length > 1) {
            try {
                user = message.getMentionedMembers().get(0).getUser();
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException();
            }
        } else {
            user = author;
        }
        Exchanger exchanger = new Exchanger<>();

        EmbedBuilder embed = (EmbedBuilder) MongoDBInterface.get((gDoc, uID, ex) -> {
            UserStats stats = new UserStats(gDoc, uID);
            // (int) (((455.0/6)* xp) + (22.5*Math.pow(xp, 2.0)) + ((5/3)*Math.pow(xp, 3.0)));
            EmbedBuilder em = new EmbedBuilder()
                    .addField("level",
                            Integer.toString(stats.getLevel()),
                            false)
                    .addField("xp",
                            Integer.toString(stats.getLevelXP()),
                            false)
                    .setColor(Color.ORANGE);
            return em;
        }, message.getGuild().getIdLong(), user.getIdLong(), exchanger);


        message.getChannel().sendMessage(embed.build()).queue();

        //5 * (lvl ^ 2) + 50 * lvl + 100
        //((455.0/6)*xp) + (22.5*xp^2) + ((5/3)*xp^3)
    }
}
