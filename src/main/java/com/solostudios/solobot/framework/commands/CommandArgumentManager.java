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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;

import java.util.HashMap;

public class CommandArgumentManager {
    HashMap<String, Object> args = new HashMap<>();
    JDA jda;

    CommandArgumentManager(JDA jda, String[] keyList, Object[] argList) {
        this.jda = jda;

        assert keyList.length == argList.length;

        for (int i = 0; i < keyList.length; i++) {
            args.put(keyList[i], argList[i]);
        }
    }

    public Object get(String key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        Object object = args.get(key);
        if (object == null) {
            throw new NullPointerException("Object[" + key + "] not found.");
        }
        return object;
    }

    public boolean getBoolean(String key) throws NullPointerException {
        Object object = this.get(key);
        if (object.equals(Boolean.FALSE)
                || (object instanceof String && ((String) object)
                .equalsIgnoreCase("false"))) {
            return false;
        } else if (object.equals(Boolean.TRUE)
                || (object instanceof String && ((String) object)
                .equalsIgnoreCase("true"))) {
            return true;
        }
        throw new NullPointerException("Object[" + key
                + "] is not a Boolean.");
    }

    public double getDouble(String key) throws NullPointerException {
        Object object = this.get(key);
        try {
            return object instanceof Number ? ((Number) object).doubleValue()
                    : Double.parseDouble(object.toString());
        } catch (Exception e) {
            throw new NullPointerException("Object[" + key
                    + "] is not a double.");
        }
    }

    public int getInt(String key) throws NullPointerException {
        Object object = this.get(key);
        try {
            return object instanceof Number ? ((Number) object).intValue()
                    : Integer.parseInt((String) object);
        } catch (Exception e) {
            throw new NullPointerException("Object[" + key
                    + "] is not an int.");
        }
    }

    public User getUser(String key) throws NullPointerException {
        Object object = this.get(key);
        try {
            return (object instanceof User ? ((User) object)
                    : (object instanceof String ? jda.getUserById((String) object) : jda.getUserById((long) object)));
        } catch (Exception e) {
            throw new NullPointerException("Object[" + key
                    + "] is not user.");
        }
    }

    public Role getRole(String key) throws NullPointerException {
        Object object = this.get(key);
        try {
            return (object instanceof Role ? ((Role) object)
                    : (object instanceof String ? jda.getRoleById((String) object) : jda.getRoleById((long) object)));
        } catch (Exception e) {
            throw new NullPointerException("Object[" + key
                    + "] is not user.");
        }
    }

    public String getString(String key) throws NullPointerException {
        Object object = this.get(key);
        if (object instanceof String) {
            return (String) object;
        }
        throw new JSONException("Object[" + key + "] not a string.");
    }

    public boolean has(String key) {
        return args.containsKey(key);
    }

}
