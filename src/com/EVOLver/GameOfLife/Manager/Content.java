package com.EVOLver.GameOfLife.Manager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Content {

	// class constants
	public static final BufferedImage[][] MENUBG = load("/HUD/menuscreen.png", 128, 144);
	public static final BufferedImage[][] BAR = load("/HUD/bar.png", 128, 16);
	public static final BufferedImage[][] PLAYER = load("/Sprites/commander.png", 16, 16);
	public static final BufferedImage[][] SMART_TV = load("/Sprites/smart_tv.png", 16, 16);
	public static final BufferedImage[][] CONTAINER = load("/Sprites/container.png", 16, 16);
	public static final BufferedImage[][] SPARKLE = load("/Sprites/sparkle.png", 16, 16);
	public static final BufferedImage[][] ITEMS = load("/Sprites/items.gif", 16, 16);
	public static final BufferedImage[][] FONT = load("/HUD/font.png", 8, 8);
	public static final BufferedImage[][] CURSOR = load("/Logo/logo_cursor.png", 16, 16);

	// load image
	public static BufferedImage[][] load(String dir, int tileWidth, int tileHeight) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(dir));
			int width = spritesheet.getWidth() / tileWidth;
			int height = spritesheet.getHeight() / tileHeight;
			ret = new BufferedImage[height][width];
			// store sprite sheet/tile set
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++)
					ret[i][j] = spritesheet.getSubimage(j * tileWidth, i * tileHeight, tileWidth, tileHeight);
			} return ret;
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		} return null;
	}

	public static void drawString(Graphics2D g, String s, int x, int y) {
		s = s.toUpperCase();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == 47) c = 36; // slash
			if(c == 58) c = 37; // colon
			if(c == 32) c = 38; // space
			if(c >= 65 && c <= 90) c -= 65; // letters
			if(c >= 48 && c <= 57) c -= 22; // numbers
			int row = c / FONT[0].length;
			int col = c % FONT[0].length;
			g.drawImage(FONT[row][col], x + 8 * i, y, null);
		}
	}

}
