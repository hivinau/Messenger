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

import utils.*;
import server.*;
import common.*;
import helpers.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import server.Server.*;
import java.awt.event.*;
import common.annotations.*;
import implementation.views.*;
import implementation.views.SettingsView.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class RootController extends JFrame implements SettingsViewListener, LogEventListener {
	
	private SettingsView settingsView = null;
	private Server server = null;
	
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
	public void stateChanged(final SettingsView settingsView, final boolean state) {
		
		Log.i(RootController.class.getName(), "stateChanged called: state = " + (state ? "true" : "false"));
		
    	try {
			
    		if(state) {
				
				int port = settingsView.getPort();
				
				ServerConfiguration configuration = ServerConfiguration.getInstance()
																	   .setPort(port);
				server = new Server(configuration);
				server.addLogEventListener(this);
				
				InetAddress address = server.start();
		        
		        String message = String.format("Listening on '%s:%d'", address.getHostAddress(), port);
				Log.i(RootController.class.getName(), message);
				
			} else {
				
				Log.i(RootController.class.getName(), "Host stopped");
				
				if(server != null) {

					server.removeLogEventListener(this);
					
					server.yield();
					server = null;
				}
			}
			
		} catch (Exception exception) {
			
			Log.e(RootController.class.getName(), exception.getMessage());
		}
	}

	@Override
	public void log(final String message, final boolean isError) {
		
		UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				if(isError) {

					Log.e(RootController.class.getName(), message);
					
				} else {

					Log.i(RootController.class.getName(), message);
				}
			}
		});
	}
}
