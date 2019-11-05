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

package com.solostudios.solobot.framework.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class UserMessageStateMachine extends ListenerAdapter {
    @SuppressWarnings("FieldCanBeLocal")
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private long channelID, userID;
    private BiConsumer<MessageReceivedEvent, String[]> action;
    private JDA jda;

    public UserMessageStateMachine(long channelID, long userID, JDA jda, BiConsumer<MessageReceivedEvent, String[]> action) {
        logger.info("constructing state machine");
        this.channelID = channelID;
        this.userID = userID;
        this.action = action;
        this.jda = jda;

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable messageTimeOut = () -> {
            this.destroyStateMachine();
            logger.debug("UserMessageStateMachine has awaited input for too long and is now exiting.");
            executor.shutdown();
        };
        executor.schedule(messageTimeOut, 20, TimeUnit.SECONDS);
    }

    public void setAction(BiConsumer<MessageReceivedEvent, String[]> action) {
        this.action = action;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().toLowerCase().split(" ");
        Message message = event.getMessage();
        if (message.getChannel().getIdLong() == channelID && message.getAuthor().getIdLong() == userID) {
            if (args[0].toLowerCase().equals("cancel")) {
                message.getChannel().sendMessage("Canceling.").queue();
                destroyStateMachine();
            } else {
                action.accept(event, args);
            }
        }
    }

    private void destroyStateMachine() {
        action = null;
        channelID = userID = 0L;
        jda.removeEventListener(this);
    }
}
