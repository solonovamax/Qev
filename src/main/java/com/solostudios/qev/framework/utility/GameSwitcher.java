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

package com.solostudios.qev.framework.utility;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;


public class GameSwitcher extends Thread {
	private JDA shard;
	
	public GameSwitcher(JDA shard) {
		this.shard = shard;
	}
	
	@Override
	public void run() {
		int      index = (int) Math.round(Math.random() * (Presence.values().length - 1));
		Presence p     = Presence.values()[index];
		
		shard.getPresence().setActivity(Activity.of(p.getGameType(), p.getAction()));
	}
}


