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

import com.solostudios.qev.framework.api.actions.Action;
import com.solostudios.qev.framework.api.database.Database;
import com.solostudios.qev.framework.api.entities.saveable.Guild;
import com.solostudios.qev.framework.api.entities.saveable.Member;
import com.solostudios.qev.framework.api.events.Event;
import com.solostudios.qev.framework.api.events.EventListener;
import com.solostudios.qev.framework.api.events.EventManager;
import net.dv8tion.jda.api.JDA;

import java.util.Set;
import java.util.function.Consumer;


//TODO: rename client to an interesting name (whatever I decide to name the project)
public interface Client {
    
    /**
     * Gets the token for your discord bot.
     *
     * @return The token for your discord bot.
     */
    String getToken();
    
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
    
    void addEventListeners(Class<? extends EventListener>... listeners);
    
    void removeEventListeners(Class<? extends EventListener>... listeners);
    
    void addEventListener(Class<? extends EventListener> listener);
    
    void removeEventListener(Class<? extends EventListener> listener);
    
    void registerShutdownListener(Runnable shutdownListener);
    
    /**
     * Gets the database.
     *
     * @return The database.
     */
    Database getDatabase();
    
    long getDatabasePing();
    
    Action<Set<Guild>> getGuilds();
    
    Action<Set<Guild>> getGuildCache();
    
    Action<Set<Guild>> getGuildsByName(String name);
    
    Action<Set<Guild>> getGuildsByName(String name, boolean ignoreCase);
    
    Action<Guild> getGuildByID(long id);
    
    Action<Guild> getGuildByID(String id);
    
    Action<Set<Guild>> getMutualGuilds(Member member);
    
    Action<Set<Member>> getUsersByName(String name);
    
    Action<Set<Member>> getUsersByName(String name, boolean ignoreCase);
    
    Action<Member> getUserByID(long id);
    
    Action<Member> getUserByID(String id);
    
    Action<Member> getUserByTag(String username, String discriminator);
    
    /**
     * Checks if the Client is shut down.
     *
     * @return
     */
    boolean isShutdown();
    
    /**
     * Shuts down the client. <b>ALWAYS</b> call this method before you shut down the bot, or else you may lose some guild data due to the
     * caches not being saved.
     * <p>
     * This will automatically be called if the JVM shuts down, via a shutdown hook.
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
