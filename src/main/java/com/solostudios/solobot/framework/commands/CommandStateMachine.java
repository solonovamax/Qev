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

package com.solostudios.solobot.framework.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandStateMachine extends ListenerAdapter {
    private final long channelID, userID;
    private final JDA jda;
    private final AbstractCommand command;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private JSONObject args;

    public CommandStateMachine(MessageReceivedEvent event, JDA jda, AbstractCommand command) {
        this.command = command;
        logger.info("constructing state machine");
        this.channelID = event.getChannel().getIdLong();
        this.userID = event.getAuthor().getIdLong();
        this.jda = jda;
        args = command.getDefaultArgs();
        jda.addEventListener(this);

        if (command.getArguments() == null || command.fitsArguments(args)) {
            command.run(event, args);
            destroyStateMaching();
        } else {
            event.getChannel().sendMessage(command.nextArgPrompt(args)).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        Message message = event.getMessage();
        if (message.getChannel().getIdLong() == channelID && message.getAuthor().getIdLong() == userID) {
            if (message.getContentStripped().toLowerCase().equals("cancel")) {
                message.getChannel().sendMessage("Canceling.").queue();
                destroyStateMaching();
            } else {
                try {
                    command.putNextArg(event, args);
                } catch (IllegalArgumentException e) {
                    event.getChannel().sendMessage(e.getMessage() + " ").queue();
                    e.printStackTrace();
                }
                if (command.fitsArguments(args)) {
                    command.run(event, args);
                    destroyStateMaching();
                } else {
                    event.getChannel().sendMessage(command.nextArgPrompt(args)).queue();
                }
            }
        }
    }

    private void destroyStateMaching() {
        jda.removeEventListener(this);
    }
}
