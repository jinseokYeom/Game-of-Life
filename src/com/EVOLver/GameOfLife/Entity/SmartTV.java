package com.EVOLver.GameOfLife.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.EVOLver.GameOfLife.Manager.Content;
import com.EVOLver.GameOfLife.Manager.JukeBox;
import com.EVOLver.GameOfLife.TileMap.TileMap;

public class SmartTV extends Entity {
	
	// sprites
	private BufferedImage[] downSprites;

	// animation
	private final int DOWN = 0;
	
	private boolean onWater;
	private long ticks;
	
	// constructor
	public SmartTV(TileMap tm) {
		// player settings
		super(tm);
		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;
		moveSpeed = 1;
		// sprite settings
		downSprites = Content.SMART_TV[0];
		// animation settings
		animation.setFrames(downSprites);
		animation.setDelay(5);
	}
	
	// set animation
	private void setAnimation(int i, BufferedImage[] bi, int d) {
		currentAnimation = i;
		animation.setFrames(bi);
		animation.setDelay(d);
	}
	
	// Used to update time.
	public long getTicks() { return ticks; }
	
	// update
	public void update() {
		ticks++;		
		// check if on water
		boolean current = onWater;
		if(tileMap.getIndex(ydest / tileSize, xdest / tileSize) == 4) onWater = true;
		else onWater = false;
		// if going from land to water
		if(!current && onWater) JukeBox.play("splash");
		// set animation
		if(down && !onWater && currentAnimation != DOWN)
			setAnimation(DOWN, downSprites, 5); 
		// update position
		super.update();
	}
	
	// Draw Player
	public void draw(Graphics2D g) { super.draw(g); }
}