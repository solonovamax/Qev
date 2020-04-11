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

package com.solostudios.qev.core.internal;

import com.solostudios.qev.core.api.Client;
import com.solostudios.qev.core.database.api.Database;
import net.dv8tion.jda.api.JDA;


public class AutoShardedClient implements Client {
    @Override
    public void init() {
    
    }
    
    @Override
    public Status getStatus() {
        return null;
    }
    
    @Override
    public JDA getJDA() {
        return null;
    }
    
    @Override
    public Database getDatabase() {
        return null;
    }
    
    @Override
    public long getDatabasePing() {
        return 0;
    }
    
    @Override
    public long getDiscordHeartbeat() {
        return 0;
    }
    
    @Override
    public long getDiscordRestPing() {
        return 0;
    }
    
    //TODO: code here
}
