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

package com.solostudios.qev.core.api;

import com.solostudios.qev.core.database.api.Database;
import net.dv8tion.jda.api.JDA;


//TODO: rename client to an interesting name (whatever I decide to name the project)
public interface Client {
    void init();
    
    /*
    Possibly remove this? It might not be needed to have a status return.
    Maybe switch out to event-based system where you can only get the status though the events.
     */
    Status getStatus();
    
    
    JDA getJDA();
    
    Database getDatabase();
    
    long getDatabasePing();
    
    long getDiscordHeartbeat();
    
    long getDiscordRestPing();
    
    
    enum Status {
        INITIALIZING(true),
        LOADING_DATABASES(true),
        LOGGING_IN(true),
        REGISTERING_COMMANDS(true),
        FINALIZING_INIT(true),
        RUNNING,
        SHUTTING_DOWN,
        HANDLING_CRITICAL_ERROR;
        
        /*
        The remaining part of the enum code was taken from JDA and is under the Apache V2 License.
        (I mean, it's such a small snippet that realistically nobody will care, but better safe than sorry.)
        
        Copyright 2015-2019 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors.
        You can get a copy of the license at http://www.apache.org/licenses/LICENSE-2.0.
         */
        private final boolean isInit;
        
        Status(boolean isInit) {
            this.isInit = isInit;
        }
        
        Status() {
            this.isInit = false;
        }
        
        public boolean isInit() {
            return isInit;
        }
        /*
        - - - Everything after this is my own code and is under GPLv3 - - -
         */
    }
    
}
