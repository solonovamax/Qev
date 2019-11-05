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
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public final class AbstractCommandBuilder<T extends AbstractCommand> {
    private Class<T> commandReference;
    private String name;
    private String[] aliases;
    private String category;
    private JSONArray arguments;
    private String usage;
    private String description;
    private boolean enabled;
    private Permission[] userPermissions;
    private Permission[] clientPermissions;
    private boolean ownerOnly;

    private AbstractCommandBuilder(@NotNull Class<T> command, @NotNull String name, boolean enabled) {
        Class<AbstractCommand> commandClass = AbstractCommand.class;
        this.name = name;
        commandReference = command;
        this.enabled = enabled;
    }

    public static <V extends AbstractCommand> AbstractCommandBuilder anAbstractCommand(@NotNull Class<V> command, @NotNull String name, boolean enabled) {
        return new AbstractCommandBuilder<>(command, name, enabled);
    }

    public AbstractCommandBuilder withAliases(String... aliases) {
        this.aliases = aliases;
        return this;
    }

    public AbstractCommandBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    /**
     * @param usage
     * @return instance of calling object.
     */
    public AbstractCommandBuilder withUsage(String usage) {
        this.usage = usage;
        return this;
    }

    public AbstractCommandBuilder withArguments(JSONArray arguments) {
        if (verifyArguments(arguments)) {
            this.arguments = arguments;
            return this;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param description
     * @return instance of calling object
     */
    public AbstractCommandBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param userPermissions
     * @return instance of calling object
     */
    public AbstractCommandBuilder withUserPermissions(Permission[] userPermissions) {
        this.userPermissions = userPermissions;
        return this;
    }

    /**
     * @param clientPermissions
     * @return instance of calling object
     */
    public AbstractCommandBuilder withClientPermissions(Permission[] clientPermissions) {
        this.clientPermissions = clientPermissions;
        return this;
    }

    /**
     * @param ownerOnly
     * @return instance of calling object
     */
    public AbstractCommandBuilder withOwnerOnly(boolean ownerOnly) {
        this.ownerOnly = ownerOnly;
        return this;
    }

    /**
     * @return an abstract command built using the builder.
     */
    public AbstractCommand build() {
        try {
            CommandInfo info = new CommandInfo(name, category, arguments, usage, description, userPermissions, clientPermissions, ownerOnly, enabled, aliases);
            return commandReference.getConstructor(CommandInfo.class).newInstance(info);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
            return null;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public boolean verifyArguments(JSONArray args) {
        if (args.length() > 1) {
            return false;
        }


        for (int i = 0; i < args.length(); i++) {
            JSONObject arg;
            try {
                arg = args.getJSONObject(i);
            } catch (JSONException e) {
                return false;
            }
            if (!arg.has("key") || !arg.has("type"))
                return false;

            switch (arg.getString("type").toLowerCase()) {
                case "integer":
                case "double":
                case "role":
                case "user":
                case "member":
                case "string":
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    /*
    private JSONArray parseArguments(String argumentsToBeParsed) {
        JSONArray tempArguments = new JSONArray();

        String[] args = argumentsToBeParsed.trim().split("\\S*\\s{(?:string|integer|user|member|role)(?:(?:\\s)+?(?:(?:@optional|@default.*?|@prompt.*?))*)}");

        return tempArguments;
    }
     */
}
