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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class FirstMessage extends AbstractCommand {

    public FirstMessage() {
        super("firstmessage",
                "Statistics",
                "Returns the first message of a given user",
                "firstmessage \n" +
                        "firstmessage {user}",
                false);
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

        message.getChannel().sendMessage(user.getAsMention() + "**'s first message was** " + message.getGuild()).queue();
    }
}
