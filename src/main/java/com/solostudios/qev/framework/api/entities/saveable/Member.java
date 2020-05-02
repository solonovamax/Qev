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

package com.solostudios.qev.framework.api.entities.saveable;

import com.solostudios.qev.framework.api.actions.Action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public interface Member extends User {
    
    Guild getGuild();
    
    long getGuildId();
    
    String getGuildName();
    
    Collection<Role> getRoles();
    
    long getXp();
    
    long getLevel();
    
    long getXpToNextLevel();
    
    Settings<Member> getMemberSettings();
    
    Object getGuildSetting(String key);
    
    default Action<Member> mute(long length) {
        return mute(length, false, true);
    }
    
    Action<Member> mute(long length, boolean keepRoles, boolean returnRoles);
    
    default Action<User> kick() {
        return kick(true);
    }
    
    Action<User> kick(boolean keepRolesOnReturn);
    
    default Action<BannedMember> ban() {
        return ban(2);
    }
    
    Action<BannedMember> ban(int daysToDelete);
    
    default Action<BannedMember> tempBan(long banLength) {
        return tempBan(banLength, true);
    }
    
    Action<BannedMember> tempBan(long banLength, boolean keepRolesOnReturn);
    
    default boolean hasPermission(Member.PermissionLevel level) {
        return getPermissionLevel().getLevel() >= level.getLevel();
    }
    
    Member.PermissionLevel getPermissionLevel();
    
    enum PermissionLevel {
        /**
         * The Supreme leader of the bot. You can do literally anything.
         */
        BOT_OWNER(9),
        /**
         * An administrator of the bot.
         */
        BOT_ADMIN(8),
        /**
         * A moderator of the bot.
         */
        BOT_MOD(7),
        /**
         * Owner of the relevant server.
         */
        SERVER_OWNER(6),
        /**
         * An admin in the relevant server.
         */
        SERVER_ADMIN(5),
        /**
         * Moderator of the relevant server.
         */
        SERVER_MOD(4),
        /**
         * Third role for the server to manager.
         */
        SERVER_ROLE_3(3),
        /**
         * Second role for the server to manage.
         */
        SERVER_ROLE_2(2),
        /**
         * First role for the server to manage.
         */
        SERVER_ROLE_1(1),
        /**
         * A generic member with no permissions.
         */
        GENERIC_MEMBER(0);
        
        private static final Map<Integer, PermissionLevel> permissionLevels = new HashMap<Integer, PermissionLevel>();
        
        static {
            for (PermissionLevel errorCode : PermissionLevel.values()) {
                permissionLevels.put(errorCode.getLevel(), errorCode);
            }
        }
        
        private final int level;
        
        PermissionLevel(int level) {
            this.level = level;
        }
        
        public static PermissionLevel getErrorCodeByNumber(Integer errorNumber) {
            return permissionLevels.get(errorNumber);
        }
        
        int getLevel() {
            return level;
        }
    }
}
