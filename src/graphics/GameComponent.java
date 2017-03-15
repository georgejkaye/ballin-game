package graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

import networking.Updater;
import resources.Character;
import resources.Map;
import resources.Map.World;
import resources.Resources;

/**
 * Contains everything on the screen that the player can see
 * 
 * @author George Kaye
 *
 */

@SuppressWarnings("serial")
public class GameComponent extends JFrame implements ActionListener {

	private ArrayList<Character> characters;
	private Timer timer;
	public static LayeredPane layers;
	private TopBar bar;
	private JLabel label;
	private Resources resources;
	private int firstPlayerIndex = 0;

	private boolean fullScreen = false;

	int oldValueX, newValueX, oldValueY, newValueY;

	/**
	 * Create a new game component (which comprises everything the player can
	 * see!)
	 * 
	 * @param characters
	 *            an ArrayList of characters on the board
	 * @param map
	 *            the map the board is displaying
	 */

	public GameComponent(Resources resources, int width, int height, Updater updater, boolean debugPaths) {

		super();

		setLayout(new BorderLayout());

		// This code block below is just for testing!

		addKeyListener(new TAdapter(resources));
		setFocusable(true);
		
		label = new JLabel();
		label.setText("hello");
		label.setFont(new Font("Verdana", Font.PLAIN, 45));
		
		bar = new TopBar(resources);
		layers = new LayeredPane(resources, debugPaths);
		
		

		// End test code block
		this.resources = resources;

		characters = resources.getPlayerList();

		if (updater != null) {
			for (Character model : resources.getPlayerList()) {
				model.addObserver(layers.getView());
				if (model.getId().equals(resources.getId())) {
					model.addObserver(updater);
				}
			}
		}

		for (int i = 0; i < characters.size(); i++) {
			if (characters.get(i).getId().equals(resources.getId())) {
				firstPlayerIndex = i;
				System.out.println("Player Number: " + characters.get(i).getPlayerNumber());
				break;
			}
		}


		label.setHorizontalAlignment(JLabel.CENTER);
		
		
		
		add(label, BorderLayout.NORTH);
		add(bar, BorderLayout.NORTH);
		setUndecorated(true);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		//GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
	
		add(layers, BorderLayout.CENTER);
		
		toggleFullscreen();
		
		timer = new Timer(10, this);
		timer.start();
		
		setVisible(true);
		pack();
	}

	// All code below here is for testing

	/**
	 * Testing keyboard inputs
	 */

	public void actionPerformed(ActionEvent arg0) {

			layers.repaint();
			bar.paint();
			bar.updateScores();
			bar.updateStats();
			
		
	}
	
	/**
	 * Switch between fullscreen and windowed
	 */

	public void toggleFullscreen() {

		if (fullScreen) {

			layers.getView().setFullScreen(false);
			getContentPane().setPreferredSize(new Dimension(1200, 625));
			pack();
			setLocationRelativeTo(null);
			fullScreen = false;

		} else {

			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			int width = gd.getDisplayMode().getWidth();
			int height = gd.getDisplayMode().getHeight();
			setLocation(0, 0);
			layers.getView().setFullScreen(true);
			getContentPane().setPreferredSize(new Dimension(width, height));
			pack();
			fullScreen = true;

		}
		
		setFocusable(true);
		requestFocus();
		
	}

	public void cycleWorld() {

		World world = resources.getMap().getWorldType();
		World newWorld = null;

		switch (world) {
		case CAVE:
			newWorld = Map.World.SPACE;
			break;
		case SPACE:
			newWorld = Map.World.LAVA;
			break;
		case LAVA:
			newWorld = Map.World.CAKE;
			break;
		case CAKE:
			newWorld = Map.World.CAVE;
			break;
		case DESERT:
			break;
		case ICE:
			break;
		default:
			break;
		}

		resources.getMap().setWorldType(newWorld);
		layers.getView().makeMap();
		fullScreen = false;
		toggleFullscreen();

	}
	
	private class TAdapter extends KeyAdapter {
		
		private Resources resources;
		private int leftKey;
		private int rightKey;
		private int upKey;
		private int downKey;
		private int dashKey;
		private int blockKey;
		
		public TAdapter(Resources resources) {
			this.resources = resources;
			
			
			setUpControls();
			
		}
		
		public void setUpControls(){
			
			leftKey = resources.getLeft();
			rightKey = resources.getRight();
			upKey = resources.getUp();
			downKey = resources.getDown();
			
			blockKey = resources.getBlock();
			dashKey = resources.getDash();
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if(key == leftKey){
				characters.get(firstPlayerIndex).setLeft(false);
			} else if(key == rightKey){
				characters.get(firstPlayerIndex).setRight(false);
			} else if(key == upKey){
				characters.get(firstPlayerIndex).setUp(false);
			} else if(key == downKey){
				characters.get(firstPlayerIndex).setDown(false);
			}
			
			/*switch (key) {
			case KeyEvent.VK_A:
				characters.get(firstPlayerIndex).setLeft(false);
				break;
			case KeyEvent.VK_D:
				characters.get(firstPlayerIndex).setRight(false);
				break;
			case KeyEvent.VK_W:
				characters.get(firstPlayerIndex).setUp(false);
				break;
			case KeyEvent.VK_S:
				characters.get(firstPlayerIndex).setDown(false);
				break;
			case KeyEvent.VK_UP:
				characters.get(secondPlayerIndex).setUp(false);
				break;
			case KeyEvent.VK_DOWN:
				characters.get(secondPlayerIndex).setDown(false);
				break;
			case KeyEvent.VK_LEFT:
				characters.get(secondPlayerIndex).setLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				characters.get(secondPlayerIndex).setRight(false);
				break;
			}*/
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			if(key == leftKey){
				characters.get(firstPlayerIndex).setLeft(true);
			} else if(key == rightKey){
				characters.get(firstPlayerIndex).setRight(true);
			} else if(key == upKey){
				characters.get(firstPlayerIndex).setUp(true);
			} else if(key == downKey){
				characters.get(firstPlayerIndex).setDown(true);
			} else if(key == dashKey){
				characters.get(firstPlayerIndex).requestDashing();
			} else if(key == blockKey){
				characters.get(firstPlayerIndex).setBlocking(true);
			} else if(key == KeyEvent.VK_ESCAPE){
				System.exit(0);
			} else if(key == KeyEvent.VK_Z){
				cycleWorld();
			} else if(key == KeyEvent.VK_P){
				if(LayeredPane.menuShowing)
					layers.setLayer(LayeredPane.inGameMenu, new Integer(20));
				else
					layers.setLayer(LayeredPane.inGameMenu, new Integer(10));
				LayeredPane.menuShowing = !LayeredPane.menuShowing;
			}
		}
	}

}
