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
import com.solostudios.solobot.framework.commands.ArgumentContainer;
import com.solostudios.solobot.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;


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
	
	@SuppressWarnings("IntegerDivisionInFloatingPointContext")
	@Override
	public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		//Record start time
		long start = System.nanoTime();
		
		//Send event to ping API.
		event.getChannel().sendTyping().complete();
		
		//Get ping time
		long time = System.nanoTime() - start;
		
		//Send ping message
		event.getChannel().sendMessage(new EmbedBuilder().setAuthor("Ping time to discord API: " + ((float) Math.round(time / 100000)) / 10 + " milliseconds.")
														 .setColor(Color.GREEN)
														 .build()).queue();
	}
}
