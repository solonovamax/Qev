/*
 * Copyright (c) 2020 solonovamax <solonovamax@12oclockpoint.com>
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
 */

package com.solostudios.qev.framework.old.utility;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Generic class that contains methods to use for message manipulation
 */
public class MessageUtilities {
	private static final Logger logger = LoggerFactory.getLogger(MessageUtilities.class);
	
	public static User getUserFromMessage(MessageReceivedEvent event, String prefix) {
		Member member = getMemberFromMessage(event, prefix);
		return (member != null ? member.getUser() : null);
	}
	
	public static Member getMemberFromMessage(MessageReceivedEvent event, String prefix) {
		return getMemberFromString(event.getMessage().getContentRaw().replaceFirst(prefix, ""), event.getGuild());
	}
	
	public static Member getMemberFromString(String message, Guild guild) {
		return getMemberFromString(message, guild, 0);
	}
	
	public static Member getMemberFromString(String message, Guild guild, int item) {
		
		String[] mentions = message.trim().split("(?!<@)(\\d{18})(?=>)");
		
		if (mentions.length > item) {
			Member member = null;
			try {
				member = Objects.requireNonNull(
						guild.getMemberById(mentions[item]));
			} catch (NumberFormatException | NullPointerException ignored) {
			}
			if (member != null) {
				return member;
			}
		}
		
		if (message.length() > 0) {
			List<Member> memberList = guild.getMembers();
			
			for (Member member : memberList) {
				String name          = member.getUser().getName().toLowerCase();
				String effectiveName = member.getEffectiveName().toLowerCase();
				if ((effectiveName.contains(message.toLowerCase())) || name.contains(message.toLowerCase())) {
					return member;
				}
			}
		}
		return null;
	}
	
	public static TextChannel getTextChannelFromMessage(MessageReceivedEvent event, String prefix) {
		return getTextChannelFromString(event.getMessage().getContentRaw().replaceFirst(prefix, ""), event.getGuild());
	}
	
	public static TextChannel getTextChannelFromString(String message, Guild guild) {
		return getTextChannelFromString(message, guild, 0);
	}
	
	public static TextChannel getTextChannelFromString(String message, Guild guild, int item) {
		
		String[] channelIDs = message.split("(?!<@)(\\d{18})(?=>)");
		
		if (channelIDs.length > item) {
			TextChannel channel = null;
			try {
				channel = Objects.requireNonNull(
						guild.getTextChannelById(channelIDs[item]));
			} catch (NumberFormatException | NullPointerException ignored) {
			}
			if (channel != null) {
				return channel;
			}
		}
		
		if (message.length() > 0) {
			List<TextChannel> memberList = guild.getTextChannels();
			
			for (TextChannel channel : memberList) {
				String name = channel.getName().toLowerCase();
				if (name.contains(message.toLowerCase())) {
					return channel;
				}
			}
		}
		return null;
	}
	
	public static GuildChannel getChannelFromMessage(MessageReceivedEvent event, String prefix) {
		return getChannelFromString(event.getMessage().getContentRaw().replaceFirst(prefix, ""), event.getGuild());
	}
	
	public static GuildChannel getChannelFromString(String message, Guild guild) {
		return getChannelFromString(message, guild, 0);
	}
	
	public static GuildChannel getChannelFromString(String message, Guild guild, int item) {
		
		String[] channelIDs = message.split("(?!<@)(\\d{18})(?=>)");
		
		if (channelIDs.length > item) {
			TextChannel channel = null;
			try {
				channel = Objects.requireNonNull(
						guild.getTextChannelById(channelIDs[item]));
			} catch (NumberFormatException | NullPointerException ignored) {
			}
			if (channel != null) {
				return channel;
			}
		}
		
		if (message.length() > 0) {
			List<GuildChannel> memberList = guild.getChannels();
			
			for (GuildChannel channel : memberList) {
				String name = channel.getName().toLowerCase();
				if (name.contains(message.toLowerCase())) {
					return channel;
				}
			}
		}
		return null;
	}
	
	public static VoiceChannel getVoiceChannelFromMessage(MessageReceivedEvent event, String prefix) {
		return getVoiceChannelFromString(event.getMessage().getContentRaw().replaceFirst(prefix, ""), event.getGuild());
	}
	
	public static VoiceChannel getVoiceChannelFromString(String message, Guild guild) {
		
		if (message.length() > 0) {
			List<VoiceChannel> memberList = guild.getVoiceChannels();
			
			for (VoiceChannel channel : memberList) {
				String name = channel.getName().toLowerCase();
				if (name.contains(message.toLowerCase())) {
					return channel;
				}
			}
		}
		return null;
	}
	
	public static User getBannedUserFromMessage(MessageReceivedEvent event, String prefix) {
		return getBannedUserFromString(event.getMessage().getContentRaw().replaceFirst(prefix, ""), event.getGuild());
	}
	
	public static Role getRoleFromMessage(MessageReceivedEvent event, String prefix) {
		return getRoleFromString(event.getMessage().getContentRaw().replaceFirst(prefix, ""), event.getGuild());
	}
	
	public static Role getRoleFromString(String message, Guild guild) {
		return getRoleFromString(message, guild, 0);
	}
	
	public static Role getRoleFromString(String message, Guild guild, int item) {
		
		String[] roleIDs = message.split("(?!<@&)(\\d{18})(?=>)");
		
		if (roleIDs.length > item) {
			Role role = null;
			try {
				role = guild.getRoleById(roleIDs[item]);
			} catch (NumberFormatException | NullPointerException ignored) {
			}
			if (role != null) {
				return role;
			}
		}
		
		if (message.length() > 1) {
			
			List<Role> roleList = guild.getRoles();
			
			for (Role role : roleList) {
				String name = role.getName().toLowerCase();
				if (name.contains(message.toLowerCase())) {
					return role;
				}
			}
		}
		return null;
	}
	
	public static User getBannedUserFromString(String message, Guild guild) {
		
		if (message.length() > 1) {
			
			Exchanger<List<Guild.Ban>> ex = new Exchanger<>();
			List<Guild.Ban>            banList;
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
				banList = ex.exchange(null, 1, TimeUnit.SECONDS);
			} catch (InterruptedException | TimeoutException ignored) {
				banList = null;
			}
			
			if (!(banList == null)) {
				for (Guild.Ban bannedUser : banList) {
					String name = bannedUser.getUser().getName().toLowerCase();
					if (name.contains(message.toLowerCase())) {
						return bannedUser.getUser();
					}
				}
				return null;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static User getUserFromString(String message, Guild guild) {
		return getUserFromString(message, guild, 0);
	}
	
	public static User getUserFromString(String message, Guild guild, int item) {
		Member member = getMemberFromString(message, guild, item);
		
		return (member != null ? member.getUser() : null);
	}
}
