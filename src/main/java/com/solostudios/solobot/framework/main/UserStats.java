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

package com.solostudios.solobot.framework.main;

import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.Exchanger;

public class UserStats {
    private Long user;
    private Long guild;
    private Document userData;


    public UserStats(Document guildData, User user) {
        this.user = user.getIdLong();
        this.guild = guildData.getLong("guild");
        userData = pullUserData(guildData);
    }

    public UserStats(Document guildData, Long userID) {
        this.user = userID;
        this.guild = guildData.getLong("guild");
        userData = pullUserData(guildData);
    }

    private Document pullUserData(@NotNull Document gData) {
        Document uData;

        if (gData.containsKey(user)) {
            uData = (Document) gData.get(user);
        } else {
            uData = MongoDBInterface.newUserData
                    .append("userIDString", user.toString())
                    .append("userIDLong", user);
        }


        if (!uData.getString("version").equals(MongoDBInterface.userDataVersion)) {
            uData = updateData(uData);
        }
        return uData;
    }


    private Document updateData(@NotNull Document uData) {
        Document nUData = MongoDBInterface.newUserData;

        if (uData.getString("version").equals("1.0.0")
                || uData.getString("version").equals("1.0.1")
                || uData.getString("version").equals("1.0.2")
                || uData.getString("version").equals("1.1.0")) {
            for (String i : uData.keySet()) {
                if (Objects.equals(i, "version"))
                    continue;
                nUData.put(i, uData.get(i));
            }
        } else {
            throw new UnknownFormatConversionException("");
        }

        uData.put("levelXp", uData.getInteger("xp"));

        uData.remove("firstMessage");

        return nUData;
    }

    public void updateTime() {
        userData.put("lastMessageTime", System.currentTimeMillis());
    }

    public Document getUserData() {
        return userData;
    }

    public Long getUser() {
        return user;
    }

    public int getXP() {
        return userData.getInteger("xp");
    }

    public int getLevelXP() {
        return userData.getInteger("levelXp");
    }

    public int getLevel() {
        return userData.getInteger("level");
    }

    public void addXP(int xp) {
        userData.put("xp", getXP() + xp);
        userData.put("levelXp", getLevelXP() + xp);
        updateXp();
    }

    public long getLastMessageTime() {
        return userData.getLong("lastMessageTime");
    }

    public boolean isSoftBanned() {
        return userData.getBoolean("isSoftBanned");
    }

    public void setFirstMessage(String firstMessage) {
        userData.put("firstMessage", firstMessage);
    }


    public long getRemainingSoftBan() {
        return userData.getLong("softBanLength") - System.currentTimeMillis() - userData.getLong("softBanTime");
    }

    private void updateXp() {
        Exchanger waiter = new Exchanger();
        MongoDBInterface.set((collection, guildID, userID, ex) -> {
            Document uData = collection.find(new Document("userIDLong", userID)).first();
            int lvl = uData.getInteger("level");

            while (uData.getInteger("levelXp") > (5 * (lvl ^ 2) + 50 * lvl + 100)) {
                lvl = uData.getInteger("level");

                userData.put("levelXp", (uData.getInteger("levelXp") - (5 * (lvl ^ 2) + 50 * lvl + 100)));
                userData.put("level", (lvl + 1));
            }
            try {
                ex.exchange(null);
            } catch (InterruptedException ignored) {
            }
        }, guild, user, waiter);
        try {
            waiter.exchange(null);
        } catch (InterruptedException ignored) {
        }
        LogHandler.debug("pppppppp");
    }
}
