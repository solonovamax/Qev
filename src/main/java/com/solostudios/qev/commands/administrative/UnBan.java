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

package com.solostudios.qev.commands.administrative;

import com.solostudios.qev.core.command.handler.AbstractCommand;
import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UnBan extends AbstractCommand {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Permission BAN_MEMBERS = Permission.BAN_MEMBERS;
	
	public UnBan() {
		super("unban");
		this.setCategory("Moderation");
		this.setDescription("Unbans a user.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "user")
											   .put("type", "BannedUser")
											   .put("error", "Invalid user!")));
		this.setUsage("unban {user}");
		this.setClientPermissions(Permission.BAN_MEMBERS);
		this.setUserPermissions(Permission.BAN_MEMBERS);
	}
	
	@Override
	public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		event.getChannel().sendMessage("Unbanning user " + ((User) args.get("user")).getName()).queue();
		event.getGuild().unban((User) args.get("user")).queue();
	}
}