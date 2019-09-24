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

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Main extends JFrame {

    // Declare an instance of the drawing canvas,
    // which is an inner class called DrawCanvas extending javax.swing.JPanel.

    public static final Color darkGrey = new Color(5, 5, 12);
    // Constructor to set up the GUI components and event handlers
    public static final Stroke barThickness = new BasicStroke(2);
    public static final Stroke pFPThickness = new BasicStroke(4);
    public static final Rectangle2D.Double overlay = new Rectangle2D.Double(0, 0, 200, 200);
    public static final Ellipse2D.Double subtract = new Ellipse2D.Double(0, 0, 200, 200);
    // Define constants
    public static final int CANVAS_WIDTH = 1500;
    public static final int CANVAS_HEIGHT = 600;
    public static final int IMG_SIZE = 256;
    public static final int IMG_X = 250;
    public static final int IMG_Y = 100;
    public static final int LEVEL_BAR_X = 250;
    public static final int LEVEL_BAR_Y = 460;
    public static final int LEVEL_BAR_WIDTH = 1150;
    public static final int LEVEL_BAR_HEIGHT = 40;
    public static final int LEVEL_BAR_ARC_SIZE = 20;
    public static final int LEVEL_XP_X = 1100;
    public static final int LEVEL_XP_Y = 487;
    public static final Font xpFont = new Font("Javanese Text", Font.PLAIN, 25);
    public static final int USERNAME_X = 556;
    public static final int USERNAME_Y = 150;
    public static final Font usernameFont = new Font("Javanese Text", Font.PLAIN, 50);
    public static final Font usernameTagFong = new Font("Javanese Text", Font.PLAIN, 25);
    public static final Font positionFont = new Font("Franklin Gothic Medium", Font.PLAIN, 60);
    public static final int POSITION_X = 875;
    public static final int POSITION_Y = 250;
    //public static final Color backgroundPane = new Color(255, 255, 255, 150);
    public static final int PANE_X = 200;
    public static final int PANE_Y = 50;
    public static final int PANE_WIDTH = 1250;
    public static final int PANE_HEIGHT = 500;
    public static final Font levelFont = new Font("Franklin Gothic Medium", Font.PLAIN, 84);
    public static final Font levelTextFont = new Font("Georgia", Font.PLAIN, 48);
    public static final int LEVEL_X = 600;
    public static final int LEVEL_Y = 250;
    public static final Stroke verticalBarThickness = new BasicStroke(6);
    public static final Color pane = new Color(255, 255, 255, 175);
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    public static Area circleCrop = new Area(overlay);
    public static BufferedImage circleBuffer = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_ARGB);
    public static Graphics2D circleBufferGraphics = circleBuffer.createGraphics();
    public static BufferedImage backgroundPane;

    static {
        circleCrop.subtract(new Area(subtract));
    }

    static {
        try {
            backgroundPane = ImageIO.read(new File("src/levelCard.png"));
        } catch (IOException e) {
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

    private DrawCanvas canvas;

    public Main() {
        canvas = new DrawCanvas();    // Construct the drawing canvas
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // Set the Drawing JPanel as the JFrame's content-pane
        Container cp = getContentPane();
        cp.add(canvas);
        // or "setContentPane(canvas);"

        setDefaultCloseOperation(EXIT_ON_CLOSE);   // Handle the CLOSE button
        pack();              // Either pack() the components; or setSize()
        setTitle("Image test");  // "super" JFrame sets the title
        setVisible(true);    // "super" JFrame show
    }
    // The entry main method

    public static BufferedImage makeLevelCard(User user, int xp, int xpRemaining, int userLevel, int userPosition, int memberCount) throws MalformedURLException {
        long startTime = System.currentTimeMillis();

        BufferedImage avatar;
        try {
            avatar = ImageIO.read(new URL("https://cdn.discordapp.com/avatars/" + user.getIdLong() + "/" + user.getAvatarId() + ".png?size=256"));
        } catch (IOException e) {
            throw new MalformedURLException();
        }

        double levelpercent = (double) xp / xpRemaining;

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
        levelCardGraphics.fillRect(LEVEL_BAR_X + 4, LEVEL_BAR_Y + 4, (int) ((LEVEL_BAR_WIDTH - 7) * levelpercent), LEVEL_BAR_HEIGHT - 7);
        levelCardGraphics.setColor(Color.DARK_GRAY);
        levelCardGraphics.drawRect(LEVEL_BAR_X, LEVEL_BAR_Y, LEVEL_BAR_WIDTH, LEVEL_BAR_HEIGHT);

        /*

        levelCardGraphics.setStroke(barThickness);
        levelCardGraphics.setColor(Color.WHITE);
        levelCardGraphics.fillRoundRect(LEVEL_BAR_X, LEVEL_BAR_Y, LEVEL_BAR_WIDTH, LEVEL_BAR_HEIGHT, LEVEL_BAR_ARC_SIZE, LEVEL_BAR_ARC_SIZE);
        levelCardGraphics.setColor(Color.LIGHT_GRAY);
        levelCardGraphics.fillRoundRect(LEVEL_BAR_X, LEVEL_BAR_Y, (int) (LEVEL_BAR_WIDTH * levelpercent), LEVEL_BAR_HEIGHT, LEVEL_BAR_ARC_SIZE, LEVEL_BAR_ARC_SIZE);
        levelCardGraphics.setColor(Color.BLACK);
        levelCardGraphics.drawRoundRect(LEVEL_BAR_X, LEVEL_BAR_Y, LEVEL_BAR_WIDTH, LEVEL_BAR_HEIGHT, LEVEL_BAR_ARC_SIZE, LEVEL_BAR_ARC_SIZE);

         */

        levelCardGraphics.setColor(Color.WHITE);
        levelCardGraphics.setFont(usernameFont);
        FontMetrics metrics = levelCardGraphics.getFontMetrics(usernameFont);
        String username = (metrics.stringWidth(user.getName()) > 900 ? user.getName().substring(0, (int) (((double) 700 / metrics.stringWidth(user.getName())) * user.getName().length())) + ". . ." : user.getName());

        levelCardGraphics.drawString(username, USERNAME_X, USERNAME_Y);
        levelCardGraphics.setColor(Color.GRAY);
        levelCardGraphics.setFont(usernameTagFong);
        levelCardGraphics.drawString(user.getAsTag().replace(user.getName(), ""), USERNAME_X + metrics.stringWidth(username), USERNAME_Y);

        levelCardGraphics.setColor(Color.GRAY);
        levelCardGraphics.setFont(xpFont);
        levelCardGraphics.drawString(format(xp) + "/" + format(xpRemaining), LEVEL_XP_X, LEVEL_XP_Y);

        levelCardGraphics.setColor(Color.GRAY);
        levelCardGraphics.setFont(levelTextFont);
        levelCardGraphics.drawString("Level", LEVEL_X, LEVEL_Y);

        FontMetrics levelMetrics = levelCardGraphics.getFontMetrics(levelFont);
        FontMetrics levelTextMetrics = levelCardGraphics.getFontMetrics(levelTextFont);

        levelCardGraphics.setFont(levelFont);
        levelCardGraphics.drawString(String.valueOf(userLevel), LEVEL_X + ((levelTextMetrics.stringWidth("Level") / 2) - (levelMetrics.stringWidth(String.valueOf(userLevel)) / 2)), LEVEL_Y + (levelMetrics.getHeight()));

        levelCardGraphics.setColor(Color.GRAY);
        levelCardGraphics.setFont(levelTextFont);
        levelCardGraphics.drawString("Rank", POSITION_X, POSITION_Y);

        levelCardGraphics.setFont(levelFont);
        levelCardGraphics.drawString(String.valueOf(userPosition), POSITION_X + ((levelTextMetrics.stringWidth("Rank") / 2) - (levelMetrics.stringWidth(String.valueOf(userPosition)) / 2)), POSITION_Y + (levelMetrics.getHeight()));

        levelCardGraphics.setStroke(verticalBarThickness);
        levelCardGraphics.drawLine(800, 200, 800, 400);

        System.out.println("render time: " + (System.currentTimeMillis() - startTime) + "ms");

        return levelCard;
    }

    public static String format(long value) {
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

    public static void main(String[] args) {
        // Run the GUI codes on the Event-Dispatching thread for thread safety
        // Let the constructor do the job
        SwingUtilities.invokeLater(Main::new);

    }

    /**
     * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
     */
    private class DrawCanvas extends JPanel {

        // Override paintComponent to perform your own painting
        @Override
        public void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            try {
                g2.drawImage(makeLevelCard(new User() {
                    @Nonnull
                    @Override
                    public String getName() {
                        return "solonovamax";
                    }

                    @Nonnull
                    @Override
                    public String getDiscriminator() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getAvatarId() {
                        return "04173d2894c10accbeae99eada7f29c4";
                    }

                    @Nonnull
                    @Override
                    public String getDefaultAvatarId() {
                        return null;
                    }

                    @Nonnull
                    @Override
                    public String getAsTag() {
                        return "solonovamax#3163";
                    }

                    @Override
                    public boolean hasPrivateChannel() {
                        return false;
                    }

                    @Nonnull
                    @Override
                    public RestAction<PrivateChannel> openPrivateChannel() {
                        return null;
                    }

                    @Nonnull
                    @Override
                    public List<Guild> getMutualGuilds() {
                        return null;
                    }

                    @Override
                    public boolean isBot() {
                        return false;
                    }

                    @Nonnull
                    @Override
                    public JDA getJDA() {
                        return null;
                    }

                    @Override
                    public boolean isFake() {
                        return false;
                    }

                    @Nonnull
                    @Override
                    public String getAsMention() {
                        return null;
                    }

                    @Override
                    public long getIdLong() {
                        return 195735703726981120L;
                    }
                }, 4874, 9874, 24, 10, 56), 0, 0, null);
            } catch (MalformedURLException ignored) {
            }
        }

    }
}