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

package com.solostudios.qev.core.main;

import com.mongodb.client.model.ReplaceOptions;
import com.solostudios.qev.core.database.MongoDBInterface;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Exchanger;


public class UserStats {
	private final Logger   logger = LoggerFactory.getLogger(this.getClass());
	private       Long     user;
	private       Long     guild;
	private       Document userData;
	
	
	public UserStats(Document guildData, User user) {
		this.user = user.getIdLong();
		this.guild = guildData.getLong("guild");
		pullUserData(guildData);
		updateXp();
	}
	
	private void pullUserData(@NotNull Document gData) {
		if (gData.containsKey(Long.toString(user))) {
			userData = (Document) gData.get(Long.toString(user));
		} else {
			userData = MongoDBInterface.newUserData
					.append("userIDString", user.toString())
					.append("userIDLong", user);
		}
		
		
		if (!userData.getString("version").equals(MongoDBInterface.userDataVersion)) {
			updateData();
		}
		
	}
	
	private void updateXp() {
		int lvl = this.getLevel();
		
		while (this.getLevelXP() > (5 * (lvl ^ 2) + 50 * lvl + 100)) {
			lvl = this.getLevel();
			
			userData.put("levelXp", (this.getLevelXP() - (5 * (lvl ^ 2) + 50 * lvl + 100)));
			userData.put("level", (lvl + 1));
		}
		save();
	}
	
	private void updateData() {
		Document nUserData = MongoDBInterface.newUserData;
		
		if (userData.getString("version").equals("1.0.0")
			|| userData.getString("version").equals("1.0.1")
			|| userData.getString("version").equals("1.0.2")
			|| userData.getString("version").equals("1.1.0")) {
			for (String i : userData.keySet()) {
				if (Objects.equals(i, "version")) {
					continue;
				}
				nUserData.put(i, userData.get(i));
			}
		} else {
			logger.warn("Unknown format in user database!");
		}
		userData = nUserData;
	}
	
	public int getLevel() {
		return userData.getInteger("level");
	}
	
	public int getLevelXP() {
		return userData.getInteger("levelXp");
	}
	
	@SuppressWarnings("unchecked")
	private void save() {
		Exchanger e = new Exchanger();
		Qev.databaseInterface.set((userData, guildID, userID, ex) -> {
			try {
				Document newData   = (Document) ex.exchange(null);
				Document guildData = userData.find(new Document("guild", guildID)).first();
				if (guildData != null) {
					guildData.put(Long.toString(userID), newData);
				} else {
					guildData = MongoDBInterface.newGuildData;
					guildData.append(Long.toString(userID), newData);
				}
				
				userData.replaceOne(new Document("guild", guildID), guildData, new ReplaceOptions().upsert(true));
			} catch (InterruptedException ignored) {
			}
		}, guild, user, e);
		try {
			e.exchange(userData);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
	
	public UserStats(Document guildData, Long userID) {
		this.user = userID;
		this.guild = guildData.getLong("guild");
		pullUserData(guildData);
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
	
	public int getXpToNextLevel() {
		return (5 * (getLevel() ^ 2) + 50 * getLevel() + 100);
	}
	
	public void addXP(int xp) {
		userData.put("xp", getXP() + xp);
		userData.put("levelXp", getLevelXP() + xp);
		updateXp();
		Qev.databaseInterface.set((collection, guildID, userID, ex) -> {
			
			Document guildData = collection.find(new Document("guild", guildID)).first();
			
			if (guildData != null) {
				guildData.put(Long.toString(userID), userData);
				collection.replaceOne(new Document("guild", guildID), guildData, new ReplaceOptions().upsert(true));
			}
			
		}, guild, user);
	}
	
	public int getXP() {
		return userData.getInteger("xp");
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
	
	public void close() {
		save();
	}
}
