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

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONException;
import org.json.JSONObject;

public class ArgumentHandler extends JSONObject {
    public Member getMember(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof Member) {
            return (Member) object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] not a member.");
    }

    public User getBannedUser(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof User) {
            return (User) object;
        } else {
            if (object instanceof Member) {
                return ((Member) object).getUser();
            }
        }
        throw new JSONException("JSONObject[" + quote(key) + "] not a member.");
    }

    public Role getRole(String key) throws JSONException {
        Object object = this.get(key);
        if (object instanceof Role) {
            return (Role) object;
        }
        throw new JSONException("JSONObject[" + quote(key) + "] not a member.");
    }
}
