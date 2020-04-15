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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.solostudios.qev.core.api.database.structure.usable.EntityManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class CachedEntityManager<E extends CachedEntity<M, E>, M extends CachedEntityManager<E, M>>
        implements EntityManager<E, M> {
    
    private final Cache<Long, E> cache = CacheBuilder.newBuilder()
                                                     .expireAfterAccess(600, TimeUnit.SECONDS)
                                                     .removalListener(new DefaultRemovalListener<>(this))
                                                     .build();
    
    @Override
    public final boolean usesCaching() {
        return true;
    }
    
    @Override
    public final void save(E e) {
        cache.invalidate(e.getIdLong());
        //TODO: save code
    }
    
    @Override
    public final E getEntityById(long id) {
        return cache
                .asMap()
                .getOrDefault(id, getEntityFromSave(id));
    }
    
    @Override
    public final E getEntityByFilter(Predicate<E> filter) {
        return cache
                .asMap()
                .values()
                .stream()
                .filter(filter)
                .findFirst()
                .orElse(getEntityFromSave(filter));
    }
    
    @Override
    public final Collection<E> getEntitiesByFilter(Predicate<E> filter) {
        return Stream.concat(cache.asMap().values().stream().filter(filter), getEntitiesFromSave(filter))
                     .collect(Collectors.toList());
    }
    
    protected final Stream<E> getEntitiesFromSave(Predicate<E> predicate) {
        return getAllEntities()
                .stream()
                .filter(predicate);
    }
    
    protected final E getEntityFromSave(Predicate<E> filter) {
        return getAllEntities()
                .stream()
                .filter(filter)
                .findFirst()
                .orElse(null);
    }
    
    protected final E getEntityFromSave(long id) {
        return getAllEntities()
                .stream()
                .filter((e) -> e.getIdLong() == id)
                .findFirst()
                .orElse(null);
    }
    
    public abstract Collection<E> getAllEntities();
    
    public final Map<Long, E> getCache() {
        return new HashMap<>(cache.asMap());
    }
    
    
    protected final class DefaultRemovalListener<V extends CachedEntityManager<E, M>> implements RemovalListener<Long, E> {
        V entityManager;
        
        protected DefaultRemovalListener(V entityManager) {
            this.entityManager = entityManager;
        }
        
        @Override
        public void onRemoval(RemovalNotification<Long, E> notification) {
            Long guildId = notification.getKey();
            E    guild   = notification.getValue();
            
            entityManager.save(guild);
        }
    }
}
