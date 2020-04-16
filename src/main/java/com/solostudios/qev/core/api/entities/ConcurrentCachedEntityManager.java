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

import com.google.common.cache.*;
import com.solostudios.qev.core.api.database.Database;
import com.solostudios.qev.core.api.database.structure.raw.DataObject;
import com.solostudios.qev.core.api.database.structure.usable.Entity;
import com.solostudios.qev.core.api.database.structure.usable.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;


public abstract class ConcurrentCachedEntityManager<E extends Entity<M, E>, M extends ConcurrentCachedEntityManager<E, M>>
        extends EntityManager<E, M> {
    protected final LoadingCache<Long, E> cache;
    private final   Executor              executor;
    
    
    public ConcurrentCachedEntityManager(Database database) {
        this(database, Executors.newFixedThreadPool(4));
    }
    
    public ConcurrentCachedEntityManager(Database database, Executor executor) {
        super(database);
        this.executor = executor;
        cache = CacheBuilder.newBuilder()
                            .concurrencyLevel(4)
                            .expireAfterAccess(600, TimeUnit.SECONDS)
                            .removalListener(new DefaultRemovalListener<>(this))
                            .build(new DefaultCacheLoader());
    }
    
    @Override
    public final boolean usesCaching() {
        return true;
    }
    
    @Override
    public boolean isConcurrent() {
        return true;
    }
    
    @Override
    public final E getEntityById(long id) {
        return cache
                .asMap()
                .getOrDefault(id, loadEntity(id));
    }
    
    /**
     * Gets entities using a predicate that filters through the cache.
     * <p>
     * Note: this will not get any entities that are not already loaded into cache.
     *
     * @param filter
     *         Predicate to filter through the cache with. Must be stateless.
     *
     * @return Entity from the cache.
     */
    @Override
    public final E getEntityByFilter(Predicate<E> filter) {
        return cache
                .asMap()
                .values()
                .stream()
                .filter(filter)
                .findFirst()
                .orElse(null);
    }
    
    
    @Override
    public final Collection<E> getEntitiesByFilter(Predicate<E> filter) {
        Set<E> filterSet = new HashSet<>();
        
        cache.asMap()
             .values()
             .stream()
             .filter(filter)
             .forEach(filterSet::add); //must be after, so items in the cache are prioritized.
        
        return filterSet;
    }
    
    /**
     * All the entities in the cache.
     *
     * @return Collection of all the entities in the cache.
     */
    @Override
    public final Collection<E> getAllEntities() {
        return cache.asMap().values();
    }
    
    @Override
    public final void saveAll() {
        cache.invalidateAll();
    }
    
    protected abstract E loadEntity(long id);
    
    @Override
    protected final void save(E e) {
        if (cache.asMap().containsValue(e)) {
            cache.invalidate(e.getIdLong());
            return;
        }
        //TODO: save code
    }
    
    @Override
    protected E fromDataObject(DataObject object) {
        return null;
    }
    
    public final Map<Long, E> getCacheMap() {
        return new HashMap<>(cache.asMap());
    }
    
    public final Executor getCachedExecutor() {
        return executor;
    }
    
    final class DefaultCacheLoader extends CacheLoader<Long, E> {
        @Override
        public E load(@NotNull Long id) {
            return loadEntity(id);
        }
    }
    
    
    protected final class DefaultRemovalListener<V extends ConcurrentCachedEntityManager<E, M>> implements RemovalListener<Long, E> {
        V entityManager;
        
        protected DefaultRemovalListener(V entityManager) {
            this.entityManager = entityManager;
        }
        
        @Override
        public void onRemoval(@NotNull RemovalNotification<Long, E> notification) {
            executor.execute(() -> {
                entityManager.save(notification.getValue());
            });
        }
    }
    
    
}
