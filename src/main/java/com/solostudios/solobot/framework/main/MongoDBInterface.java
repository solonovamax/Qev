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

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.Exchanger;

import static com.solostudios.solobot.framework.utility.JSONtoMongoDB.toDocument;

public class MongoDBInterface {
    public static final String userDataVersion = "1.1.1";
    public static final Document newUserData = new Document()
            .append("version", userDataVersion)
            .append("userIDString", "")
            .append("userIDLong", 0L)
            .append("xp", 0)
            .append("level", 0)
            .append("levelXp", 0)
            .append("interactionIndex", 0F)
            .append("lastMessageTime", 0L)
            .append("isSoftBanned", false)
            .append("softBanTime", 0L)
            .append("softBanLength", 0L)
            .append("muteTime", 0L)
            .append("muteLength", 0L);
    public static final String guildDataVersion = "1.1.0";
    public static final Document newGuildData = new Document()
            .append("version", guildDataVersion)
            .append("guild", 0L)
            .append("prefix", "!")
            .append("updateTime", 0L);
    @NotNull
    private static final String uriString = "mongodb://bot:tob@localhost:27017/GuildXP?authSource=admin";
    @NotNull
    private static MongoClient mongoClient;
    @NotNull
    private static MongoDatabase botData;
    @NotNull
    private static MongoCollection<Document> userData;

    public MongoDBInterface() {
        initialize();
    }

    public static Object get(MongoGetOperation op, Long guildID, Long userID) {
        try {
            return op.run(getGuildDocument(guildID), userID, new Exchanger());
        } catch (InterruptedException ignored) {
            return null;
        }
    }

    public static Object get(MongoGetOperation op, Long guildID, Long userID, Exchanger ex) {
        try {
            return op.run(getGuildDocument(guildID), userID, ex);
        } catch (InterruptedException ignored) {
            return null;
        }
    }

    public static void set(MongoSetOperation op, Long guild, Long userID) {
        JobQueue.add(op, userData, guild, userID);
    }

    public static void set(MongoSetOperation op, Long guildID, Long userID, Exchanger ex) {
        JobQueue.add(op, userData, guildID, userID, ex);
    }

    private static boolean guildExists(@NotNull Guild guild) {
        return userData.find(new Document("guild", guild.getIdLong())).first() != null;
    }

    private static Document updateGuild(Long guildID) throws InterruptedException {
        Exchanger<? extends Document> exchanger = new Exchanger<>();
        JobQueue.update((userData, gID, uID, ex) -> {
            Document nGData = newGuildData;
            Document gData = userData.find(new Document("guild", gID)).first();

            if (gData != null && gData.getString("version").equals(guildDataVersion)) {
                try {
                    LogHandler.debug("Testeadeaew");
                    ex.exchange(gData);
                    LogHandler.debug("iwandwaiewad\n\n\n\neawdwae");
                } catch (InterruptedException ignored) {
                }
                return;
            }

            if (gData != null) {
                if (gData.getString("version").equals("1.0.0") ||
                        gData.getString("version").equals("1.1.0")) {
                    for (String i : gData.keySet()) {
                        if (Objects.equals(i, "version"))
                            continue;
                        nGData.put(i, gData.get(i));
                    }
                } else {
                    throw new UnknownFormatConversionException("");
                }
            } else {
                nGData.put("guild", gID);
            }
            userData.replaceOne(new Document("guild", guildID), nGData, new UpdateOptions().upsert(true));
            LogHandler.debug("Test");
            try {
                ex.exchange(nGData);
            } catch (InterruptedException ignored) {
            }
        }, userData, guildID, 0L, exchanger);
        return exchanger.exchange(null);
    }

    private static boolean guildExists(Long guildID) {
        try {
            updateGuild(guildID);
        } catch (InterruptedException ignored) {
        }
        return userData.find(new Document("guild", guildID)).first() != null;
    }

    private static Document getGuildDocument(Guild guild) {
        if (!guildExists(guild)) {
            addGuild(guild);
        }
        return userData.find(new Document("guild", guild.getIdLong())).first();
    }

    private static Document getGuildDocument(Long guildID) {
        if (!guildExists(guildID)) {
            addGuild(guildID);
        }
        return userData.find(new Document("guild", guildID)).first();
    }

    public static void messageEvent(@NotNull MessageReceivedEvent messageReceivedEvent) {
        if (messageReceivedEvent.getAuthor().isBot())
            return;


        Guild guild = messageReceivedEvent.getGuild();
        User author = messageReceivedEvent.getAuthor();
        JobQueue.update((collection, guildID, userID, ex) -> {
            LogHandler.debug("xp");
            UserStats userStats = new UserStats(collection.find(new Document("guild", guildID)).first(), userID);

            if (System.currentTimeMillis() > userStats.getLastMessageTime() + 1000 * 60) {
                LogHandler.debug("Adding random amount of xp to user " + author.getAsTag());
                userStats.addXP((int) (Math.random() * 10 + 15));
                LogHandler.debug("User now has " + userStats.getXP() + " xp.");
                userStats.updateTime();

                userData.replaceOne(new Document("guild", guildID), collection.find(new Document("guild", guildID)).first(), new UpdateOptions().upsert(true));
                saveUserStats(guild, userStats);
            }
        }, userData, guild.getIdLong(), author.getIdLong(), null);
    }

    public static void guildJoinEvent(@NotNull GuildJoinEvent guildJoinEvent) {
        if (!guildExists(guildJoinEvent.getGuild())) {
            addGuild(guildJoinEvent.getGuild());
        }
    }

    public static String getPrefix(Long guild) {
        return getGuild(guild).getString("prefix");
    }

    public static String getPrefix(@NotNull Guild guild) {
        return getGuild(guild).getString("prefix");
    }

    @Nullable
    public static Document getGuild(@NotNull Guild guild) {
        if (!guildExists(guild)) {
            addGuild(guild);
        }
        return userData.find(new Document("guild", guild.getIdLong())).first();
    }

    @Nullable
    public static Document getGuild(Long guild) {
        if (!guildExists(guild)) {
            addGuild(guild);
        }
        return userData.find(new Document("guild", guild)).first();
    }

    private static void addGuild(@NotNull Guild guild) {
        Document futureGuild = new Document(newGuildData);
        futureGuild.remove("guild");
        futureGuild.put("guild", guild.getIdLong());

        saveGuildList(futureGuild);
    }

    private static void addGuild(Long guild) {
        Document futureGuild = new Document(newGuildData);
        futureGuild.replace("guild", guild);

        saveGuildList(futureGuild);
    }

    static void saveUserStats(@NotNull Guild guild, @NotNull Document userStats) {
        Document guildList = getGuild(guild);
        guildList.put(userStats.getString("userIDString"), userStats);
        saveGuildList(guildList);
    }

    static boolean saveUserStats(@NotNull Guild guild, @NotNull UserStats userStats) {
        return saveGuildList(getGuild(guild)
                .append(userStats.getUser().toString(), userStats.getUserData()));
    }

    private static boolean saveGuildList(@NotNull JSONObject guildList) {
        Document replaceDocument = toDocument(guildList);
        return saveGuildList(replaceDocument);
    }

    private synchronized static boolean saveGuildList(@NotNull Document guildList) {
        LogHandler.debug("Saving");

        userData.replaceOne(new Document("guild", guildList.getLong("guild")), guildList, new UpdateOptions().upsert(true));
        return false;
    }

    private void initialize() {
        new JobQueue();
        mongoClient = MongoClients.create(uriString);
        botData = mongoClient.getDatabase("soloBOT");
        userData = botData.getCollection("UserData");
        try {
            updateGuild(0L);
        } catch (InterruptedException ignored) {
        }
    }
}
