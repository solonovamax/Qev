package com.solostudios.qev.commands.fun;

import com.solostudios.qev.framework.commands.AbstractCommand;
import com.solostudios.qev.framework.commands.ArgumentContainer;
import com.solostudios.qev.framework.commands.errors.IllegalInputException;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.stream.IntStream;


public class ToBinary extends AbstractCommand {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ToBinary() {
		super("toBin");
		this.setAliases("toBinary", "binaryify");
		this.setCategory("Fun");
		this.setDescription("Takes a string and transforms it into its binary equivalent.");
		this.setArguments(new JSONArray()
								  .put(new JSONObject()
											   .put("key", "toBinary")
											   .put("type", String.class)
											   .put("error", "Not a valid string!")));
	}
	
	@Override
	public void run(@NotNull MessageReceivedEvent event, ArgumentContainer args) throws IllegalInputException {
		String    s = args.getString("toBinary");
		IntStream b = s.chars();
		
		StringJoiner stringJoiner = new StringJoiner(" ");
		
		b.forEach((i) -> {
			stringJoiner.add(Integer.toBinaryString(i));
		});
		
		event.getChannel().sendMessage("Binary String: " + stringJoiner.toString()).queue();
	}
}