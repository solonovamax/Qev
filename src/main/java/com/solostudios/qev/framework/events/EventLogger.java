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

package com.solostudios.qev.framework.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.*;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.update.*;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.update.GenericRoleUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;


@SuppressWarnings("WeakerAccess")
public class EventLogger {
	static Logger logger = LoggerFactory.getLogger(EventLogger.class);
	
	public static void onGenericEvent(GenericEvent event) {
		if (event instanceof TextChannelUpdatePermissionsEvent) {
			logger.info("TextChannelUpdatePermissionsEvent");
		}
		if (event instanceof GenericTextChannelEvent) {
			if (event instanceof GenericTextChannelUpdateEvent) {
				textChannelUpdateEvent((GenericTextChannelUpdateEvent) event);
			} else {
				textChannelEvent((GenericTextChannelEvent) event);
			}
			return;
		}
		
		if (event instanceof GenericVoiceChannelEvent) {
			if (event instanceof GenericVoiceChannelUpdateEvent) {
				voiceChannelUpdateEvent((GenericVoiceChannelUpdateEvent) event);
			} else {
				voiceChannelEvent((GenericVoiceChannelEvent) event);
			}
			return;
		}
		
		if (event instanceof GenericCategoryEvent) {
			if (event instanceof GenericCategoryUpdateEvent) {
				categoryUpdateEvent((GenericCategoryUpdateEvent) event);
			} else {
				categoryEvent((GenericCategoryEvent) event);
			}
			return;
		}
		
		if (event instanceof GenericEmoteEvent) {
			if (event instanceof GenericEmoteUpdateEvent) {
				emoteUpdateEvent((GenericEmoteUpdateEvent) event);
			} else {
				emoteEvent((GenericEmoteEvent) event);
			}
			return;
		}
		
		if (event instanceof GenericGuildEvent) {
			if (event instanceof GenericGuildMemberEvent) {
				if (event instanceof GenericGuildMemberUpdateEvent) {
					guildMemberUpdateEvent((GenericGuildMemberUpdateEvent) event);
				} else {
					guildMemberEvent((GenericGuildMemberEvent) event);
				}
			} else {
				if (event instanceof GenericGuildUpdateEvent) {
					genericGuildUpdateEvent((GenericGuildUpdateEvent) event);
				} else {
					if (event instanceof GenericGuildVoiceEvent) {
						genericGuildVoiceEvent((GenericGuildVoiceEvent) event);
					} else {
						if (event instanceof GenericGuildMessageEvent) {
							if (event instanceof GenericGuildMessageReactionEvent) {
								genericGuildMessageReactionEvent((GenericGuildMessageReactionEvent) event);
							} else {
								genericGuildMessageEvent((GenericGuildMessageEvent) event);
							}
						} else {
							genericGuildEvent((GenericGuildEvent) event);
						}
					}
				}
			}
			return;
		}
		
		if (event instanceof GenericRoleEvent) {
			if (event instanceof GenericRoleUpdateEvent) {
				genericRoleUpdateEvent((GenericRoleUpdateEvent) event);
			} else {
				genericRoleEvent((GenericRoleEvent) event);
			}
			return;
		}
		
		
	}
	
	private static void textChannelUpdateEvent(GenericTextChannelUpdateEvent event) {
		String update = null;
		Color  color  = null;
		
		if (event instanceof TextChannelUpdateNameEvent) {
			update = "Name updated from " + event.getOldValue() + " to " + event.getNewValue() + ".";
			color = Color.YELLOW;
		}
		if (event instanceof TextChannelUpdateNSFWEvent) {
			update = "NSFW updated from " + event.getOldValue() + " to " + event.getNewValue() + ".";
			color = Color.PINK;
		}
		if (event instanceof TextChannelUpdateParentEvent) {
			Category oldC = ((TextChannelUpdateParentEvent) event).getOldParent();
			Category newC = ((TextChannelUpdateParentEvent) event).getOldParent();
			update = "Parent updated from " + (oldC != null ? oldC.getName() : "no category") + " to " +
					 (newC != null ? newC.getName() : "no category") + ".";
			color = Color.GREEN;
		}
		if (event instanceof TextChannelUpdateSlowmodeEvent) {
			update = "Slowmode updated from " + event.getOldValue() + "s to " + event.getNewValue() + "s.";
			color = Color.LIGHT_GRAY;
		}
		if (event instanceof TextChannelUpdateTopicEvent) {
			update = "Topic updated from " + event.getOldValue() + " to " + event.getNewValue() + ".";
			color = Color.CYAN;
		}
		if (update == null) {
			return;
		}
		channelUpdateEvent(event.getGuild(), "Text Channel Updated", event.getEntity().getName(), update, color);
	}
	
	private static void channelUpdateEvent(Guild guild, String eventName, String channelName, String update,
										   Color color) {
		loggingEvent(guild, new EmbedBuilder()
				.setTitle(eventName)
				.addField("Channel Name", channelName, true)
				.addField("Change", update, true)
				.setColor(color)
				.build());
	}
	
	private static void loggingEvent(Guild guild, MessageEmbed embed) {
		guild.getSystemChannel().sendMessage(embed).queue();
	}
	
	private static void voiceChannelUpdateEvent(GenericVoiceChannelUpdateEvent event) {
		String update = null;
		Color  color  = null;
		
		if (event instanceof VoiceChannelUpdateNameEvent) {
			update = "Name updated from " + event.getOldValue() + " to " + event.getNewValue() + ".";
			color = Color.YELLOW;
		}
		if (event instanceof VoiceChannelUpdateBitrateEvent) {
			update = "Bitrate updated from " + event.getOldValue() + "bps to " + event.getNewValue() + "bps.";
			color = Color.CYAN;
		}
		if (event instanceof VoiceChannelUpdateParentEvent) {
			Category oldC = ((VoiceChannelUpdateParentEvent) event).getOldParent();
			Category newC = ((VoiceChannelUpdateParentEvent) event).getOldParent();
			update = "Updated parent from " + (oldC != null ? oldC.getName() : "no category") + " to " +
					 (newC != null ? newC.getName() : "no category") + ".";
			color = Color.GREEN;
		}
		if (event instanceof VoiceChannelUpdateUserLimitEvent) {
			update = "User limit updated from " + event.getOldValue() + " users to " + event.getNewValue() + " users.";
			color = Color.LIGHT_GRAY;
		}
		if (update == null) {
			return;
		}
		channelUpdateEvent(event.getGuild(), "Voice Channel Updated", event.getEntity().getName(), update, color);
	}
	
	private static void categoryUpdateEvent(GenericCategoryUpdateEvent event) {
		String update = null;
		Color  color  = null;
		if (event instanceof CategoryUpdateNameEvent) {
			update = "Name updated from " + event.getOldValue() + " to " + event.getNewValue() + ".";
			color = Color.YELLOW;
		}
		if (update == null) {
			return;
		}
		channelUpdateEvent(event.getGuild(), "Category Updated", event.getEntity().getName(), update, color);
	}
	
	private static void textChannelEvent(GenericTextChannelEvent event) {
		
		String update    = null;
		String eventName = null;
		Color  color     = null;
		
		if (event instanceof TextChannelCreateEvent) {
			update = "Text channel " + event.getChannel().getName() + " created.";
			eventName = "Channel Created.";
			color = Color.GREEN;
		}
		if (event instanceof TextChannelDeleteEvent) {
			update = "Text channel " + event.getChannel().getName() + " deleted.";
			eventName = "Channel Deleted.";
			color = Color.RED;
		}
		if (event instanceof TextChannelUpdatePermissionsEvent) {
			List<Role> changedRoleList = ((TextChannelUpdatePermissionsEvent) event).getChangedRoles();
			
			EmbedBuilder embed = new EmbedBuilder()
					.setTitle("List of roles changed.")
					.setColor(Color.BLUE);
			
			changedRoleList.forEach((role) -> {
				embed.addField(role.getName(), "", true);
			});
			
			embed.setAuthor("test");
			loggingEvent(event.getGuild(), embed.build());
		}
		if (update == null) {
			return;
		}
		channelUpdateEvent(event.getGuild(), "Text Channel Updated", event.getChannel().getName(), update, color);
	}
	
	private static void categoryEvent(GenericCategoryEvent event) {
	
	}
	
	private static void emoteUpdateEvent(GenericEmoteUpdateEvent event) {
	
	}
	
	private static void emoteEvent(GenericEmoteEvent event) {
	
	}
	
	private static void guildMemberUpdateEvent(GenericGuildMemberUpdateEvent event) {
	
	}
	
	private static void guildMemberEvent(GenericGuildMemberEvent event) {
	
	}
	
	private static void genericGuildUpdateEvent(GenericGuildUpdateEvent event) {
	
	}
	
	private static void genericGuildVoiceEvent(GenericGuildVoiceEvent event) {
	
	}
	
	private static void genericGuildMessageReactionEvent(GenericGuildMessageReactionEvent event) {
	
	}
	
	private static void genericGuildMessageEvent(GenericGuildMessageEvent event) {
	
	}
	
	private static void genericGuildEvent(GenericGuildEvent event) {
	
	}
	
	private static void genericRoleUpdateEvent(GenericRoleUpdateEvent event) {
	
	}
	
	private static void genericRoleEvent(GenericRoleEvent event) {
	
	}
	
	private static void voiceChannelEvent(GenericVoiceChannelEvent event) {
	
	}
	
}
