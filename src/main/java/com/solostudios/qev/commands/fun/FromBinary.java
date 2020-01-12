package com.solostudios.qev.commands.fun;

import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.command.handler.old.ArgumentContainer;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FromBinary extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public FromBinary() {
		super("frombin");
		this.setAliases("unBinaryify", "fromBinary");
		this.setCategory("Fun");
		this.setDescription("Takes a binary string and transforms it into the original string.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "unBin")
											   .put("type", String.class)
											   .put("error", "Not a valid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		try {
			String[] hex = args.getString("unBin").split(" ");
			
			StringBuilder str = new StringBuilder();
			for (String s : hex) {
				int decimal = Integer.parseInt(s, 2);
				str.append((char) decimal);
			}
			
			event.getChannel().sendMessage("Original String: " + str.toString().replace("@everyone", "@ everyone")).queue();
		} catch (NumberFormatException e) {
			throw new IllegalInputException("This is not a valid binary string!");
		}
	}
}