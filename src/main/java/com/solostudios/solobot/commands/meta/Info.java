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

package com.solostudios.solobot.commands.meta;

import com.solostudios.solobot.abstracts.AbstractCommand;
import com.solostudios.solobot.soloBOT;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Info extends AbstractCommand {
    public Info() {
        super("info",
                "Utility",
                "Returns bot info",
                "info",
                true);
    }

    @Override
    public void run(MessageReceivedEvent event, Message message, String[] args) throws IllegalArgumentException {
        JDA jda = event.getJDA();
        JDA.ShardInfo shardInfo = jda.getShardInfo();
        int shardID = shardInfo.getShardId();
        int shardTotal = shardInfo.getShardTotal();
        message.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("soloBOT Shard #" + (shardID + 1) + " out of " + shardTotal)
                .addField("# of servers", event.getJDA().getGuilds().size() + "", false)
                .addField("Github", "https://github.com/solonovamax/soloBOT", false)
                .addField("Uptime: ",
                        ((int) ((System.currentTimeMillis() - (soloBOT.START_TIME)) / 1000.00 / 60.00 / 60.00 * 100.00)) / 100.00 + " hours",
                        false)
                .addField("Support Server", soloBOT.SUPPORT_SERVER, false)
                .setThumbnail(jda.getSelfUser().getAvatarUrl())
                .setImage(jda.getSelfUser().getAvatarUrl())
                .setFooter("By solonovamax#3163", "https://cdn.discordapp.com/avatars/195735703726981120/f6277c9582ee4509be2ab7094b340dec.png")
                .build()).queue();
    }
}
