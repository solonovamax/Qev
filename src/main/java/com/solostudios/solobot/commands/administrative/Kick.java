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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class Kick extends AbstractCommand {
    private Permission KICK_MEMBERS = Permission.KICK_MEMBERS;
    public Kick() {
        super("kick",
                "Moderation",
                "kicks a user",
                "kick {@user}\n" +
                        "kick {@user} {reason}",
                true);
    }

    @Override
    public Permission getPermissions() {
        return Permission.KICK_MEMBERS;
    }

    @Override
    public void run(@NotNull MessageReceivedEvent event, @NotNull Message message, String[] args) throws IllegalArgumentException {
        String response;
        User author = event.getAuthor();

        Member memberToKick = null;

        if (message.getMentionedMembers().size() > 0) {
            memberToKick = message.getMentionedMembers().get(0);
        } else {
            if (args.length > 1) {
                memberToKick = MessageUtils.getMemberFromMessage(event, args[0]);
                if (memberToKick == null) {
                    message.getChannel().sendMessage("Could not find specified user").queue();
                    return;
                }
            } else {
                message.getChannel().sendMessage("Which user would you like to kick?").queue((m) -> {
                    UserMessageStateMachine stateMachine = new UserMessageStateMachine(message.getChannel().getIdLong(), message.getAuthor().getIdLong(), event.getJDA(), null);
                    event.getJDA().addEventListener(stateMachine);
                    stateMachine.setAction((e, argList) -> {
                        Member member = MessageUtils.getMemberFromMessage(e, "");
                        if (member != null) {
                            if (!(author == member.getUser())) { //Check if user mentioned user is the same as themselves.
                                if (!(member.getUser() == event.getJDA().getSelfUser())) { //Check if mentioned user is equal to the bot
                                    message.getChannel().sendMessage("Kicked user " + member.getEffectiveName()).queue();
                                    event.getGuild().kick(member).queue();
                                    stateMachine.destroyStateMaching();
                                } else {
                                    message.getChannel().sendMessage("You cannot kick me! " + author.getAsMention()).queue();
                                }
                            } else {
                                message.getChannel().sendMessage("You cannon kick yourself! " + author.getAsMention()).queue();
                            }
                        } else {
                            message.getChannel().sendMessage("Please say a valid user.").queue();
                        }
                    });
                });
                return;
            }
        }

        if (event.getGuild().getMember(author).getPermissions().contains(KICK_MEMBERS)) { //Check if the user can ban members.
            if (!(author == memberToKick.getUser())) { //Check if user mentioned user is the same as themselves.
                if (!(memberToKick.getUser() == event.getJDA().getSelfUser())) { //Check if mentioned user is equal to the bot
                    if ((args.length > 2)) { //If there is a reason provided (more than 2 arguments.) then provide reason in ban.
                        StringBuilder reason = new StringBuilder();
                        for (int x = 2; x < args.length; x++) {
                            reason.append(args[x]).append(" ");
                        }
                        message.getGuild().kick(memberToKick, reason.toString()).queue(); //Ban member.
                        response = "Kicked user " + memberToKick.getAsMention() + "!\n" + "For reason: `" + reason.toString() + "`";
                    } else {
                        message.getGuild().kick(memberToKick).queue(); //Ban member.
                        response = "Kicked user " + memberToKick.getAsMention() + "!"; //Ban message
                    }
                } else {
                    response = "You cannot kick me! " + author.getAsMention();
                }
            } else {
                response = "You cannon ban yourself! " + author.getAsMention();
            }
        } else {
            response = "You have insufficient permissions! " + author.getAsMention();
        }
        message.getChannel().sendMessage(response).queue();
    }
}
