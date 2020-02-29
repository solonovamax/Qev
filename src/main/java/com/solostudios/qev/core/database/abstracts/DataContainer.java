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

package com.solostudios.qev.core.database.abstracts;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class DataContainer {
    private final Map<String, Object> dataMap;
    
    public DataContainer() {
        dataMap = new LinkedHashMap<String, Object>();
    }
    
    public DataContainer(String key, final Object value) {
        dataMap = new LinkedHashMap<String, Object>();
        dataMap.put(key, value);
    }
    
    public DataContainer(Map<String, Object> map) {
        dataMap = new LinkedHashMap<String, Object>(map);
    }
    
    public DataContainer(Set<Map.Entry<String, Object>> entrySet) {
        dataMap = new LinkedHashMap<>();
        entrySet.forEach((entry) -> {
            dataMap.put(entry.getKey(), entry.getValue());
        });
    }
    
    public DataContainer append(final String key, final Object value) {
        dataMap.put(key, value);
        return this;
    }
    
    public <T> T get(final String key, final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null!");
        }
        return clazz.cast(dataMap.get(key));
    }
    
    public Integer getInteger(final String key) {
        return (Integer) get(key);
    }
    
    public Object get(String key) {
        return dataMap.get(key);
    }
    
    public Long getLong(final String key) {
        return (Long) get(key);
    }
    
    public Double getDouble(final String key) {
        return (Double) get(key);
    }
    
    public String getString(final String key) {
        return (String) get(key);
    }
    
    public Boolean getBoolean(final String key) {
        return (Boolean) get(key);
    }
    
    public boolean containsValue(final Object value) {
        return dataMap.containsValue(value);
    }
    
    public boolean containsKey(final Object key) {
        return dataMap.containsKey(key);
    }
    
}