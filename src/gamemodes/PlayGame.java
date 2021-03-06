package gamemodes;

import ai.AITemplate;
import ai.FightingAI;
import ai.HotPotatoAI;
import resources.Character;
import resources.Map;
import resources.MapCosts;
import resources.Resources;
import resources.Resources.Mode;

/**
 * Set up the game objects and launch the game.
 */

public class PlayGame {

	/**
	 * Start the game directly for testing purposes only.
	 */
	public static void main(String[] args) {

		Resources resources = new Resources();

		start(resources, "asteroid", Mode.Deathmatch, Map.World.CAVE);

	}

	/**
	 * Initialise game objects and start the game.
	 * 
	 * @param resources
	 *            The resources object being used for the game.
	 */
	public static void start(Resources resources, String mapName, Mode modeType, Map.World style) {
		resources.mode = modeType;

		resources.clearPlayerList();

		resources.setMap(new Map(1200, 650, style, mapName));
		new MapCosts(resources);
		// Create and add players
		Character player = new Character(Character.Class.WIZARD, 1, "Player");
		// Will want a way to choose how many AIs to add

		resources.addPlayerToList(player);

		for (int i = 1; i < 8; i++) {
			Character character = new Character(Character.Class.getRandomClass(), i + 1, "CPU" + i);
			resources.addPlayerToList(character);
			AITemplate ai = (resources.mode == Mode.HotPotato) ? new HotPotatoAI(resources, character)
					: new FightingAI(resources, character);
			character.setAI(ai);
		}
		GameModeFFA mode;
		switch (resources.mode) {
		case Deathmatch:
			mode = new Deathmatch(resources, false, true);
			break;
		case LastManStanding:
			mode = new LastManStanding(resources, 5, false, true);
			break;
		case HotPotato:
			mode = new HotPotato(resources, false, true);
			break;
		default:
			mode = new Deathmatch(resources, false, true);
			break;
		}
		((Thread) mode).start();

	}
}
