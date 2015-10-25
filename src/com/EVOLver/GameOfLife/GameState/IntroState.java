package com.EVOLver.GameOfLife.GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.EVOLver.GameOfLife.Main.GamePanel;
import com.EVOLver.GameOfLife.Manager.GameStateManager;
import com.EVOLver.GameOfLife.Manager.Keys;

public class IntroState extends GameState {
	
	// instance variables
	private BufferedImage logo;
	private int alpha;
	private int ticks;

	// class constants
	private static final int FADE_IN = 120;
	private static final int LENGTH = 120;
	private static final int FADE_OUT = 120;
	
	public IntroState(GameStateManager gsm) { super(gsm); }
	
	public void init() {
		ticks = 0;
		try {
			logo = ImageIO.read(getClass().getResourceAsStream("/Logo/logo.png"));
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void update() {
		handleInput();
		ticks++;
		if(ticks < FADE_IN) {
			alpha = (int) (255 - 255 * (1.0 * ticks / FADE_IN));
			if(alpha < 0) alpha = 0;
		}
		if(ticks > FADE_IN + LENGTH) {
			alpha = (int) (255 * (1.0 * ticks - FADE_IN - LENGTH) / FADE_OUT);
			if(alpha > 255) alpha = 255;
		}
		if(ticks > FADE_IN + LENGTH + FADE_OUT)
			gsm.setState(GameStateManager.MENU);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT2);
		g.drawImage(logo, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT2, null);
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT2);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ENTER))
			gsm.setState(GameStateManager.MENU);
	}
}