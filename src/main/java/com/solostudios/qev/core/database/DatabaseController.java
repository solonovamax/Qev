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

package com.solostudios.qev.core.database;

import com.solostudios.qev.core.database.abstracts.AbstractDatabase;
import com.solostudios.qev.core.database.entities.Guild;
import com.solostudios.qev.core.database.interfaces.GuildController;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class DatabaseController {
    private final AbstractDatabase database;
    private final GuildController  guildController;
    
    
    public DatabaseController(AbstractDatabase database) {
        this.database = database;
        this.guildController = new GuildController(database);
    }
    
    /**
     * Gets a guild object from the database.
     *
     * @param id
     *         ID of the guild object you want to retrieve.
     *
     * @return Returns a future with the guild.
     */
    public CompletableFuture<Guild> getGuild(long id) {
        return guildController.getGuild(id);
    }
    
    /**
     * Returns a list of all guilds.
     *
     * @return Future with a list of all the guilds in the database.
     */
    public CompletableFuture<List<Guild>> getGuilds() {
    
    }
    
    public void forceSync() {
    
    }
}
