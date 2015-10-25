package com.EVOLver.GameOfLife.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.EVOLver.GameOfLife.Manager.Content;
import com.EVOLver.GameOfLife.Manager.JukeBox;
import com.EVOLver.GameOfLife.TileMap.TileMap;

public class Player extends Entity {
	
	// sprites
	private BufferedImage[] downSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	private BufferedImage[] upSprites;
	private BufferedImage[] swimDownSprites;
	private BufferedImage[] swimLeftSprites;
	private BufferedImage[] swimRightSprites;
	private BufferedImage[] swimUpSprites;
	// animation
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int UP = 2;
	private final int RIGHT = 3;
	private final int DOWN_SWIM = 4;
	private final int LEFT_SWIM = 5;
	private final int UP_SWIM = 6;
	private final int RIGHT_SWIM = 7;
	
	// gameplay
	private int money;
	private int maxMoney;
	private boolean onWater;
	private long ticks;
	
	// constructor
	public Player(TileMap tm) {
		// player settings
		super(tm);
		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;
		moveSpeed = 1;
		money = 0;
		// sprite settings
		downSprites = Content.PLAYER[0];
		leftSprites = Content.PLAYER[1];
		upSprites = Content.PLAYER[2];
		rightSprites = Content.PLAYER[3];
		swimDownSprites = Content.PLAYER[4];
		swimLeftSprites = Content.PLAYER[5];
		swimUpSprites = Content.PLAYER[6];
		swimRightSprites = Content.PLAYER[7];
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
	
	// status changes
	public void collectedMoney() { money++; }
	public int amountMoney() { return money; }
	public int getMaxMoney() { return maxMoney; }
	public void setMaxMoney(int i) { maxMoney = i; }
	
	// Used to update time.
	public long getTicks() { return ticks; }
	
	// Keyboard input. Moves the player.
	public void setDown() { super.setDown(); }
	public void setLeft() { super.setLeft(); }
	public void setRight() { super.setRight(); }
	public void setUp() { super.setUp(); }

	// set possible actions
	public void setAction() {
		if(currentAnimation == UP && tileMap.getIndex(rowTile - 1, colTile) == 21) {
			tileMap.setTile(rowTile - 1, colTile, 1);
			JukeBox.play("tilechange");
		}
		if(currentAnimation == DOWN && tileMap.getIndex(rowTile + 1, colTile) == 21) {
			tileMap.setTile(rowTile + 1, colTile, 1);
			JukeBox.play("tilechange");
		}
		if(currentAnimation == LEFT && tileMap.getIndex(rowTile, colTile - 1) == 21) {
			tileMap.setTile(rowTile, colTile - 1, 1);
			JukeBox.play("tilechange");
		}
		if(currentAnimation == RIGHT && tileMap.getIndex(rowTile, colTile + 1) == 21) {
			tileMap.setTile(rowTile, colTile + 1, 1);
			JukeBox.play("tilechange");
		}
	}
	
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
		if(down) {
			if(onWater && currentAnimation != DOWN_SWIM)
				setAnimation(DOWN_SWIM, swimDownSprites, 5);
			else if(!onWater && currentAnimation != DOWN)
				setAnimation(DOWN, downSprites, 5);
		}
		if(left) {
			if(onWater && currentAnimation != LEFT_SWIM)
				setAnimation(LEFT_SWIM, swimLeftSprites, 5);
			else if(!onWater && currentAnimation != LEFT)
				setAnimation(LEFT, leftSprites, 5);
		}
		if(right) {
			if(onWater && currentAnimation != RIGHT_SWIM)
				setAnimation(RIGHT_SWIM, swimRightSprites, 5);
			else if(!onWater && currentAnimation != RIGHT)
				setAnimation(RIGHT, rightSprites, 5);
		}
		if(up) {
			if(onWater && currentAnimation != UP_SWIM)
				setAnimation(UP_SWIM, swimUpSprites, 5);
			else if(!onWater && currentAnimation != UP)
				setAnimation(UP, upSprites, 5);
		}
		// update position
		super.update();
	}
	
	// Draw Player
	public void draw(Graphics2D g) { super.draw(g); }
}