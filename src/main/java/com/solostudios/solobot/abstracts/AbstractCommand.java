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

package com.solostudios.solobot.abstracts;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class AbstractCommand {

    private final String name;
    private final String[] aliases;
    private final String usage;
    private final String description;
    private final String category;
    private final boolean enabled;

    public AbstractCommand(String name, String category, String description, String usage, boolean enabled, String... aliases) {
        this.name = name;
        this.aliases = aliases;
        this.category = category;
        this.description = description;
        this.usage = usage;
        this.enabled = enabled;
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final String getName() {
        return name;
    }

    public final String[] getAliases() {
        return aliases;
    }

    public final String getCategory() {
        if (category.equals("")) {
            return "No Category";
        } else {
            return category;
        }
    }

    public final String getUsage() {
        return usage;
    }

    public final String getDescription() {
        return description;
    }

    public Permission getPermissions() {
        return Permission.MESSAGE_WRITE;
    }


    public abstract void run(MessageReceivedEvent messageReceivedEvent, Message message, String[] args) throws IllegalArgumentException;

}
