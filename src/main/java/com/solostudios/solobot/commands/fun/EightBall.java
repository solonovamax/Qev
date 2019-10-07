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

package com.solostudios.solobot.commands.fun;

import com.solostudios.solobot.framework.commands.AbstractCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

public class EightBall extends AbstractCommand {

    @NotNull
    private String[] answerList = new String[]{
            "Maybe.",
            "Who knows?",
            "Yes!",
            "No.",
            "Only the future will tell.",
            "Ask again!",
            "It is certain.",
            "Most likely.",
            "Very doubtful.",
            "Reply hazy, ask again later!",
            "Concentrate and ask again.",
            "My reply is no.",
            "My reply is yes.",
            "Outlook not so good.",

    };

    public EightBall() {
        super("8ball");
        this.withAliases("eightball");
        this.withCategory("Fun");
        this.withDescription("Ask the magic 8 ball a question! It will give you the answer.");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "question")
                        .put("type", String.class)
                        .put("error", "Invalid question!")));
        this.withUsage("8ball {question}");
    }

    @Override
    public void run(@NotNull MessageReceivedEvent event, JSONObject args) throws IllegalArgumentException {
        String response = answerList[(int) Math.floor(answerList.length * Math.random())];
        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + response).queue();
    }
}