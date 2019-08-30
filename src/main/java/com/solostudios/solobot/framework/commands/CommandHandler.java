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

package com.solostudios.solobot.framework.commands;

import com.solostudios.solobot.abstracts.AbstractCategory;
import com.solostudios.solobot.abstracts.AbstractCommand;
import com.solostudios.solobot.framework.main.LogHandler;
import com.solostudios.solobot.soloBOT;
import javassist.bytecode.DuplicateMemberException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandHandler {

    @NotNull
    public static HashMap<String, AbstractCommand> executedCommandList = new HashMap<>();
    @NotNull
    public static HashMap<String, AbstractCommand> commandList = new HashMap<>();
    @NotNull
    public static HashMap<AbstractCategory, HashMap<String, AbstractCommand>> categoryList = new HashMap<>();

    public CommandHandler() {

        LogHandler.info("Adding Categories.");

        Reflections categories = new Reflections("com.solostudios.solobot.categories");
        Set<Class<? extends AbstractCategory>> Categories = categories.getSubTypesOf(AbstractCategory.class);

        for (Class<? extends AbstractCategory> category : Categories) {
            try {
                LogHandler.debug("Attempting to add " + category.getName() + ".");

                if (Modifier.isAbstract(category.getModifiers())) {
                    LogHandler.debug("Rejecting " + category.getName() + ", as it is an abstract class.");
                    continue;
                }

                AbstractCategory c = category.getConstructor().newInstance();

                LogHandler.debug("Adding " + category.getName() + ".");
                categoryList.put(c, new HashMap<>());
                LogHandler.info("Command " + category.getName() + " successfully added.");

            } catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }


        LogHandler.info("Adding built in commands.");

        Reflections builtins = new Reflections("com.solostudios.solobot.framework.commands.builtins");
        Set<Class<? extends AbstractCommand>> builtIns = builtins.getSubTypesOf(AbstractCommand.class);

        for (Class<? extends AbstractCommand> command : builtIns) {
            try {
                LogHandler.debug("Attempting to add " + command.getName() + ".");

                if (Modifier.isAbstract(command.getModifiers())) {
                    LogHandler.debug("Rejecting " + command.getName() + ", as it is an abstract class.");
                    continue;
                }

                AbstractCommand c = command.getConstructor().newInstance();

                if (!c.isEnabled()) {
                    LogHandler.info("Rejecting " + command.getName() + ", as it is not enabled");
                    continue;
                }

                LogHandler.debug("Adding " + command.getName() + ".");
                addCommand(c, c.getName(), c.getAliases());
                LogHandler.info("Command " + command.getName() + " successfully added.");

            } catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (DuplicateMemberException e) {
                LogHandler.warning("Rejecting " + command.getName() + ", as there is already a command with that name or alias in the command list.\n" +
                        "Please either change the command name/alias, or change the name/alias of the other command.");
            }
        }


        LogHandler.info("Adding user commands");

        Reflections reflections = new Reflections("com.solostudios.solobot.commands");
        Set<Class<? extends AbstractCommand>> commandClasses = reflections.getSubTypesOf(AbstractCommand.class);

        for (Class<? extends AbstractCommand> command : commandClasses) {
            try {
                LogHandler.debug("Attempting to add " + command.getName() + ".");

                if (Modifier.isAbstract(command.getModifiers())) {
                    LogHandler.debug("Rejecting " + command.getName() + ", as it is an abstract class.");
                    continue;
                }

                AbstractCommand c = command.getConstructor().newInstance();

                if (!c.isEnabled()) {
                    LogHandler.info("Rejecting " + command.getName() + ", as it is not enabled");
                    continue;
                }

                LogHandler.debug("Adding " + command.getName() + ".");
                addCommand(c, c.getName(), c.getAliases());
                LogHandler.info("Command " + command.getName() + " successfully added.");

            } catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            } catch (DuplicateMemberException e) {
                LogHandler.warning("Rejecting " + command.getName() + ", as there is already a command with that name or alias in the command list.\n" +
                        "Please either change the command name/alias, or change the name/alias of the other command.");
            }
        }


    }


    public static void parseMessage(@NotNull MessageReceivedEvent event, @NotNull Message msg, String[] args) {


        AbstractCommand command = executedCommandList.get(args[0]);

        Thread commandThread = new Thread(() -> {
            if (command != null) {
                LogHandler.debug("Message contains valid command. Attempting to run " + command.getName());
                try {
                    if (event.getGuild().getMemberById(event.getJDA().getSelfUser().getId()).hasPermission(event.getTextChannel(), command.getPermissions())) {
                        command.run(event, msg, args);
                        LogHandler.debug(command.getName() + " command run properly.");
                    } else {
                        event.getChannel().sendMessage("Insufficient permissions.\n" +
                                "I require the " + command.getPermissions().getName() + " permission to run this command.").queue();
                    }
                } catch (IllegalArgumentException e) {
                    LogHandler.debug("Command syntax is illegal! Returning usage message.");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        soloBOT.threadPool.execute(commandThread);


    }

    private static void addCommand(@NotNull AbstractCommand command, String name, @NotNull String... aliases) throws DuplicateMemberException {

        if (executedCommandList.get(name) != null) {
            throw new DuplicateMemberException("");
        }


        for (Map.Entry<AbstractCategory, HashMap<String, AbstractCommand>> category : categoryList.entrySet()) {
            AbstractCategory categoryMap = category.getKey();
            String commandCategory = command.getCategory();

            if (commandCategory.equalsIgnoreCase(categoryMap.getName())) {
                HashMap<String, AbstractCommand> temp;
                if (categoryList.get(categoryMap) == null) {
                    temp = new HashMap<>();
                } else {
                    temp = categoryList.get(categoryMap);
                }
                temp.put(name, command);
                categoryList.replace(categoryMap, temp);
            }
        }

        commandList.put(name, command);
        executedCommandList.put(name, command);
        for (String alias : aliases) {
            executedCommandList.put(alias, command);
        }
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
