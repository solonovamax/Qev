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

import com.solostudios.solobot.abstracts.AbstractCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

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
        super("8ball",
                "Fun",
                "Ask the magic 8 ball a question! It will give you the answer.",
                "8ball {question}",
                true,
                "eightball");
    }

    @Override
    public void run(@NotNull MessageReceivedEvent messageReceivedEvent, @NotNull Message message, String[] args) throws IllegalArgumentException {
        String response = answerList[(int) Math.floor(answerList.length * Math.random())];
        messageReceivedEvent.getChannel().sendMessage(message.getAuthor().getAsMention() + " " + response).queue();
    }
}