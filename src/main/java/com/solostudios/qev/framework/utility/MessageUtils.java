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

package com.solostudios.qev.framework.utility;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;


public class MessageUtils {
	private static Logger logger = LoggerFactory.getLogger(MessageUtils.class);
	
	public static Member getMemberFromMessage(MessageReceivedEvent event, String prefix) {
		if (event.getMessage().getMentionedMembers().size() > 0) {
			return event.getMessage().getMentionedMembers().get(0);
		}
		String userFetchString =
				(event.getMessage().getContentRaw().startsWith(prefix) ? event.getMessage().getContentRaw().substring(
						prefix.length()) : event.getMessage().getContentRaw());
		
		if (userFetchString.length() > 1) {
			Guild guild = event.getGuild();
			
			List<Member> memberList = guild.getMembers();
			
			for (Member member : memberList) {
				String name          = member.getUser().getName().toLowerCase();
				String effectiveName = member.getEffectiveName().toLowerCase();
				if ((effectiveName.contains(userFetchString.toLowerCase())) ||
					name.contains(userFetchString.toLowerCase())) {
					return member;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	public static User getUserFromMessage(MessageReceivedEvent event, String prefix) {
		if (event.getMessage().getMentionedMembers().size() > 0) {
			return event.getMessage().getMentionedMembers().get(0).getUser();
		}
		String userFetchString =
				(event.getMessage().getContentRaw().startsWith(prefix) ? event.getMessage().getContentRaw().substring(
						prefix.length()) : event.getMessage().getContentRaw());
		
		if (userFetchString.length() > 0) {
			Guild guild = event.getGuild();
			
			List<Member> memberList = guild.getMembers();
			
			for (Member member : memberList) {
				String name          = member.getUser().getName().toLowerCase();
				String effectiveName = member.getEffectiveName().toLowerCase();
				if ((effectiveName.contains(userFetchString.toLowerCase())) ||
					name.contains(userFetchString.toLowerCase())) {
					return member.getUser();
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	public static User getBannedUserFromMessage(MessageReceivedEvent event, String prefix) {
		String userFetchString =
				(event.getMessage().getContentRaw().startsWith(prefix) ? event.getMessage().getContentRaw().substring(
						prefix.length()) : event.getMessage().getContentRaw());
		
		if (userFetchString.length() > 1) {
			Guild guild = event.getGuild();
			
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
					if (name.contains(userFetchString.toLowerCase())) {
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
	
	public static Role getRoleFromMessage(MessageReceivedEvent event, String prefix) {
		if (event.getMessage().getMentionedRoles().size() > 0) {
			return event.getMessage().getMentionedRoles().get(0);
		}
		
		String roleFetchString =
				(event.getMessage().getContentRaw().startsWith(prefix) ? event.getMessage().getContentRaw().substring(
						prefix.length()) : event.getMessage().getContentRaw());
		
		if (roleFetchString.length() > 0) {
			Guild guild = event.getGuild();
			
			List<Role> roleList = guild.getRoles();
			
			for (Role role : roleList) {
				String name = role.getName().toLowerCase();
				if (name.contains(roleFetchString.toLowerCase())) {
					return role;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	public static Member getMemberFromString(String message, Guild guild) {
		// <@\d{18}>
		
		Pattern checkMention = Pattern.compile("<@\\d{18}>");
		
		if (checkMention.matcher(message.trim()).matches()) {
			Member member = null;
			try {
				member = guild.getMemberById(message.replace("<@", "").replace(">", ""));
			} catch (NumberFormatException ignored) {
			}
			if (member != null) {
				return member;
			}
		}
		
		if (message.length() > 1) {
			List<Member> memberList = guild.getMembers();
			
			for (Member member : memberList) {
				String name          = member.getUser().getName().toLowerCase();
				String effectiveName = member.getEffectiveName().toLowerCase();
				if ((effectiveName.contains(message.toLowerCase())) || name.contains(message.toLowerCase())) {
					return member;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
	public static User getUserFromString(String message, Guild guild) {
		
		Pattern checkMention = Pattern.compile("<@\\d{18}>");
		
		if (checkMention.matcher(message.trim()).matches()) {
			User user = null;
			try {
				user = Objects.requireNonNull(
						guild.getMemberById(message.replace("<@", "").replace(">", ""))).getUser();
			} catch (NumberFormatException | NullPointerException ignored) {
			}
			if (user != null) {
				return user;
			}
		}
		
		if (message.length() > 0) {
			List<Member> memberList = guild.getMembers();
			
			for (Member member : memberList) {
				String name          = member.getUser().getName().toLowerCase();
				String effectiveName = member.getEffectiveName().toLowerCase();
				if ((effectiveName.contains(message.toLowerCase())) || name.contains(message.toLowerCase())) {
					return member.getUser();
				}
			}
			return null;
		} else {
			return null;
		}
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
	
	public static Role getRoleFromString(String message, Guild guild) {
		
		//<@&\d{18}>
		
		Pattern checkMention = Pattern.compile("<@&\\d{18}>");
		
		if (checkMention.matcher(message.trim()).matches()) {
			Role role = null;
			try {
				role = guild.getRoleById(message.replace("<@&", "").replace(">", ""));
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
			return null;
		} else {
			return null;
		}
	}
}
