package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.esotericsoftware.kryonet.Client;

import graphics.sprites.SheetDeets;
import graphics.sprites.Sprite;
import graphics.sprites.Sprite.SheetType;
import networking.ClientInformation;
import networking.Command;
import networking.ConnectionDataModel;
import networking.Message;
import networking.Note;
import networking.Session;

@SuppressWarnings("serial")
public class InLobbyMenu extends JPanel implements Observer{
	
	private Session session;
	private ConnectionDataModel cModel;
	private SessionListMenu sessionList;
	
	public InLobbyMenu(Session session, Client client, ConnectionDataModel cModel, SessionListMenu sessionList){
		this.session = session;
		this.cModel = cModel;
		this.sessionList = sessionList;
		
		cModel.addObserver(this);
		setOpaque(false);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setOpaque(false);
		setLayout(new BorderLayout());
		add(Box.createHorizontalStrut(10));
		UIRes.getButtonAndIcon(buttonPanel, leaveLobbyButton(client));
		add(Box.createHorizontalStrut(10));
		add(buttonPanel, BorderLayout.PAGE_START);
		updateInLobbyPanel();
		add(UIRes.playersPanel, BorderLayout.CENTER);
	}
	
	JButton leaveLobbyButton(Client client) {
		JButton button = new JButton("Leave Lobby");
		button.setOpaque(false);
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			
			Message leaveMessage = new Message(Command.SESSION, Note.LEAVE, cModel.getMyId(), "", cModel.getSessionId(),
					cModel.getHighlightedSessionId());
			try {
				cModel.getConnection().sendTCP(leaveMessage);
				cModel.setReady(false);
				//SessionListMenu lobbyList = new SessionListMenu(client, cModel);
				System.out.println("model changed: " + cModel.hasChanged());
				updateInLobbyPanel();
				UIRes.switchPanel(sessionList);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		return button;
	}
	
	void updateInLobbyPanel() {
		Session session;
		UIRes.playersPanel.removeAll();
		if(cModel.getSessionId() != null && (!cModel.getSessionId().equals(""))) {
			session = cModel.getSession(cModel.getSessionId());
			for (int i = 0; i < session.getAllClients().size(); i++) {
				addPlayerToLobby(session.getAllClients().get(i), i + 1);
			}
		}
		else {
			session = getSession();
		}

	}
	
	void addPlayerToLobby(ClientInformation client, int index) {
		UIRes.playersPanel.setOpaque(false);
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension((int)(UIRes.width * 0.95), (int)(UIRes.height * 0.12)));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel playerLabel = UIRes.getLabel(client.getName());
		

		JComboBox<ImageIcon> characterClass = new JComboBox<ImageIcon>();
		for (int i = 0; i < UIRes.numberIcons; i++) {
			BufferedImage icon = Sprite.getSprite(Sprite.loadSpriteSheet(SheetType.CHARACTER), 0, i,
					SheetDeets.CHARACTERS_SIZEX, SheetDeets.CHARACTERS_SIZEY);
			characterClass.addItem(new ImageIcon(icon));
		}

		Color colour = UIRes.resources.getPlayerColor(index);
		panel.setBorder(new CompoundBorder(new LineBorder(colour, 15), new EmptyBorder(10, 10, 10, 10)));

		JButton readyCheck = new JButton("Ready");
		UIRes.customiseButton(readyCheck, false);

		
		readyCheck.setForeground(Color.RED);
		readyCheck.addActionListener(e -> {
			if (readyCheck.getForeground() == Color.RED) {
				readyCheck.setForeground(Color.GREEN);
				client.setReady(true);
				if(cModel.getSession(cModel.getSessionId()).getAllClients().size() > 0) {
					if(!cModel.isGameInProgress()) {
						Message message = new Message(Command.GAME, Note.START, cModel.getMyId(), null, cModel.getSessionId(), null);
						try {
							cModel.getConnection().sendTCP(message);
							cModel.setReady(true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			} else {
				readyCheck.setForeground(Color.RED);
				client.setReady(false);
				if(cModel.getSession(cModel.getSessionId()).getAllClients().size() > 0) {
					if(!cModel.isGameInProgress()) {
						Message message = new Message(Command.GAME, Note.STOP, cModel.getMyId(), null, cModel.getSessionId(), null);
						try {
							cModel.getConnection().sendTCP(message);
							cModel.setReady(false);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			System.out.println(client.isReady());
		});

		panel.add(Box.createHorizontalGlue());
		panel.add(playerLabel);
		panel.add(Box.createHorizontalGlue());
		panel.add(characterClass);
		panel.add(Box.createHorizontalGlue());
		panel.add(readyCheck);
		panel.add(Box.createHorizontalGlue());
		UIRes.playersPanel.add(panel);

	}
	
	void setSession(Session session){
		this.session = session;
	}
	
	Session getSession(){
		return this.session;
	}

	@Override
	public void update(Observable o, Object arg) {
		updateInLobbyPanel();
		repaint();
		validate();
	}

}
