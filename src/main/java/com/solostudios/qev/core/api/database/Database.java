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

package com.solostudios.qev.core.api.database;

public interface Database {
    void setConnectionURL(String connectionURL);
    
    /**
     * Initialization phase for the database. This is always called before anything else.
     */
    void init();
    
    /**
     * Opens the connection to the database.
     */
    void openConnection();
    
    /**
     * Gets a <strike>collection</strike> table from the database, based on name.
     * <p>
     * I come from a MongoDB background, so I included this function along with the {@link Database#getTable(String)}
     * method. This method just calls that method; they are identical.
     *
     * @param name
     *         Name of the collection you want to get.
     */
    default void getCollection(String name) {
        getTable(name);
    }
    
    /**
     * Gets a table from the database, based on name.
     *
     * @param name
     *         Name of the table you want to get.
     */
    void getTable(String name);
}
