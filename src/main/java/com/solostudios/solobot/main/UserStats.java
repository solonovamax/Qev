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

package com.solostudios.solobot.main;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import org.bson.Document;
import org.json.JSONObject;

import static com.solostudios.solobot.framework.utility.JSONtoMongoDB.toDocument;
import static com.solostudios.solobot.framework.utility.MongoDBtoJSON.toJSONObject;

public class UserStats {
    private User user;
    private JSONObject userData;


    UserStats(User user, Guild guild) {
        this.user = user;
        userData = getUserData(StatsHandler.getGuild(guild));
    }

    public JSONObject getUserData(JSONObject gData) {
        JSONObject uData;

        if (!gData.has(user.getId())) {
            gData = putNewUser(gData);
            uData = gData.getJSONObject(user.getId());
        } else {
            uData = toJSONObject((Document) gData.get(user.getId()));
        }


        if (!uData.getString("version").equals(StatsHandler.userDataVersion)) {
            uData = updateData(uData);
        }
        return uData;
    }

    private JSONObject putNewUser(JSONObject gData) {
        gData.put(user.getId(), toDocument(new JSONObject(StatsHandler.newUserData, JSONObject.getNames(StatsHandler.newUserData))
                .put("userIDString", user.getId())
                .put("userIDLong", user.getIdLong()))
        );

        return gData;
    }

    public JSONObject getUserData() {
        return userData;
    }

    public int getXP() {
        return userData.getInt("xp");
    }

    public void addXP(int xp) {
        userData.put("xp", getXP() + xp);
    }

    public long getLastMessageTime() {
        return userData.getLong("lastMessageTime");
    }

    public void updateUserData(JSONObject uData) {
        if (uData.getString("version").equals("1.0.0")) {
            uData
                    .put("isSoftBanned", false)
                    .put("softBanTime", 0L)
                    .put("softBanLength", 0L);
        }
        userData = uData;
    }

    private JSONObject updateData(JSONObject uData) {
        return uData;
    }

    public void updateTime() {
        userData.put("lastMessageTime", System.currentTimeMillis());
    }
}
