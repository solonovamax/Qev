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


public interface Member extends User {
    
    Guild getGuild();
    
    Collection<Role> getRoles();
    
    default Action<BannedMember> ban() {
        return ban(2);
    }
    
    Action<BannedMember> ban(int daysToDelete);
    
    default Action<BannedMember> tempBan(long banLength) {
        return tempBan(banLength, true);
    }
    
    Action<BannedMember> tempBan(long banLength, boolean keepRolesOnReturn);
    
    default Action<User> kick() {
        return kick(true);
    }
    
    Action<User> kick(boolean keepRolesOnReturn);
    
    default Action<Member> mute(long length) {
        return mute(length, false, true);
    }
    
    Action<Member> mute(long length, boolean keepRoles, boolean returnRoles);
    
    long getGuildId();
    
    long getXp();
    
    long getLevel();
    
    long getRemainingXp();
    
    Settings<Member> getMemberSettings();
    
    Object getGuildSetting(String key);
    
    String getGuildName();
}
