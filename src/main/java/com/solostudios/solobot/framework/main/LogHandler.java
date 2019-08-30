/*
 *
 * Copyright 2016 2019 solonovamax <solonovamax@12oclockpoint.com>
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
 *
 */

package com.solostudios.solobot.framework.main;

import com.solostudios.solobot.soloBOT;

public class LogHandler {

    private static final String C_RED = "\u001B[31m";
    private static final String C_YELLOW = "\u001B[33m";
    private static final String C_GREEN = "\u001B[32m";
    private static final String C_WHITE = "\u001b[37m";
    private static final String C_RESET = "\u001B[0m";


    /**
     * Used to show a debug message in both the logs and in the console.
     * Should not show unless DEBUG is set to true in the settings.json.
     *
     * @param message Message you want to show.
     */
    public static void debug(String message) {

        if (soloBOT.DEBUG) {
            System.out.println(C_WHITE + "[DEBUG]: " + message + C_RESET);
        } else {
        }

    }


    /**
     * Used to show a debug message in both the logs and in the console.
     * Should not show unless DEBUG is set to true in the settings.json.
     *
     * @param message Message you want to show.
     */
    public static void info(String message) {

        System.out.println(C_GREEN + "[INFO]: " + message + C_RESET);


    }


    /**
     * Used for a
     *
     * @param message Message you want to show.
     */
    public static void warning(String message) {

        System.out.println(C_YELLOW + "[WARNING]: " + message + C_RESET);

    }


    /**
     * Used when an error is thrown that should be displayed in the logs
     *
     * @param message Message you want to show.
     */
    public static void error(String message) {

        System.out.println(C_RED + "[ERROR]: " + message + C_RESET);

    }


    /**
     * Used when a fatal error happens. Will immediately exit after message is printed.
     *
     * @param message Message you want to show.
     */
    public static void fatal(String message) {
        soloBOT.executor.shutdown();
        System.out.println(C_RED + "[FATAL]: " + message + C_RESET);
        System.exit(0);

    }

}
