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

import com.solostudios.qev.core.command.handler.abstracts.AbstractCategory;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.main.Qev;
import javassist.bytecode.DuplicateMemberException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;


public class CommandHandler {
	private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
	
	@SuppressWarnings("WeakerAccess")
	@NotNull
	public static HashMap<String, AbstractCommand>                            executedCommandList = new HashMap<>();
	@SuppressWarnings("WeakerAccess")
	@NotNull
	public static HashMap<String, AbstractCommand>                            commandList         = new HashMap<>();
	@SuppressWarnings("WeakerAccess")
	@NotNull
	public static HashMap<AbstractCategory, HashMap<String, AbstractCommand>> categoryList        = new HashMap<>();
	
	public CommandHandler() {
		
		logger.info("Registering Categories.");
		
		Reflections                            categories = new Reflections("com.solostudios.qev.categories");
		Set<Class<? extends AbstractCategory>> Categories = categories.getSubTypesOf(AbstractCategory.class);
		
		for (Class<? extends AbstractCategory> category : Categories) {
			try {
				logger.debug("Attempting to register {}.", category.getName());
				
				if (Modifier.isAbstract(category.getModifiers())) {
					logger.debug("Rejecting {}, as it is an abstract class.", category.getName());
					continue;
				}
				
				AbstractCategory c = category.getConstructor().newInstance();
				
				logger.debug("Adding {}.", category.getName());
				categoryList.put(c, new HashMap<>());
				logger.info("Category {} registered.", category.getName());
				
			} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		
		logger.info("Successfully registered categories.");
		
		logger.info("Registering built in commands.");
		
		Reflections builtins =
				new Reflections("com.solostudios.qev.framework.commands.builtins");
		Set<Class<? extends AbstractCommand>> builtIns = builtins.getSubTypesOf(AbstractCommand.class);
		
		for (Class<? extends AbstractCommand> command : builtIns) {
			try {
				logger.debug("Attempting to add {}.", command.getName());
				
				if (Modifier.isAbstract(command.getModifiers())) {
					logger.debug("Rejecting {}, as it is an abstract class.", command.getName());
					continue;
				}
				
				AbstractCommand c = command.getConstructor().newInstance();
				
				if (!c.isEnabled()) {
					logger.info("Rejecting {}, as it is not enabled", command.getName());
					continue;
				}
				
				logger.debug("Adding {}.", command.getName());
				addCommand(c, c.getName(), c.getAliases());
				logger.debug("Command {} successfully added.", command.getName());
				
			} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			} catch (DuplicateMemberException e) {
				logger.warn(
						"Rejecting {}, as there is already a command with that name or alias in the command list.\n" +
						"Please either change the command name/alias, or change the name/alias of the other command.",
						command.getName());
			}
		}
		
		logger.info("Successfully registered built in commands.");
		
		logger.info("Registering user commands.");
		
		Reflections                           reflections    = new Reflections("com.solostudios.qev.commands");
		Set<Class<? extends AbstractCommand>> commandClasses = reflections.getSubTypesOf(AbstractCommand.class);
		
		for (Class<? extends AbstractCommand> command : commandClasses) {
			try {
				logger.debug("Attempting to add {}.", command.getName());
				
				if (Modifier.isAbstract(command.getModifiers())) {
					logger.debug("Rejecting {}, as it is an abstract class.", command.getName());
					continue;
				}
				
				AbstractCommand c = command.getConstructor().newInstance();
				
				if (!c.isEnabled()) {
					logger.warn("Rejecting {}, as it is not enabled", command.getName());
					continue;
				}
				
				logger.debug("Adding {}.", command.getName());
				addCommand(c, c.getName(), c.getAliases());
				logger.debug("Command {} successfully added.", command.getName());
				
			} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			} catch (DuplicateMemberException e) {
				logger.warn(
						"Rejecting {}, as there is already a command with that name or alias in the command list.\n" +
						"Please either change the command name/alias, or change the name/alias of the other command. name: {}",
						command.getName(), e.getMessage());
			}
		}
		
		logger.info("Successfully registered user commands.");
		
		
	}
	
	private static void addCommand(@NotNull AbstractCommand command, String name, @NotNull String... aliases) throws DuplicateMemberException {
		String   _name    = name.toLowerCase();
		String[] _aliases = new String[aliases.length];
		for (int i = 0; i < aliases.length; i++) {
			_aliases[i] = aliases[i].toLowerCase();
		}
		
		if (executedCommandList.containsKey(_name)) {
			throw new DuplicateMemberException(executedCommandList.get(_name).getClass().getCanonicalName());
		}
		
		
		for (Map.Entry<AbstractCategory, HashMap<String, AbstractCommand>> category : categoryList.entrySet()) {
			AbstractCategory categoryMap     = category.getKey();
			String           commandCategory = command.getCategory();
			
			if (commandCategory.equalsIgnoreCase(categoryMap.getName())) {
				HashMap<String, AbstractCommand> temp;
				if (categoryList.get(categoryMap) == null) {
					temp = new HashMap<>();
				} else {
					temp = categoryList.get(categoryMap);
				}
				temp.put(_name, command);
				categoryList.replace(categoryMap, temp);
			}
		}
		
		commandList.put(_name, command);
		executedCommandList.put(_name, command);
		for (String alias : _aliases) {
			executedCommandList.put(alias, command);
		}
	}
	
	public static void parseMessage(@NotNull MessageReceivedEvent event, @NotNull Message msg, String[] args) {
		
		
		AbstractCommand command = executedCommandList.get(args[0]);
		
		Thread commandThread = new Thread(() -> {
			if (command != null) {
				logger.debug("Message contains valid command. Attempting to run {}", command.getName());
				/*
				try {
				 */
				for (Permission permission : command.getClientPermissions()) {
					if (!Objects.requireNonNull(
							event.getGuild().getMemberById(event.getJDA().getSelfUser().getId())).hasPermission(
							event.getTextChannel(), permission)) {
						event.getChannel().sendMessage("Insufficient permissions.\n" +
													   "I require the " + permission.getName() +
													   " permission to run this command.").queue();
						return;
					}
				}
				
				for (Permission permission : command.getUserPermissions()) {
					if (!Objects.requireNonNull(
							event.getGuild().getMemberById(msg.getAuthor().getId())).hasPermission(
							event.getTextChannel(), permission)) {
						event.getChannel().sendMessage("Insufficient permissions.\n" +
													   "You must have the " + permission.getName() +
													   " permission to run this command.").queue();
						return;
					}
				}
				
				ArrayList<String> tmp   = new ArrayList<>();
				boolean           hide  = false;
				boolean           trace = false;
				boolean           debug = false;
				
				for (String arg : args) {
					switch (arg) {
						case "-hide":
							hide = true;
							continue;
						case "-trace":
							trace = true;
							continue;
						case "-debug":
							debug = true;
							continue;
						default:
					}
					tmp.add(arg);
				}
				tmp.trimToSize();
				
				if (hide && !event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
					event.getChannel().sendMessage(
							"I cannot delete your message, as I do not have the delete messages permission.").queue();
				} else {
					try {
						command.prerun(event, AbstractCommand.parseArgs(event, command, tmp.toArray(
								new String[tmp.size() - 1])), trace);
						if (hide) {
							event.getTextChannel().retrieveMessageById(event.getMessage().getId()).queue(
									m -> m.delete().queue(),
									error -> {
									});
						}
					} catch (IllegalArgumentException e) {
						event.getChannel().sendMessage(e.getMessage() + " ").queue();
						
						CommandStateMachine argumentWaiter =
								new CommandStateMachine(event, event.getJDA(), command);
						
						if (hide) {
							argumentWaiter.withDeleteMessages(true);
						}
						if (trace) {
							argumentWaiter.withTrace(true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					
					/*
					logger.debug("{} command run properly.", command.getName());
				} catch (java.lang.IllegalArgumentException e) {
					e.printStackTrace();
					logger.debug("Command syntax is illegal! Returning usage message.");
					StringBuilder aliases = new StringBuilder();
					for (String alias : command.getAliases()) {
						aliases.append("\n `").append(alias).append("`");
					}
					
					msg.getChannel().sendMessage(new EmbedBuilder()
														 .setTitle("Incorrect syntax!")
														 .addField(command.getName().toUpperCase(), "", false)
														 .addField("Usage:", "`" + command.getUsage() + "`", false)
														 .addField("Aliases: `", aliases.toString() + "`", false)
														 .setColor(0x0084ff)
														 .build())
					   .queue();
				}
					 */
			}
		});
		
		Qev.commandThreadPool.execute(commandThread);
		
		
	}
	
	@NotNull
	public static HashMap<String, AbstractCommand> getCommandList() {
		return commandList;
	}
	
	@NotNull
	public static HashMap<String, AbstractCommand> getExecutedCommandList() {
		return executedCommandList;
	}
	
	@NotNull
	public static HashMap<AbstractCategory, HashMap<String, AbstractCommand>> getCategoryList() {
		return categoryList;
	}
	
}
