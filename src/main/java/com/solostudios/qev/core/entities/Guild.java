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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class Guild implements Saveable {
    private final JDA                                jda;
    private final long                               id;
    private final net.dv8tion.jda.api.entities.Guild jdaGuild;
    private final Map<Long, User>                    userMap;
    private final Map<Long, Role>                    roleMap;
    private       String                             prefix;
    
    private Guild(JDA jda, net.dv8tion.jda.api.entities.Guild guild, Long id, DataContainer container) {
        this.jda = jda;
        this.id = id;
        this.jdaGuild = guild;
        
        this.userMap = new HashMap<>();
        this.roleMap = new HashMap<>();
        
        container.<Long, DataContainer>getMap("users").forEach((userID, userData) -> {
        
        });
    }
    
    private Guild(JDA jda, Long id) {
        this.jda = jda;
        this.id = id;
        
        this.jdaGuild = jda.getGuildById(id);
        this.userMap = new HashMap<>();
        this.roleMap = new HashMap<>();
    }
    
    public static Guild guildFromDataContainer(JDA jda, Long id, DataContainer container) {
        net.dv8tion.jda.api.entities.Guild jdaGuild = jda.getGuildById(id);
        Guild                              guild    = new Guild(jda, jdaGuild, id, container);
        return guild;
    }
    
    public static Guild guildFromDataContainer(JDA jda, net.dv8tion.jda.api.entities.Guild jdaGuild, Long id,
                                               DataContainer container) {
        Guild guild = new Guild(jda, jdaGuild, id, container);
        return guild;
    }
    
    public static Guild initNewGuild(JDA jda, Long id) {
        Guild guild = new Guild(jda, id);
        
        return guild;
    }
    
    @Override
    public DataContainer toDataContainer() {
        DataContainer container = new DataContainer();
        container.put("id", id);
        container.put("prefix", prefix);
        container.put("users", userMap);
        container.put("roles", roleMap);
        return container;
    }
    
    public boolean isJDAGuild() {
        return jdaGuild != null;
    }
    
    public long getId() {
        return id;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public Map<Long, Role> getRoles() {
        return roleMap;
    }
    
    public Map<Long, User> getUsers() {
        return userMap;
    }
    
    public CompletableFuture<User> getUser(Long userID) {
        CompletableFuture<User> userFuture = new CompletableFuture<>();
        userFuture.completeAsync(() -> {
            if (!userMap.containsKey(id)) {
                userMap.put(id, new User(jda, this, userID));
            }
            return userMap.get(userID);
        });
        return userFuture;
    }
}
