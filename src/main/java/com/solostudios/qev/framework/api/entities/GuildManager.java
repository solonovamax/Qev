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

package com.solostudios.qev.framework.api.entities;

import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.GenericDatabase;
import com.solostudios.qev.framework.api.database.structure.raw.DataObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;


public final class GuildManager extends ConcurrentCachedEntityManager<Guild, GuildManager> {
    
    public GuildManager(GenericDatabase database, Client client) {
        this(database, getDefaultExecutor(), client);
    }
    
    public GuildManager(GenericDatabase database, ExecutorService executor, Client client) {
        super(database, executor, "GuildConfig", client, GuildManager.class);
    }
    
    @Override
    protected Guild loadEntity(long id) {
        DataObject obj;
        if ((obj = entityTable.get(id)) == null) {
            return createNew(id);
        } else {
            return new Guild(obj, this, new UserManager(database, id, client), new RoleManager(database, id, client), client);
        }
    }
    
    @Override
    protected Guild createNew(long id) {
        HashMap<String, Object> initData = new HashMap<>();
        //TODO
        return new Guild(database.createDataObj(initData), this, new UserManager(database, id, client),
                         new RoleManager(database, id, client), client);
    }
    
    @Override
    public Guild fromDataObject(DataObject object) {
        return new Guild(object, this, new UserManager(database, object.getId(), client), new RoleManager(database, object.getId(), client),
                         client);
    }
    
    @Override
    public void shutdown() {
    
    }
}
