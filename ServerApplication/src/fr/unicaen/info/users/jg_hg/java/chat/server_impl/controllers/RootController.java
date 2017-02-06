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

package fr.unicaen.info.users.jg_hg.java.chat.server_impl.controllers;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import fr.unicaen.info.users.jg_hg.java.chat.utils.*;
import fr.unicaen.info.users.jg_hg.java.chat.server.*;
import fr.unicaen.info.users.jg_hg.java.chat.helpers.*;
import fr.unicaen.info.users.jg_hg.java.chat.server_impl.views.*;
import fr.unicaen.info.users.jg_hg.java.chat.server_impl.views.SettingsView.*;

/**
 * 
 * @author Jesus GARNICA OLARRA
 */
@SuppressWarnings("serial")
public class RootController extends JFrame implements SettingsViewListener {
	
	private SettingsView settingsView = null;
	private Listener manager = null;
	
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
		
		setPreferredSize(new Dimension(400, 400));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
		settingsView = new SettingsView();
		settingsView.addSettingsViewListener(this);
		
		container.add(settingsView);
		
		addWindowListener(windowAdapter);
		
		pack();
		setLocationByPlatform(true);
	}

	@Override
	public void stateChanged(final SettingsView view, final boolean state) {
		
		Log.i(RootController.class.getName(), "stateChanged called: state = " + (state ? "true" : "false"));
		
		try {
			
			if(state) {
				
				int port = view.getPort();
				
				manager = new Listener(port);
				manager.start();
				
				Log.i(RootController.class.getName(), "Host started on port " + port);
			} else {
				
				Log.i(RootController.class.getName(), "Host stopped");
				
				if(manager != null) {
					
					manager.stop();
					manager = null;
				}
			}
			
		} catch (Exception exception) {
			
			Log.e(RootController.class.getName(), exception.getMessage());
		}
	}
}
