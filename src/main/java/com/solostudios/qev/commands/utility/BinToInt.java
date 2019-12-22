package com.solostudios.qev.commands.utility;

import com.solostudios.qev.core.command.handler.AbstractCommand;
import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BinToInt extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public BinToInt() {
		super("BinToInt");
		this.setCategory("Utility");
		this.setAliases("bin", "bin-int");
		this.setDescription("Takes a binary number and transforms it into an integer.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "bin")
											   .put("type", String.class)
											   .put("error", "This is not a valid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		String hex = args.getString("bin");
		try {
			int decimal = Integer.parseInt(hex, 2);
			event.getChannel().sendMessage("Integer: " + decimal).queue();
		} catch (NumberFormatException e) {
			throw new IllegalInputException("This is not a valid binary string!");
		}
	}
}