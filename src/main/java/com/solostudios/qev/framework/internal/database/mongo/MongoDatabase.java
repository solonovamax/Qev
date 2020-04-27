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

package com.solostudios.qev.framework.internal.database.mongo;

import com.google.common.base.Charsets;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.solostudios.qev.framework.api.Client;
import com.solostudios.qev.framework.api.database.DataObject;
import com.solostudios.qev.framework.api.database.Database;
import com.solostudios.qev.framework.api.database.Table;

import java.net.URLEncoder;
import java.util.Map;


public class MongoDatabase implements Database {
    private final MongoClient client;
    
    public MongoDatabase(String ip, int port, String username, String password) {
        client = MongoClients.create(MongoClientSettings.builder()
                                                        .retryWrites(true)
                                                        .retryReads(true)
                                                        .applyConnectionString(
                                                                new ConnectionString("mongodb://" +
                                                                                     URLEncoder.encode(username,
                                                                                                       Charsets.UTF_8) + ":" +
                                                                                     URLEncoder.encode(password,
                                                                                                       Charsets.UTF_8) + "@" +
                                                                                     ip + ":" + port + "/Bot?authSource=admin")).build());
    }
    
    @Override
    public Table getTable(String name) {
        return null;
    }
    
    @Override
    public DataObject createDataObj(Map<String, Object> map) {
        return null;
    }
    
    @Override
    public Client getClient() {
        return null;
    }
}
