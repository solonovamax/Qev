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

package com.solostudios.qev.core.internal.utils;

import com.solostudios.qev.core.api.database.structure.usable.GenericEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;


public class EntityUtil {
    private static final Logger logger       = LoggerFactory.getLogger(GenericEntity.class);
    private static       long   previousTime = 0;
    private static       int    sequence;
    
    /**
     * Method that can generate unique ids. This is thread safe and can be called as many times per second as needed.
     * <p>
     * The ids are generated in a similar method to Twitter's Snowflake-id method.
     * <p>
     * NOTE: if you are using this same method on other computers, then storing those objects in the same database, there may be 2 ids that
     * are the same. This method can only be used if you have a single client.
     *
     * @return Unique snowflake-like id.
     */
    public static synchronized long generateUniqueID() {
        long currentTime = Instant.now().toEpochMilli();
        if (currentTime < previousTime) {
            logger.warn(
                    "System clock is running backwards. This is either because the crystal that your computer is using is not functioning" +
                    " properly, or because you did something bad.");
            throw new IllegalStateException(
                    "System clock is running backwards. This is either because the crystal that your computer is using is not functioning" +
                    " properly, or because you did something bad.");
        }
        if (currentTime == previousTime) {
            sequence = (sequence + 1) & 4095;
            if (sequence == 0) {
                currentTime = waitNextMillis(currentTime);
            }
        } else {
            sequence = 0;
        }
    
        previousTime = currentTime;
    
        return ((currentTime - 1586740912452L) << 12) | sequence;
    }
    
    // Block and wait till next millisecond
    static private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == previousTime) {
            currentTimestamp = Instant.now().toEpochMilli();
        }
        return currentTimestamp;
    }
}