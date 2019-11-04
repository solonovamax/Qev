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
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class Avatar extends AbstractCommand {
    public Avatar() {
        super("avatar");
        this.withCategory("Utility");
        this.withDescription("Gets a user's avatar");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "user")
                        .put("type", Member.class)
                        .put("optional", true)
                        .put("error", "Invalid command!")));
        this.withUsage("avatar <member>");
    }

    @Override
    public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
        try {
            User author = event.getAuthor();
            if (args.has("user")) {
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setImage(((Member) args.get("user")).getUser().getAvatarUrl()
                                .replace(".png", ".webp?size=256"))
                        .build())
                        .queue();
                event.getChannel().sendMessage(((Member) args.get("user")).getUser().getAvatarUrl().replace(".png", ".webp?size=256")).queue();
            } else {
                event.getChannel().sendMessage(new EmbedBuilder().setImage(author.getAvatarUrl().replace(".png", ".webp?size=256")).build()).queue();
                //message.getChannel().sendMessage(author.getAvatarUrl().replace(".png", ".webp?size=256")).queue();
            }
        } catch (NullPointerException e) {
            event.getChannel().sendMessage("Unknown user").queue();
        }
    }
}
