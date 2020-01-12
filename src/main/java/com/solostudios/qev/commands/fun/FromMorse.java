package com.solostudios.qev.commands.fun;

import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.command.handler.old.ArgumentContainer;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import com.solostudios.qev.core.utility.GenericUtil;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;


public class FromMorse extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public FromMorse() {
		super("fromMorse");
		this.setCategory("Fun");
		this.setDescription("Translates morse code into normal text");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "morse")
											   .put("type", String.class)
											   .put("error", "Invalid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		String[] morse = args.getString("morse").split(" ");
		
		StringJoiner sj = new StringJoiner("");
		
		for (String s : morse) {
			if (GenericUtil.morse.contains(s)) {
				sj.add(GenericUtil.alphabet.get(GenericUtil.morse.indexOf(s)).toString());
			}
		}
		
		event.getMessage().getChannel().sendMessage(sj.toString()).queue();
	}
}