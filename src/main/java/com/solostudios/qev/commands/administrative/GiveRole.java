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

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GiveRole extends AbstractCommand {
	private final Logger     logger       = LoggerFactory.getLogger(this.getClass());
	private       Permission MANAGE_ROLES = Permission.MANAGE_ROLES;
	
	public GiveRole() {
		super("giverole");
		this.setAliases("grole");
		this.setCategory("Utility");
		this.setDescription("Gives a role to a user");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "role")
											   .put("type", Role.class)
											   .put("error", "Invalid role!"))
								  .put(new JSONObject()
											   .put("key", "user")
											   .put("type", Member.class)
											   .put("error", "Invalid user!")));
		this.setClientPermissions(Permission.MANAGE_ROLES);
		this.setUserPermissions(Permission.MANAGE_ROLES);
	}
	
	@Override
	public void run(MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		
		Member userToGiveRole = ((Member) args.get("user"));
		Role   roleToGive     = ((Role) args.get("role"));
		
		
		String role = roleToGive.getName();
		String user = userToGiveRole.getEffectiveName();
		try {
			event.getGuild().addRoleToMember(userToGiveRole, roleToGive).queue(
					(ignored) -> event.getChannel().sendMessage(
							"Successfully given role " + role + " to " + user).queue(),
					(ex) -> event.getChannel().sendMessage(
							"Failed to give role " + role + " to " + user + " for reason:\n" +
							ex.getMessage()).queue());
		} catch (HierarchyException e) {
			event.getChannel().sendMessage("Could not give role to user " + user + ", as the role " + role +
										   " is higher than my highest one.").queue();
		}
	}
}