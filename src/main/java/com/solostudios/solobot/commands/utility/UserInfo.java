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

import com.solostudios.solobot.framework.commands.AbstractCommand;
import com.solostudios.solobot.framework.commands.ArgumentContainer;
import com.solostudios.solobot.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UserInfo extends AbstractCommand {
    public UserInfo() {
        super("userinfo");
        this.withCategory("Utility");
        this.withDescription("Gets info for a certain user");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "user")
                        .put("type", Member.class)
                        .put("optional", true)
                        .put("error", "Invalid user!")));
    }

    @Override
    public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
        User author = event.getAuthor();
        User user;
        if (args.has("user")) {
            user = ((Member) args.get("user")).getUser();
        } else {
            user = author;
        }
        Date discJoinDateD = new Date(user.getTimeCreated().toInstant().toEpochMilli());
        Date servJoinDateD = new Date(event.getGuild().getMember(user).getTimeJoined().toInstant().toEpochMilli());
        DateFormat formatter = new SimpleDateFormat("YYYY-L-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String discJoinDate = formatter.format(discJoinDateD);
        String servJoinDate = formatter.format(servJoinDateD);

        EmbedBuilder info = new EmbedBuilder()
                .setTitle(user.getName())
                .setThumbnail(user.getAvatarUrl().replace(".png", ".webp?size=256"))
                .addField("ID", user.getId(), true)
                .addField("Link", "[Link](" + user.getAvatarUrl().replace(".png", ".webp?size=256") + ")", true)
                .addField("Account Created Date", discJoinDate + " UTC", true)
                .addField("Server Joined Date", servJoinDate + " UTC", true);

        event.getChannel().sendMessage(info.build()).queue();
    }
}