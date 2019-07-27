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
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Poll extends AbstractCommand {

    private static final String[] reactionNumbers = new String[]{
            "\u0030\u20E3",
            "\u0031\u20E3",
            "\u0032\u20E3",
            "\u0033\u20E3",
            "\u0034\u20E3",
            "\u0035\u20E3",
            "\u0036\u20E3",
            "\u0037\u20E3",
            "\u0038\u20E3",
            "\u0039\u20E3",
            "\uD83D\uDD1F"
    };

    public Poll() {
        super("poll",
                "Utility",
                "Generates a poll.\n" +
                        "You may have up to 10 answers.",
                "poll \"{question}\" {[\"answer\"] [\"answer\"] . . .}",
                true);
    }

    @Override
    public Permission getPermissions() {
        return Permission.MESSAGE_ADD_REACTION;
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {

        String[] items = Pattern.compile("\"(.*?)\"")
                .matcher(message.getContentRaw())
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);


        if (items.length < 3 || items.length > 10)
            throw new IllegalArgumentException();

        EmbedBuilder poll = new EmbedBuilder()
                .setTitle("Poll by: " + messageReceivedEvent.getAuthor().getAsTag())
                .setThumbnail(messageReceivedEvent.getAuthor().getAvatarUrl())
                .setTimestamp(OffsetDateTime.now());

        StringBuilder p = new StringBuilder();
        for (int x = 1; x < items.length; x++) {
            p.append("\n").append(reactionNumbers[x]).append(": ").append(items[x]);
        }

        poll.addField(items[0], p.toString(), false);


        message.getChannel().sendMessage(poll.build()).queue(pollMessage -> {
            for (int x = 1; x < items.length; x++) {
                pollMessage.addReaction(reactionNumbers[x]).queue();
            }
        });
    }
}
