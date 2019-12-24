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

import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;


public class SoftBan extends AbstractCommand {
	public SoftBan() {
		super("tempban");
		this.setAliases("softban", "temporaryban");
		this.setCategory("Moderation");
		this.setDescription("Temporarily bans a user.");
		this.setIsEnabled(false);
		this.setClientPermissions(Permission.KICK_MEMBERS);
		this.setUserPermissions(Permission.KICK_MEMBERS, Permission.BAN_MEMBERS);
        /*
        super("tempban",
                "Moderation",
                "Temporarily Bans a user for a selected amount of time.",
                "tempban {@user} {hours\\days\\weeks} {length}\n" +
                        "tempban {@user} {hours\\days\\weeks} {length} {reason}",
                false,
                "softban", "temporaryban");
         */
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
	
	}
}