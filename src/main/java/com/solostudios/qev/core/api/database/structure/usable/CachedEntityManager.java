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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public abstract class CachedEntityManager<T extends Entity<? extends CachedEntityManager<T>>>
        implements EntityManager<T> {
    
    private final Cache<Long, T> cache = CacheBuilder.newBuilder()
                                                     .expireAfterAccess(600, TimeUnit.SECONDS)
                                                     .removalListener(new DefaultRemovalListener<>(this))
                                                     .build();
    
    @Override
    public final boolean usesCaching() {
        return true;
    }
    
    @Override
    public final void save(T t) {
        cache.invalidate(t.getIdLong());
        //TODO: save code
    }
    
    @Override
    public T getEntityByID(long id) {
        return null;
    }
    
    @Override
    public T getEntityByFilter(Function<T, Boolean> filter) {
        return null;
    }
    
    public final Map<Long, T> getCache() {
        return new HashMap<>(cache.asMap());
    }
    
    
    private final class DefaultRemovalListener<V extends CachedEntityManager<T>> implements RemovalListener<Long, T> {
        V entityManager;
        
        protected DefaultRemovalListener(V entityManager) {
        
        }
        
        @Override
        public void onRemoval(RemovalNotification<Long, T> notification) {
            Long guildId = notification.getKey();
            T    guild   = notification.getValue();
            
            entityManager.save(guild);
        }
    }
}
