package resources;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import resources.Map.Tile;

//import graphics.old.CharacterModel;

/*
 * These things are needed by UI, Physics and Graphics, and possibly Networking,
 *   so I've put them here for ease of access.
 *   If we'd prefer to just pass them to the relevant things, that's cool too.
 */
public class Resources {
	
	// keybindings
	private int default_up = KeyEvent.VK_W;
	private int default_down = KeyEvent.VK_S;
	private int default_left = KeyEvent.VK_A;
	private int default_right = KeyEvent.VK_D;
	private int default_dash = KeyEvent.VK_SPACE;
	private int up = KeyEvent.VK_W;
	private int down = KeyEvent.VK_S;
	private int left = KeyEvent.VK_A;
	private int right = KeyEvent.VK_D;
	private int dash = KeyEvent.VK_SPACE;

	// sound effect "volume" (0 is normal)
	// can be as negative as you like but no larger than about 10 I think
	// let's just agree to have 0 as the max
	private int sfx_gain = 0;
	
	// max deaths a character can have.
	private int maxDeaths = 4;
	
	// characters
	private ArrayList<Character> playerList = new ArrayList<Character>();
	// powerups in play
	private ArrayList<Powerup> powerupList = new ArrayList<Powerup>();
	
	// puck used in hockey game mode, using character class to make things easy
	private Character puck;
	
	// map
	private Map map;
	
	private int[][] proxMask; // how many tiles away from danger
	private int[][] costMask; // cost for AI to pathfind there
	
	// client ID
	private int id;
	
	private ArrayList<Tile> bad_tiles; // tiles to path-find around 
	
	// Counter of how many ticks have happened
	private int globalTimer = 0;
	
	// destination list for pathfinding
	private LinkedList<Point> destList = new LinkedList<Point>();
	
	public Resources()
	{
		bad_tiles = new ArrayList<Tile>();
		bad_tiles.add(Tile.ABYSS);
		bad_tiles.add(Tile.EDGE_ABYSS); // TODO add walls when they are implemented
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get the default up keybinding
	 * @return the default up keybinding
	 */
	
	public int getDefaultUp() {
		return default_up;
	}
	
	/**
	 * Set the default up keybinding
	 * @param default_up the default up keybinding
	 */
	
	public void setDefaultUp(int default_up) {
		this.default_up = default_up;
	}
	
	/**
	 * Get the default down keybinding
	 * @return the default down keybinding
	 */
	
	public int getDefaultDown() {
		return default_down;
	}
	
	/**
	 * Set the default down keybinding
	 * @param default_down the default down keybinding
	 */
	
	public void setDefaultDown(int default_down) {
		this.default_down = default_down;
	}
	
	/**
	 * Get the deault left keybinding
	 * @return the default left keybinding
	 */
	
	public int getDefaultLeft() {
		return default_left;
	}
	
	/**
	 * Set the default left keybinding
	 * @param default_left the default left keybinding
	 */
	
	public void setDefaultLeft(int default_left) {
		this.default_left = default_left;
	}
	
	/**
	 * Get the default right keybinding
	 * @return the default right keybinding
	 */
	
	public int getDefaultRight() {
		return default_right;
	}
	
	/**
	 * Set the default right keybinding
	 * @param default_right the default right keybinding
	 */
	
	public void setDefaultRight(int default_right) {
		this.default_right = default_right;
	}
	
	/**
	 * Get the default dash keybinding
	 * @return the default dash keybinding
	 */
	
	public int getDefaultDash() {
		return default_dash;
	}
	
	/**
	 * Set the default dash keybinding
	 * @param default_right the default dash keybinding
	 */
	
	public void setDefaultDash(int default_dash) {
		this.default_dash = default_dash;
	}
	
	/**
	 * Get the up keybinding
	 * @return the up keybinding
	 */
	
	public int getUp() {
		return up;
	}
	
	/**
	 * Set the up keybinding
	 * @param up the up keybinding
	 */
	
	public void setUp(int up) {
		this.up = up;
	}
	
	/**
	 * Get the down keybinding
	 * @return the down keybinding
	 */
	
	public int getDown() {
		return down;
	}
	
	/**
	 * Set the down keybinding
	 * @param down the down keybinding
	 */
	
	public void setDown(int down) {
		this.down = down;
	}
	
	/**
	 * Get the left keybinding
	 * @return the left keybinding
	 */
	
	public int getLeft() {
		return left;
	}
	
	/**
	 * Set the left keybinding
	 * @param left the left keybinding
	 */
	
	public void setLeft(int left) {
		this.left = left;
	}
	
	/**
	 * Get the right keybinding
	 * @return the right keybinding
	 */
	
	public int getRight() {
		return right;
	}
	
	/**
	 * Set the right keybinding
	 * @param right the right keybinding
	 */
	
	public void setRight(int right) {
		this.right = right;
	}
	
	/**
	 * Get the dash keybinding
	 * @return the dash keybinding
	 */
	
	public int getDash() {
		return dash;
	}
	
	/**
	 * Set the dash keybinding
	 * @param right the dash keybinding
	 */
	
	public void setDash(int dash) {
		this.dash = dash;
	}
	
	/**
	 * Get the SFX gain
	 * @return the SFX gain
	 */
	
	public int getSFXGain() {
		return sfx_gain;
	}
	
	/**
	 * Set the SFX gain
	 * @param sfx_gain the SFX gain
	 */
	
	public void setSFXGain(int sfx_gain) {
		this.sfx_gain = sfx_gain;
	}
	
	/**
	 * Get the player list
	 * @return the player list
	 */
	
	public ArrayList<Character> getPlayerList() {
		return playerList;
	}
	
	/**
	 * Add a character to the player list
	 * @param character the character
	 */
	
	public void addPlayerToList(Character character){
		playerList.add(character);
	}
	
	/**
	 * Set a player list
	 * @param playerList the player list
	 */
	
	public void setPlayerList(ArrayList<Character> playerList) {
		this.playerList = playerList;
	}
	
	/**
	 * Get the map
	 * @return the map
	 */
	
	public Map getMap() {
		return map;
	}
	
	/**
	 * Set the map
	 * @param map the map
	 */
	
	public void setMap(Map map) {
		this.map = map;
	}
	

	
	public ArrayList<Tile> getBadTiles()
	{
		return bad_tiles;
	}
	
	/**
	 * set the maximum number of lives for each character.
	 * @param maxDeaths
	 */
	public void setMaxDeaths(int maxDeaths) {
		this.maxDeaths = maxDeaths;
	}
	
	/**
	 * get the maximum number of lives for each character.
	 * @return
	 */
	public int maxDeaths() {
		return maxDeaths;
	}
	
	/**
	 * Creates a puck for the hockey game mode. 
	 */
	public void createPuck() {
		// need new character class to represent puck?
		this.puck = new Character();
	}

	public int getGlobalTimer() {
		return globalTimer;
	}

	public void incrementGlobalTimer() {
		globalTimer++;
	}
	
	public void setDestList(LinkedList<Point> destList){
		this.destList = destList;
	}
	
	public LinkedList<Point> getDestList(){
		return this.destList;
	}

	public ArrayList<Powerup> getPowerupList() {
		return powerupList;
	}

	public void addPowerup(Powerup p) {
		powerupList.add(p);
	}
	
	public void removePowerup(Powerup p) {
		boolean b = powerupList.remove(p);
		if (b) {
			System.out.println("Removed powerup");
		}
	}
	
	//public static MapReader mapReader = new MapReader();
	//public static Map.Tile[][] map1 = mapReader.readMap("./resources/maps/map1.csv");
	
}
