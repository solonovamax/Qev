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

package com.solostudios.qev.core.command.handler.abstracts;

import com.solostudios.qev.core.command.handler.old.ArgumentContainer;
import com.solostudios.qev.core.exceptions.IllegalArgumentException;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import com.solostudios.qev.core.main.Qev;
import com.solostudios.qev.core.utility.MessageUtilities;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.entities.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;


@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public abstract class AbstractCommand {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractCommand.class);
	
	private String            name;
	private String[]          aliases           = new String[]{};
	private String            category          = "";
	private JSONArray         arguments         = new JSONArray();
	private String            usage;
	private String            description       = "";
	private boolean           enabled           = true;
	private Permission[]      userPermissions   = new Permission[]{};
	private Permission[]      clientPermissions = new Permission[]{Permission.MESSAGE_WRITE};
	private boolean           ownerOnly;
	private String            example           = "";
	private boolean           nsfw              = false;
	private boolean           guildOnly         = false;
	private ArgumentContainer defaultArgs       = new ArgumentContainer();
	
	
	protected AbstractCommand(String name) {
		this.name = name;
		this.usage = name;
	}
	
	public static ArgumentContainer parseArgs(MessageReceivedEvent event, AbstractCommand command, String[] args) throws
			IllegalArgumentException {
		
		
		JSONArray         arguments = command.getArguments();
		ArgumentContainer temp      = new ArgumentContainer(command.getDefaultArgs());
		
		for (int i = 0, j = 1; j < arguments.length() || i < arguments.length(); i++) {
			
			JSONObject obj         = arguments.getJSONObject(i);
			String     key         = obj.getString("key");
			boolean    skippable   = (obj.has("optional") && obj.getBoolean("optional"));
			boolean    defaultable = obj.has("default");
			
			
			try {
				if (obj.get("type").equals("BannedUser")) {
					User put;
					if ((put = MessageUtilities.getBannedUserFromString(args[j++], event.getGuild())) == null) {
						if (skippable) {
							temp.setNull(key);
							continue;
						}
						if (defaultable) {
							continue;
						}
						throw new java.lang.IllegalArgumentException(obj.getString("error"));
					}
					temp.put(key, put);
					continue;
				} else {
					
					Class clazz = (Class) obj.get("type");
					
					
					Object put;
					
					if (clazz.equals(RoleImpl.class)) {
						if ((put = MessageUtilities.getRoleFromString(args[j++], event.getGuild())) == null) {
							if (skippable) {
								temp.setNull(key);
								continue;
							}
							if (defaultable) {
								continue;
							}
							throw new java.lang.IllegalArgumentException(obj.getString("error"));
						}
						temp.put(key, put);
						continue;
					}
					
					if (clazz.equals(UserImpl.class)) {
						if ((put = MessageUtilities.getUserFromString(args[j++], event.getGuild())) == null) {
							if (skippable) {
								temp.setNull(key);
								continue;
							}
							if (defaultable) {
								continue;
							}
							throw new java.lang.IllegalArgumentException(obj.getString("error"));
						}
						temp.put(key, put);
						continue;
					}
					
					if (clazz.equals(MemberImpl.class)) {
						if ((put = MessageUtilities.getMemberFromString(args[j++], event.getGuild())) == null) {
							if (skippable) {
								temp.setNull(key);
								continue;
							}
							if (defaultable) {
								continue;
							}
							throw new java.lang.IllegalArgumentException(obj.getString("error"));
						}
						temp.put(key, put);
						continue;
					}
					
					if (clazz.equals(TextChannelImpl.class)) {
						if ((put = MessageUtilities.getMemberFromString(args[j++], event.getGuild())) == null) {
							if (skippable) {
								temp.setNull(key);
								continue;
							}
							if (defaultable) {
								continue;
							}
							throw new java.lang.IllegalArgumentException(obj.getString("error"));
						}
						temp.put(key, put);
						continue;
					}
					
					if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
						temp.put(key, parseBoolean(args[j++]));
						continue;
					}
					
					if (clazz.equals(String.class)) {
						StringBuilder sb = new StringBuilder();
						for (; j < args.length; j++) {
							sb.append(args[j]).append(" ");
						}
						if (sb.toString().strip().equals("")) {
							if (skippable) {
								temp.setNull(key);
								continue;
							}
							if (defaultable) {
								continue;
							}
							throw new java.lang.IllegalArgumentException(obj.getString("error"));
						}
						temp.put(key, sb.toString().trim());
						continue;
					}
					
					try {
						if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
							temp.put(key, Integer.parseInt(args[j++]));
							continue;
						}
						if (clazz.equals(double.class) || clazz.equals(Double.class)) {
							temp.put(key, Double.parseDouble(args[j++]));
							continue;
						}
					} catch (NumberFormatException e) {
						if (skippable) {
							temp.setNull(key);
							continue;
						}
						if (defaultable) {
							continue;
						}
						throw new java.lang.IllegalArgumentException(obj.getString("error"));
					}
					
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				if (skippable) {
					temp.setNull(key);
					continue;
				}
				if (defaultable) {
					continue;
				}
				throw new java.lang.IllegalArgumentException(obj.getString("error"));
			}
		}
		
		
		if (command.fitsArguments(temp)) {
			return temp;
		} else {
			throw new IllegalArgumentException("Arguments did not conform after compile");
		}
		
	}
	
	public JSONArray getArguments() {
		return arguments;
	}
	
	public ArgumentContainer getDefaultArgs() {
		return new ArgumentContainer(defaultArgs);
	}
	
	private static boolean parseBoolean(String s) {
		if (s.equals("t") || s.equals("true") || s.equals("yes")) {
			return true;
		}
		if (s.equals("f") || s.equals("false") || s.equals("no")) {
			return false;
		}
		throw new java.lang.IllegalArgumentException();
	}
	
	public boolean fitsArguments(ArgumentContainer args) {
		
		logger.debug("Must conform to \n" + arguments.toString(11));
		
		for (int i = 0; i < arguments.length(); i++) {
			
			JSONObject obj = arguments.getJSONObject(i);

            /*
            try {
                if (obj.has("optional") && obj.getBoolean("optional")) {
                    logger.info("skipping optional arg");
                    continue;
                }
            } catch (JSONException e) {
            }
             */
			
			if (!(args.contains(obj.getString("key")))) {
				return false;
			}
			
			if (obj.get("type").equals("BannedUser")) {
				if (!args.isNull(obj.getString("key")) && args.get(obj.getString("key")).getClass() != UserImpl.class) {
					return false;
				}
			} else {
				if (!args.isNull(obj.getString("key")) &&
					args.get(obj.getString("key")).getClass() != obj.get("type")) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	protected void setArguments(JSONArray arguments) {
		if (verifyArguments(arguments)) {
			this.arguments = arguments;
		} else {
			throw new java.lang.IllegalArgumentException();
		}
	}
	
	private boolean verifyArguments(JSONArray args) {
		if (args.length() < 1) {
			return false;
		}
		
		
		for (int i = 0; i < args.length(); i++) {
			JSONObject arg;
			try {
				arg = args.getJSONObject(i);
			} catch (JSONException e) {
				return false;
			}
			if (!arg.has("key") || !arg.has("type")) {
				return false;
			}
			
			Object c = arg.get("type");
			if (!(c == String.class || c == int.class || c == double.class || c == Role.class || c == Member.class || c == TextChannel.class || c == VoiceChannel.class ||
				  c == GuildChannel.class || c == boolean.class || c.equals("BannedUser"))) {
				return false;
			}
			
			if (c == int.class) {
				args.put(i, arg.put("type", Integer.class));
			}
			if (c == Role.class) {
				args.put(i, arg.put("type", RoleImpl.class));
			}
			if (c == Member.class) {
				args.put(i, arg.put("type", MemberImpl.class));
			}
			if (c == GuildChannel.class) {
				args.put(i, arg.put("type", GuildChannel.class));
			}
			if (c == TextChannel.class) {
				args.put(i, arg.put("type", TextChannelImpl.class));
			}
			if (c == VoiceChannel.class) {
				args.put(i, arg.put("type", VoiceChannelImpl.class));
			}
			if (c == User.class) {
				args.put(i, arg.put("type", UserImpl.class));
			}
			if (c == double.class) {
				args.put(i, arg.put("type", Double.class));
			}
			if (c == boolean.class) {
				args.put(i, arg.put("type", Boolean.class));
			}
			
			if (arg.has("default")) {
				if (arg.get("default").getClass() != c) {
					return false;
				}
				defaultArgs.put(arg.getString("key"), arg.get("default"));
			}
			
			if (arg.has("optional")) {
				try {
					arg.getBoolean("optional");
				} catch (JSONException e) {
					return false;
				}
			}
			
			if (!arg.has("prompt")) {
				arg.put("prompt", "Please input the " + arg.getString("key"));
			}
		}
		return true;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public final boolean isEnabled() {
		return enabled;
	}
	
	public final String getName() {
		return name;
	}
	
	public final String[] getAliases() {
		return aliases;
	}
	
	protected void setAliases(String... aliases) {
		this.aliases = aliases;
	}
	
	public final String getCategory() {
		return category;
	}
	
	protected void setCategory(String category) {
		this.category = category;
	}
	
	public final String getUsage() {
		return usage;
	}
	
	/**
	 * @param usage
	 *
	 * @return instance of calling object.
	 */
	protected void setUsage(String usage) {
		this.usage = usage;
	}

    /* layout of the JSONArray
    [{
        "key": String,
        "type": Clazz, <- Must be integer, double, boolean, role, member or string
        "optional": boolean,
        "default": Object <- Must be of class type
        "error": "Invalid input" <- What it says when there is an error
        "prompt": "Please input an argument" <- What is says when the user need to input something.
     },
     {
        "key": String,
        "type": Clazz, <- Must be integer, double, boolean, role, member or string
        "optional": boolean,
        "default": Object <- Must be of class type
        "error": "Invalid input" <- What it says when there is an error
        "prompt": "Please input an argument" <- What is says when the user need to input something.
     }]
     */
	
	public final String getDescription() {
		return description;
	}
	
	/**
	 * @param description
	 *
	 * @return instance of calling object
	 */
	protected void setDescription(String description) {
		this.description = description;
	}
	
	public String getExample() {
		return example;
	}
	
	protected void setExample(String example) {
		this.example = example;
	}
	
	public void prerun(MessageReceivedEvent event, ArgumentContainer args, boolean trace) {
		
		for (Permission perm : this.getUserPermissions()) {
			if (event.getMember() != null && !event.getMember().hasPermission(perm)) {
				event.getChannel().sendMessage(
						"You require the " + perm.getName() + " permission to use this command.\n" +
						"Please contact the server owner if you think you should have this permission, or contact @solonovamax#6983 if you think this is an error.").queue();
				return;
			}
		}
		
		for (Permission perm : this.getClientPermissions()) {
			event.getGuild().getSelfMember();
			if (!event.getGuild().getSelfMember().hasPermission(perm)) {
				event.getChannel().sendMessage("I require the " + perm.getName() +
											   " permission for this command to work. Please ask the server owner to give me this permission.").queue();
				return;
			}
		}
		
		if (isNsfw() && !event.getTextChannel().isNSFW()) {
			event.getChannel().sendMessage(
					"This channel is not marked as an NSFW channel. You can only use this command in an NSFW channel.").queue();
			return;
		}
		
		if (isGuildOnly() && !event.isFromGuild()) {
			event.getChannel().sendMessage(
					"This command can only be used in a guild. Please enter a guild to use it.").queue();
			return;
		}
		
		if (isOwnerOnly() && !event.getAuthor().getId().equals(Qev.BOT_OWNER)) {
			event.getChannel().sendMessage("This command can only be used by the owner.").queue();
			return;
		}
		try {
			run(event, args);
		} catch (IllegalInputException | IllegalArgumentException e) {
			event.getChannel().sendMessage(e.getMessage()).queue();
			if (trace) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter  printWriter  = new PrintWriter(stringWriter);
				
				e.printStackTrace(printWriter);
				
				event.getChannel().sendMessage(stringWriter.toString()).queue();
			}
		} catch (Exception e) {
			event.getChannel().sendMessage("Error occurred while running the command.\n" + e.getMessage()).queue();
			if (trace) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter  printWriter  = new PrintWriter(stringWriter);
				
				e.printStackTrace(printWriter);
				
				event.getChannel().sendMessage("Stacktrace:" + "```" + stringWriter.toString() + "```").queue();
			}
		}
	}
	
	public Permission[] getUserPermissions() {
		return userPermissions;
	}
	
	public Permission[] getClientPermissions() {
		return clientPermissions;
	}
	
	public boolean isNsfw() {
		return nsfw;
	}
	
	public boolean isGuildOnly() {
		return guildOnly;
	}
	
	public boolean isOwnerOnly() {
		return ownerOnly;
	}
	
	public abstract void run(MessageReceivedEvent event, ArgumentContainer arguments) throws IllegalInputException;
	
	/**
	 * @param ownerOnly
	 *
	 * @return instance of calling object
	 */
	protected void setOwnerOnly(boolean ownerOnly) {
		this.ownerOnly = ownerOnly;
	}
	
	/**
	 * @param clientPermissions
	 *
	 * @return instance of calling object
	 */
	protected void setClientPermissions(Permission... clientPermissions) {
		Permission[] tempPermissions = new Permission[clientPermissions.length + 1];
		tempPermissions[0] = Permission.MESSAGE_WRITE;
		System.arraycopy(clientPermissions, 0, tempPermissions, 1, clientPermissions.length);
		this.clientPermissions = tempPermissions;
	}
	
	/**
	 * @param userPermissions
	 *
	 * @return instance of calling object
	 */
	protected void setUserPermissions(Permission... userPermissions) {
		this.userPermissions = userPermissions;
	}
	
	protected void setIsEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	protected void setIsNSFW(boolean nsfw) {
		this.nsfw = nsfw;
	}
	
	protected void setIsGuildOnly(boolean guildOnly) {
		this.guildOnly = guildOnly;
	}
	
	public String nextArgPrompt(ArgumentContainer args) {
		for (int i = 0; i < arguments.length(); i++) {
			JSONObject obj = arguments.getJSONObject(i);
			
			if (args.contains(obj.getString("key"))) {
				continue;
			}
			try {
				if (obj.has("default")) {
					return obj.getString("prompt") +
						   "\nThis argument has a default. Please say \"null\" to use the default.";
				}
				if (obj.has("optional") && obj.getBoolean("optional")) {
					return obj.getString("prompt") + "\nSay skip if you want to skip this optional argument.";
				}
			} catch (JSONException ignored) {
			}
			
			return obj.getString("prompt");
		}
		
		return "error";
	}
	
	public void putNextArg(MessageReceivedEvent event, ArgumentContainer args) throws
			java.lang.IllegalArgumentException {
		for (int i = 0; i < arguments.length(); i++) {
			
			JSONObject obj = arguments.getJSONObject(i);
			
			if (args.contains(obj.getString("key"))) {
				continue;
			}
			String key = obj.getString("key");
			
			if (event.getMessage().getContentRaw().equals("null") && obj.has("default")) {
				args.put(key, obj.get("default"));
				return;
			}
			
			logger.debug(event.getMessage().getContentRaw());
			
			
			if (event.getMessage().getContentRaw().equals("skip") && obj.has("optional") &&
				obj.getBoolean("optional")) {
				args.setNull(key);
				return;
			}
			
			if (obj.get("type").equals("BannedUser")) {
				User put;
				
				if ((put = MessageUtilities.getBannedUserFromMessage(event, "")) == null) {
					throw new java.lang.IllegalArgumentException();
				}
				args.put(key, put);
				return;
			} else {
				
				Class clazz = (Class) obj.get("type");
				
				logger.debug(clazz.getName());
				
				try {
					Object put;
					if (clazz.equals(RoleImpl.class)) {
						if ((put = MessageUtilities.getRoleFromMessage(event, "")) == null) {
							throw new java.lang.IllegalArgumentException();
						}
						args.put(key, put);
						return;
					}
					if (clazz.equals(UserImpl.class)) {
						if ((put = MessageUtilities.getUserFromMessage(event, "")) == null) {
							throw new java.lang.IllegalArgumentException();
						}
						args.put(key, put);
						return;
					}
					if (clazz.equals(MemberImpl.class)) {
						if ((put = MessageUtilities.getMemberFromMessage(event, "")) == null) {
							throw new java.lang.IllegalArgumentException();
						}
						args.put(key, put);
						return;
					}
					if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
						args.put(key, Integer.parseInt(event.getMessage().getContentRaw()));
						return;
					}
					if (clazz.equals(double.class) || clazz.equals(Double.class)) {
						args.put(key, Double.parseDouble(event.getMessage().getContentRaw()));
						return;
					}
					if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
						args.put(key, parseBoolean(event.getMessage().getContentRaw()));
						return;
					}
					if (clazz.equals(String.class)) {
						args.put(key, event.getMessage().getContentDisplay());
						return;
					}
				} catch (java.lang.IllegalArgumentException ignored) {
				}
			}
			throw new java.lang.IllegalArgumentException(
					(!obj.has("error") ? "Invalid input" : obj.getString("error")));
		}
		throw new java.lang.IllegalArgumentException("No more arguments");
	}
	
}
