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
import com.solostudios.qev.framework.api.database.Database;
import com.solostudios.qev.framework.api.database.Table;
import com.solostudios.qev.framework.api.entities.SerializableEntity;
import com.solostudios.qev.framework.api.entities.SerializableEntityManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class ConcurrentCachedEntityManager<E extends SerializableEntity<ConcurrentCachedEntityManager<E>, E>>
        implements SerializableEntityManager<E, ConcurrentCachedEntityManager<E>> {
    private final LoadingCache<Long, E>   cache;
    private final Client                  client;
    private final ExecutorService         executor;
    private final Database                database;
    private final Table                   entityTable;
    private final Function<DataObject, E> loaderFunction;
    private final Function<Long, E>       creationFunction;
    private final Logger                  logger = LoggerFactory.getLogger(ConcurrentCachedEntityManager.class);
    
    public ConcurrentCachedEntityManager(Database database, ExecutorService executor, String tableName, Client client,
                                         Function<DataObject, E> loaderFunction, Function<Long, E> creationFunction,
                                         Weigher<Long, E> weigher, long maximumWeight) {
        this.database = database;
        this.executor = executor;
        this.entityTable = database.getTable(tableName);
        this.loaderFunction = loaderFunction;
        this.creationFunction = creationFunction;
        this.client = client;
        this.cache = CacheBuilder.newBuilder()
                                 .concurrencyLevel(4)
                                 .expireAfterAccess(600, TimeUnit.SECONDS)
                                 .removalListener(new DefaultRemovalListener<>(this))
                                 .weigher(weigher)
                                 .maximumWeight(maximumWeight)
                                 .build(new DefaultCacheLoader());
        client.registerShutdownListener(this::shutdown);
        
    }
    
    public ConcurrentCachedEntityManager(Database database, ExecutorService executor, String tableName, Client client,
                                         Function<DataObject, E> loaderFunction, Function<Long, E> creationFunction, long maximumSize) {
        this.database = database;
        this.executor = executor;
        this.entityTable = database.getTable(tableName);
        this.loaderFunction = loaderFunction;
        this.creationFunction = creationFunction;
        this.client = client;
        this.cache = CacheBuilder.newBuilder()
                                 .concurrencyLevel(4)
                                 .expireAfterAccess(600, TimeUnit.SECONDS)
                                 .removalListener(new DefaultRemovalListener<>(this))
                                 .maximumSize(maximumSize)
                                 .build(new DefaultCacheLoader());
        client.registerShutdownListener(this::shutdown);
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
    protected E loadEntity(long id) {
        return runUnwrapped(() -> loadEntityAsync(id).get());
        //https://github.com/welovedevs/react-ultimate-resume
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
    protected final CompletableFuture<E> loadEntityAsync(long id) {
        return CompletableFuture.supplyAsync(() -> {
            DataObject obj = getEntityTable().get(id);
            if (obj == null) {
                return creationFunction.apply(id);
            } else {
                return loaderFunction.apply(obj);
            }
        }, executor);
    }
    
    protected final <T> T runUnwrapped(SupplierWithException<T> unwappedFunction) {
        try {
            return unwappedFunction.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public LoadingCache<Long, E> getCache() {
        return cache;
    }
    
    /**
     * Saves the provided entity.
     *
     * @param e
     *         The entity to save.
     */
    protected final void save(E e) {
        if (cache.getIfPresent(e) != null) {
            cache.invalidate(e.getIdLong());
            return;
        }
        
        getEntityTable().put(e.getIdLong(), e.toDataObject());
    }
    
    @Override
    public Database getDatabase() {
        return database;
    }
    
    @Override
    public Table getEntityTable() {
        return entityTable;
    }
    
    @Override
    public Client getClient() {
        return client;
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
        return runUnwrapped(() -> asyncGetEntityById(id).get());
    }
    
    /**
     * Asynchronously gets an entity by id. If it doesn't exist in cache or in the database, it will create a new entity.
     *
     * @param id
     *         The id of the entity.
     *
     * @return Completable future containing the entity that the id represents.
     */
    public final CompletableFuture<E> asyncGetEntityById(long id) {
        return CompletableFuture.supplyAsync(() -> cache.getUnchecked(id), executor);
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
        return runUnwrapped(() -> asyncGetEntityByFilter(filter).get());
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
        return CompletableFuture.supplyAsync(() -> {
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
        }, executor);
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
        return runUnwrapped(() -> asyncGetEntitiesByFilter(filter).get());
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
        return CompletableFuture.supplyAsync(() -> {
            Collection<E> collection = cache.asMap()
                                            .values()
                                            .stream()
                                            .filter(filter)
                                            .collect(Collectors.toList());
            for (E e : collection) { cache.refresh(e.getIdLong()); }
            return collection;
        }, executor);
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
    public void shutdown() {
        cache.invalidateAll();
    }
    
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
    
    private interface SupplierWithException<T> {
        /**
         * Gets a result.
         *
         * @return a result
         */
        T get() throws Exception;
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
    
    
    final class DefaultRemovalListener<V extends ConcurrentCachedEntityManager<E>> implements RemovalListener<Long, E> {
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
