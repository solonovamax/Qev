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

package com.solostudios.solobot.framework.commands.Errors;

public class ArgumentError extends RuntimeException {
    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 0;

    /**
     * Constructs a JSONException with an explanatory message.
     *
     * @param message Detail about the reason for the exception.
     */
    public ArgumentError(final String message) {
        super(message);
    }

    /**
     * Constructs a JSONException with an explanatory message and cause.
     *
     * @param message Detail about the reason for the exception.
     * @param cause   The cause.
     */
    public ArgumentError(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new JSONException with the specified cause.
     *
     * @param cause The cause.
     */
    public ArgumentError(final Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
