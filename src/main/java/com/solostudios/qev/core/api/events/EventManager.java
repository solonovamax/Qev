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

package com.solostudios.qev.core.api.events;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


@SuppressWarnings({"unused", "unchecked"})
public interface EventManager {
    void init();
    
    <T extends EventListener> void register(T... listeners);
    
    <T extends EventListener> void unregister(T... listeners);
    
    <T extends EventListener> void register(T listener);
    
    <T extends EventListener> void unregister(T listener);
    
    <T extends Event> void register(Consumer<T>... consumer);
    
    <T extends Event> void unregister(Consumer<T>... consumer);
    
    <T extends Event> void register(Consumer<T> consumer);
    
    <T extends Event> void unregister(Consumer<T> consumer);
    
    <T extends EventListener> void registerStatic(Class<T>... listeners);
    
    <T extends EventListener> void unregisterStatic(Class<T>... listeners);
    
    <T extends EventListener> void registerStatic(Class<T> listener);
    
    <T extends EventListener> void unregisterStatic(Class<T> listener);
    
    //<T extends Event> void addListener(Class<T> eventClass, Consumer<T> consumer); //possibly remove
    
    //<T extends Event> void addListener(Class<T> eventClass, Method method); //possibly remove
    
    Map<Class<? extends Event>, Set<Consumer<? extends Event>>> getRegisteredEventListeners();
    
    void dispatch(Event e);
}
