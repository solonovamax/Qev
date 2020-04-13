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

public class EntityUtil {
    private static long previosTime = 0;
    private static int  sequence;
    
    /**
     * Method that can generate unique ids. This is
     *
     * @return
     */
    public static synchronized long generateUniqueID() {
        long currentTime = System.currentTimeMillis();
        if (currentTime < previosTime) {
            throw new IllegalStateException("bruh");
        }
        if (currentTime == previosTime) {
            sequence = (sequence + 1) & 16;
            if (sequence == 0) {
                currentTime = waitNextMillisecond(currentTime);
            }
        } else {
            sequence = 0;
        }
        
        previosTime = currentTime;
        
        return (currentTime << 48) | sequence;
    }
    
    private static long waitNextMillisecond(long currentTime) {
        while (currentTime == previosTime) {
            currentTime = System.currentTimeMillis();
        }
        return currentTime;
    }
}