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

package com.solostudios.qev.core.command.handler.old;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONArray;


public class CommandInfo {
	private final String       name;
	private final String       category;
	private final String[]     aliases;
	private final JSONArray    arguments;
	private final String       usage;
	private final String       description;
	private final Permission[] userPermissions;
	private final Permission[] clientPermissions;
	private final boolean      enabled;
	private       boolean      ownerOnly;
	
	CommandInfo(String name, String category, JSONArray arguments, String usage, String description,
				Permission[] userPermissions, Permission[] clientPermissions, boolean ownerOnly, boolean enabled,
				String... aliases) {
		this.name = name;
		this.category = category;
		this.aliases = aliases;
		this.arguments = arguments;
		this.usage = usage;
		this.description = description;
		this.userPermissions = userPermissions;
		this.clientPermissions = clientPermissions;
		this.ownerOnly = ownerOnly;
		this.enabled = enabled;
	}
	
	
	public boolean fitsArguments(Message message) {
		return fitsArguments(message.getContentRaw());
	}
	
	private boolean fitsArguments(String message) {
		return false;
	}
	
	public JSONArray getArguments() {
		return arguments;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String[] getAliases() {
		return aliases;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getUsage() {
		return usage;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Permission[] getUserPermissions() {
		return userPermissions;
	}
	
	public Permission[] getClientPermissions() {
		return clientPermissions;
	}
	
	public boolean getOwnerOnly() {
		return ownerOnly;
	}
}