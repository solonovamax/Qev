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

package com.solostudios.qev.commands.fun;

import com.solostudios.qev.framework.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.framework.command.handler.old.ArgumentContainer;
import com.solostudios.qev.framework.old.exceptions.IllegalInputException;
import com.solostudios.qev.framework.old.utility.GenericUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;


public class ToMorse extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public ToMorse() {
		super("toMorse");
		this.setCategory("Fun");
		this.setDescription("Takes a string and turns it into morse code.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "toMorse")
											   .put("type", String.class)
											   .put("error", "Invalid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		String toMorse = args.getString("toMorse");
		
		StringJoiner sj = new StringJoiner(" ");
		
		for (byte aByte : toMorse.getBytes()) {
			if (GenericUtil.alphabet.contains((char) aByte)) {
				sj.add(GenericUtil.morse.get(GenericUtil.alphabet.indexOf((char) aByte)));
			}
		}
		
		event.getMessage().getChannel().sendMessage(sj.toString()).queue();
	}
}