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

package com.solostudios.qev.core.command.builtins;

import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.command.handler.CommandHandler;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCategory;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.TreeMap;


public class CommandHelp extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public CommandHelp() {
		super("help");
		this.setAliases("h", "command", "commands");
		this.setCategory("Utility");
		this.setDescription("Returns a list of all commands.\n" +
							"Or, if used with a command name, it will return a message with how to use that command");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "command")
											   .put("type", String.class)
											   .put("optional", true)
											   .put("error", "Invalid command!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		
		//https://solonovamax.github.io/qev/#commands
		
		String cmd;
		
		logger.info(args.toString(11));
		
		try {
			cmd = args.getString("command").trim();
			
			if (cmd.equals("all")) {
				logger.debug("Retrieving category list.");
				TreeMap<AbstractCategory, HashMap<String, AbstractCommand>> categoryList =
						new TreeMap<>(CommandHandler.getCategoryList());
				logger.debug("Generating categories.");
				
				ArrayList<EmbedBuilder> categoryEmbedList = new ArrayList<>(categoryList.size());
				
				categoryList.forEach(
						(AbstractCategory abstractCategory, HashMap<String, AbstractCommand> commandList) -> {
							EmbedBuilder category = new EmbedBuilder();
							category.setTitle(abstractCategory.getName())
									.setColor(abstractCategory.getColor());
							logger.debug("Generating category {}.", abstractCategory.getName());
							StringBuilder cList = new StringBuilder();
							commandList.forEach((String name, AbstractCommand command) -> {
								if (command.isOwnerOnly()) {
									return;
								}
								logger.debug("Adding command {}.", name);
								StringBuilder _cmd = new StringBuilder();
								StringJoiner commandName = new StringJoiner("/", "", "")
										.add(name);
								for (String alias : command.getAliases()) {
									commandName.add(alias);
								}
								_cmd.append("**`").append(commandName.toString().replace("`", "")).append(
										"`** ").append(
										command.getDescription()).append("\n");
								cList.append(_cmd);
							});
							category.addField("Commands:", cList.toString(),
											  false)
									.setFooter("Type !help {Command Name} to get usage for a specific command.");
							categoryEmbedList.add(category);
						});
				event.getAuthor().openPrivateChannel().queue((ch) -> {
					Boolean exit = Boolean.TRUE;
					ch.sendMessage("Command List:").queue((message) -> {
						for (EmbedBuilder category : categoryEmbedList) {
							ch.sendMessage(category.build()).queue();
						}
					}, (throwable) -> event.getChannel().sendMessage(
							"I cannot message you. Please enabled your DMs if you want to be able to use this feature.\n" +
							"If you think this is an error, please contact @solonovamax#6983.").queue());
				}, (throwable -> event.getChannel().sendMessage("You must enable DMs to use this feature.").queue()));
				return;
			}
			
			logger.info(cmd);
			AbstractCommand command = CommandHandler.getExecutedCommandList().get(cmd);
			
			if (command == null) {
				throw new IllegalInputException("Invalid command name!");
			}
			EmbedBuilder help = new EmbedBuilder()
					.setTitle(command.getName())
					.addField("Category:", command.getCategory(), false)
					.addField("Description:", command.getDescription(), false)
					.addField("Usage:", command.getUsage(), false);
			if (command.getAliases().length > 0) {
				StringBuilder aliases = new StringBuilder();
				for (String s : command.getAliases()) {
					aliases.append("\n").append(s);
				}
				help.addField("Aliases:", aliases.toString(), false);
			}
			
			event.getChannel().sendMessage(help.build()).queue();
		} catch (NullPointerException e) {
			logger.warn(e.getMessage());
			event.getChannel().sendMessage(new EmbedBuilder()
												   .addField("Info",
															 "Please type `!help {string}` with the name of a command or group to get info on a specific command. " +
															 "If you want info for all commands, you can either use `!help all`, or you can click the link below.",
															 false)
												   .addField("Link to commands",
															 "[commands](https://solonovamax.github.io/Qev/#commands)",
															 false)
												   .setColor(Color.BLUE)
												   .build()).queue();
		}
		
	}
}
