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

package com.solostudios.qev.framework.api;

import com.solostudios.qev.framework.api.database.DatabaseManager;
import com.solostudios.qev.framework.api.database.GenericDatabase;
import com.solostudios.qev.framework.api.entities.Guild;
import com.solostudios.qev.framework.api.entities.User;
import com.solostudios.qev.framework.api.events.Event;
import com.solostudios.qev.framework.api.events.EventListener;
import com.solostudios.qev.framework.api.events.EventManager;
import net.dv8tion.jda.api.JDA;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


//TODO: rename client to an interesting name (whatever I decide to name the project)
public interface Client {
    
    /**
     * Gets the token for your discord bot.
     *
     * @return The token for your discord bot.
     */
    String getToken();
    
    /**
     * All general initialization is performed within this event.
     * <p>
     * This will be called automatically when the client object is constructed.
     */
    void init();
    
    Client.Status getStatus();
    
    /**
     * Method blocks until it reaches the specified status.
     *
     * @param status
     */
    void awaitStatus(Client.Status status);
    
    /**
     * <b>!!!</b>
     * Javadoc for this command was copied from the JDA Javadoc, as this just forwards to a JDA method.
     * <b>!!!</b>
     * <p>
     * This method will block until the {@link JDA} has reached the specified connection status.
     * <p>
     * Please see {@link JDA#awaitStatus(JDA.Status)} or {@link JDA#awaitStatus(JDA.Status, JDA.Status...)} for more.
     *
     * @param status
     *         The init status to wait for, once JDA has reached the specified stage of the startup cycle this method will return.
     */
    void awaitJDAStatus(JDA.Status status);
    
    /**
     * This will execute an action when the Client reaches the {@link Client.Status#RUNNING} status.
     *
     * @param action
     *         Consumer to be run when the Client reaches the {@link Client.Status#RUNNING} status.
     */
    void onRunning(Consumer<Event> action);
    
    /**
     * This will execute an action when the Client is in the {@link Client.Status#SHUTTING_DOWN} phase.
     *
     * @param action
     *         Consumer to be run when the Client reaches the {@link Client.Status#RUNNING} status.
     */
    void onShutdown(Consumer<Event> action);
    
    boolean isThreaded();
    
    JDA getJDA();
    
    long getDatabasePing();
    
    long getDiscordHeartbeat();
    
    long getDiscordRestPing();
    
    
    /**
     * Gets the event manager.
     *
     * @return The event manager.
     */
    EventManager getEventManager();
    
    <T extends EventListener> void addEventListeners(T... listeners);
    
    <T extends EventListener> void removeEventListeners(T... listeners);
    
    <T extends EventListener> void addEventListener(T listener);
    
    <T extends EventListener> void removeEventListener(T listener);
    
    <T extends Event> void dispatchEvent(T e);
    
    void registerShutdownListener(Runnable shutdownListener);
    
    /**
     * Gets the database.
     *
     * @return The database.
     */
    GenericDatabase getDatabase();
    
    /**
     * Gets the database controller.
     *
     * @return The database controller.
     */
    DatabaseManager getDatabaseManager();
    
    CompletableFuture<Set<Guild>> getGuilds();
    
    CompletableFuture<Set<Guild>> getGuildCache();
    
    CompletableFuture<Set<Guild>> getGuildsByName(String name);
    
    CompletableFuture<Set<Guild>> getGuildsByName(String name, boolean ignoreCase);
    
    CompletableFuture<Guild> getGuildByID(long id);
    
    CompletableFuture<Guild> getGuildByID(String id);
    
    CompletableFuture<Set<Guild>> getMutualGuilds(User user);
    
    CompletableFuture<Set<User>> getUsersByName(String name);
    
    CompletableFuture<Set<User>> getUsersByName(String name, boolean ignoreCase);
    
    CompletableFuture<User> getUserByID(long id);
    
    CompletableFuture<User> getUserByID(String id);
    
    CompletableFuture<User> getUserByTag(String username, String discriminator);
    
    /**
     * Checks if the Client is shut down.
     *
     * @return
     */
    boolean isShutdown();
    
    /**
     * Shuts down the client. <b>ALWAYS</b> call this method before you shut down the bot, or else you may lose some guild data due to the
     * caches not being saved.
     */
    void shutdown();
    
    enum Status {
        INITIALIZING(true),
        INITIALIZING_SUBSYSTEMS(true),
        REGISTERING_COMMANDS(true),
        RUNNING,
        SHUTTING_DOWN,
        HANDLING_CRITICAL_ERROR;
        
        /*
        The remaining part of the enum code was taken from JDA and is under the Apache V2 License.
        (I mean, it's such a small snippet that realistically nobody will care, but better safe than sorry.)
        
        Copyright 2015-2019 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors.
        You can get a copy of the license at http://www.apache.org/licenses/LICENSE-2.0.
         */
        private final boolean isInit;
        
        Status(boolean isInit) {
            this.isInit = isInit;
        }
        
        Status() {
            this.isInit = false;
        }
        
        public boolean isInit() {
            return isInit;
        }
        /*
        - - - Everything after this is my own code and is under GPLv3 - - -
         */
    }
    
    
}
