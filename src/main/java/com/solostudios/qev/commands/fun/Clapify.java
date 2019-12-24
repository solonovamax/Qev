package com.solostudios.qev.commands.fun;

import com.solostudios.qev.core.command.handler.ArgumentContainer;
import com.solostudios.qev.core.command.handler.abstracts.AbstractCommand;
import com.solostudios.qev.core.exceptions.IllegalInputException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Clapify extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Clapify() {
		super("clapify");
		this.setCategory("clap");
		this.setDescription("Makes:clap:a:clap:message:clap:look:clap:like:clap:this.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "toClap")
											   .put("type", String.class)
											   .put("error", "Invalid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		event.getChannel().sendMessage(args.getString("toClap").replace(" ", ":clap:")).queue();
	}
}