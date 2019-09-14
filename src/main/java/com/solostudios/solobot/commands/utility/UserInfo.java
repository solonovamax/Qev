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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserInfo extends AbstractCommand {
    public UserInfo() {
        super("userinfo",
                "Utility",
                "Gets info for specified user. (Defaults to self.)",
                "userinfo\n" +
                        "userinfo {@user}",
                true);
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {
        User author = message.getAuthor();
        User user;
        if (args.length > 1) {
            try {
                user = message.getMentionedMembers().get(0).getUser();
            } catch (IndexOutOfBoundsException e) {
                throw new IllegalArgumentException();
            }
        } else {
            user = author;
        }
        Date discJoinDateD = new Date(user.getTimeCreated().toInstant().toEpochMilli());
        Date servJoinDateD = new Date(messageReceivedEvent.getGuild().getMember(user).getTimeJoined().toInstant().toEpochMilli());
        DateFormat formatter = new SimpleDateFormat("YYYY-L-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String discJoinDate = formatter.format(discJoinDateD);
        String servJoinDate = formatter.format(servJoinDateD);

        EmbedBuilder info = new EmbedBuilder()
                .setTitle(user.getName())
                .setThumbnail(user.getAvatarUrl())
                .addField("ID", user.getId(), true)
                .addField("Link", "[Link](" + user.getAvatarUrl() + ")", true)
                .addField("Account Created Date", discJoinDate + " UTC", true)
                .addField("Server Joined Date", servJoinDate + " UTC", true);

        message.getChannel().sendMessage(info.build()).queue();
    }
}