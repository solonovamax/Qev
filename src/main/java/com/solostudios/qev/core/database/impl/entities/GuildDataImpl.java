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

package com.solostudios.qev.core.database.impl.entities;

import com.solostudios.qev.core.database.api.GuildData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class GuildDataImpl implements GuildData, Iterable<UserDataImpl> {
    private final long                    id;
    private final long                    timeCreated;
    private final long                    timeSaved;
    private final Map<String, Object>     configMap = new HashMap<>();
    private final Map<Long, UserDataImpl> userMap   = new HashMap<>();
    
    public GuildDataImpl(long id, long timeCreated) {
        this.id = id;
        this.timeCreated = timeCreated;
        this.timeSaved = System.currentTimeMillis();
    }
    
    public GuildDataImpl(long id, long timeCreated, long timeSaved) {
        this.id = id;
        this.timeCreated = timeCreated;
        this.timeSaved = timeSaved;
    }
    
    public CompletableFuture<UserDataImpl> getUser(long id) {
        return null;
    }
    
    public CompletableFuture<List<UserDataImpl>> getUsers() {
        return null;
    }
    
    public CompletableFuture<List<UserDataImpl>> getUsersByName(String name) {
        CompletableFuture<List<UserDataImpl>> userListFuture = new CompletableFuture<>();
        //userListFuture.completeAsync();
        return userListFuture;
    }
    
    
    @Override
    public String getId() {
        return id + "_G";
    }
    
    @Override
    public long getIdLong() {
        return id;
    }
    
    @Override
    public long getTimeCreated() {
        return timeCreated;
    }
    
    @Override public long lastTimeSaved() {
        return timeSaved;
    }
    
    @Override
    @NotNull
    public Iterator<UserDataImpl> iterator() {
        return userMap.values().iterator();
    }
    
    @Override
    public void forEach(Consumer<? super UserDataImpl> action) {
        userMap.values().forEach(action);
    }
    
    @Override
    public Spliterator<UserDataImpl> spliterator() {
        return userMap.values().spliterator();
    }
}
