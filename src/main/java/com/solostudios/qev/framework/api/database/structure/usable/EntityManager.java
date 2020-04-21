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
import com.solostudios.qev.framework.api.database.GenericDatabase;
import com.solostudios.qev.framework.api.database.structure.raw.DataObject;
import com.solostudios.qev.framework.api.database.structure.raw.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class EntityManager<E extends Entity<M, E>, M extends EntityManager<E, M>> implements GenericEntityManager<E, M> {
    protected final Logger          logger;
    protected final GenericDatabase database;
    protected final Table           entityTable;
    protected final Client          client;
    protected       boolean         isShutdown = false;
    
    public EntityManager(GenericDatabase database, String tableName, Client client, Class<? extends M> clazz) {
        this.database = database;
        logger = LoggerFactory.getLogger(clazz);
        entityTable = database.getTable(tableName);
        this.client = client;
    }
    
    @Override
    public final boolean isShutdown() {
        return isShutdown;
    }
    
    protected abstract void save(E e);
    
    public final GenericDatabase getDatabase() {
        return database;
    }
    
    protected abstract E createNew(long id);
    
    /**
     * Constructs an {@link GenericEntity} using a {@link DataObject}.
     *
     * @param object
     *         The {@link DataObject} that is used to construct a new {@link GenericEntity}.
     *
     * @return The new {@link GenericEntity} that you constructed.
     */
    protected abstract E fromDataObject(DataObject object);
}
