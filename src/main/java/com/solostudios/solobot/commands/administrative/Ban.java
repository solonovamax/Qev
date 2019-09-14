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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Exchanger;

public class Ban extends AbstractCommand {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Permission BAN_MEMBERS = Permission.BAN_MEMBERS;

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
    public void run(@NotNull MessageReceivedEvent event, @NotNull Message message, @NotNull String[] args) throws IllegalArgumentException {
        String response;
        User author = event.getAuthor();

        User userToBan = null;

        if (message.getMentionedMembers().size() > 0) {
            userToBan = message.getMentionedMembers().get(0).getUser();
        } else {
            if (args.length == 0) {
                message.getChannel().sendMessage("Please mention a user, or say that user's name (one word only)").queue();
                return;
            }
            String suserToBan = args[1];
            Guild guild = message.getGuild();

            List<Member> memberList = guild.getMembers();
            Exchanger<List<Guild.Ban>> ex = new Exchanger<>();
            boolean selectedUser = false;
            for (Member member : memberList) {
                String name = member.getUser().getName().toLowerCase();
                String effectiveName = member.getEffectiveName().toLowerCase();
                if (((effectiveName.contains(suserToBan.toLowerCase())) || name.contains(suserToBan.toLowerCase())) && !selectedUser) {
                    userToBan = member.getUser();
                    selectedUser = true;
                }
            }
            if (userToBan == null) {
                message.getChannel().sendMessage("Could not find user " + suserToBan).queue();
                return;
            }
        }

        if (event.getGuild().getMember(author).getPermissions().contains(BAN_MEMBERS)) { //Check if the user can ban members.

            if (!(author == userToBan)) { //Check if user mentioned user is the same as themselves.

                if (!(userToBan == event.getJDA().getSelfUser())) { //Check if mentioned user is equal to the bot

                    if ((args.length > 2)) { //If there is a reason provided (more than 2 arguments.) then provide reason in ban.

                        StringBuilder reason = new StringBuilder();
                        for (int x = 2; x < args.length; x++) {
                            reason.append(args[x]).append(" ");
                        }

                        message.getGuild().ban(userToBan, 7, reason.toString()).queue(); //Ban member.
                        response = "Banned user " + userToBan.getAsMention() + "!\n" + "For reason: `" + reason.toString() + "`";
                    } else {
                        message.getGuild().ban(userToBan, 7).queue(); //Ban member.
                        response = "Banned user " + userToBan.getAsMention() + "!"; //Ban message
                    }
                } else {
                    response = "You cannot ban me! " + author.getAsMention();
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
