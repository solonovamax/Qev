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
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class Ban extends AbstractCommand {
    public Ban() {
        super("ban",
                "Moderation",
                "Bans a user.",
                "ban {@user}\n" +
                        "ban {@user} {reason}",
                true);
    }

    @Override
    public Permission getPermissions() {
        return Permission.BAN_MEMBERS;
    }

    @Override
    public void run(MessageReceivedEvent event, Message message, String[] args) throws IllegalArgumentException {
        User author = event.getAuthor();
        List<Member> mentionedMembers = message.getMentionedMembers();
        Member mentionedMember = mentionedMembers.get(0);

        List<Permission> permissions = event.getGuild().getMember(event.getJDA().getSelfUser()).getPermissions();
        if (!(permissions.contains(Permission.KICK_MEMBERS) && permissions.contains(Permission.BAN_MEMBERS))) {
            message.getChannel().sendMessage("You must give me ban & kick permissions!").queue();
            return;
        }

        if (mentionedMembers.isEmpty()) { //Check if user has mention a user.
            message.getChannel().sendMessage("You must mention a user!\n" + author.getAsMention()).queue();
        }

        if (author == mentionedMember.getUser()) { //Check if user mentioned user is the same as themselves.
            message.getChannel().sendMessage("You cannot ban yourself!\n" + author.getAsMention()).queue();
            return;
        }

        if (!event.getGuild().getMember(author).getPermissions().contains(Permission.BAN_MEMBERS)) { //Check if the user can ban members.
            message.getChannel().sendMessage("You have insufficient permissions\n" + author.getAsMention()).queue();
            return;
        }

        if (mentionedMember.getUser() == event.getJDA().getSelfUser()) { //Check if mentioned user is equal to the bot
            message.getChannel().sendMessage("You cannot ban me!\n" + author.getAsMention()).queue();
            return;
        }

        if (args.length > 2) { //If there is a reason provided (more than 2 arguments.) then provide reason in ban.
            StringBuilder reason = new StringBuilder();
            for (int x = 0; x < args.length; x++) {
                if (x < 2)
                    continue;

                reason.append(args[x]);
            }
            message.getChannel().sendMessage("Banned user " + mentionedMember.getAsMention() + "!\n" +
                    "For reason: `" + reason.toString() + "`").queue(); //Queue ban message
            message.getGuild().getController().ban(mentionedMember, 7, reason.toString()).queue(); //Ban member.
        } else {
            message.getChannel().sendMessage("Banned user " + mentionedMember.getAsMention() + "!").queue(); //Queue ban message
            message.getGuild().getController().ban(mentionedMember, 7).queue(); //Ban member.
        }

    }
}
