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

package com.solostudios.qev.core.database.abstracts;

public abstract class AbstractDatabase<S, T> {
	protected String connectionURL;
	protected S      client;
	
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}
	
	public final S openConnection() {
		return client = connect();
	}
	
	protected abstract S connect();
	
	protected abstract DataContainer convertDataFormat(T data);
	
	public abstract void get(long guildID, long userID, DatabaseGetOperation operation);
	
	public abstract void set(long guildID, long userID, DatabaseGetOperation operation);
	
	public abstract void get(long guildID, DatabaseGetOperation operation);
	
	public abstract void set(long guildID, DatabaseGetOperation operation);
}