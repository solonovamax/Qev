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

package com.solostudios.qev.core.database.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DataContainer extends HashMap<String, Object> {
    public DataContainer() {
        super();
    }
    
    public DataContainer(Map<String, Object> map) {
        super(map);
    }
    
    public <T> T get(final String key, final Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null!");
        }
        return clazz.cast(get(key));
    }
    
    public Integer getInteger(final String key) {
        return (Integer) get(key);
    }
    
    public Object put(String key, Object value) {
        if (value instanceof Saveable) {
            value = ((Saveable) value).toDataContainer();
        }
        return super.put(key, value);
    }
    
    public DataContainer getDataContainer(final String key) {
        return (DataContainer) get(key);
    }
    
    public <T, V> Map<T, V> getMap(final String key) {
        return (Map<T, V>) get(key);
    }
    
    public <T> Set<T> getSet(final String key) {
        return (Set<T>) get(key);
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
}