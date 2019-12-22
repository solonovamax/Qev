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

package com.solostudios.qev.core.command.handler;

import com.solostudios.qev.core.exceptions.NoMoreArgumentsException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CommandStateMachine extends ListenerAdapter {
	private final long channelID, userID;
	private final JDA                jda;
	private final AbstractCommand    command;
	private       Logger             logger         = LoggerFactory.getLogger(this.getClass());
	private       ArgumentContainer  args;
	private       boolean            deleteMessages = false;
	private       boolean            trace          = false;
	private       ArrayList<Message> messageList    = new ArrayList<>();
	
	public CommandStateMachine(MessageReceivedEvent event, JDA jda, AbstractCommand command) {
		messageList.add(event.getMessage());
		this.command = command;
		logger.info("constructing state machine");
		this.channelID = event.getChannel().getIdLong();
		this.userID = event.getAuthor().getIdLong();
		this.jda = jda;
		args = command.getDefaultArgs();
		jda.addEventListener(this);
		
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		
		Runnable messageTimeOut = () -> {
			logger.debug("UserMessageStateMachine has awaited input for too long and is now exiting.");
			this.destroyStateMachine();
			executor.shutdown();
		};
		executor.schedule(messageTimeOut, 20, TimeUnit.SECONDS);
		
		
		if (command.getArguments() == null || command.fitsArguments(args)) {
			runCommand(event);
		} else {
			event.getChannel().sendMessage(command.nextArgPrompt(args)).queue();
		}
	}
	
	private void destroyStateMachine() {
		logger.info("destroying state machine");
		jda.removeEventListener(this);
		if (this.deleteMessages) {
			for (Message message : messageList) {
				message.getTextChannel().retrieveMessageById(message.getId()).queue(msg -> msg.delete().queue(),
																					error -> {
																					});
			}
		}
	}
	
	private void runCommand(MessageReceivedEvent event) {
		command.prerun(event, args, trace);
		
		
		destroyStateMachine();
	}
	
	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
	public CommandStateMachine withDeleteMessages(boolean deleteMessages) {
		this.deleteMessages = deleteMessages;
		return this;
	}
	
	@SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
	public CommandStateMachine withTrace(boolean trace) {
		this.trace = trace;
		return this;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		if (message.getChannel().getIdLong() == channelID && message.getAuthor().getIdLong() == userID) {
			
			logger.info(args.toString(11));
			
			messageList.add(event.getMessage());
			if (message.getContentStripped().toLowerCase().equals("cancel")) {
				message.getChannel().sendMessage("Canceling.").queue();
				destroyStateMachine();
			} else {
				try {
					command.putNextArg(event, args);
				} catch (IllegalArgumentException e) {
					logger.info("sending message " + e.getMessage());
					event.getChannel().sendMessage(e.getMessage() + " ").queue();
					e.printStackTrace();
				} catch (NoMoreArgumentsException e) {
					if (command.fitsArguments(args)) {
						runCommand(event);
					}
				}
				
				if (command.fitsArguments(args)) {
					runCommand(event);
				} else {
					event.getChannel().sendMessage(command.nextArgPrompt(args)).queue();
				}
			}
		}
	}
}
