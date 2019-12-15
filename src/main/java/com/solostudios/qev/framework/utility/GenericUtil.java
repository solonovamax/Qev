package com.solostudios.qev.framework.utility;

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
	
	public static boolean getBoolean(String s) {
		if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("true")) { return true; }
		if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("false")) { return false; }
		
		throw new NullPointerException("Not a boolean");
	}
}
