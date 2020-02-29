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

package com.solostudios.qev.core.database.entities;

public interface DataEntity {
    
    /**
     * This is the numerical ID of the object with an identifier at the end for different types of objects.
     *
     * @return The ID of the object + _[identifier]
     */
    String getId();
    
    /**
     * This is the raw, un altered id of the object.
     *
     * @return The numerical ID of the object. It will contain an ID resulting from the discord API, or 0.
     */
    long getIdLong();
    
    /**
     * The creation time of the object
     *
     * @return The long timestamp of the object's creation time
     */
    long getTimeCreated();
    
    /**
     * The last time that the object was saved to the database.
     *
     * @return The long timestamp of when the object was last saved to the database. (Doesn't include updates in cache.)
     */
    long lastTimeSaved();
    
    
}
