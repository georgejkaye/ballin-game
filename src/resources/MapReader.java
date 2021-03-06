package resources;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Reads a Comma Separated Value file and returns it as an ArrayList of lines
 * Could be done statically but for the dictionary
 * 
 * @author Oliver Gratton
 */
public class MapReader
{
	private BufferedReader br;
	
	private String line = "";
	private final String cvsSplitBy = ",";
	private final String comment = "#";
	private final String metadata = "@";
	
	private  Hashtable<String, Map.Tile> tileDict;

	/**
	 * Make a new object and initialise the dictionary of string to enum
	 */
	public MapReader()
	{
		tileDict = new Hashtable<String, Map.Tile>();
		tileDict.put("a", Map.Tile.ABYSS);
		tileDict.put("b", Map.Tile.FLAT);
		tileDict.put("c", Map.Tile.EDGE_N);
		tileDict.put("d", Map.Tile.EDGE_E);
		tileDict.put("e", Map.Tile.EDGE_S);
		tileDict.put("f", Map.Tile.EDGE_W);
		tileDict.put("g", Map.Tile.EDGE_NE);
		tileDict.put("h", Map.Tile.EDGE_SE);
		tileDict.put("i", Map.Tile.EDGE_SW);
		tileDict.put("j", Map.Tile.EDGE_NW);
		tileDict.put("k", Map.Tile.EDGE_NS);
		tileDict.put("l", Map.Tile.EDGE_EW);
		tileDict.put("m", Map.Tile.EDGE_NES);
		tileDict.put("n", Map.Tile.EDGE_ESW);
		tileDict.put("o", Map.Tile.EDGE_SWN);
		tileDict.put("p", Map.Tile.EDGE_WNE);
		tileDict.put("q", Map.Tile.EDGE_NESW);
		tileDict.put("r", Map.Tile.EDGE_ABYSS);
		tileDict.put("s", Map.Tile.WALL);
	}
	
	/**
	 * Read the contents of a csv into an ArrayList
	 * Ignore lines that start with a #
	 * 
	 * NOTE: ensure file has no empty whitespace lines
	 * 
	 * @param filename the filepath of the file relative to the src folder
	 * @return array list of string arrays
	 */
	private ArrayList<String[]> readContents(String filename) throws IOException
	{
		ArrayList<String[]> lines = new ArrayList<String[]>();

		br = new BufferedReader(new FileReader(filename));

		while ((line = br.readLine()) != null)
		{
			if (!line.startsWith(comment) && !line.startsWith(metadata))
			{
				// use comma as separator
				String[] items = line.split(cvsSplitBy);
				lines.add(items);
			}
		}
		return lines;

	}
	
	/**
	 * Read and convert a file to Tile enum
	 * @param filename the filepath of the file relative to the src folder
	 * @return 2D Tile array
	 */
	public Map.Tile[][] readMap(String filename) throws IOException
	{
		ArrayList<String[]> mapString = readContents(filename);
		int height = mapString.size();
		int width = mapString.get(0).length;
		
		Map.Tile[][] map = new Map.Tile[height][width];
		
		for (int i = 0; i < map.length; i++)
		{
			String[] row = mapString.get(i);
			for (int j = 0; j < map[i].length; j++)
			{
				map[i][j] = tileDict.get(row[j]);
			}
		}
		
		return map;
	}
	
	/**
	 * For testing
	 */
	public static void main(String[] args)
	{
		MapReader mr = new MapReader();	
		try
		{
			Map.Tile[][] map = mr.readMap(FilePaths.maps + "potato2.csv");
			System.out.println("I guess it worked then");
			System.out.println(map);
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
		}
	}
}
