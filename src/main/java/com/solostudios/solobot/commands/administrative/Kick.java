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

import java.util.List;
import java.util.concurrent.Exchanger;

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

        User userToKick = null;

        if (message.getMentionedMembers().size() > 0) {
            userToKick = message.getMentionedMembers().get(0).getUser();
        } else {
            if (args.length == 0) {
                message.getChannel().sendMessage("Please mention a user, or say that user's name (one word only)").queue();
                return;
            }
            String suserToKick = args[1];
            Guild guild = message.getGuild();

            List<Member> banList = guild.getMembers();
            Exchanger<List<Guild.Ban>> ex = new Exchanger<>();
            boolean selectedUser = false;
            for (Member member : banList) {
                String name = member.getUser().getName().toLowerCase();
                String effectiveName = member.getEffectiveName().toLowerCase();
                if (((effectiveName.contains(suserToKick.toLowerCase())) || name.contains(suserToKick.toLowerCase())) && !selectedUser) {
                    userToKick = member.getUser();
                    selectedUser = true;
                }
            }
            if (userToKick == null) {
                message.getChannel().sendMessage("Could not find user " + userToKick).queue();
                return;
            }
        }

        if (event.getGuild().getMember(author).getPermissions().contains(KICK_MEMBERS)) { //Check if the user can kick members.

            if (!(author == userToKick)) { //Check if user mentioned user is the same as themselves.

                if (!(userToKick == event.getJDA().getSelfUser())) { //Check if mentioned user is equal to the bot

                    if ((args.length > 2)) { //If there is a reason provided (more than 2 arguments.) then provide reason in kick.

                        StringBuilder reason = new StringBuilder();
                        for (int x = 2; x < args.length; x++) {
                            reason.append(args[x]).append(" ");
                        }

                        message.getGuild().kick(userToKick.getId(), reason.toString()).queue(); //Kick member.
                        response = "Kicked user " + userToKick.getAsMention() + "!\n" + "For reason: `" + reason.toString() + "`";
                    } else {
                        message.getGuild().kick(userToKick.getId()).queue(); //Kick member.
                        response = "Kicked user " + userToKick.getAsMention() + "!"; //Kick message
                    }
                } else {
                    response = "You cannot kick me! " + author.getAsMention();
                }
            } else {
                response = "You cannon kick yourself! " + author.getAsMention();
            }
        } else {
            response = "You have insufficient permissions! " + author.getAsMention();
        }
        message.getChannel().sendMessage(response).queue();
    }
}
