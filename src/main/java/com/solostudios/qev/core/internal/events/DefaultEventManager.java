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

package com.solostudios.qev.core.internal.events;

import com.solostudios.qev.core.api.events.Event;
import com.solostudios.qev.core.api.events.EventListener;
import com.solostudios.qev.core.api.events.EventManager;
import net.jodah.typetools.TypeResolver;

import java.util.*;
import java.util.function.Consumer;


public class DefaultEventManager implements EventManager {
    Set<Object>                                                  listenerObjects;
    Map<Class<? extends Event>, List<Consumer<? extends Event>>> listeners;
    
    @Override
    public void init() {
        listeners = new HashMap<>();
        listenerObjects = new HashSet<>();
    }
    
    @Override
    public void register(EventListener... listeners) {
        for (EventListener eventListener : listeners) {
            register(eventListener);
        }
    }
    
    @Override
    public void unregister(EventListener... listeners) {
        for (EventListener eventListener : listeners) {
            unregister(eventListener);
        }
    }
    
    @Override
    public <T extends EventListener> void register(T listener) {
    
    }
    
    @Override
    public <T extends EventListener> void unregister(T listeners) {
    
    }
    
    @Override
    public <T extends Event> void register(Consumer<T>... consumers) {
        for (Consumer<T> consumer : consumers) {
            register(consumer);
        }
    }
    
    @Override
    public <T extends Event> void unregister(Consumer<T>... consumers) {
        for (Consumer<T> consumer : consumers) {
            unregister(consumer);
        }
    }
    
    @Override
    public <T extends Event> void register(Consumer<T> consumer) {
        Class<T> eventClass = (Class<T>) TypeResolver.resolveRawArgument(Consumer.class, consumer.getClass());
        if ((Class<?>) eventClass == TypeResolver.Unknown.class) {
            throw new IllegalStateException("Failed to resolve consumer event type: " + consumer.toString());
        }
        registerStatic(eventClass, consumer);
    }
    
    @Override
    public <T extends Event> void unregister(Consumer<T> consumer) {
    
    }
    
    @Override
    public <T extends EventListener> void registerStatic(Class<T>... listeners) {
        for (Class<T> eventListenerClass : listeners) {
            if (!EventListener.class.isAssignableFrom(eventListenerClass)) {
                throw new IllegalStateException("Listeners must extend the EventListener class!");
            }
            registerStatic(eventListenerClass);
        }
    }
    
    @Override
    public <T extends EventListener> void unregisterStatic(Class<T>... listeners) {
        for (Class<T> eventListenerClass : listeners) {
            if (!EventListener.class.isAssignableFrom(eventListenerClass)) {
                throw new IllegalStateException("Listeners must extend the EventListener class!");
            }
            unregisterStatic(eventListenerClass);
        }
    }
    
    @Override
    public <T extends EventListener> void registerStatic(Class<T> listeners) {
        
    }
    
    @Override
    public <T extends EventListener> void unregisterStatic(Class<T> listeners) {
    
    }
    
    @Override
    public <T extends Event> void registerStatic(Class<T> eventClass, Consumer<T> consumer) {
    
    }
    
    @Override
    public Map<Class<? extends Event>, List<Consumer<? extends Event>>> getRegisteredEventListeners() {
        return new HashMap<>(listeners);
    }
    
    @Override
    public void dispatch(Event e) {
    
    }
    
    public <T extends Event> void castLambda(Class<T> eventClass, Consumer<T> consumer, Event e) {
        T cast = (T) e;
        consumer.accept(cast);
    }
}
