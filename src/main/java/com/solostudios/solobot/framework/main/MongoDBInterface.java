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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.Exchanger;

public class MongoDBInterface {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBInterface.class);
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
            .append("updateTime", 0L)
            .append("muteRole", 0L);
    @NotNull
    private static final String uriString = "mongodb://bot:tob@solo-serv.local:27017/soloBOT?authSource=admin";
    @NotNull
    private static MongoClient mongoClient = MongoClients.create(uriString);
    @NotNull
    private static MongoDatabase botData = mongoClient.getDatabase("soloBOT");
    @NotNull
    private static MongoCollection<Document> userData = botData.getCollection("UserData");

    public MongoDBInterface() {
        initialize();
    }

    public static Object get(MongoGetOperation op, Long guildID, Long userID) {
        try {
            return op.run(getGuildDocument(guildID), userID, new Exchanger());
        } catch (InterruptedException e) {
            return null;
        }
    }

    public static <T> T get(MongoGetOperation op, Long guildID, Long userID, Class<T> clazz) {
        try {
            return (T) op.run(getGuildDocument(guildID), userID, new Exchanger());
        } catch (InterruptedException e) {
            return null;
        }
    }

    public static Object get(MongoGetOperation op, Long guildID, Long userID, Exchanger ex) {
        try {
            return op.run(getGuildDocument(guildID), userID, ex);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public static <T> T get(MongoGetOperation op, Long guildID, Long userID, Exchanger ex, Class<T> clazz) {
        logger.info("fuck you8");
        try {
            logger.info("running");
            return (T) op.run(getGuildDocument(guildID), userID, ex);
        } catch (InterruptedException e) {
            logger.warn("FUCK YOU", e);
            return null;
        }
    }

    public static void set(MongoSetOperation op, Long guild, Long userID) {
        JobQueue.add(op, userData, guild, userID);
    }

    public static void set(MongoSetOperation op, Long guildID, Long userID, Exchanger ex) {
        JobQueue.add(op, userData, guildID, userID, ex);
    }

    private static Document updateGuild(Long guildID) throws InterruptedException {
        Exchanger<? extends Document> exchanger = new Exchanger<>();
        JobQueue.update((userData, gID, uID, ex) -> {
            Document nGData = newGuildData;
            Document gData = userData.find(new Document("guild", gID)).first();

            if (gData != null && gData.getString("version").equals(guildDataVersion)) {
                try {
                    ex.exchange(gData);
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


    public static Document getGuildDocument(Long guildID) {
        if (!guildExists(guildID)) {
            addGuild(guildID);
        }
        return userData.find(new Document("guild", guildID)).first();
    }

    public static void messageEvent(@NotNull MessageReceivedEvent messageReceivedEvent) {
        if (!messageReceivedEvent.getChannelType().isGuild()) {
            return;
        }
        if (messageReceivedEvent.getAuthor().isBot())
            return;


        Guild guild = messageReceivedEvent.getGuild();
        User author = messageReceivedEvent.getAuthor();
        UserStats userStats = new UserStats(getGuildDocument(guild.getIdLong()), author.getIdLong());

        if (System.currentTimeMillis() > (userStats.getLastMessageTime() + (1000 * 60))) {
            logger.debug("Adding random amount of xp to user {}", author.getAsTag());
            userStats.addXP((int) (Math.random() * 10 + 15));
            logger.debug("User now has {} xp.", userStats.getXP());
            userStats.updateTime();
            userStats.close();
        }
    }

    public static void guildJoinEvent(@NotNull GuildJoinEvent guildJoinEvent) {
        if (!guildExists(guildJoinEvent.getGuild().getIdLong())) {
            addGuild(guildJoinEvent.getGuild().getIdLong());
        }
    }

    public static String getPrefix(Long guild) {
        return getGuildDocument(guild).getString("prefix");
    }

    public static void setPrefix(Long guild, String prefix) {
        MongoDBInterface.set((userData, guildID, userID, ex) -> {
            Document newGuildData = userData.find(new Document("guild", guildID)).first();
            newGuildData.put("prefix", prefix);
            userData.replaceOne(new Document("guild", guildID), newGuildData, new UpdateOptions().upsert(true));
        }, guild, 0L);
    }

    private static void addGuild(Long guild) {
        MongoDBInterface.set((collection, guildID, ignore, ex) -> {
            Document futureGuild = new Document(MongoDBInterface.newGuildData);
            futureGuild.replace("guild", guild);
            collection.insertOne(futureGuild);
        }, guild, 0L);
    }

    private void initialize() {
        new JobQueue();
        try {
            updateGuild(0L);
        } catch (InterruptedException ignored) {
        }
    }
}
