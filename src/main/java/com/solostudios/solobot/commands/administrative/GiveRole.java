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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        } else {
            if (args.length == 0) {
                message.getChannel().sendMessage("Please mention a user, or say that user's name (one word only)").queue();
                return;
            }
            String suserToGiveRole = args[1];
            Guild guild = message.getGuild();

            List<Member> banList = guild.getMembers();
            boolean selectedUser = false;
            for (Member member : banList) {
                String name = member.getUser().getName().toLowerCase();
                String effectiveName = member.getEffectiveName().toLowerCase();
                if (((effectiveName.contains(suserToGiveRole.toLowerCase())) || name.contains(suserToGiveRole.toLowerCase())) && !selectedUser) {
                    userToGiveRole = member;
                    selectedUser = true;
                }
            }
            if (userToGiveRole == null) {
                message.getChannel().sendMessage("Could not find user " + suserToGiveRole).queue();
                return;
            }
        }

        if (message.getMentionedRoles().size() > 0) {
            roleToGive = message.getMentionedRoles().get(0);
        } else {
            if (args.length == 1) {
                message.getChannel().sendMessage("Please mention a role, or say that role's name (one word only)").queue();
                return;
            }
            String sroleToGive = args[2];
            Guild guild = message.getGuild();

            List<Role> roleList = guild.getRoles();
            boolean selectedRole = false;
            for (Role role : roleList) {
                String name = role.getName();
                if (((name.contains(sroleToGive.toLowerCase()))) && !selectedRole) {
                    roleToGive = role;
                    selectedRole = true;
                }
            }
            if (roleToGive == null) {
                message.getChannel().sendMessage("Could not find role " + sroleToGive).queue();
                return;
            }
        }

        if (messageReceivedEvent.getGuild().getMember(author).getPermissions().contains(MANAGE_ROLES)) { //Check if the user can ban members.
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
                return;
            }
            return;
        } else {
            message.getChannel().sendMessage("You have insufficient permissions! " + author.getAsMention()).queue();
        }
    }
}