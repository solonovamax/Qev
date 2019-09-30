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

package com.solostudios.solobot.commands.administrative;

import com.solostudios.solobot.framework.commands.AbstractCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Clear extends AbstractCommand {
    public Clear() {
        super("clear",
                "Utility",
                "Purges messages from a chat",
                "clear {number}",
                true,
                "purge");
    }

    @Override
    public Permission getPermissions() {
        return Permission.MESSAGE_MANAGE;
    }

    @Override
    public void run(MessageReceivedEvent event, @NotNull Message message, String[] args) throws IllegalArgumentException {
        message.delete().queue();

        if (!event.getGuild().getMember(message.getAuthor()).getPermissions().contains(Permission.MESSAGE_MANAGE)) { //Check if the user can ban members.
            message.getChannel().sendMessage("You have insufficient permissions\n" + message.getAuthor().getAsMention()).queue();
            return;
        }

        int len;
        try {
            len = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        if (len > 100) {
            for (int i = 0; i < Math.floorDiv(len, 100); i++) {
                MessageHistory history = new MessageHistory(message.getChannel());
                List<Message> msgs = history.retrievePast(100).complete();
                message.getChannel().purgeMessages(msgs);
            }
        }

        MessageHistory history = new MessageHistory(message.getChannel());
        List<Message> msgs = history.retrievePast(len % 100).complete();

        message.getChannel().purgeMessages(msgs);
    }
}
