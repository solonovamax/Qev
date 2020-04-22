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

import com.google.common.cache.*;
import com.google.common.collect.ImmutableMap;
import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.DataObject;
import com.solostudios.qev.framework.api.database.GenericDatabase;
import com.solostudios.qev.framework.api.database.entities.SerializableEntity;
import com.solostudios.qev.framework.api.database.entities.SerializableEntityManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class ConcurrentCachedEntityManager<E extends SerializableEntity<M, E>, M extends ConcurrentCachedEntityManager<E, M>>
        implements SerializableEntityManager<E, M> {
    private static final ExecutorService       defaultExecutorService = Executors.newFixedThreadPool(4);
    protected final      LoadingCache<Long, E> cache;
    private final        ExecutorService       executor;
    
    
    public ConcurrentCachedEntityManager(GenericDatabase database, String tableName, Client client, Class<M> clazz) {
        this(database, Executors.newFixedThreadPool(4), tableName, client, clazz);
    }
    
    public ConcurrentCachedEntityManager(GenericDatabase database, ExecutorService executor, String tableName, Client client,
                                         Class<M> clazz) {
        this.executor = executor;
        this.cache = CacheBuilder.newBuilder()
                                 .concurrencyLevel(4)
                                 .expireAfterAccess(600, TimeUnit.SECONDS)
                                 .removalListener(new DefaultRemovalListener<>(this))
                                 .build(new DefaultCacheLoader());
        client.registerShutdownListener(this::shutdown);
    }
    
    /**
     * Returns the default executor.
     *
     * @return The default executor service.
     */
    public static ExecutorService getDefaultExecutor() {
        return defaultExecutorService;
    }
    
    /**
     * Whether or not this manager allows for caching.
     *
     * @return true.
     */
    @Override
    public final boolean usesCaching() {
        return true;
    }
    
    /**
     * Whether or not this is manager allows for concurrency.
     *
     * @return true.
     */
    @Override
    public boolean isConcurrent() {
        return true;
    }
    
    /**
     * Gets an entity by id. If it doesn't exist in cache or in the database, it will create a new entity.
     *
     * @param id
     *         The id of the entity.
     *
     * @return The entity that the id represents.
     */
    @Override
    public final E getEntityById(long id) {
        return cache.getUnchecked(id);
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
        E e = cache
                .asMap()
                .values()
                .stream()
                .filter(filter)
                .findFirst()
                .orElse(null);
        if (e != null)
            cache.refresh(e.getIdLong());
    
        return e;
    }
    
    /**
     * Filters through all the entities in the <b>cache</b>, with a given predicate.
     *
     * @param filter
     *         Predicate used to filter the entities in cache.
     *
     * @return The Collection of entities found.
     */
    @Override
    public final Collection<E> getEntitiesByFilter(Predicate<E> filter) {
        Collection<E> collection = cache.asMap()
                                        .values()
                                        .stream()
                                        .filter(filter)
                                        .collect(Collectors.toList());
        for (E e : collection) { cache.refresh(e.getIdLong()); }
        return collection;
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
    
    /**
     * Forces the entire cache to be written.
     */
    @Override
    public final void saveAll() {
        cache.invalidateAll();
    }
    
    /**
     * Asynchronously filters through all the entities in the <b>cache</b>, with a given predicate.
     *
     * @param filter
     *         Predicate used to filter the entities in cache.
     *
     * @return The entity found.
     */
    public final CompletableFuture<E> asyncGetEntityByFilter(Predicate<E> filter) {
        return CompletableFuture.supplyAsync(() -> getEntityByFilter(filter), executor);
    }
    
    /**
     * Asynchronously filters through all the entities in the <b>cache</b>, with a given predicate.
     *
     * @param filter
     *         Predicate used to filter the entities in cache.
     *
     * @return The Collection of entities found.
     */
    public final CompletableFuture<Collection<E>> asyncGetEntitiesByFilter(Predicate<E> filter) {
        return CompletableFuture.supplyAsync(() -> getEntitiesByFilter(filter), executor);
    }
    
    /**
     * Asynchronously loads an entity by id from the database. This method is guaranteed to return an object, as it will create one if it
     * does not exist.
     *
     * @param id
     *         The id of the entity.
     *
     * @return Completable future containing the entity that the id represents.
     */
    public final CompletableFuture<E> loadEntityAsync(long id) {
        return CompletableFuture.supplyAsync(() -> loadEntity(id));
    }
    
    /**
     * Loads and entity from database of flatmap, with the given id. This method is guaranteed to return an object, as it will create one if
     * it does not exist.
     *
     * @param id
     *         id used to search for the entity.
     *
     * @return The entity the id represents.
     */
    protected abstract E loadEntity(long id);
    
    /**
     * Asynchronously gets an entity by id. If it doesn't exist in cache or in the database, it will create a new entity.
     *
     * @param id
     *         The id of the entity.
     *
     * @return Completable future containing the entity that the id represents.
     */
    public final CompletableFuture<E> asyncGetEntityById(long id) {
        return CompletableFuture.supplyAsync(() -> getEntityById(id), executor);
    }
    
    /**
     * Saves the provided entity.
     *
     * @param e
     *         The entity to save.
     */
    @Override
    protected final void save(E e) {
        if (cache.getIfPresent(e) != null) {
            cache.invalidate(e.getIdLong());
            return;
        }
    
        entityTable.put(e.getIdLong(), e.toDataObject());
    }
    
    /**
     * Creates an entity using a data object that represents this entity.
     *
     * @param object
     *         The {@link DataObject} that is used to construct a new {@link SerializableEntity}.
     *
     * @return The Entity that was constructed using the data object.
     */
    @Override
    abstract protected E fromDataObject(DataObject object);
    
    /**
     * Returns the entire cache as an immutable map.
     *
     * @return The contents of the cache.
     */
    public final Map<Long, E> getCacheMap() {
        return new ImmutableMap.Builder<Long, E>().putAll(cache.asMap()).build();
    }
    
    /**
     * Returns the executor used for all asynchronous tasks.
     *
     * @return The executor used for all asynchronous tasks.
     */
    public final Executor getCachedExecutor() {
        return executor;
    }
    
    final class DefaultCacheLoader extends CacheLoader<Long, E> {
        @Override
        public E load(@NotNull Long id) {
            try {
                return loadEntityAsync(id).get();
            } catch (ExecutionException | InterruptedException e) {
                return load(id, 1);
            }
        }
        
        public E load(@NotNull Long id, int depth) {
            try {
                return loadEntityAsync(id).get();
            } catch (ExecutionException | InterruptedException e) {
                if (depth > 20) {
                    throw new RuntimeException("Exceeded 20 successive calls which were all errors. Aborting load task.", e);
                }
                return load(id, ++depth);
            }
        }
    }
    
    
    final class DefaultRemovalListener<V extends ConcurrentCachedEntityManager<E, M>> implements RemovalListener<Long, E> {
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
