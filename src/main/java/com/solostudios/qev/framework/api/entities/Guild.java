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

package com.solostudios.qev.framework.api.entities;

import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.structure.raw.DataObject;
import com.solostudios.qev.framework.api.database.structure.usable.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class Guild extends Entity<GuildManager, Guild> {
    public static Map<String, Object> INIT_DATA;
    static {
        INIT_DATA = new HashMap<>();
        INIT_DATA.put("channelConfig", new ArrayList<>());
    }
    private final UserManager                        userManager;
    private final RoleManager                        roleManager;
    private final net.dv8tion.jda.api.entities.Guild jdaGuild;
    private       long                               id;
    
    
    protected Guild(DataObject object, GuildManager manager, UserManager userManager, RoleManager roleManager, Client client) {
        super(manager, object, client);
        this.userManager = userManager;
        this.roleManager = roleManager;
        this.jdaGuild = client.getJDA().getGuildById(object.getId());
    }
    
    public net.dv8tion.jda.api.entities.Guild getJdaGuild() {
        return jdaGuild;
    }
    
    public UserManager getUserManager() {
        return userManager;
    }
    
    @Override
    public final void forceSave() {
        manager.save(this);
    }
    
    public RoleManager getRoleManager() {
        return roleManager;
    }
    
    @Override
    public long getIdLong() {
        return id;
    }
    
    @Override
    public DataObject toDataObject() {
        return null;
    }
    
    /*
    @Override
    public Guild fromDataObject(DataObject data) {
        throw new UnsupportedOperationException(
                "You must use Guild#fromDataObject(DataObject, Map)! A Guild always requires a config map!");
    }
    
    @Override
    public Guild fromDataObject(DataObject data, Map<String, Object> config) {
        GuildManager guildManager = (GuildManager) config.get("guildManager");
        UserManager  userManager  = (UserManager) config.get("userManager");
        RoleManager  roleManager  = (RoleManager) config.get("roleManager");
        Objects.requireNonNull(guildManager);
        Objects.requireNonNull(userManager);
        Objects.requireNonNull(roleManager);
        
        
        return new Guild(data, guildManager, userManager, roleManager);
    }
     */
}
