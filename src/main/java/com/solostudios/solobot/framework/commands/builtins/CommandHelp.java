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

package com.solostudios.solobot.framework.commands.builtins;

import com.solostudios.solobot.framework.commands.AbstractCommand;
import com.solostudios.solobot.framework.commands.CommandHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class CommandHelp extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CommandHelp() {
        super("help");
        this.withAliases("h", "command", "commands");
        this.withCategory("Utility");
        this.withDescription("Returns a list of all commands.\n" +
                "Or, if used with a command name, it will return a message with how to use that command");
        this.withArguments(new JSONArray()
                .put(new JSONObject()
                        .put("key", "command")
                        .put("type", String.class)
                        .put("optional", true)
                        .put("error", "Invalid command!")));
    }

    @Override
    public void run(@NotNull MessageReceivedEvent event, JSONObject args) throws IllegalArgumentException {

        //https://solonovamax.github.io/soloBOT/#commands

        if (!args.has("command")) {
            event.getChannel().sendMessage(new EmbedBuilder()
                    .addField("Info", "Please type `!help {string}` with the name of a command to get info on a specific command. Otherwise, click the link below.", false)
                    .addField("Link to commands", "[commands](https://solonovamax.github.io/soloBOT/#commands)", false)
                    .setColor(Color.BLUE)
                    .build()).queue();
            /*
            logger.debug("Retrieving category list.");

            TreeMap<AbstractCategory, HashMap<String, AbstractCommand>> categoryList = new TreeMap<>(CommandHandler.getCategoryList());

            logger.debug("Generating categories.");
            categoryList.forEach((AbstractCategory abstractCategory, HashMap<String, AbstractCommand> commandList) -> {
                EmbedBuilder category = new EmbedBuilder();
                category.setTitle(abstractCategory.getName())
                        .setColor(abstractCategory.getColor());
                logger.debug("Generating category {}.", abstractCategory.getName());

                StringBuilder cList = new StringBuilder();

                commandList.forEach((String name, AbstractCommand command) -> {

                    logger.debug("Adding command {}.", name);

                    StringBuilder cmd = new StringBuilder();

                    StringBuilder commandName = new StringBuilder()
                            .append(name);
                    for (String alias : command.getAliases()) {
                        commandName.append("/").append(alias);
                    }

                    cmd.append("**`").append(commandName).append("`** ").append(command.getDescription()).append("\n");

                    cList.append(cmd);

                });
                category.addField("Commands:", cList.toString() +
                        "\nType !help {Command Name} to get usage for a specific command.", false);

                message.getChannel().sendMessage(category.build()).queue();

            });
            */
        } else {
            String cmd = args.getString("command");
            AbstractCommand command = CommandHandler.getCommandList().get(cmd);

            if (command == null) {
                event.getChannel().sendMessage("Invalid command name!").queue();
                return;
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
        }

    }
}
