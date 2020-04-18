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

package com.solostudios.qev.framework.api.database.structure.usable;

import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.structure.raw.DataObject;
import net.dv8tion.jda.api.JDA;

import java.util.HashMap;
import java.util.Map;


public abstract class Entity<M extends EntityManager<E, M>, E extends Entity<M, E>> implements GenericEntity<M, E> {
    protected final M                   manager;
    protected final Map<String, Object> settings;
    protected final JDA                 jda;
    protected final Client              client;
    
    public Entity(M manager, DataObject object, Client client, JDA jda) {
        this.jda = jda;
        this.client = client;
        this.manager = manager;
        
        settings = new HashMap<>();
        for (Map.Entry<String, Object> entry : object.keySet()) {
            settings.put(entry.getKey(), entry.getValue());
        }
    }
    
    public void forceSave() {
        //noinspection unchecked
        manager.save((E) this);
    }
    
    public final String getId() {
        return Long.toString(getIdLong());
    }
    
    @Override
    public M getManager() {
        return manager;
    }
    
}
