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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Timer;

public class SoftBan extends AbstractCommand {
    public SoftBan() {
        super("tempban",
                "Moderation",
                "Temporarily Bans a user for a selected amount of time.",
                "tempban {@user} {hours\\days\\weeks} {length}\n" +
                        "tempban {@user} {hours\\days\\weeks} {length} {reason}",
                false,
                "softban", "temporaryban");
    }

    @Override
    public Permission getPermissions() {
        return Permission.KICK_MEMBERS;
    }

    @Override
    public void run(@NotNull MessageReceivedEvent event, @NotNull Message message, @NotNull String[] args) throws IllegalArgumentException {

        EnumSet<Permission> permissions = event.getGuild().getMember(event.getJDA().getSelfUser()).getPermissions();
        if (!(permissions.contains(Permission.KICK_MEMBERS) && permissions.contains(Permission.BAN_MEMBERS))) {
            message.getChannel().sendMessage("You must give me ban & kick permissions!").queue();
            return;
        }

        User author = event.getAuthor();

        if (message.getMentionedMembers().isEmpty() || args.length < 4) { //Check if user has mention a user.
            throw new IllegalArgumentException();
        }

        if (author == message.getMentionedMembers().get(0).getUser()) { //Check if user mentioned user is the same as themselves.
            message.getChannel().sendMessage("You cannot ban yourself!\n" + author.getAsMention()).queue();
            return;
        }

        if (!message.getGuild().getMember(author).getPermissions().contains(Permission.BAN_MEMBERS)) { //Check if the user can ban members.
            message.getChannel().sendMessage("You have insufficient permissions\n" + author.getAsMention()).queue();
            return;
        }

        if (message.getMentionedMembers().get(0).getUser() == event.getJDA().getSelfUser()) { //Check if mentioned user is equal to the bot
            message.getChannel().sendMessage("You cannot ban me!\n" + author.getAsMention()).queue();
            return;
        }


        if (args.length > 4) { //If there is a reason provided (more than 4 arguments.) then provide reason in ban.
            StringBuilder reason = new StringBuilder();
            for (int x = 0; x < args.length; x++) {
                if (x < 2)
                    continue;

                reason.append(args[x]);
            }
            message.getChannel().sendMessage("Banned user " + message.getMentionedMembers().get(0).getAsMention() + "!\n" +
                    "For reason: `" + reason.toString() + "`").queue(); //Queue ban message
            message.getGuild().ban(message.getMentionedMembers().get(0), 7, reason.toString()).queue(); //Ban member.
        } else {
            message.getChannel().sendMessage("Banned user " + message.getMentionedMembers().get(0).getAsMention() + "!").queue(); //Queue ban message
            message.getGuild().ban(message.getMentionedMembers().get(0), 7).queue(); //Ban member.
        }


        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        // close the thread
                        t.cancel();
                    }
                }, 5000);

    }
}