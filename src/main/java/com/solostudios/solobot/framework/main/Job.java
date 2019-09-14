/*
 *
 * Copyright 2016 2019 solonovamax <solonovamax@12oclockpoint.com>
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
 *
 */

package com.solostudios.solobot.framework.main;

import com.mongodb.client.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Exchanger;

public class Job<T> implements Comparable<Job<T>> {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Exchanger exchanger;
    private MongoCollection<org.bson.Document> userData;
    private Long guildID;
    private Long userID;
    private T payload;
    private Priority priority;

    public Job(T payload, Priority priority, MongoCollection<org.bson.Document> userData, Long guildID, Long userID, Exchanger exchanger) {
        this.exchanger = exchanger;
        this.guildID = guildID;
        this.userID = userID;
        this.userData = userData;
        this.payload = payload;
        this.priority = priority;
    }

    public T getPayload() {
        return payload;
    }

    public Priority getPriority() {
        return priority;
    }

    public int compareTo(Job<T> that) {
        return Integer.compare(that.priority.ordinal(), this.priority.ordinal());
    }

    public Exchanger getExchanger() {
        return exchanger;
    }

    public MongoCollection<org.bson.Document> getuserData() {
        return userData;
    }

    public Long getGuildID() {
        return guildID;
    }

    public Long getUserID() {
        return userID;
    }

    public enum Priority {LOW, MEDIUM, HIGH, HIGHEST}
}