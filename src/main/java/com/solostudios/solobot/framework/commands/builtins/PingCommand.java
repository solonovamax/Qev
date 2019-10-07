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

package com.solostudios.solobot.framework.commands.builtins;

import com.solostudios.solobot.framework.commands.AbstractCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

public class PingCommand extends AbstractCommand {

    private static final String[] pingMessages = new String[]{
            "First ping",
            "Second Ping",
            "Third Ping",
            "Fourth Ping"
    };


    public PingCommand() {
        super("ping");
        this.withAliases("p");
        this.withCategory("Utility");
        this.withDescription("Used to ping the bot to test if it is working.\n" +
                "Can also be used to see response times of the bot.");
    }

    @Override
    public void run(MessageReceivedEvent event, JSONObject args) throws IllegalArgumentException {

        event.getChannel().sendMessage("Checking Ping...").queue(pingMessage -> {
            int pings = 4;
            double t;
            double sum = 0, min = 999, max = 0;
            double start = System.nanoTime() / 1000000;
            for (int i = 0; i < pings; i++) {
                pingMessage.editMessage(pingMessages[i]).queue();

                t = (double) System.nanoTime() / 1000000 - start;

                sum += t;

                min = Math.min(min, t);
                max = Math.max(max, t);

                start = System.nanoTime() / 1000000;
            }
            pingMessage.editMessage(":arrow_right: Average ping is "
                    + (float) Math.round(sum / 4 * 1000) / 1000
                    + "ms (min: "
                    + (float) Math.round(min * 1000) / 1000
                    + "ms, max: "
                    + (float) Math.round(max * 1000) / 1000
                    + "ms) :arrow_left:")
                    .queue();
        });
    }
}
