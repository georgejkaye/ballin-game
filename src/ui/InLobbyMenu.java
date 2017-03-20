package ui;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Timestamp;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.esotericsoftware.kryonet.Client;

import networking.ClientInformation;
import networking.Command;
import networking.Message;
import networking.Note;
import networking.Session;
import resources.Character;

@SuppressWarnings("serial")
public class InLobbyMenu extends JPanel implements Observer{
	
	private Session session;
	private Client client;
	
	public InLobbyMenu(Session session, Client client){
		this.session = session;
		this.client = client;
		
		UIRes.cModel.addObserver(this);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		buttonPanel.add(leaveLobbyButton(client));
		add(buttonPanel, BorderLayout.PAGE_START);
		updateInLobbyPanel();
		add(UIRes.playersPanel, BorderLayout.CENTER);
	}
	
	JButton leaveLobbyButton(Client client) {
		JButton button = new JButton("Leave Lobby");
		UIRes.customiseButton(button, true);
		button.addActionListener(e -> {
			System.out.println("Session ID sent: " + UIRes.cModel.getSessionId());
			Message leaveMessage = new Message(Command.SESSION, Note.LEAVE, UIRes.cModel.getMyId(), "", UIRes.cModel.getSessionId(),
					UIRes.cModel.getHighlightedSessionId());
			try {
				UIRes.cModel.getConnection().sendTCP(leaveMessage);
				UIRes.cModel.setReady(false);
				SessionListMenu lobbyList = new SessionListMenu(client);
				System.out.println("model changed: " + UIRes.cModel.hasChanged());
				updateInLobbyPanel();
				repaint();
				revalidate();
				UIRes.switchPanel(lobbyList);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		return button;
	}
	
	void updateInLobbyPanel() {
		Session session;
		UIRes.playersPanel.removeAll();
		if(UIRes.cModel.getSessionId() != null && (!UIRes.cModel.getSessionId().equals(""))) {
			session = UIRes.cModel.getSession(UIRes.cModel.getSessionId());
			System.out.println("Clients in this session at " + new Timestamp(System.currentTimeMillis()) + " :" + session.getAllClients().size());
			for (int i = 0; i < session.getAllClients().size(); i++) {
				addPlayerToLobby(session.getAllClients().get(i), i + 1);
			}
		}
		else {
			session = this.session;
		}
		
		UIRes.playersPanel.repaint();
		UIRes.playersPanel.revalidate();

	}
	
	void addPlayerToLobby(ClientInformation client, int index) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension((int)(this.getWidth() * 0.95), (int)(this.getHeight() * 0.15)));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		JLabel playerLabel = new JLabel(client.getName());

		Choice characterClass = new Choice();
		for (Character.Class character : Character.Class.values()) {
			characterClass.add(character + "");
		}

		Color colour = UIRes.resources.getPlayerColor(index);
		panel.setBorder(BorderFactory.createLineBorder(colour, 15));

		JButton readyCheck = new JButton("Ready");
		UIRes.customiseButton(readyCheck, false);

		readyCheck.setForeground(Color.RED);
		readyCheck.addActionListener(e -> {
			if (readyCheck.getForeground() == Color.RED) {
				readyCheck.setForeground(Color.GREEN);
				client.setReady(true);
			} else {
				readyCheck.setForeground(Color.RED);
				client.setReady(false);
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
		UIRes.playersPanel.add(Box.createVerticalStrut(20));

	}
	
	void setSession(Session session){
		this.session = session;
	}
	
	Session getSession(){
		return this.session;
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println(UIRes.username + " lobby update reached");
		updateInLobbyPanel();
	//	repaint();
	//	validate();
		updateUI();
	}

}
