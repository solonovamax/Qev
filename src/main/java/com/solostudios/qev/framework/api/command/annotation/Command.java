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

package com.solostudios.qev.framework.api.command.annotation;

import com.solostudios.qev.framework.api.command.Category;
import net.dv8tion.jda.api.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String name();
    
    String[] aliases() default {};
    
    String description();
    
    String[] examples() default {};
    
    Class<? extends Category>[] category() default {Category.NoCategory.class};
    
    
    boolean nsfw() default false;
    
    boolean guildLocked() default false;
    
    boolean ownerLocked() default false;
    
    boolean botAdminLocked() default false;
    
    Permission[] userPermissions() default {Permission.MESSAGE_READ};
    
    Permission[] botPermissions() default {Permission.MESSAGE_WRITE};
    
    boolean enabled() default true;
}
