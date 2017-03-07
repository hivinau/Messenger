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
import common.*;
import client.*;
import helpers.*;
import utils.ui.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.prefs.*;
import common.annotations.*;
import implementation.global.*;
import implementation.controllers.SettingsController.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class RootController extends Blurable implements SettingsControllerListener, LogEventListener {
	
	private final SettingsController settingsController;
	private final ExecutorService executor; 
	private final List<Future<?>> threads;
	
	private Client client = null;
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
			
			Log.i(RootController.class.getName(), "windowClosing called");
			
			Resource resource = Resource.getInstance();
			
			final String title = resource.getString("close_app__title");
			final String message = resource.getString("close_app__message");
			
			int canClose = JOptionPane.showConfirmDialog(RootController.this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
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
		
		executor = new ThreadPoolExecutor(5, 
				8, 
				1000, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>());
		
		threads = new ArrayList<>();
		
		setMinimumSize(new Dimension(600, 400));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
        JTabbedPane tab = new JTabbedPane();
        tab.addTab(Resource.getInstance().getString("private_section"), new PrivateChatController(this));
        tab.addTab(Resource.getInstance().getString("public_section"), new JScrollPane(new JTree()));
        
        container.add(tab);
		
		settingsController = new SettingsController(this, Resource.getInstance().getString("settings"));
		settingsController.addSettingsControllerListener(this);
		
		addWindowListener(windowAdapter);
		
		pack();
		setLocationRelativeTo(null);
	}
	
	public void showSettings() {
		
		settingsController.setVisible(true);
	}
	
	@Override
	public void visibilityChanged(SettingsController controller, boolean shown) {
		
		setEnabled(!shown); //(un)blur frame while settings are shown
		
		if(!shown && controller.canConnect) {
			
			Preferences preferences = Preferences.userRoot();

	        String name = preferences.get(Application.USERNAME, null);
	        String host = preferences.get(Application.HOSTNAME, null);
	        int port = preferences.getInt(Application.HOSTPORT, -1);
			
	        if(name != null && host != null && port != -1) {
	        	
	        	try {
	    	        
	    	        String message = String.format("%s connecting to '%s:%d'", name, host, port);
	    			Log.i(RootController.class.getName(), message);
					
	    			client = new Client(name, host, port);
					client.addLogEventListener(this);

					Future<?> thread = executor.submit(client);
					threads.add(thread);
					
				} catch (Exception exception) {
					
					Log.e(RootController.class.getName(), exception.getMessage());
				}
	        }
		} else {

			if(client != null) {

				client.removeLogEventListener(this);
				
				client.yield();
				client = null;
				
				for (Iterator<Future<?>> iterator = threads.iterator(); iterator.hasNext();) {
					
					Future<?> thread = iterator.next();
					
					if(!thread.isCancelled()) {
						
						thread.cancel(true);
					}
					
					iterator.remove();
				}
				executor.shutdownNow();
			}
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
