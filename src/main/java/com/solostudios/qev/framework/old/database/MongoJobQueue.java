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

package com.solostudios.qev.framework.old.database;

import com.mongodb.client.MongoCollection;
import com.solostudios.qev.framework.old.main.Job;
import com.solostudios.qev.framework.old.main.Qev;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("WeakerAccess")
public class MongoJobQueue {
	private static final Queue<Job<MongoSetOperation>> jobQueue = new PriorityQueue<>(20);
	private final        Logger                        logger   = LoggerFactory.getLogger(this.getClass());
	
	Qev qev;
	
	@SuppressWarnings("WeakerAccess")
	public MongoJobQueue(Qev qev) {
		this.qev = qev;
		
		Runnable task = () -> {
			try {
				if (jobQueue.size() > 0) {
					Job<MongoSetOperation> job = MongoJobQueue.getNext();
					job.getPayload().run(job.getuserData(), job.getGuildID(), job.getUserID(), job.getExchanger());
				}
			} catch (Exception e) {
				logger.error(e.getClass().getName(), e);
			}
		};
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(task, 0, 10, TimeUnit.MILLISECONDS);
	}
	
	private static Job<MongoSetOperation> getNext() {
		return jobQueue.remove();
	}
	
	public static void add(MongoSetOperation op, MongoCollection<Document> userData, Long guildID, Long userID,
						   Exchanger exchanger) {
		jobQueue.add(new Job<>(op, Job.Priority.MEDIUM, userData, guildID, userID, exchanger));
	}
	
	public static void update(MongoSetOperation op, MongoCollection<Document> userData, Long guildID, Long userID,
							  Exchanger ex) {
		jobQueue.add(new Job<>(op, Job.Priority.HIGH, userData, guildID, userID, ex));
	}
	
	public static boolean hasNext() {
		return !jobQueue.isEmpty();
	}
}
