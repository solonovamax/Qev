package com.solostudios.qev.commands.utility;

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IntToHex extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public IntToHex() {
		super("IntToHex");
		this.setCategory("Utility");
		this.setDescription("Takes an integer and transforms it into a hexadecimal number.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "int")
											   .put("type", Integer.class)
											   .put("error", "")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		int toHex = args.getInt("int");
		event.getChannel().sendMessage("Integer: " + Integer.toHexString(toHex)).queue();
	}
}