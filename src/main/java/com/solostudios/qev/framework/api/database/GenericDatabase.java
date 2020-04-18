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

import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.structure.raw.DataObject;
import com.solostudios.qev.framework.api.database.structure.raw.Table;

import java.util.Map;


public interface GenericDatabase {
    /**
     * Gets a table from the database, based on name.
     *
     * @param name
     *         Name of the table you want to get.
     */
    Table getTable(String name);
    
    /**
     * Used to create a {@link DataObject} from a {@link Map}. Used for saving objects and etc.
     *
     * @param map
     *         {@link Map} used to construct the data object from.
     *
     * @return The new DataObject.
     */
    DataObject createDataObj(Map<String, Object> map);
    
    /**
     * Returns a reference to the client. Useful for bringing together many different subsystems.
     * <p>
     * Yes, I too can pretend I know how to write good code.
     *
     * @return A client. What did you think it returned?
     */
    Client getClient();
}
