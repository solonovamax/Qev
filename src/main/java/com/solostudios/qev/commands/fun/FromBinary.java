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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FromBinary extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public FromBinary() {
		super("frombin");
		this.setAliases("unBinaryify", "fromBinary");
		this.setCategory("Fun");
		this.setDescription("Takes a binary string and transforms it into the original string.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "unBin")
											   .put("type", String.class)
											   .put("error", "Not a valid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		try {
			String[] hex = args.getString("unBin").split(" ");
			
			StringBuilder str = new StringBuilder();
			for (String s : hex) {
				int decimal = Integer.parseInt(s, 2);
				str.append((char) decimal);
			}
			
			event.getChannel().sendMessage("Original String: " + str.toString().replace("@everyone", "@ everyone")).queue();
		} catch (NumberFormatException e) {
			throw new IllegalInputException("This is not a valid binary string!");
		}
	}
}