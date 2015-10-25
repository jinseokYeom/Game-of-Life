package com.EVOLver.GameOfLife.HUD;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import com.EVOLver.GameOfLife.Entity.Container;
import com.EVOLver.GameOfLife.Entity.Player;
import com.EVOLver.GameOfLife.Main.GamePanel;
import com.EVOLver.GameOfLife.Manager.Content;

public class Hud {
	// instance variables
	private int yoffset;
	private BufferedImage bar;
	private BufferedImage container;
	private BufferedImage boat;
	private BufferedImage axe;
	private Player player;
	private int numContainers;
	private Font font;
	private Color textColor;
	// class constant
	private static final int FPS = 60;

	public Hud(Player p, ArrayList<Container> c) {
		player = p;
		numContainers = c.size();
		yoffset = GamePanel.HEIGHT;
		bar = Content.BAR[0][0];
		container = Content.CONTAINER[0][0];
		boat = Content.ITEMS[0][0];
		axe = Content.ITEMS[0][1];
		font = new Font("Arial", Font.PLAIN, 10);
		textColor = new Color(47, 64, 126);
	}

	public void draw(Graphics2D g) {
		// draw hud
		g.drawImage(bar, 0, yoffset, null);
		// draw Container bar
		g.setColor(textColor);
		g.fillRect(8, yoffset + 6, (int)(28.0 * player.amountMoney() / numContainers), 4);
		// draw Container amount
		g.setColor(textColor);
		g.setFont(font);
		String s = player.amountMoney() + "/" + numContainers;
		Content.drawString(g, s, 40, yoffset + 3);
		if(player.amountMoney() >= 10) g.drawImage(container, 80, yoffset, null);
		else g.drawImage(container, 72, yoffset - 1, null);

//		 draw items
//		 change it to player status
//		if(player.hasCar()) g.drawImage(boat, 100, yoffset, null);
//		if(player.hasAxe()) g.drawImage(axe, 112, yoffset, null);

		// draw time
		int minutes = (int) (player.getTicks() / 1800);
		int seconds = (int) ((player.getTicks() / FPS) % 60);
		if(minutes < 10) {
			if(seconds < 10) Content.drawString(g, "0" + minutes + ":0" + seconds, 85, 3);
			else Content.drawString(g, "0" + minutes + ":" + seconds, 85, 3);
		}
		else {
			if(seconds < 10) Content.drawString(g, minutes + ":0" + seconds, 85, 3);
			else Content.drawString(g, minutes + ":" + seconds, 85, 3);
		}
	}
}
