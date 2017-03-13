package graphics;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import resources.Character;
import resources.Resources;

/**
 * Class to display a panel detailing all players in game
 * @author George Kaye
 *
 */

public class PlayerPanel extends JPanel {

	private Resources resources;
	private ArrayList<PlayerInfo> boxes;

	/**
	 * Create a new player panel
	 * @param resources the resources object
	 */
	
	public PlayerPanel(Resources resources) {

		super();
		this.resources = resources;

		setLayout(new GridLayout(2, 4));
		
		ArrayList<Character> characters = resources.getPlayerList();
		boxes = new ArrayList<>();
		
		int i = 0;
		
		for(Character c : characters){
			
			PlayerInfo box = new PlayerInfo(c);
			boxes.add(box);
			add(box, i%4, i/4);
			
		}
		
		setPreferredSize(new Dimension(1600, 100));
		
	}
	
	/**
	 * Set the order of the players within the panel
	 */
	
	public void setOrder(){
		/*
		ArrayList<Character> characters = resources.gamemode.getOrderedScores();
		
		for(int i  = 0; i < characters.size(); i++){
			
			boxes.get(characters.size() - 1 - i).setCharacter(characters.get(i));
		}*/
		
	}

}
