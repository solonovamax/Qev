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

package com.solostudios.qev.core.utility;

import com.solostudios.qev.core.main.Qev;
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
	
	public static boolean getBoolean(String s) {
		if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y") || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("t")) { return true; }
		if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("n") || s.equalsIgnoreCase("false") || s.equalsIgnoreCase("f")) { return false; }
		
		throw new NullPointerException("Not a boolean");
	}
}
