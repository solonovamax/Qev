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

package com.solostudios.qev.core.api.database.structure.usable;

import com.solostudios.qev.core.api.database.Database;
import com.solostudios.qev.core.api.database.structure.raw.DataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class EntityManager<E extends Entity<M, E>, M extends EntityManager<E, M>> implements GenericEntityManager<E, M> {
    protected final Logger   logger;
    protected       Database database;
    
    public EntityManager(Database database, Class<? extends M> clazz) {
        this.database = database;
        logger = LoggerFactory.getLogger(clazz);
    }
    
    protected abstract void save(E e);
    
    public final Database getDatabase() {
        return database;
    }
    
    protected abstract void createNew(long id);
    
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
