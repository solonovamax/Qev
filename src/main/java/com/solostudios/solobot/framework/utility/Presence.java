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

package com.solostudios.solobot.framework.utility;

import com.solostudios.solobot.soloBOT;
import net.dv8tion.jda.core.entities.Game;

public enum Presence {
    helpI(Game.GameType.DEFAULT, "Type !help for help."),
    helpII(Game.GameType.DEFAULT, "Type !help for help."),
    helpIII(Game.GameType.DEFAULT, "Type !help for help."),
    helpIV(Game.GameType.DEFAULT, "Type !help for help."),
    writtenI(Game.GameType.DEFAULT, "Written by solonovamax#3163"),
    writtenII(Game.GameType.DEFAULT, "Written by solonovamax#3163"),
    infoI(Game.GameType.DEFAULT, "Type !info."),
    infoII(Game.GameType.DEFAULT, "Type !info."),
    anime(Game.GameType.WATCHING, "Anime"),
    serverI(Game.GameType.DEFAULT, soloBOT.SUPPORT_SERVER),
    serverII(Game.GameType.DEFAULT, soloBOT.SUPPORT_SERVER),
    videoGames(Game.GameType.DEFAULT, "Video Games"),
    technoblade(Game.GameType.WATCHING, "Technoblade win minecraft mondays."),
    minecraft(Game.GameType.DEFAULT, "Minecraft"),
    urmom(Game.GameType.DEFAULT, "With your mom.");

    private final Game.GameType gameType;   // in kilograms
    private final String action; // in meters

    Presence(Game.GameType gameType, String action) {
        this.gameType = gameType;
        this.action = action;
    }

    public Game.GameType getGameType() {
        return gameType;
    }

    public String getAction() {
        return action;
    }

}