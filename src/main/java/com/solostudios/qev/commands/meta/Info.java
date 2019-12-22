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

package com.solostudios.qev.commands.meta;

import com.solostudios.qev.Qev;
import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import com.solostudios.qev.framework.main.MongoDBInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;


public class Info extends AbstractCommand {
	public Info() {
		super("info");
		this.setCategory("Utility");
		this.setDescription("Returns bot info");
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		
		long tDiff    = (System.currentTimeMillis() - (Qev.START_TIME));
		int  uMonths  = (int) (tDiff / 1000 / 60 / 60 / 24 / 30.42);
		int  uDays    = (int) (tDiff / 1000 / 60 / 60 / 24 % 30.42);
		int  uHours   = (int) (tDiff / 1000 / 60 / 60 % 24);
		int  uMinutes = (int) (tDiff / 1000 / 60 % 60);
		int  uSeconds = (int) (tDiff / 1000 % 60);
		
		String uptime = getUptime(uMonths, uDays, uHours, uMinutes, uSeconds);
		
		DateFormat formatter = new SimpleDateFormat("YYYY-L-dd HH:mm");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		JDA           jda        = event.getJDA();
		JDA.ShardInfo shardInfo  = jda.getShardInfo();
		int           shardID    = shardInfo.getShardId();
		int           shardTotal = shardInfo.getShardTotal();
		
		try {
			event.getChannel().sendMessage(new EmbedBuilder()
												   .setTitle("Qev Shard " + (shardID + 1) + " out of " + shardTotal)
												   .setDescription("**Servers on this shard:** " +
																   event.getJDA().getGuilds().size() + "\n" +
																   "[Github](https://github.com/solonovamax/Qev)\n" +
																   "**Uptime:** " + uptime + "\n" +
																   "[Support Server](" + Qev.SUPPORT_SERVER + ")")
												   .addField("Prefix",
															 MongoDBInterface.getPrefix(event.getGuild().getIdLong()),
															 true)
												   .addField("Guild Name", event.getGuild().getName(), true)
												   .addField("Guild Region", event.getGuild().getRegion().getName(),
															 true)
												   .addField("Guild Members Online",
															 event.getGuild().getMembers().stream().filter(member ->
																												   member.getOnlineStatus() ==
																												   OnlineStatus.DO_NOT_DISTURB
																												   ||
																												   member.getOnlineStatus() ==
																												   OnlineStatus.IDLE
																												   ||
																												   member.getOnlineStatus() ==
																												   OnlineStatus.ONLINE)
																  .count()
															 + "/" + event.getGuild().getMembers().size(), true)
												   .addField("Guild Creation Date",
															 event.getGuild().getTimeCreated().format(
																	 DateTimeFormatter.ofPattern("YYYY-L-dd HH:mm")),
															 true)
												   .addField("Guild Owner", Objects.requireNonNull(
														   event.getGuild().getOwner()).getAsMention(), true)
												   .setThumbnail(jda.getSelfUser().getAvatarUrl())
												   .setFooter("By solonovamax#3163",
															  "https://cdn.discordapp.com/avatars/195735703726981120/f6277c9582ee4509be2ab7094b340dec.png")
												   .build()).queue();
		} catch (NullPointerException e) {
			event.getChannel().sendMessage("Error when trying to get guild owner. Guild owner doesn't exist??\n" +
										   "(This should be an unreachable error. If you are seeing this, then may god have mercy on your sould.)").queue();
		}
	}
	
	@NotNull
	private String getUptime(int uMonths, int uDays, int uHours, int uMinutes, int uSeconds) {
		if (uMonths > 0) {
			return getWithPlural(uMonths, "month") + ", " +
				   getWithPlural(uDays, "day") + ", " +
				   getWithPlural(uHours, "hour") + ", " +
				   getWithPlural(uMinutes, "minute") + ", " +
				   getWithPlural(uSeconds, "second");
		} else {
			if (uDays > 0) {
				return getWithPlural(uDays, "day") + ", " +
					   getWithPlural(uHours, "hour") + ", " +
					   getWithPlural(uMinutes, "minute") + ", " +
					   getWithPlural(uSeconds, "second");
			} else {
				return getWithPlural(uHours, "hour") + ", " +
					   getWithPlural(uMinutes, "minute") + ", " +
					   getWithPlural(uSeconds, "second");
			}
		}
	}
	
	@NotNull
	private String getWithPlural(int x, String name) {
		if (x == 1) {
			return x + " " + name;
		} else {
			return x + " " + name + "s";
		}
	}
}
