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

package com.solostudios.qev.core.api.entities;

import com.solostudios.qev.core.api.database.Database;
import com.solostudios.qev.core.api.database.structure.usable.Entity;
import com.solostudios.qev.core.api.database.structure.usable.EntityManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class InMemoryManager<E extends Entity<M, E>, M extends InMemoryManager<E, M>> extends EntityManager<E, M> {
    private Boolean      finalized = false;
    private Map<Long, E> entityMap;
    
    public InMemoryManager(Database database, Class<M> clazz) {
        super(database, clazz);
        entityMap = new HashMap<>();
    }
    
    @Override
    public final boolean usesCaching() {
        return true;
    }
    
    @Override
    public final E getEntityById(long id) {
        if (finalized) throw new NullPointerException("You have already finalized this object. It can no longer be used.");
        return entityMap.get(id);
    }
    
    @Override
    public final E getEntityByFilter(Predicate<E> filter) {
        if (finalized) throw new NullPointerException("You have already finalized this object. It can no longer be used.");
        return entityMap.values()
                        .stream()
                        .filter(filter)
                        .findFirst()
                        .orElse(null);
    }
    
    @Override
    public final Collection<E> getEntitiesByFilter(Predicate<E> filter) {
        if (finalized) throw new NullPointerException("You have already finalized this object. It can no longer be used.");
        return entityMap.values()
                        .stream()
                        .filter(filter)
                        .collect(Collectors.toList());
    }
    
    @Override
    public final Collection<E> getAllEntities() {
        return new HashSet<>(entityMap.values());
    }
    
    @Override
    public final void saveAll() {
        for (E e : entityMap.values()) {
            save(e);
        }
        database = null;
        entityMap = null;
        finalized = true;
    }
    
    public final boolean isFinalized() {
        return finalized;
    }
}
