package com.solostudios.qev.commands.fun;

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import com.solostudios.qev.framework.utility.GenericUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;


public class ToMorse extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public ToMorse() {
		super("toMorse");
		this.setCategory("Fun");
		this.setDescription("Takes a string and turns it into morse code.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "toMorse")
											   .put("type", String.class)
											   .put("error", "Invalid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		String toMorse = args.getString("toMorse");
		
		StringJoiner sj = new StringJoiner(" ");
		
		for (byte aByte : toMorse.getBytes()) {
			if (GenericUtil.alphabet.contains((char) aByte)) {
				sj.add(GenericUtil.morse.get(GenericUtil.alphabet.indexOf((char) aByte)));
			}
		}
		
		event.getMessage().getChannel().sendMessage(sj.toString()).queue();
	}
}