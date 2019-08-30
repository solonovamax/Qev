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
import org.bson.Document;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Exchanger;

public class JobQueue {
    private static Queue<Job<MongoSetOperation>> jobQueue = new PriorityQueue<>(20);

    public JobQueue() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (jobQueue.size() > 0) {
                    Job<MongoSetOperation> job = JobQueue.getNext();
                    job.getPayload().run(job.getuserData(), job.getGuildID(), job.getUserID(), job.getExchanger());
                }
            }
        };

        Timer timer = new Timer();

        timer.schedule(timerTask, 0L, 100L); //call the run() method at 1 second intervals
    }

    public static void add(MongoSetOperation op, MongoCollection<Document> userData, Long guildID, Long userID, Exchanger exchanger) {
        jobQueue.add(new Job<>(op, Job.Priority.MEDIUM, userData, guildID, userID, exchanger));
    }

    public static void add(MongoSetOperation op, MongoCollection<Document> userData, Long guildID, Long userID) {
        jobQueue.add(new Job<>(op, Job.Priority.MEDIUM, userData, guildID, userID, new Exchanger()));
    }

    public static void update(MongoSetOperation op, MongoCollection<Document> userData, Long guildID, Long userID, Exchanger ex) {
        jobQueue.add(new Job<>(op, Job.Priority.HIGH, userData, guildID, userID, ex));
    }

    public static Job<MongoSetOperation> getNext() {
        return jobQueue.remove();
    }

    public static boolean hasNext() {
        return !jobQueue.isEmpty();
    }
}
