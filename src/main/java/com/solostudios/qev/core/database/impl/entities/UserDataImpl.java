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

package com.solostudios.qev.core.database.impl.entities;

import com.solostudios.qev.core.database.api.UserData;

import java.util.LinkedHashMap;
import java.util.Map;


public class UserDataImpl implements UserData {
    private final long                id;
    private final long                timeCreated;
    private final Map<String, Object> configMap = new LinkedHashMap<>();
    private       int                 rawXp;
    private       long                lastMessageTime;
    
    public UserDataImpl(long id, long timeCreated) {
        this.id = id;
        this.timeCreated = timeCreated;
    }
    
    @Override
    public String getId() {
        return id + "_U";
    }
    
    @Override
    public long getIdLong() {
        return id;
    }
    
    @Override
    public long getTimeCreated() {
        return timeCreated;
    }
    
    @Override public long lastTimeSaved() {
        return 0;
    }
}
