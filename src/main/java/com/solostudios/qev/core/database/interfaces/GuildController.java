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

package com.solostudios.qev.core.database.interfaces;

import com.avairebot.utilities.CacheUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.solostudios.qev.core.database.api.Database;
import com.solostudios.qev.core.database.api.GuildData;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


public class GuildController {
    public static final Cache<Long, GuildData> cache = CacheBuilder.newBuilder()
                                                                   .expireAfterAccess(600,
                                                                                      TimeUnit.SECONDS) // 10
                                                                   // minutes
                                                                   .removalListener(new GuildRemovalListener())
                                                                   .build();
    private final       Database               database;
    
    public GuildController(Database database) {
        this.database = database;
    }
    
    /**
     * Gets a guild object from the database.
     *
     * @param id
     *         ID of the guild object you want to retrieve.
     *
     * @return Returns a future with the guild.
     */
    public CompletableFuture<GuildData> getGuild(long id) {
        CompletableFuture<GuildData> guildFuture = new CompletableFuture<>();
        guildFuture.completeAsync(() -> CacheUtil.getUncheckedUnwrapped(cache, id, () -> database.getGuild(id).get()));
        return guildFuture;
    }
    
    private GuildData getGuildIfExists(long id) {
    
    }
    
    
    private static class GuildRemovalListener implements RemovalListener<Long, GuildData> {
        
        @Override
        public void onRemoval(RemovalNotification<Long, GuildData> notification) {
            Long      guildId = notification.getKey();
            GuildData guild   = notification.getValue();
        }
    }
}
