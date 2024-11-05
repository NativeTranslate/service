package net.fedustria.nativetranslate.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static java.awt.RenderingHints.*;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente) Created on: 9/25/2024 7:48 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

/**
 * Utility class for generating images with various graphical effects.
 */
public class ImageGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ImageGenerator.class);
    private static final Random rand = new Random();
    private static final int shadowSize = 5;
    private static final int width = 200;
    private static final int height = 200;
    private static Font font;

    static {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/RedHatDisplay-Black.ttf")).deriveFont(115f);
        } catch (final Exception e) {
            LOG.warn("Failed to load font", e);
            font = new Font("Arial", Font.PLAIN, 115);
        }
    }
    
    /**
     * Generates a random pastel color.
     *
     * @return a Color object representing a random pastel color
     */
    private static Color getRandomPastelColor() {
        final float hue = rand.nextFloat();
        final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
        final float luminance = 0.9f;
        return Color.getHSBColor(hue, saturation, luminance);
    }

    /**
     * Adds an inner shadow effect to a given RoundRectangle2D shape.
     *
     * @param g2d  the Graphics2D context to draw on
     * @param rect the RoundRectangle2D shape to add the inner shadow to
     */
    private static void addInnerShadow(final Graphics2D g2d, final RoundRectangle2D rect) {
        final Rectangle2D bounds = rect.getBounds2D();

        for (int i = 0; i < shadowSize; i++) {
            final float alpha = 0.1f * (shadowSize - i);
            g2d.setColor(new Color(0, 0, 0, (int) (alpha * 255)));
            g2d.setStroke(new BasicStroke(i));

            g2d.draw(new RoundRectangle2D.Float(
                    (float) (bounds.getX() * i),
                    (float) (bounds.getY() * i),
                    (float) (bounds.getWidth() - i),
                    (float) (bounds.getHeight() - i),
                    (float) (rect.getArcWidth() * i),
                    (float) (rect.getArcHeight() * i)
            ));
        }
    }

    /**
     * Adds a glow border effect to a given RoundRectangle2D shape.
     *
     * @param g2d  the Graphics2D context to draw on
     * @param rect the RoundRectangle2D shape to add the glow border to
     */
    private static void addGlowBorder(final Graphics2D g2d, final RoundRectangle2D rect) {
        final int glowSize = 4;
        final Color glowColor = new Color(255, 255, 255, 255);

        for (int i = 0; i < glowSize; i++) {
            final float alpha = 0.1f * (glowSize - i);
            g2d.setColor(new Color(
                    glowColor.getRed(),
                    glowColor.getGreen(),
                    glowColor.getBlue(),
                    (int) (alpha * 255)
            ));
            g2d.setStroke(new BasicStroke(i));

            final Rectangle2D bounds = rect.getBounds2D();
            g2d.draw(new RoundRectangle2D.Float(
                    -i,
                    -i,
                    (float) (bounds.getWidth() + 2 * i),
                    (float) (bounds.getHeight() + 2 * i),
                    (float) (rect.getArcWidth() + 2 * i),
                    (float) (rect.getArcHeight() + 2 * i)
            ));
        }
    }

    /**
     * Generates an image with a given letter and applies various graphical effects.
     *
     * @param letter the letter to be drawn in the image
     */
    public static void generateImage(final String path, final char letter) throws IOException {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
        g2d.fillRect(0, 0, width, height);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        final RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(0, 0, width, height, 0, 0);

        final Color color1 = getRandomPastelColor();
        final Color color2 = getRandomPastelColor();
        final GradientPaint gp = new GradientPaint(0, 0, color1, width, height, color2);

        g2d.setPaint(gp);
        g2d.fill(roundedRectangle);

        addGlowBorder(g2d, roundedRectangle);

        addInnerShadow(g2d, roundedRectangle);

        g2d.setFont(font);

        final var strLetter = String.valueOf(letter);
        final FontMetrics fm = g2d.getFontMetrics();
        final int x = (width - fm.stringWidth(strLetter)) / 2;
        final int y = ((height - fm.getHeight()) / 2) + fm.getAscent();

        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.drawString(strLetter, x + 2, y + 2);

        g2d.setColor(Color.WHITE);
        g2d.drawString(strLetter, x, y);

        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.drawString(strLetter, x - 1, y - 1);

        g2d.dispose();

        ImageIO.write(bufferedImage, "png", new File(path));
    }
}