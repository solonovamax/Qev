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

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public interface EventManager {
    void init();
    
    <T extends EventListener> void register(T... listeners);
    
    <T extends EventListener> void unregister(T... listeners);
    
    <T extends EventListener> void register(T listener);
    
    <T extends EventListener> void unregister(T listeners);
    
    <T extends Event> void register(Consumer<T>... consumer);
    
    <T extends Event> void unregister(Consumer<T>... consumer);
    
    <T extends Event> void register(Consumer<T> consumer);
    
    <T extends Event> void unregister(Consumer<T> consumer);
    
    <T extends EventListener> void registerStatic(Class<T>... listeners);
    
    <T extends EventListener> void unregisterStatic(Class<T>... listeners);
    
    <T extends EventListener> void registerStatic(Class<T> listeners);
    
    <T extends EventListener> void unregisterStatic(Class<T> listeners);
    
    <T extends Event> void registerStatic(Class<T> eventClass, Consumer<T> consumer);
    
    Map<Class<? extends Event>, List<Consumer<? extends Event>>> getRegisteredEventListeners();
    
    void dispatch(Event e);
}
