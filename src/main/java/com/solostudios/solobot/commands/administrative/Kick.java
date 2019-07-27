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
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Kick extends AbstractCommand {
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
    public void run(MessageReceivedEvent event, Message message, String[] args) throws IllegalArgumentException {

        List<Permission> permissions = event.getGuild().getMember(event.getJDA().getSelfUser()).getPermissions();
        if (!(permissions.contains(Permission.KICK_MEMBERS) && permissions.contains(Permission.BAN_MEMBERS))) {
            message.getChannel().sendMessage("You must give me ban & kick permissions!").queue();
            return;
        }

        User author = event.getAuthor();

        if (message.getMentionedMembers().isEmpty()) { //Check if user has mention a user.
            message.getChannel().sendMessage("You must mention a user!\n" + author.getAsMention()).queue();
        }

        if (author == message.getMentionedMembers().get(0).getUser()) { //Check if user mentioned user is the same as themselves.
            message.getChannel().sendMessage("You cannot kick yourself!\n" + author.getAsMention()).queue();
            return;
        }

        if (!message.getGuild().getMember(author).getPermissions().contains(Permission.KICK_MEMBERS)) { //Check if the user can ban members.
            message.getChannel().sendMessage("You have insufficient permissions\n" + author.getAsMention()).queue();
            return;
        }

        if (message.getMentionedMembers().get(0).getUser() == event.getJDA().getSelfUser()) { //Check if mentioned user is equal to the bot
            message.getChannel().sendMessage("You cannot kick me!\n" + author.getAsMention()).queue();
            return;
        }

        message.getChannel().sendMessage("Banned user " + message.getMentionedMembers().get(0).getAsMention() + "!").queue(); //Queue ban message
        message.getGuild().getController().kick(message.getMentionedMembers().get(0)).queue(); //Ban member.
    }
}
