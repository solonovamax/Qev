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

package com.solostudios.qev.core.database.impl;

import com.solostudios.qev.core.database.api.Database;
import com.solostudios.qev.core.entities.InternalGuild;
import com.solostudios.qev.core.entities.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class MongoDBDatabase implements Database {
    
    @Override
    public void setConnectionURL(String connectionURL) {
    
    }
    
    @Override
    public void init() {
    
    }
    
    @Override
    public void openConnection() {
    
    }
    
    @Override
    public Object getClient() {
        return null;
    }
    
    @Override
    public CompletableFuture<User> getUser(long guild, long user) {
        return null;
    }
    
    @Override
    public boolean hasUser(long guild, long user) {
        return false;
    }
    
    @Override
    public CompletableFuture<List<User>> getUsers(long guild) {
        return null;
    }
    
    @Override
    public CompletableFuture<InternalGuild> getGuild(long guild) {
        return null;
    }
    
    @Override
    public boolean hasGuild(long id) {
        return false;
    }
    
    @Override
    public void saveUser(long guild, User user) {
    
    }
    
    @Override
    public void saveGuild(InternalGuild internalGuild) {
    
    }
    
    @Override
    public CompletableFuture<Map<InternalGuild, List<User>>> dumpData() {
        return null;
    }
}