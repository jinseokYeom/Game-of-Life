package com.EVOLver.GameOfLife.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import com.EVOLver.GameOfLife.Manager.Content;
import com.EVOLver.GameOfLife.TileMap.TileMap;

// change Container into easter egg
public class Container extends Entity {

	// instance variables
	BufferedImage[] sprites;
	private ArrayList<int[]> tileChanges;

	public Container(TileMap tm) {
		super(tm);
		width = 16;
		height = 16;
		cwidth = 12;
		cheight = 12;
		sprites = Content.CONTAINER[0];
		animation.setFrames(sprites);
		animation.setDelay(10);
		tileChanges = new ArrayList<int[]>();
	}

	public void addChange(int[] i) { tileChanges.add(i); }
	public ArrayList<int[]> getChanges() { return tileChanges; }
	public void update() { animation.update(); }
	public void draw(Graphics2D g) { super.draw(g); }
}
