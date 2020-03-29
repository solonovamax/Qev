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

package com.solostudios.qev.core.database.api;

import com.solostudios.qev.core.entities.InternalGuild;
import com.solostudios.qev.core.entities.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public interface Database {
    void setConnectionURL(String connectionURL);
    
    /**
     * Initialization phase for the database. This is always called before anything else.
     */
    void init();
    
    /**
     * Opens the connection to the database.
     */
    void openConnection();
    
    /**
     * Gets the client object that is connected to the database.
     *
     * @return The client object that is connected to the database.
     */
    Object getClient();
    
    /**
     * Gets a user from the database
     *
     * @param guild
     *         The ID of the guild to get the user from
     * @param user
     *         The ID of the user to get
     *
     * @return Future containing the user.
     */
    CompletableFuture<User> getUser(long guild, long user);
    
    /**
     * Checks to see if the database contains the specified user.
     *
     * @param guild
     *         The guild to check if the user is in.
     * @param user
     *         The user to check.
     *
     * @return Whether or not the user exists in that guild.
     */
    boolean hasUser(long guild, long user);
    
    /**
     * Gets a list of users in a certain guild
     *
     * @param guild
     *         ID of guild to get the list from
     *
     * @return Future with the list of users.
     */
    CompletableFuture<List<User>> getUsers(long guild);
    
    /**
     * Gets a guild from the database
     *
     * @param guild
     *         The Id of the guild to get
     *
     * @return Future with the guild
     */
    CompletableFuture<InternalGuild> getGuild(long guild);
    
    /**
     * Checks if the database contains the specified guild.
     *
     * @param id
     *         Guild to check.
     *
     * @return Whether or not the database has this guild.
     */
    boolean hasGuild(long id);
    
    /**
     * Saves a user to the database.
     *
     * @param guild
     *         The ID of the guild to which this user is to be saved to.
     * @param user
     *         The user object that is to be saved to the database.
     */
    void saveUser(long guild, User user);
    
    /**
     * Saves a guild to the database.
     *
     * @param internalGuild
     *         The guild that is to be saved to the database.
     */
    void saveGuild(InternalGuild internalGuild);
    
    /**
     * Complete dump of the guild - user pairing in the database.
     *
     * @return Future with all the guild data in the database.
     */
    CompletableFuture<Map<InternalGuild, List<User>>> dumpData();
}
