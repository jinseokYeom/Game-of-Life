package com.EVOLver.GameOfLife.GameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.EVOLver.GameOfLife.Entity.Container;
import com.EVOLver.GameOfLife.Entity.Item;
import com.EVOLver.GameOfLife.Entity.Player;
import com.EVOLver.GameOfLife.Entity.SmartTV;
import com.EVOLver.GameOfLife.Entity.Sparkle;
import com.EVOLver.GameOfLife.HUD.Hud;
import com.EVOLver.GameOfLife.Main.GamePanel;
import com.EVOLver.GameOfLife.Manager.Data;
import com.EVOLver.GameOfLife.Manager.GameStateManager;
import com.EVOLver.GameOfLife.Manager.JukeBox;
import com.EVOLver.GameOfLife.Manager.Keys;
import com.EVOLver.GameOfLife.TileMap.TileMap;

public class PlayState extends GameState {
	// player
	private Player player;
	// people
	private SmartTV smartTV;
	// tilemap
	private TileMap tileMap;
	// shades
	private TileMap shade;
	// Containers
	private ArrayList<Container> containers;
	// items
	private ArrayList<Item> items;
	// sparkles
	private ArrayList<Sparkle> sparkles;
	// camera position
	private int xsector;
	private int ysector;
	private int sectorSize;
	// hud
	private Hud hud;
	// events
	private boolean blockInput;
	private boolean eventStart;
	private boolean eventFinish;
	private int eventTick;
	// transition box
	private ArrayList<Rectangle> boxes;

	// constructor
	public PlayState(GameStateManager gsm) { super(gsm); }

	public void init() {
		// initialize array list
		containers = new ArrayList<Container>();
		sparkles = new ArrayList<Sparkle>();
		items = new ArrayList<Item>();
		// load tileMap
		tileMap = new TileMap(16);
		tileMap.loadTiles("/Tilesets/tileset.png");
		tileMap.loadMap("/Maps/town.map");
		// load shade
		shade = new TileMap(16);
		shade.loadTiles("/Tilesets/shade.png");
		shade.loadMap("/Maps/town_shade.map");
		// create player
		player = new Player(tileMap);
		smartTV = new SmartTV(tileMap);
		// fill lists
		populateContainers();
		// initialize player
		player.setTilePosition(2, 3);
		player.setMaxMoney(containers.size());
		// initialize smartTV
		smartTV.setTilePosition(2, 4);
		// set up camera position
		sectorSize = GamePanel.WIDTH;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPositionImmediately(-xsector * sectorSize, -ysector * sectorSize);
		// load hud
		hud = new Hud(player, containers);
		// load music
		JukeBox.load("/Music/bgm.mp3", "music1");
		JukeBox.setVolume("music1", -10);
		JukeBox.loop("music1", 1000, 1000, JukeBox.getFrames("music1") - 1000);
		JukeBox.load("/Music/finish.mp3", "finish");
		JukeBox.setVolume("finish", -10);
		// load sfx
		JukeBox.load("/SFX/collect.wav", "collect");
		JukeBox.load("/SFX/mapmove.wav", "mapmove");
		JukeBox.load("/SFX/tilechange.wav", "tilechange");
		JukeBox.load("/SFX/splash.wav", "splash");
		// start event
		boxes = new ArrayList<Rectangle>();
		eventStart = true;
		eventStart();
	}

	private void populateContainers() {
		Container c;
		c = new Container(tileMap);
		c.setTilePosition(20, 20);
		c.addChange(new int[] { 23, 19, 1 });
		c.addChange(new int[] { 23, 20, 1 });
		containers.add(c);
		c = new Container(tileMap);
		c.setTilePosition(12, 36);
		c.addChange(new int[] { 31, 17, 1 });
		containers.add(c);
		c = new Container(tileMap);
		c.setTilePosition(28, 4);
		c.addChange(new int[] {27, 7, 1});
		c.addChange(new int[] {28, 7, 1});
		containers.add(c);
		c = new Container(tileMap);
		c.setTilePosition(4, 34);
		c.addChange(new int[] { 31, 21, 1 });
		containers.add(c);

		c = new Container(tileMap);
		c.setTilePosition(28, 19);
		containers.add(c);
	}

	public void update() {
		// check keys
		handleInput();
		// check events
		if(eventStart) eventStart();
		if(eventFinish) eventFinish();
		if(player.amountMoney() == player.getMaxMoney())
			eventFinish = blockInput = true;
		// update camera
		int oldxs = xsector;
		int oldys = ysector;
		xsector = player.getx() / sectorSize;
		ysector = player.gety() / sectorSize;
		tileMap.setPosition(-xsector * sectorSize, -ysector * sectorSize);
		tileMap.update();
		shade.setPosition(-xsector * sectorSize, -ysector * sectorSize);
		shade.update();

		if(oldxs != xsector || oldys != ysector)
			JukeBox.play("mapmove");
		if(tileMap.isMoving()) return;
		// update player
		player.update();
		// update smartTV
		smartTV.update();
		// update Containers
		for(int i = 0; i < containers.size(); i++) {
			Container d = containers.get(i);
			d.update();
			// player collects Container
			if(player.intersects(d)) {
				// remove from list
				containers.remove(i);
				i--;
				// increment amount of collected Containers
				player.collectedMoney();
				// play collect sound
				JukeBox.play("collect");
				// add new sparkle
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(d.getx(), d.gety());
				sparkles.add(s);
				// make any changes to tile map
				ArrayList<int[]> ali = d.getChanges();
				for(int[] j : ali)
					tileMap.setTile(j[0], j[1], j[2]);
				if(ali.size() != 0)
					JukeBox.play("tilechange");
			}
		}

		// update sparkles
		for(int i = 0; i < sparkles.size(); i++) {
			Sparkle s = sparkles.get(i);
			s.update();
			if(s.shouldRemove()) {
				sparkles.remove(i);
				i--;
			}
		}

		// update items
		for(int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if(player.intersects(item)) {
				items.remove(i);
				i--;
//				item.collected(player);
				JukeBox.play("collect");
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(item.getx(), item.gety());
				sparkles.add(s);
			}
		}
	}

	public void draw(Graphics2D g) {
		// draw tilemap
		tileMap.draw(g);
		// draw shade
		shade.draw(g);
		// draw player
		player.draw(g);
		// draw smartTV
		smartTV.draw(g);
		// draw Containers
		for(Container c : containers)
			c.draw(g);
		// draw sparkles
		for(Sparkle s : sparkles)
			s.draw(g);
		// draw items
		for(Item i : items)
			i.draw(g);
		// draw hud
		hud.draw(g);
		// draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < boxes.size(); i++)
			g.fill(boxes.get(i));
	}

	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			JukeBox.stop("music1");
			gsm.setPaused(true);
		}
		if(blockInput) return;
		if(Keys.isDown(Keys.LEFT)) player.setLeft();
		if(Keys.isDown(Keys.RIGHT)) player.setRight();
		if(Keys.isDown(Keys.UP)) player.setUp();
		if(Keys.isDown(Keys.DOWN)) player.setDown();
		if(Keys.isPressed(Keys.SPACE)) player.setAction();
	}

	// animation when the game starts
	private void eventStart() {
		eventTick++;
		if(eventTick == 1) {
			boxes.clear();
			for(int i = 0; i < 9; i++)
				boxes.add(new Rectangle(0, i * 16, GamePanel.WIDTH, 16));
		}
		if(eventTick > 1 && eventTick < 32) {
			for(int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if(i % 2 == 0) r.x -= 4;
				else r.x += 4;
			}
		}
		if(eventTick == 33) {
			boxes.clear();
			eventStart = false;
			eventTick = 0;
		}
	}

	// animation when the game finishes
	private void eventFinish() {
		eventTick++;
		if(eventTick == 1) {
			boxes.clear();
			for(int i = 0; i < 9; i++) {
				if(i % 2 == 0) boxes.add(new Rectangle(-128, i * 16, GamePanel.WIDTH, 16));
				else boxes.add(new Rectangle(128, i * 16, GamePanel.WIDTH, 16));
			}
			JukeBox.stop("music1");
			JukeBox.play("finish");
		}
		if(eventTick > 1) {
			for(int i = 0; i < boxes.size(); i++) {
				Rectangle r = boxes.get(i);
				if(i % 2 == 0 && r.x < 0) r.x += 4;
				else if(r.x > 0) r.x -= 4;
			}
		}
		if(eventTick > 33) {
			if(!JukeBox.isPlaying("finish")) {
				Data.setTime(player.getTicks());
				gsm.setState(GameStateManager.GAMEOVER);
			}
		}
	}
}
