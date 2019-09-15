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
import com.solostudios.solobot.framework.events.UserMessageStateMachine;
import com.solostudios.solobot.framework.utility.MessageUtils;
import com.solostudios.solobot.soloBOT;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCMD extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TestCMD() {
        super("test",
                "Utility",
                "test",
                "test",
                false);
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {
        if (!message.getAuthor().getId().equals(soloBOT.BOT_OWNER)) {
            return;
        }

        if (args.length == 1) {
            message.getChannel().sendMessage("Which user would you like to select?").queue((m) -> {
                logger.info("message sent properly");
                UserMessageStateMachine stateMachine = new UserMessageStateMachine(message.getChannel().getIdLong(), message.getAuthor().getIdLong(), messageReceivedEvent.getJDA(), null);
                messageReceivedEvent.getJDA().addEventListener(stateMachine);
                stateMachine.setAction((event, argList) -> {
                    User user = MessageUtils.getUserFromMessage(event, "");
                    if (user != null) {
                        message.getChannel().sendMessage("Selected user " + user.getAsTag()).queue();
                        stateMachine.destroyStateMaching();
                    } else
                        message.getChannel().sendMessage("Please say a valid user.").queue();
                });
            });
        }
    }
}