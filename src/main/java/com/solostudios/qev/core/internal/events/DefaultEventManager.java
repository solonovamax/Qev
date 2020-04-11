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
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.jodah.typetools.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;


public class DefaultEventManager implements EventManager {
    private final Logger      logger = LoggerFactory.getLogger(DefaultEventManager.class);
    private       Set<Object> listenerObjects;
    
    private Map<Class<? extends Event>, List<Consumer<? extends Event>>> listeners;
    
    private void buildListeners() {
        logger.debug("Rebuilding the listener list...");
        listeners.clear();
        for (Object listener : listenerObjects) {
            boolean isClass      = listener instanceof Class;
            boolean isConsumer   = listener instanceof Consumer;
            boolean isInterfaced = listener instanceof EventListener;
            if (isClass) {
                //noinspection unchecked
                Class<? extends EventListener> clazz = (Class<? extends EventListener>) listener;
                /*
                The following code snipped was taken from the forge EventBus class.
                It is under the GNU Lesser General Public License.
                 */
                Arrays.stream(clazz.getMethods())
                      .filter(method -> Modifier.isStatic(method.getModifiers()))
                      .filter(method -> method.isAnnotationPresent(SubscribeEvent.class))
                      .forEach(method -> {
                          Class<?>[] parameterTypes = method.getParameterTypes();
                          if (parameterTypes.length != 1) {
                              throw new IllegalArgumentException(
                                      "Method " + method + " has @SubscribeEvent annotation. " +
                                      "It has " + parameterTypes.length + " arguments, " +
                                      "but event handler methods require a single argument only."
                              );
                          }
                    
                          Class<?> eventType = parameterTypes[0];
                          if (!Event.class.isAssignableFrom(eventType)) {
                              throw new IllegalArgumentException(
                                      "Method " + method + " has @SubscribeEvent annotation, " +
                                      "but takes an argument that is not an Event subtype : " + eventType);
                          }
                    
                          //noinspection unchecked
                          addListener((Class<? extends Event>) eventType, method);
                      });
                /*
                Okay, the rest of the code probably isn't stolen from anywhere. (I hope)
                 */
            } else if (isConsumer) {
                //noinspection unchecked
                Consumer<? extends Event> consumer = (Consumer<? extends Event>) listener;
                //noinspection rawtypes
                Class resolvedArgument = TypeResolver.resolveRawArguments(Consumer.class,
                                                                          consumer.getClass())[0];
                
                if (resolvedArgument == null || resolvedArgument.equals(TypeResolver.Unknown.class)) {
                    throw new IllegalStateException(
                            "Could not resolve argument type for " + consumer.getClass().toString());
                }
                //noinspection unchecked
                if (!resolvedArgument.isAssignableFrom(Object.class)) {//Ignore java.lang.Object.
                    if (Event.class.isAssignableFrom(resolvedArgument)) {//Check it is a subtype of Event.
                        addListener(resolvedArgument, consumer);
                    }
                }
            } else if (isInterfaced) {
                EventListener eventInterface = (EventListener) listener;
                addListener(Event.class, eventInterface::onEvent);
            } else {
                throw new IllegalStateException("If you are seeing this, then something has gone very, very wrong.\n" +
                                                "Any object in the listenerObjects set should either be a Consumer<? " +
                                                "extends Event>, the EventListener interface, or a Class<? extends " +
                                                "EventListener>.");
            }
        }
    }
    
    public <T extends Event> void addListener(Class<T> eventClass, Method method) {
    
    }
    
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
        addListener(eventClass, consumer);
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
    public <T extends Event> void addListener(Class<T> eventClass, Consumer<T> consumer) {
    
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
