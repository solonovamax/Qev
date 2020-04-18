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

package com.solostudios.qev.framework.api.database;

import com.solostudios.qev.framework.api.database.structure.raw.Table;


public abstract class Database implements GenericDatabase {
    protected final String connectionURL;
    
    public Database(String connectionURL) {
        this.connectionURL = connectionURL;
    }
    
    /**
     * Gets a <strike>collection</strike> table from the database, based on name.
     * <p>
     * I come from a MongoDB background, so I included this function along with the {@link GenericDatabase#getTable(String)} method. This
     * method just calls that method; they are identical.
     *
     * @param name
     *         Name of the collection you want to get.
     */
    public final Table getCollection(String name) {
        return getTable(name);
    }
}
