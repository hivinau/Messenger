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

package implementation;

import utils.*;
import helpers.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import common.annotations.*;
import implementation.views.*;
import implementation.controllers.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class Application extends JFrame {
	
	private final ServerController serverController;
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
			
			Log.i(Application.class.getName(), "Window is in the process of being closed");
			
			Resource resource = Resource.getInstance();
			
			final String title = resource.getString("close_app__title");
			final String message = resource.getString("close_app__message");
			
			int canClose = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if(canClose == JOptionPane.YES_OPTION) {
				
				try {
					
					serverController.destroy();
					
					Log.i(Application.class.getName(), "Application is closed");
					System.exit(0);
					
				} catch(Exception exception) {
					
					Log.e(Application.class.getName(), exception.getMessage());
				}
			}
		}
	};

	public Application(String title) {
		super(title);
		
		setPreferredSize(new Dimension(400, 400));
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
		SettingsView settingsView = new SettingsView();
		container.add(settingsView);
		
		serverController = new ServerController(settingsView); 
		
		addWindowListener(windowAdapter);
		
		pack();
		setLocationByPlatform(true);
	}

	public static void main(String[] args) {
		
		try {
			
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            
        	Log.e(Application.class.getName(), exception.getMessage());
        }
		
		UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				String title = Resource.getInstance().getString("app__name");
				
				Application application = new Application(title);
				application.setVisible(true);
				
				Log.i(Application.class.getName(), title + " launched");
			}
		}); 
	}
}
