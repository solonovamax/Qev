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

package com.solostudios.qev.core.command.handler.abstracts;

import org.jetbrains.annotations.NotNull;

import java.awt.*;


public abstract class AbstractCategory implements Comparable {
	private final Color  color;
	private final String name;
	
	public AbstractCategory(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public int compareTo(@NotNull Object o) {
		if (o instanceof AbstractCategory) {
			return this.getName().compareTo(((AbstractCategory) o).getName());
		}
		return this.getName().compareTo(o.getClass().getName());
	}
	
	public String getName() {
		return name;
	}
}
