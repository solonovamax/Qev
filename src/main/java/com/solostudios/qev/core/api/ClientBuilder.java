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

import com.solostudios.qev.core.internal.clients.AutoShardedClient;
import net.dv8tion.jda.api.JDABuilder;

import java.util.Map;


public class ClientBuilder {
    private final Class<? extends Client> type;
    private final JDABuilder              jdaBuilder;
    private       Map<String, Object>     configMap;
    private       String                  token;
    private       int                     cacheTimeout = 300;
    private       int                     shards;
    
    private ClientBuilder(String token, Class<? extends Client> type, Map<String, Object> configMap) {
        this(token, type);
        this.configMap = configMap;
    }
    
    private ClientBuilder(String token, Class<? extends Client> type) {
        jdaBuilder = new JDABuilder(token);
        this.type = type;
        this.token = token;
    }
    
    public static ClientBuilder autoShardedClient(String token) {
        return new ClientBuilder(token, AutoShardedClient.class);
    }
    
    public static ClientBuilder shardedClient(String token, int shards) {
        return new ClientBuilder(token, AutoShardedClient.class).setShards(shards);
    }
    
    public ClientBuilder setShards(int shards) {
        this.shards = shards;
        return this;
    }
    
    public ClientBuilder setCacheTimeout(int seconds) {
        this.cacheTimeout = seconds;
        return this;
    }
    
    public ClientBuilder setToken(String token) {
        this.token = token;
        return this;
    }
    
    public JDABuilder configJDABuilder() {
        return jdaBuilder;
    }
    
    public Client build() {
        if (type == AutoShardedClient.class) {
        }
        return null;
    }
}
