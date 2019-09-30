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

package com.solostudios.solobot.framework.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

public abstract class AbstractCommand {

    private CommandInfo info;


    private AbstractCommand(CommandInfo info) {
        this.info = info;
    }

    public abstract AbstractCommand getAbstractCommand();

    public final boolean isEnabled() {
        return info.isEnabled();
    }

    public final String getName() {
        return info.getName();
    }

    public final String[] getAliases() {
        return info.getAliases();
    }

    public final String getCategory() {
        return info.getCategory();
    }

    public final String getUsage() {
        return info.getUsage();
    }

    public final String getDescription() {
        return info.getDescription();
    }

    public final CommandInfo getCommandInfo() {
        return info;
    }

    public Permission[] getUserPermissions() {
        return info.getUserPermissions();
    }

    public Permission[] getClientPermissions() {
        return info.getClientPermissions();
    }


    public abstract void run(MessageReceivedEvent messageReceivedEvent, JSONObject arguments) throws IllegalArgumentException;

}
