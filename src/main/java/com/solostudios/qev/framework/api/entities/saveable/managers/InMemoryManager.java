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

package com.solostudios.qev.framework.api.entities.saveable.managers;

import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.GenericDatabase;
import com.solostudios.qev.framework.api.database.entities.SerializableEntity;
import com.solostudios.qev.framework.api.database.entities.SerializableEntityManager;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class InMemoryManager<E extends SerializableEntity<M, E>, M extends InMemoryManager<E, M>>
        extends SerializableEntityManager<E, M> {
    private final Boolean      finalized = false;
    private final Map<Long, E> entityMap;
    
    public InMemoryManager(GenericDatabase database, String tableName, Client client, Class<M> clazz) {
        super(database, tableName, client, clazz);
        entityMap = new HashMap<>();
    }
    
    @Override
    public final boolean usesCaching() {
        return true;
    }
    
    @Override
    public boolean isConcurrent() {
        return false;
    }
    
    @Override
    public final E getEntityById(long id) {
        E e;
        return (e = entityMap.get(id)) != null ? e : createNew(id);
    }
    
    @Override
    @Nullable
    public final E getEntityByFilter(Predicate<E> filter) {
        return entityMap.values()
                        .stream()
                        .filter(filter)
                        .findFirst()
                        .orElse(null);
    }
    
    @Override
    public final Collection<E> getEntitiesByFilter(Predicate<E> filter) {
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
    }
    
    public final boolean isFinalized() {
        return finalized;
    }
}
