package com.solostudios.qev.core.utility;

import com.solostudios.qev.Qev;
import net.dv8tion.jda.api.JDA;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


/**
 * Contains a bunch or generic utility items.
 * <p>
 * Methods, variables, etc.
 */
public class GenericUtil {
	/**
	 * Alphabet used for the morse code translator
	 */
	public static final List<Character> alphabet = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
																 'x', 'y', 'z', ' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
	/**
	 * Array that contains the translated items for the morse code alphabet. This should be paired with the previous array, for decoding and encoding morse.
	 */
	public static final List<String>    morse    = Arrays.asList(".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.",
																 "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", "|", ".----", "..---", "...--", "....-",
																 ".....", "-....", "--...", "---..", "----.", "-----");
	
	public static int getUserCount() {
		int userCount = 0;
		
		for (JDA shard : Qev.shardList) {
			userCount += shard.getUsers().size();
		}
		
		return userCount;
	}
	
	public static int getGuildCount() {
		int guildCount = 0;
		
		for (JDA shard : Qev.shardList) {
			guildCount += shard.getGuilds().size();
		}
		
		return guildCount;
	}
	
	@NotNull
	public static String getWithPlural(int x, String name) {
		if (x == 1) {
			return x + " " + name;
		} else {
			return x + " " + name + "s";
		}
	}
}
