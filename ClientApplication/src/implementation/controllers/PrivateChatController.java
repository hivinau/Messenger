/**
 * Copyright 2017
 *
 *
 * Sous licence Apache, Version 2.0 (la "Licence");
 * Vous ne pouvez pas utiliser ce fichier sauf en conformité avec la licence.
 * Vous pouvez obtenir une copie de la licence à l'adresse :
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Sauf si requis par la loi en vigueur ou accord écrit, le logiciel
 * Distribué sous licence est distribué «TEL QUEL»,
 * SANS GARANTIES OU CONDITIONS D'AUCUNE SORTE, express ou implicite.
 * Voir la licence pour les autorisations spécifiques aux différentes langues et
 * Limitations sous la licence.
 *
 * Contribué par : Jesus GARNICA OLARRA, Hivinau GRAFFE
 */

package implementation.controllers;

import helpers.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import common.annotations.*;
import common.serializable.*;
import implementation.events.*;
import implementation.views.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class PrivateChatController extends JPanel {
	
	private static final Color ONLINE_COLOR = Color.GREEN;
	private static final Color OFFLINE_COLOR = Color.RED;
	private static final Color CONTACT_COLOR = Color.BLUE;
	
	private final FriendsView friendsView;
	private final ChatAreaView chatAreaView;
	private final JLabel statusLabel;
	private final Set<JButton> contacts;
	private final Map<User, ArrayList<String>> posts;
	
	private ActionListener sendClickListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String message = chatAreaView.getWriterText();

			chatAreaView.setReaderText(Resource.getInstance().getString("self"), message);
			
			ArrayList<User> users = new ArrayList<>();
			
			for(JButton button: contacts) {
				
				String username = button.getText();
				
				for(Map.Entry<User, ArrayList<String>> entry: posts.entrySet()) {
					
					User user = entry.getKey();
					
					if(user.getFormattedName().equals(username)) {
						
						users.add(user);
					}
				}
			}
			
			observable.handlePost(users, message);
		}
	};
	
	public ChatObservable observable = new ChatObservable();
	
	public PrivateChatController() {
		super(new GridLayout(1, 1));
		
		friendsView = new FriendsView();
        statusLabel = friendsView.addStatusLabel(Resource.getInstance().getString("self"), PrivateChatController.OFFLINE_COLOR);
        contacts = new HashSet<>();
        
        chatAreaView = new ChatAreaView();
        chatAreaView.registerButtonListener(sendClickListener);
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, friendsView, chatAreaView);
		split.setContinuousLayout(true);
        split.setResizeWeight(.25);
        split.setDividerSize(2);
        
        add(split);
        
        posts = new HashMap<>();
	}
	
	public void addUser(User user) {
		
		JButton button = friendsView.addButton(user.getFormattedName(), PrivateChatController.CONTACT_COLOR);
		contacts.add(button);
		
		posts.put(user, new ArrayList<>());
	}
	
	public void addPost(User user, String message) {
		
		utils.Log.i(PrivateChatController.class.getName(), message);
		
		ArrayList<String> messages = posts.get(user);
		messages.add(message);
		
		posts.put(user, messages);
	}
	
	public void removeUser(User user) {
		
		Iterator<JButton> iterator = contacts.iterator();
		
		while(iterator.hasNext()) {
			
			JButton button = iterator.next();
			
			if(button.getText().equals(user.getFormattedName())) {
				
				friendsView.removeButton(button);
				iterator.remove();
				break;
			}
		}
		
		posts.remove(user);
	}
	
	public void removeAllUser() {
		
		Iterator<JButton> iterator = contacts.iterator();
		
		while(iterator.hasNext()) {
			
			friendsView.removeButton(iterator.next());
			iterator.remove();
			break;
		}
	}
	
	public void changeConnexionState(boolean state) {
		
		statusLabel.setIcon(new Icon() {
			
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				
				g.setColor(state ? PrivateChatController.ONLINE_COLOR : PrivateChatController.OFFLINE_COLOR);
				g.fillOval(x, y, getIconWidth(), getIconHeight());
                g.drawOval(x, y, getIconWidth(), getIconHeight());
			}
			
			@Override
			public int getIconWidth() {

				return 10;
			}
			
			@Override
			public int getIconHeight() {

				return 10;
			}
		});
	}
}
