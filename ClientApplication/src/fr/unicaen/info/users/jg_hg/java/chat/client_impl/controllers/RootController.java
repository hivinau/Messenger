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

package fr.unicaen.info.users.jg_hg.java.chat.client_impl.controllers;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

import fr.unicaen.info.users.jg_hg.java.chat.utils.*;
import fr.unicaen.info.users.jg_hg.java.chat.client.*;
import fr.unicaen.info.users.jg_hg.java.chat.helpers.*;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.views.*;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.views.FriendsView.*;

/**
 * 
 * @author Jesus GARNICA OLARRA
 */
@SuppressWarnings("serial")
public class RootController extends JFrame implements FriendsViewListener {
	
	private final SettingsController settingsController;
	private FriendsView friendsView = null;
	private JPanel rightPanel = null;
	private Client client = null;
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
			
			Log.i(RootController.class.getName(), "windowClosing called");
			
			Resource resource = Resource.getInstance();
			
			final String title = resource.getString("close_app__title");
			final String message = resource.getString("close_app__message");
			
			int canClose = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if(canClose == JOptionPane.YES_OPTION) {
				
				try {
					
					Log.i(RootController.class.getName(), "application will be closed");
					System.exit(0);
					
				} catch(Exception exception) {
					
					Log.e(RootController.class.getName(), exception.getMessage());
				}
			}
		}
	};

	public RootController(String title) {
		super(title);
		
		setMinimumSize(new Dimension(400, 400));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
		JSplitPane split = new JSplitPane();
        split.setResizeWeight(.5);
        split.setDividerSize(2);
        
        friendsView = new FriendsView();
        friendsView.addFriendsViewListener(this);
        friendsView.addSelf(Resource.getInstance().getString("self"), Color.RED);
        split.setLeftComponent(friendsView);
        
        rightPanel = new JPanel();
        split.setRightComponent(rightPanel);
        
        container.add(split);
		
		addWindowListener(windowAdapter);
		
		settingsController = new SettingsController(RootController.this, "test");
		
		pack();
		setLocationByPlatform(true);
		setLocationRelativeTo(null);
	}

	@Override
	public void stateChanged(FriendsView view, boolean state) {
		
		if(state) {
			
			settingsController.setVisible(true);
		}
		
		/*
		
		try {
			
			if(state) {
				
				client = new Client("localhost", 8001);
			} else {
				
				if(client != null) {
					
					client.close();
				}
			}
			
		} catch (IOException exception) {
			
			Log.e(RootController.class.getName(), exception.getMessage());
		}
		
		*/
	}
}
