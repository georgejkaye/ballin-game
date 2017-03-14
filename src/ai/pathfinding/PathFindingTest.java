package ai.pathfinding;

import java.awt.Point;
import java.io.IOException;

import ai.BasicAI;
import gamemodes.DebugMode;
import resources.Character;
import resources.Map;
import resources.MapReader;
import resources.Resources;

/**
 * Launches a game window with just one AI and debug mode on
 * 
 * @author Oliver Gratton
 *
 */
public class PathFindingTest
{
	private static Point startTile = new Point(12, 28);
	//	private static Point[] destinations = new Point[] { new Point(12, 28), new Point(8, 32), new Point(16, 38), new Point(20, 20) };
	//	private static Point[] destinations = new Point[] { new Point(6, 20), new Point(10, 14), new Point(9, 4), new Point(20, 43) }; // TODO to test moveAwayFrom Edge
	private static Point[] destinations = new Point[] { new Point(6, 20), new Point(10, 20), new Point(9, 20), new Point(20, 20) }; // TODO to test smoothing

	private static boolean followSetPoints = false;
	
	private static void testPoirot(Character player, BasicAI ai)
	{
		ai.setBehaviour("poirot"); // so we can feed it our own waypoints
		ai.setDestinations(destinations);
		player.setAI(ai);
	}
	
	private static void testOther(Character player, BasicAI ai, String behaviour)
	{
		ai.setBehaviour(behaviour);
		player.setAI(ai);
	}
	
	public static void main(String[] args)
	{

		/* SETTING UP THE MAP AND RESOURCES */

		Resources resources = new Resources();
		// make the map the default just in case the following fails
		Map.Tile[][] tiles = null;
		MapReader mr = new MapReader();
		try
		{
			tiles = mr.readMap("./resources/maps/map0.csv");
			System.out.println("Map Loaded");
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();

		}
		resources.setMap(new Map(1200, 650, tiles, Map.World.CAVE, "Test Map"));
		new MapCosts(resources);

		/* SETTING UP THE AI PLAYER */

		Point startCoords = new Point(resources.getMap().tileCoordsToMapCoords(startTile.x, startTile.y));
		
		Character controlled = new Character(Character.Class.HORSE, 1);
		controlled.setX(400);
		controlled.setY(400);
		resources.addPlayerToList(controlled);
		
		Character player = new Character(Character.Class.WIZARD, 0);
		player.setX(startCoords.getX());
		player.setY(startCoords.getY());
//		player.setPlayerNumber(0); // must be 0 otherwise no debug drawn
		resources.addPlayerToList(player);

		BasicAI ai = new BasicAI(resources, player);
		if (followSetPoints)
		{
			testPoirot(player, ai);
		}
		else
		{
			testOther(player, ai, "potato");
		}
		
		

		/* STARTING THINGS UP */

		DebugMode mode = new DebugMode(resources);
		mode.start();

		ai.start();
	}
}
