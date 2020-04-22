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

package com.solostudios.qev.framework.api.entities.saveable;

import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.DataObject;
import com.solostudios.qev.framework.api.database.GenericDatabase;
import com.solostudios.qev.framework.api.entities.saveable.managers.InMemoryManager;


public final class RoleManager extends InMemoryManager<Role, RoleManager> {
    public RoleManager(GenericDatabase database, long guildId, Client client) {
        super(database, "RoleConfig", client, RoleManager.class);
    }
    
    @Override
    public void shutdown() {
    
    }
    
    @Override
    protected void save(Role role) {
    
    }
    
    @Override
    protected Role createNew(long id) {
        return null;
    }
    
    @Override
    public Role fromDataObject(DataObject object) {
        return null;
    }
}
