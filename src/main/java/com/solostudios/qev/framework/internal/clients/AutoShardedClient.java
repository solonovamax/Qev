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

package com.solostudios.qev.framework.internal.clients;

import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.DatabaseController;
import com.solostudios.qev.framework.api.database.GenericDatabase;
import com.solostudios.qev.framework.api.events.Event;
import com.solostudios.qev.framework.api.events.EventListener;
import com.solostudios.qev.framework.api.events.EventManager;
import com.solostudios.qev.framework.entities.Guild;
import com.solostudios.qev.framework.entities.User;
import net.dv8tion.jda.api.JDA;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


public class AutoShardedClient implements Client {
    @Override
    public String getToken() {
        return null;
    }
    
    @Override
    public void init() {
    
    }
    
    @Override
    public Status getStatus() {
        return null;
    }
    
    @Override
    public void awaitStatus(Status status) {
    
    }
    
    @Override
    public void awaitJDAStatus(JDA.Status status) {
    
    }
    
    @Override
    public void onRunning(Consumer<Event> action) {
    
    }
    
    @Override
    public void onShutdown(Consumer<Event> action) {
    
    }
    
    @Override
    public boolean isThreaded() {
        return false;
    }
    
    @Override
    public JDA getJDA() {
        return null;
    }
    
    @Override
    public long getDatabasePing() {
        return 0;
    }
    
    @Override
    public long getDiscordHeartbeat() {
        return 0;
    }
    
    @Override
    public long getDiscordRestPing() {
        return 0;
    }
    
    @Override
    public EventManager getEventManager() {
        return null;
    }
    
    @Override
    public <T extends EventListener> void addEventListeners(T... listeners) {
    
    }
    
    @Override
    public <T extends EventListener> void removeEventListeners(T... listeners) {
    
    }
    
    @Override
    public <T extends EventListener> void addEventListener(T listener) {
    
    }
    
    @Override
    public <T extends EventListener> void removeEventListener(T listener) {
    
    }
    
    @Override
    public <T extends Event> void dispatchEvent(T e) {
    
    }
    
    @Override
    public GenericDatabase getDatabase() {
        return null;
    }
    
    @Override
    public DatabaseController getDatabaseController() {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<Guild>> getGuilds() {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<Guild>> getGuildCache() {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<Guild>> getGuildsByName(String name) {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<Guild>> getGuildsByName(String name, boolean ignoreCase) {
        return null;
    }
    
    @Override
    public CompletableFuture<Guild> getGuildByID(long id) {
        return null;
    }
    
    @Override
    public CompletableFuture<Guild> getGuildByID(String id) {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<Guild>> getMutualGuilds(User user) {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<User>> getUsers() {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<User>> getUserCache() {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<User>> getUsersByName(String name) {
        return null;
    }
    
    @Override
    public CompletableFuture<Set<User>> getUsersByName(String name, boolean ignoreCase) {
        return null;
    }
    
    @Override
    public CompletableFuture<User> getUserByID(long id) {
        return null;
    }
    
    @Override
    public CompletableFuture<User> getUserByID(String id) {
        return null;
    }
    
    @Override
    public CompletableFuture<User> getUserByTag(String username, String discriminator) {
        return null;
    }
    
    @Override
    public boolean isShutdown() {
        return false;
    }
    
    @Override
    public void shutdown() {
    
    }
}
