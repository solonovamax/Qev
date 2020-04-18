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

package com.solostudios.qev.framework.api.database.structure.raw;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public interface DataObject {
    Long getId();
    
    Set<Map.Entry<String, Object>> keySet();
    
    Object get(String key);
    
    Long getLong(String key);
    
    Double getDouble(String key);
    
    Integer getInt(String key);
    
    Collection<Object> getCollection(String key);
    
    <T> Collection<T> getCollection(String key, Class<T> clazz);
    
    <T> Collection<T> getCollection(String key, T obj);
    
    Map<String, Object> getMap(String key);
    
    <T> Map<String, T> getMap(String key, Class<T> clazz);
    
    <T> Map<String, T> getMap(String key, T obj);
}
