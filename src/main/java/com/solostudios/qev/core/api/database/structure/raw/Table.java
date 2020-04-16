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

package com.solostudios.qev.core.api.database.structure.raw;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;


public interface Table extends Iterable<DataObject> {
    DataObject get(long id);
    
    @NotNull
    @Override
    default Iterator<DataObject> iterator() {
        return getData().iterator();
    }
    
    Collection<DataObject> getData();
}
