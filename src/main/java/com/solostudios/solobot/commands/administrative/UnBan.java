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

import com.solostudios.solobot.abstracts.AbstractCommand;
import com.solostudios.solobot.framework.events.UserMessageStateMachine;
import com.solostudios.solobot.framework.utility.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnBan extends AbstractCommand {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Permission BAN_MEMBERS = Permission.BAN_MEMBERS;

    public UnBan() {
        super("unban",
                "Moderation",
                "Unbans a user.",
                "unban {name of user}",
                true);
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {
        User author = messageReceivedEvent.getAuthor();
        if (messageReceivedEvent.getGuild().getMember(author).getPermissions().contains(BAN_MEMBERS)) {
            if (args.length > 1) {
                User bannedUser = MessageUtils.getBannedUserFromMessage(messageReceivedEvent, args[0]);
                if (bannedUser != null) {
                    messageReceivedEvent.getGuild().unban(bannedUser).queue();
                } else {
                    message.getChannel().sendMessage("Could not find specified user").queue();
                }
            } else {
                message.getChannel().sendMessage("Which user would you like to unban?").queue((m) -> {
                    UserMessageStateMachine stateMachine = new UserMessageStateMachine(message.getChannel().getIdLong(), message.getAuthor().getIdLong(), messageReceivedEvent.getJDA(), null);
                    messageReceivedEvent.getJDA().addEventListener(stateMachine);
                    stateMachine.setAction((event, argList) -> {
                        User user = MessageUtils.getBannedUserFromMessage(event, "");
                        if (user != null) {
                            message.getChannel().sendMessage("Unbanning user " + user.getAsTag()).queue();
                            messageReceivedEvent.getGuild().unban(user).queue();
                            stateMachine.destroyStateMaching();
                        } else
                            message.getChannel().sendMessage("Please say a valid user.").queue();
                    });
                });
            }
        } else {
            message.getChannel().sendMessage("Insufficient permissions!").queue();
        }
    }
}