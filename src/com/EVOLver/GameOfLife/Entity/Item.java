package com.EVOLver.GameOfLife.Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import com.EVOLver.GameOfLife.Manager.Content;
import com.EVOLver.GameOfLife.TileMap.TileMap;

public class Item extends Entity{
	
	// instance variables
	private BufferedImage sprite;
	private int type;

	// class constances
	public static final int BOAT = 0;
	public static final int AXE = 1;
	
	public Item(TileMap tm) {
		super(tm);
		type = -1;
		width = height = 16;
		cwidth = cheight = 12;
	}
	
	public void setType(int i) {
		type = i;
		if(type == BOAT)
			sprite = Content.ITEMS[1][0];
		else if(type == AXE)
			sprite = Content.ITEMS[1][1];
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		g.drawImage(sprite, x + xmap - width / 2, y + ymap - height / 2, null);
	}
}