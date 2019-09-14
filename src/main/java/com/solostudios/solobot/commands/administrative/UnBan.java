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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Exchanger;

public class UnBan extends AbstractCommand {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public UnBan() {
        super("unban",
                "Moderation",
                "Unbans a user.",
                "unban {name of user}",
                true);
    }

    @Override
    public void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException {
        if (args.length > 1) {
            StringBuilder userToUnban = new StringBuilder();
            userToUnban.append(args[1]);
            for (int x = 2; x < args.length; x++) {
                userToUnban.append(" ").append(args[x]);
            }
            Guild guild = messageReceivedEvent.getGuild();

            List<Guild.Ban> banList;
            Exchanger<List<Guild.Ban>> ex = new Exchanger<>();
            guild.retrieveBanList().queue((bList) -> {
                try {
                    ex.exchange(bList);
                } catch (InterruptedException ignored) {
                }
            }, (e) -> {
                try {
                    ex.exchange(null);
                } catch (InterruptedException ignored) {
                }
            });

            try {
                banList = ex.exchange(null);
            } catch (InterruptedException ignored) {
                banList = null;
            }

            if (!(banList == null)) {
                boolean alreadyUnbanned = false;
                for (Guild.Ban bannedUser : banList) {
                    if ((bannedUser.getUser().getName().contains(userToUnban.toString())) && !alreadyUnbanned) {
                        guild.unban(bannedUser.getUser()).queue();
                        message.getChannel().sendMessage("Unbanned user " + bannedUser.getUser().getName()).queue();
                        alreadyUnbanned = true;
                    }
                }
                if (!alreadyUnbanned) {
                    message.getChannel().sendMessage("Could not find user " + userToUnban).queue();
                }
            } else {
                message.getChannel().sendMessage("Error retrieving ban list! Please contact the author if this persists.").queue();
            }
        } else {
            message.getChannel().sendMessage("Please say which user to unban. (it must be text, not a mention.)").queue();
        }
    }
}