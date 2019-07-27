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

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.bson.Document;
import org.json.JSONObject;

import static com.solostudios.solobot.framework.utility.JSONtoMongoDB.toDocument;
import static com.solostudios.solobot.framework.utility.MongoDBtoJSON.toJSONObject;

/**
 *
 */
public class StatsHandler {

    public static final String userDataVersion = "1.0.1";
    public static final JSONObject newUserData = new JSONObject()
            .put("version", userDataVersion)
            .put("userIDString", "")
            .put("userIDLong", 0L)
            .put("xp", 0)
            .put("firstMessage", "")
            .put("interactionIndex", 0F)
            .put("lastMessageTime", 0L)
            .put("isSoftBanned", false)
            .put("softBanTime", 0L)
            .put("softBanLength", 0L);
    public static final Document newGuildData = new Document()
            .append("version", "1.0.0")
            .append("guild", 0L)
            .append("prefix", "!");
    private static final String uriString = "mongodb://bot:tob@localhost:27017/GuildXP?authSource=admin";
    private static MongoClient mongoClient = MongoClients.create(uriString);
    private static MongoDatabase botData = mongoClient.getDatabase("soloBOT");
    private static MongoCollection<Document> userData = botData.getCollection("UserData");


    /**
     *
     */
    public StatsHandler() {
        if (!guildExists(0L)) {
            LogHandler.debug("Levels file does not exist. Creating one now.");
            StatsHandler.create();
            LogHandler.info("Levels file did not exist. One has been created.");
        }
    }

    /**
     *
     */
    private static void create() {
        userData.insertOne(new Document(newGuildData));
    }

    /**
     * @param messageReceivedEvent JDA message received event object.
     */
    public static void messageEvent(MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getAuthor().isBot())
            return;
        userData = botData.getCollection("UserData");
        Guild guild = messageReceivedEvent.getGuild();
        User author = messageReceivedEvent.getAuthor();
        UserStats userStats = getUserStats(author, guild);

        if (userStats.getUserData().getString("firstMessage").equals("")) {
            userStats.updateUserData(
                    userStats.getUserData()
                            .put("firstMessage",
                                    messageReceivedEvent.getMessage()
                                            .getContentDisplay()
                            )
            );
        }
        if (System.currentTimeMillis() > userStats.getLastMessageTime() + 1000 * 60) {
            LogHandler.debug("Adding random amount of xp to user " + author.getAsTag());
            userStats.addXP((int) (Math.random() * 10 + 15));
            LogHandler.debug("User now has " + userStats.getXP() + " xp.");
            userStats.updateTime();
            saveUserStats(guild, userStats);
        }
    }

    /**
     * @param guildJoinEvent JDA guild join event object.
     */
    public static void guildJoinEvent(GuildJoinEvent guildJoinEvent) {
        if (!guildExists(guildJoinEvent.getGuild())) {
            addGuild(guildJoinEvent.getGuild());
        }
    }

    /**
     * @param user  User of which you want to get stats.
     * @param guild Guild from which you want to get these stats.
     * @return UsersStats object that contains all the stats.
     */
    public static UserStats getUserStats(User user, Guild guild) {
        return new UserStats(user, guild);
    }

    /**
     * @param guild Guild that you want to get.
     * @return The JSONObject of the guild. Will return a new guild if one does not exist.
     */
    public static JSONObject getGuild(Guild guild) {
        if (!guildExists(guild)) {
            addGuild(guild);
        }
        return toJSONObject(userData.find(new Document("guild", guild.getIdLong())).first());
    }

    /**
     * @param guild Guild that you want to add.
     */
    private static void addGuild(Guild guild) {
        Document futureGuild = new Document(newGuildData);
        futureGuild.replace("guild", guild.getIdLong());

        userData.insertOne(futureGuild);
    }

    /**
     * @param guild Guild to check if exists.
     * @return Whether or not the guild exists.
     */
    private static boolean guildExists(Guild guild) {
        return userData.find(new Document("guild", guild.getIdLong())).first() != null;
    }

    /**
     * @param guildID ID of guild to check if exists.
     * @return Whether or not the guild exists.
     */
    private static boolean guildExists(Long guildID) {
        return userData.find(new Document("guild", guildID)).first() != null;
    }


    /**
     * @param guild     guild that the user is in.
     * @param userStats JSONObject that contains user's data.
     */
    private static void saveUserStats(Guild guild, JSONObject userStats) {
        saveGuildList(getGuild(guild).put(userStats.getString("userIDString"), userStats));
    }

    /**
     *
     */
    private static void saveUserStats(Guild guild, UserStats userStats) {
        saveGuildList(getGuild(guild)
                .put(userStats.getUserData().getString("userIDString"), userStats.getUserData()));
    }

    /**
     * @param guildList Guild list object to save.
     */
    private static void saveGuildList(JSONObject guildList) {
        Document replaceDocument = toDocument(guildList);
        LogHandler.debug("Saving");
        userData.deleteMany(new Document("guild", guildList.getLong("guild")));
        userData.findOneAndDelete(new Document("guild", guildList.getLong("guild")));
        userData.insertOne(replaceDocument);
    }
}
