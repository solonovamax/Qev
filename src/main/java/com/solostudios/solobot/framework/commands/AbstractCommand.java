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

import com.solostudios.solobot.framework.commands.errors.ArgumentError;
import com.solostudios.solobot.framework.utility.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractCommand {

    private String name;
    private String[] aliases = new String[]{""};
    private String category = null;
    private JSONArray arguments = null;
    private String usage = null;
    private String description = null;
    private boolean enabled = true;
    private Permission[] userPermissions = new Permission[]{};
    private Permission[] clientPermissions = new Permission[]{Permission.MESSAGE_WRITE};
    private boolean ownerOnly;
    private String example = null;
    private boolean nsfw = false;
    private JSONObject defaultArgs = new JSONObject();


    protected AbstractCommand(String name) {
        this.name = name;
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
        return category;
    }

    public final String getUsage() {
        return usage;
    }

    public final String getDescription() {
        return description;
    }

    public Permission[] getUserPermissions() {
        return userPermissions;
    }

    public Permission[] getClientPermissions() {
        return clientPermissions;
    }

    public JSONArray getArguments() {
        return arguments;
    }

    public JSONObject getDefaultArgs() {
        return defaultArgs;
    }

    public String getExample() {
        return example;
    }

    public boolean isOwnerOnly() {
        return ownerOnly;
    }

    public boolean isNsfw() {
        return nsfw;
    }


    public abstract void run(MessageReceivedEvent event, JSONObject arguments) throws IllegalArgumentException;

    /* layout of the JSONArray
    [{
        "key": String,
        "type": Clazz, <- Must be integer, double, boolean, role, member or string
        "optional": boolean,
        "default": Object <- Must be of class type
        "error": "Invalid input" <- What it says when there is an error
        "prompt": "Please input an argument" <- What is says when the user need to input something.
     },
     {
        "key": String,
        "type": Clazz, <- Must be integer, double, boolean, role, member or string
        "optional": boolean,
        "default": Object <- Must be of class type
        "error": "Invalid input" <- What it says when there is an error
        "prompt": "Please input an argument" <- What is says when the user need to input something.
     }]
     */


    public static JSONObject parseArgs(MessageReceivedEvent event, AbstractCommand command) throws ArgumentError {
        JSONObject temp = new JSONObject();

        if (command.getArguments().length() == 0) {
            return temp;
        }

        JSONArray argumentsList = command.getArguments();

        for (int i = 0; i < argumentsList.length(); i++) {
            try {
                JSONObject object = argumentsList.getJSONObject(i);

                Object tempClazz = object.get("key");

                if (tempClazz instanceof Class) {
                    Class clazz = (Class) tempClazz;
                }

            } catch (JSONException ignored) {
            }
        }

        return temp;
    }

    protected void withEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void withAliases(String... aliases) {
        this.aliases = aliases;
    }

    protected void withCategory(String category) {
        this.category = category;
    }

    /**
     * @param usage
     * @return instance of calling object.
     */
    protected void withUsage(String usage) {
        this.usage = usage;
    }

    protected void withArguments(JSONArray arguments) {
        if (verifyArguments(arguments)) {
            this.arguments = arguments;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @param description
     * @return instance of calling object
     */
    protected void withDescription(String description) {
        this.description = description;
    }


    protected void withExample(String example) {
        this.example = example;
    }

    protected void withNSFW(boolean nsfw) {
        this.nsfw = nsfw;
    }


    /**
     * @param userPermissions
     * @return instance of calling object
     */
    protected void withUserPermissions(Permission... userPermissions) {
        this.userPermissions = userPermissions;
    }

    /**
     * @param clientPermissions
     * @return instance of calling object
     */
    protected void withClientPermissions(Permission... clientPermissions) {
        Permission[] tempPermissions = new Permission[clientPermissions.length + 1];
        tempPermissions[0] = Permission.MESSAGE_WRITE;
        System.arraycopy(clientPermissions, 0, tempPermissions, 1, clientPermissions.length);
        this.clientPermissions = tempPermissions;
    }

    /**
     * @param ownerOnly
     * @return instance of calling object
     */
    protected void withOwnerOnly(boolean ownerOnly) {
        this.ownerOnly = ownerOnly;
    }


    private boolean verifyArguments(JSONArray args) {
        if (args.length() < 1) {
            return false;
        }


        for (int i = 0; i < args.length(); i++) {
            JSONObject arg;
            try {
                arg = args.getJSONObject(i);
            } catch (JSONException e) {
                return false;
            }
            if (!arg.has("key") || !arg.has("type")) {
                return false;
            }

            Object c = arg.get("type");
            if (!(c == String.class || c == int.class || c == double.class || c == Role.class || c == Member.class || c == boolean.class || c.equals("BannedUser"))) {
                return false;
            }

            if (arg.has("default")) {
                if (arg.get("default").getClass() != c) {
                    return false;
                }
                defaultArgs.put(arg.getString("key"), arg.get("default"));
            }

            if (arg.has("optional")) {
                try {
                    arg.getBoolean("optional");
                } catch (JSONException e) {
                    return false;
                }
            }

            if (!arg.has("prompt")) {
                arg.put("prompt", "Please input the " + arg.getString("key"));
            }
        }
        return true;
    }


    public boolean fitsArguments(JSONObject args) {
        for (int i = 0; i < arguments.length(); i++) {
            JSONObject obj = arguments.getJSONObject(i);

            try {
                if (obj.has("optional") && obj.getBoolean("optional"))
                    continue;
            } catch (JSONException e) {
            }

            if (!args.has(obj.getString("key")))
                return false;

            if (args.get(obj.getString("key")).getClass() != obj.get("type"))
                return false;
        }

        return true;
    }

    public String nextArgPrompt(JSONObject args) {
        for (int i = 0; i < arguments.length(); i++) {
            JSONObject obj = arguments.getJSONObject(i);

            if (args.has(obj.getString("key")) || (obj.has("optional") && obj.getBoolean("optional"))) {
                continue;
            }

            return obj.getString("prompt");
        }

        return "error";
    }

    public void putNextArg(MessageReceivedEvent event, JSONObject args) throws IllegalArgumentException {
        for (int i = 0; i < arguments.length(); i++) {
            JSONObject obj = arguments.getJSONObject(i);

            if (args.has(obj.getString("key"))) {
                continue;
            }
            String key = obj.getString("key");

            if (event.getMessage().getContentRaw().equals("null") && obj.has("default")) {
                args.put(key, obj.get("default"));
                return;
            }

            Class clazz = (Class) obj.get("type");


            try {
                if (clazz == Role.class) {
                    if (MessageUtils.getRoleFromMessage(event, "") == null)
                        throw new IllegalArgumentException();
                    args.put(key, MessageUtils.getRoleFromMessage(event, ""));
                    return;
                }
                if (clazz == Member.class) {
                    if (MessageUtils.getMemberFromMessage(event, "") == null)
                        throw new IllegalArgumentException();
                    args.put(key, MessageUtils.getMemberFromMessage(event, ""));
                    return;
                }
                if (clazz == int.class) {
                    args.put(key, Integer.parseInt(event.getMessage().getContentRaw()));
                    return;
                }
                if (clazz == double.class) {
                    args.put(key, Double.parseDouble(event.getMessage().getContentRaw()));
                    return;
                }
                if (clazz == boolean.class) {
                    args.put(key, parseBoolean(event.getMessage().getContentRaw()));
                    return;
                }
            } catch (IllegalArgumentException e) {
            }
            throw new IllegalArgumentException((obj.getString("error") == null ? "Invalid input" : obj.getString("error")));
        }
        throw new IllegalArgumentException("No more arguments");
    }

    private boolean parseBoolean(String s) {
        if (s.equals("t") || s.equals("true") || s.equals("yes"))
            return true;
        if (s.equals("f") || s.equals("false") || s.equals("no"))
            return false;
        throw new IllegalArgumentException();
    }

}
