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
import com.solostudios.solobot.framework.commands.ArgumentContainer;
import com.solostudios.solobot.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Clear extends AbstractCommand {
    public Clear() {
        super("clear");
        this.withCategory("Moderation");
        this.withAliases("purge");
        this.withDescription("Clears messages from chat");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "amount")
                        .put("type", int.class)
                        .put("error", "Invalid amount of messages to clear!")));
        this.withUsage("clear {# of messages to clear}");
        this.withClientPermissions(Permission.MESSAGE_MANAGE);
        this.withUserPermissions(Permission.MESSAGE_MANAGE);
    }


    @Override
    public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
        event.getTextChannel().retrieveMessageById(event.getMessage().getId()).queue(msg -> msg.delete().queue(), error -> {
        });

        int len;
        try {
            len = args.getInt("amount");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        if (len > 100) {
            for (int i = 0; i < Math.floorDiv(len, 100); i++) {
                MessageHistory history = new MessageHistory(event.getChannel());
                List<Message> msgs = history.retrievePast(100).complete();
                event.getChannel().purgeMessages(msgs);
            }
        }

        MessageHistory history = new MessageHistory(event.getChannel());
        List<Message> msgs = history.retrievePast(len % 100).complete();

        event.getChannel().purgeMessages(msgs);
    }
}
