package com.EVOLver.GameOfLife.GameState;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.EVOLver.GameOfLife.Manager.Content;
import com.EVOLver.GameOfLife.Manager.GameStateManager;
import com.EVOLver.GameOfLife.Manager.JukeBox;
import com.EVOLver.GameOfLife.Manager.Keys;

public class MenuState extends GameState {
	
	// instance variables
	private BufferedImage bg;
	private BufferedImage cursor;
	
	// class constants
	private int currentOption = 0;
	private String[] options = { "START", "QUIT" };
	
	// constructor
	public MenuState(GameStateManager gsm) { super(gsm); }
	
	public void init() {
		bg = Content.MENUBG[0][0];
		cursor = Content.CURSOR[0][0];
		JukeBox.load("/SFX/collect.wav", "collect");
		JukeBox.load("/SFX/menuoption.wav", "menuoption");
	}
	
	public void update() { handleInput(); }
	
	public void draw(Graphics2D g) {
		g.drawImage(bg, 0, 0, null);
		Content.drawString(g, options[0], 44, 90);
		Content.drawString(g, options[1], 48, 100);
		if(currentOption == 0) g.drawImage(cursor, 25, 86, null);
		else if(currentOption == 1) g.drawImage(cursor, 25, 96, null);
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.DOWN) && currentOption < options.length - 1) {
			JukeBox.play("menuoption");
			currentOption++;
		}
		if(Keys.isPressed(Keys.UP) && currentOption > 0) {
			JukeBox.play("menuoption");
			currentOption--;
		}
		if(Keys.isPressed(Keys.ENTER)) {
			JukeBox.play("collect");
			selectOption();
		}
	}
	
	private void selectOption() {
		if(currentOption == 0) gsm.setState(GameStateManager.PLAY);
		if(currentOption == 1) System.exit(0);
	}
}
