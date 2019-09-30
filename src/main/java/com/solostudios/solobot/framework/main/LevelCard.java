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

import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class LevelCard {

    private static final Stroke barThickness = new BasicStroke(2);
    private static final Stroke pFPThickness = new BasicStroke(4);

    private static final Rectangle2D.Double overlay = new Rectangle2D.Double(0, 0, 200, 200);
    private static final Ellipse2D.Double subtract = new Ellipse2D.Double(0, 0, 200, 200);
    // Define constants
    private static final int CANVAS_WIDTH = 1500;
    private static final int CANVAS_HEIGHT = 600;
    private static final int IMG_SIZE = 256;
    private static final int IMG_X = 250;
    private static final int IMG_Y = 100;
    private static final int LEVEL_BAR_X = 250;
    private static final int LEVEL_BAR_Y = 460;
    private static final int LEVEL_BAR_WIDTH = 1150;
    private static final int LEVEL_BAR_HEIGHT = 40;
    private static final int LEVEL_XP_X = 1100;
    private static final int LEVEL_XP_Y = 487;
    private static final Font xpFont = new Font("Javanese Text", Font.BOLD, 25);
    private static final int USERNAME_X = 556;
    private static final int USERNAME_Y = 150;
    private static final Font usernameFont = new Font("Javanese Text", Font.PLAIN, 64);
    private static final Font usernameTagFong = new Font("Javanese Text", Font.PLAIN, 40);
    private static final int POSITION_X = 875;
    private static final int POSITION_Y = 250;
    //public static final Color backgroundPane = new Color(255, 255, 255, 150);
    private static final int PANE_X = 200;
    private static final int PANE_Y = 50;
    private static final int PANE_WIDTH = 1250;
    private static final int PANE_HEIGHT = 500;
    private static final Font levelFont = new Font("Franklin Gothic Medium", Font.PLAIN, 84);
    private static final Font levelTextFont = new Font("Georgia", Font.PLAIN, 48);
    private static final int LEVEL_X = 600;
    private static final int LEVEL_Y = 250;
    private static final Stroke verticalBarThickness = new BasicStroke(6);
    private static final Color pane = new Color(255, 255, 255, 175);
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private static Area circleCrop = new Area(overlay);
    private static BufferedImage circleBuffer = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_ARGB);
    private static Graphics2D circleBufferGraphics = circleBuffer.createGraphics();
    private static BufferedImage backgroundPane;

    static {
        circleCrop.subtract(new Area(subtract));
    }

    static {
        try {
            backgroundPane = ImageIO.read(new File("levelCard.png"));
        } catch (IOException ignored) {
        }
        circleBufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        circleBufferGraphics.setClip(new Ellipse2D.Float(0, 0, IMG_SIZE, IMG_SIZE));
    }

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static BufferedImage makeLevelCard(User user, int xp, int xpRemaining, int userLevel, int userPosition, int memberCount) throws MalformedURLException {
        long startTime = System.currentTimeMillis();

        BufferedImage avatar;
        try {
            avatar = ImageIO.read(new URL("https://cdn.discordapp.com/avatars/" + user.getIdLong() + "/" + user.getAvatarId() + ".png?size=256"));
        } catch (IOException e) {
            throw new MalformedURLException();
        }

        double levelPercent = (double) xp / xpRemaining;

        BufferedImage levelCard = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D levelCardGraphics = levelCard.createGraphics();

        circleBufferGraphics.drawImage(avatar, 0, 0, IMG_SIZE, IMG_SIZE, null); // Adding user image to circlecrop graphics.

        levelCardGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        levelCardGraphics.drawImage(backgroundPane, 0, 0, null);

        levelCardGraphics.setColor(pane);
        levelCardGraphics.fillRect(PANE_X, PANE_Y, PANE_WIDTH, PANE_HEIGHT);


        levelCardGraphics.drawImage(circleBuffer, IMG_X, IMG_Y, null);


        levelCardGraphics.setStroke(pFPThickness);
        levelCardGraphics.setColor(Color.BLACK);
        levelCardGraphics.drawOval(IMG_X, IMG_Y, IMG_SIZE, IMG_SIZE);

        levelCardGraphics.setStroke(barThickness);
        levelCardGraphics.setColor(Color.WHITE);
        levelCardGraphics.fillRect(LEVEL_BAR_X, LEVEL_BAR_Y, LEVEL_BAR_WIDTH, LEVEL_BAR_HEIGHT);
        levelCardGraphics.setColor(Color.LIGHT_GRAY);
        levelCardGraphics.fillRect(LEVEL_BAR_X + 4, LEVEL_BAR_Y + 4, (int) ((LEVEL_BAR_WIDTH - 7) * levelPercent), LEVEL_BAR_HEIGHT - 7);
        levelCardGraphics.setColor(Color.DARK_GRAY);
        levelCardGraphics.drawRect(LEVEL_BAR_X, LEVEL_BAR_Y, LEVEL_BAR_WIDTH, LEVEL_BAR_HEIGHT);


        levelCardGraphics.setColor(Color.BLACK);
        levelCardGraphics.setFont(usernameFont);
        FontMetrics metrics = levelCardGraphics.getFontMetrics(usernameFont);
        String username = (metrics.stringWidth(user.getName()) > 900 ? user.getName().substring(0, (int) (((double) 700 / metrics.stringWidth(user.getName())) * user.getName().length())) + ". . ." : user.getName());

        levelCardGraphics.drawString(username, USERNAME_X, USERNAME_Y);
        levelCardGraphics.setColor(Color.GRAY);
        levelCardGraphics.setFont(usernameTagFong);
        levelCardGraphics.drawString(user.getAsTag().replace(user.getName(), ""), USERNAME_X + metrics.stringWidth(username), USERNAME_Y);

        levelCardGraphics.setColor(Color.DARK_GRAY);
        levelCardGraphics.setFont(xpFont);
        levelCardGraphics.drawString(format(xp) + "/" + format(xpRemaining), LEVEL_XP_X, LEVEL_XP_Y);

        levelCardGraphics.setColor(Color.DARK_GRAY);
        levelCardGraphics.setFont(levelTextFont);
        levelCardGraphics.drawString("Level", LEVEL_X, LEVEL_Y);

        FontMetrics levelMetrics = levelCardGraphics.getFontMetrics(levelFont);
        FontMetrics levelTextMetrics = levelCardGraphics.getFontMetrics(levelTextFont);

        levelCardGraphics.setFont(levelFont);
        levelCardGraphics.drawString(String.valueOf(userLevel), LEVEL_X + ((levelTextMetrics.stringWidth("Level") / 2) - (levelMetrics.stringWidth(String.valueOf(userLevel)) / 2)), LEVEL_Y + (levelMetrics.getHeight()));

        levelCardGraphics.setColor(Color.DARK_GRAY);
        levelCardGraphics.setFont(levelTextFont);
        levelCardGraphics.drawString("Rank", POSITION_X, POSITION_Y);

        levelCardGraphics.setFont(levelFont);
        levelCardGraphics.drawString(String.valueOf(userPosition), POSITION_X + ((levelTextMetrics.stringWidth("Rank") / 2) - (levelMetrics.stringWidth(String.valueOf(userPosition)) / 2)), POSITION_Y + (levelMetrics.getHeight()));

        levelCardGraphics.setStroke(verticalBarThickness);
        levelCardGraphics.drawLine(800, 200, 800, 400);

        System.out.println("render time: " + (System.currentTimeMillis() - startTime) + "ms");

        return levelCard;
    }

    private static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }
}
