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
import com.solostudios.solobot.framework.events.UserMessageStateMachine;
import com.solostudios.solobot.framework.utility.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GiveRole extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Permission MANAGE_ROLES = Permission.MANAGE_ROLES;

    public GiveRole() {
        super("giverole",
                "Utility",
                "Gives a role to the user.",
                "giverole {role name}",
                true,
                "grole");
    }

    @Override
    public Permission getPermissions() {
        return Permission.MANAGE_ROLES;
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {
        String response;
        User author = messageReceivedEvent.getAuthor();

        Member userToGiveRole = null;
        Role roleToGive = null;


        if (message.getMentionedMembers().size() > 0) {
            userToGiveRole = message.getMentionedMembers().get(0);
        }
        if (message.getMentionedRoles().size() > 0) {
            roleToGive = message.getMentionedRoles().get(0);
        }

        if (args.length > 2) {
            if (userToGiveRole == null) {
                userToGiveRole = MessageUtils.getMemberFromString(args[1], message.getGuild());
            }
            if (roleToGive == null) {
                roleToGive = MessageUtils.getRoleFromString(args[2], message.getGuild());
            }
        }

        if (messageReceivedEvent.getGuild().getMember(author).getPermissions().contains(MANAGE_ROLES)) { //Check if the user can ban members.
            if (roleToGive == null || userToGiveRole == null) {
                message.getChannel().sendMessage("Which user would you like to give a role to?").queue((m) -> {
                    UserMessageStateMachine stateMachine = new UserMessageStateMachine(message.getChannel().getIdLong(), message.getAuthor().getIdLong(), messageReceivedEvent.getJDA(), null);
                    messageReceivedEvent.getJDA().addEventListener(stateMachine);
                    stateMachine.setAction((event, argList) -> {
                        Member member = MessageUtils.getMemberFromMessage(event, "");
                        if (member != null) {
                            message.getChannel().sendMessage("Selected user " + member.getEffectiveName() + ". Which role would you like to give?").queue((mes) -> {
                                UserMessageStateMachine roleStateMachine = new UserMessageStateMachine(message.getChannel().getIdLong(), message.getAuthor().getIdLong(), messageReceivedEvent.getJDA(), null);
                                messageReceivedEvent.getJDA().addEventListener(roleStateMachine);
                                roleStateMachine.setAction((ev, arList) -> {
                                    Role role = MessageUtils.getRoleFromMessage(ev, "");
                                    if (role != null) {
                                        message.getChannel().sendMessage("Giving role " + role.getName() + " to user " + member.getEffectiveName() + ".").queue();
                                        message.getGuild().addRoleToMember(member, role).queue();
                                        roleStateMachine.destroyStateMaching();
                                    } else {
                                        message.getChannel().sendMessage("Please say a valid role.").queue();
                                    }
                                });
                            });
                            stateMachine.destroyStateMaching();
                        } else
                            message.getChannel().sendMessage("Please say a valid user.").queue();
                    });
                });
            } else {
                String role = roleToGive.getName();
                String user = userToGiveRole.getEffectiveName();
                try {
                    message.getGuild().addRoleToMember(userToGiveRole, roleToGive).queue((ignored) -> {
                        message.getChannel().sendMessage("Successfully given role " + role + " to " + user).queue();
                    }, (ex) -> {
                        message.getChannel().sendMessage("Failed to give role " + role + " to " + user + " for reason:\n" + ex.getMessage()).queue();
                    });
                } catch (HierarchyException e) {
                    message.getChannel().sendMessage("Could not give role to user " + user + ", as the role " + role + " is higher than my highest one.").queue();
                }
            }
        } else {
            message.getChannel().sendMessage("You have insufficient permissions! " + author.getAsMention()).queue();
        }
    }
}