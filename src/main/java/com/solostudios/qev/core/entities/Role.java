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

package com.solostudios.qev.core.entities;

import com.solostudios.qev.core.database.api.DataContainer;
import com.solostudios.qev.core.database.api.Saveable;
import net.dv8tion.jda.api.JDA;

import java.util.HashSet;
import java.util.Set;


public class Role implements Saveable {
    JDA                                jda;
    net.dv8tion.jda.api.entities.Guild jdaGuild;
    long                               id;
    HashSet<Permission>                permissions;
    boolean                            muteRole;
    boolean                            defaultRole;
    
    public boolean isJDAGuild() {
        return jdaGuild != null;
    }
    
    public long getId() {
        return id;
    }
    
    public boolean isDefaultRole() {
        return defaultRole;
    }
    
    public void setDefaultRole(boolean defaultRole) {
        this.defaultRole = defaultRole;
    }
    
    public boolean isMuteRole() {
        return muteRole;
    }
    
    public void setMuteRole(boolean muteRole) {
        this.muteRole = muteRole;
    }
    
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
    
    public void addPermission(Permission permission) {
        permissions.add(permission);
    }
    
    public void takePermission(Permission permission) {
        permissions.remove(permission);
    }
    
    public Set<Permission> getPermissions() {
        return permissions;
    }
    
    @Override
    public DataContainer toDataContainer() {
        DataContainer container = new DataContainer();
        container.put("id", id);
        container.put("permissions", permissions);
        return container;
    }
    
    
    public enum Permission {
        BAN,
        KICK,
        MUTE,
        TEMP_BAN,
        MANAGE_ROLE_PERMISSIONS,
        MANAGE_XP,
        BOT_ADMINISTRATOR
    }
}
