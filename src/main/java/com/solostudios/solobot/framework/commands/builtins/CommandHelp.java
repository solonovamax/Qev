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

import com.solostudios.solobot.abstracts.AbstractCategory;
import com.solostudios.solobot.abstracts.AbstractCommand;
import com.solostudios.solobot.framework.commands.CommandHandler;
import com.solostudios.solobot.main.LogHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.TreeMap;

public class CommandHelp extends AbstractCommand {

    public CommandHelp() {
        super("help",
                "Utility",
                "Returns a list of all commands. \n" +
                        "Or, if used with a command name, it will return a message with how to use that command",
                "help \n" +
                        "help {cmd}",
                true,
                "h", "commands", "command");
    }

    @Override
    public void run(MessageReceivedEvent event, Message message, String[] args) throws IllegalArgumentException {

        if (args.length < 2) {
            LogHandler.debug("Retrieving category list.");

            TreeMap<AbstractCategory, HashMap<String, AbstractCommand>> categoryList = new TreeMap<>(CommandHandler.getCategoryList());

            LogHandler.debug("Generating categories.");
            categoryList.forEach((AbstractCategory abstractCategory, HashMap<String, AbstractCommand> commandList) -> {
                EmbedBuilder category = new EmbedBuilder();
                category.setTitle(abstractCategory.getName())
                        .setColor(abstractCategory.getColor());
                LogHandler.debug("Generating category " + abstractCategory.getName() + ".");

                StringBuilder cList = new StringBuilder();

                commandList.forEach((String name, AbstractCommand command) -> {

                    LogHandler.debug("Adding command " + name + ".");

                    StringBuilder cmd = new StringBuilder();

                    StringBuilder description = new StringBuilder()
                            .append(command.getDescription());

                    StringBuilder commandName = new StringBuilder()
                            .append(name);
                    for (String alias : command.getAliases()) {
                        commandName.append("/").append(alias);
                    }

                    cmd.append("**`").append(commandName).append("`** ").append(description).append("\n");

                    cList.append(cmd);

                });
                category.addField("Commands:", cList.toString() +
                        "\nType !help {Command Name} to get usage for a specific command.", false);

                message.getChannel().sendMessage(category.build()).queue();

            });
        } else {
            String cmd = args[1];
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
