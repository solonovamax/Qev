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

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.update.GenericTextChannelUpdateEvent;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.update.GenericVoiceChannelUpdateEvent;
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


@SuppressWarnings("WeakerAccess")
public class EventLogger {
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
	
	}
	
	private static void textChannelEvent(GenericTextChannelEvent event) {
	
	}
	
	private static void voiceChannelUpdateEvent(GenericVoiceChannelUpdateEvent event) {
	
	}
	
	private static void voiceChannelEvent(GenericVoiceChannelEvent event) {
	
	}
	
	private static void categoryUpdateEvent(GenericCategoryUpdateEvent event) {
	
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
	
	private static void loggingEvent(String event, String user, String action) {
	
	}
	
}
