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
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.*;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.update.*;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
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
import java.time.Instant;
import java.util.StringJoiner;


@SuppressWarnings("WeakerAccess")
public class EventLogger {
	static Logger logger       = LoggerFactory.getLogger(EventLogger.class);
	static Color  defaultColor = new Color(40, 150, 255);
	
	public static void onGenericEvent(GenericEvent event) {
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
		
		boolean eventCaptured = false;
		
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setFooter(event.getGuild().getName())
				.setTimestamp(Instant.now())
				.setColor(defaultColor)
				.setThumbnail(event.getGuild().getIconUrl());
		
		if (event instanceof TextChannelUpdateNameEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Name of text channel " + event.getChannel().getAsMention() + " changed.")
					.addField("Old Name", String.valueOf(event.getOldValue()), true)
					.addField("New Name", String.valueOf(event.getNewValue()), true);
		}
		if (event instanceof TextChannelUpdateNSFWEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("isNSFW of voice channel " + event.getChannel().getAsMention() + " changed.")
					.addField("Old isNSFW", String.valueOf(event.getOldValue()), true)
					.addField("New isNSFW", String.valueOf(event.getNewValue()), true);
		}
		if (event instanceof TextChannelUpdateParentEvent) {
			eventCaptured = true;
			Category oldC = ((TextChannelUpdateParentEvent) event).getOldParent();
			Category newC = ((TextChannelUpdateParentEvent) event).getNewParent();
			embedBuilder
					.setAuthor("Category of text channel " + event.getChannel().getAsMention() + " changed.")
					.addField("Old Category", (oldC != null ? oldC.getName() : "no category"), true)
					.addField("New Category", (newC != null ? newC.getName() : "no category"), true);
		}
		if (event instanceof TextChannelUpdateSlowmodeEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Max users of voice channel " + event.getChannel().getAsMention() + " changed.")
					.addField("Old Max Users", String.valueOf(event.getOldValue()), true)
					.addField("New Max Users", String.valueOf(event.getNewValue()), true);
		}
		if (event instanceof TextChannelUpdateTopicEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Max users of voice channel " + event.getChannel().getAsMention() + " changed.")
					.addField("Old Max Users", String.valueOf(event.getOldValue()), true)
					.addField("New Max Users", String.valueOf(event.getNewValue()), true);
		}
		
		if (eventCaptured) {
			loggingEvent(event.getGuild(), embedBuilder.build());
		}
	}
	
	private static void loggingEvent(Guild guild, MessageEmbed embed) {
		guild.getSystemChannel().sendMessage(embed).queue();
	}
	
	private static void textChannelEvent(GenericTextChannelEvent event) {
		boolean eventCaptured = false;
		
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setFooter(event.getGuild().getName())
				.setTimestamp(Instant.now())
				.setColor(defaultColor)
				.setThumbnail(event.getGuild().getIconUrl());
		
		if (event instanceof TextChannelCreateEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Text channel " + event.getChannel().getName() + " created.")
					.addField("Channel", event.getChannel().getAsMention(), true)
					.setColor(Color.GREEN);
		}
		if (event instanceof TextChannelDeleteEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Text channel " + event.getChannel().getName() + " deleted.")
					.addField("Channel", event.getChannel().getName(), true)
					.setColor(Color.RED);
		}
		if (event instanceof TextChannelUpdatePermissionsEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Permissions for role " +
							   ((TextChannelUpdatePermissionsEvent) event).getChangedRoles().get(0).getAsMention() +
							   " changed")
					.addField("Role", ((TextChannelUpdatePermissionsEvent) event).getChangedRoles().get(0).getName(),
							  true)
					.addField("Channel", event.getChannel().getAsMention(), true);
			
		}
		if (eventCaptured) {
			loggingEvent(event.getGuild(), embedBuilder.build());
		}
	}
	
	private static void voiceChannelUpdateEvent(GenericVoiceChannelUpdateEvent event) {
		boolean eventCaptured = false;
		
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setFooter(event.getGuild().getName())
				.setTimestamp(Instant.now())
				.setColor(defaultColor)
				.setThumbnail(event.getGuild().getIconUrl());
		
		if (event instanceof VoiceChannelUpdateNameEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Name of voice channel " + event.getChannel().getName() + " changed.")
					.addField("Old Name", String.valueOf(event.getOldValue()), true)
					.addField("New Name", String.valueOf(event.getNewValue()), true);
		}
		if (event instanceof VoiceChannelUpdateBitrateEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Bitrate of voice channel " + event.getChannel().getName() + " changed.")
					.addField("Old Bitrate", String.valueOf(event.getOldValue()), true)
					.addField("New Bitrate", String.valueOf(event.getNewValue()), true);
		}
		if (event instanceof VoiceChannelUpdateParentEvent) {
			eventCaptured = true;
			Category oldC = ((VoiceChannelUpdateParentEvent) event).getOldParent();
			Category newC = ((VoiceChannelUpdateParentEvent) event).getNewParent();
			embedBuilder
					.setAuthor("Category of voice channel " + event.getChannel().getName() + " changed.")
					.addField("Old Category", (oldC != null ? oldC.getName() : "no category"), true)
					.addField("New Category", (newC != null ? newC.getName() : "no category"), true);
		}
		if (event instanceof VoiceChannelUpdateUserLimitEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Max users of voice channel " + event.getChannel().getName() + " changed.")
					.addField("Old Max Users", String.valueOf(event.getOldValue()), true)
					.addField("New Max Users", String.valueOf(event.getNewValue()), true);
		}
		
		if (eventCaptured) {
			loggingEvent(event.getGuild(), embedBuilder.build());
		}
	}
	
	private static void voiceChannelEvent(GenericVoiceChannelEvent event) {
	
	}
	
	private static void categoryUpdateEvent(GenericCategoryUpdateEvent event) {
		boolean eventCaptured = false;
		
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setFooter(event.getGuild().getName())
				.setTimestamp(Instant.now())
				.setColor(defaultColor)
				.setThumbnail(event.getGuild().getIconUrl());
		
		if (event instanceof CategoryUpdateNameEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Name of category  " + event.getCategory().getName() + " changed.")
					.addField("Old Name", String.valueOf(event.getOldValue()), true)
					.addField("New Name", String.valueOf(event.getNewValue()), true);
		}
		
		if (eventCaptured) {
			loggingEvent(event.getGuild(), embedBuilder.build());
		}
	}
	
	private static void categoryEvent(GenericCategoryEvent event) {
		boolean eventCaptured = false;
		
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setFooter(event.getGuild().getName())
				.setTimestamp(Instant.now())
				.setColor(defaultColor)
				.setThumbnail(event.getGuild().getIconUrl());
		
		if (event instanceof CategoryCreateEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Text channel " + event.getCategory().getName() + " created.")
					.addField("Category", event.getCategory().getName(), true)
					.addField("Category ID", event.getCategory().getId(), true)
					.setColor(Color.GREEN);
		}
		if (event instanceof CategoryDeleteEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Text channel " + event.getCategory().getName() + " deleted.")
					.addField("Category", event.getCategory().getName(), true)
					.addField("Category ID", event.getCategory().getId(), true)
					.setColor(Color.RED);
		}
		if (event instanceof CategoryUpdatePermissionsEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Permissions for role " +
							   ((CategoryUpdatePermissionsEvent) event).getChangedRoles().get(0).getAsMention() +
							   " changed")
					.addField("Role", ((CategoryUpdatePermissionsEvent) event).getChangedRoles().get(0).getName(),
							  true)
					.addField("Category", event.getCategory().getName(), true)
					.addField("Category ID", event.getCategory().getId(), true);
			
		}
		if (eventCaptured) {
			loggingEvent(event.getGuild(), embedBuilder.build());
		}
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
	
	private static void emoteUpdateEvent(GenericEmoteUpdateEvent event) {
		boolean eventCaptured = false;
		
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setFooter(event.getGuild().getName())
				.setTimestamp(Instant.now())
				.setColor(defaultColor)
				.setThumbnail(event.getGuild().getIconUrl());
		
		if (event instanceof EmoteUpdateNameEvent) {
			eventCaptured = true;
			embedBuilder
					.setAuthor("Name of emote " + event.getEmote().getAsMention() + " changed.")
					.addField("Old Name", String.valueOf(event.getOldValue()), true)
					.addField("New Name", String.valueOf(event.getNewValue()), true);
		}
		if (event instanceof EmoteUpdateRolesEvent) {
			eventCaptured = true;
			StringJoiner oldRoles = new StringJoiner(",");
			StringJoiner newRoles = new StringJoiner(",");
			
			((EmoteUpdateRolesEvent) event).getOldRoles().forEach((r) -> oldRoles.add(r.getName()));
			((EmoteUpdateRolesEvent) event).getOldRoles().forEach((r) -> newRoles.add(r.getName()));
			
			
			embedBuilder
					.setAuthor("White listed roles for emote" + event.getEmote().getName() + " changed.")
					.addField("Old roles", oldRoles.toString(), true)
					.addField("New roles", newRoles.toString(), true);
		}
		
		if (eventCaptured) {
			loggingEvent(event.getGuild(), embedBuilder.build());
		}
	}
	
	private static void channelUpdateEvent(Guild guild, String eventName, String channelName, String update,
										   Color color) {
		loggingEvent(guild, new EmbedBuilder()
				.setAuthor(eventName)
				.addField("Channel Name", channelName, true)
				.addField("Change", update, true)
				.setColor(color)
				.build());
	}
	
}
