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

package com.solostudios.solobot.commands.utility;

import com.solostudios.solobot.framework.commands.AbstractCommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestCMD extends AbstractCommand {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public TestCMD() {
        super("test");
        this.withEnabled(false);
        this.withCategory("Utility");
        this.withDescription("Test");
    }

    @Override
    public void run(MessageReceivedEvent event, JSONObject args) throws IllegalArgumentException {
        User author = event.getAuthor();
        event.getChannel().sendMessage(author.getAvatarUrl() + "").queue();
        event.getChannel().sendMessage(author.getDefaultAvatarUrl() + "").queue();
    }
}