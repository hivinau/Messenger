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
import utils.ui.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import common.annotations.*;
import implementation.controllers.*;
import implementation.controllers.SettingsController.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class Application extends Blurable implements ActionListener, SettingsControllerListener {
	
	private final ClientController clientController;
	private final SettingsController settingsController;
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
			
			Log.i(Application.class.getName(), "windowClosing called");
			
			Resource resource = Resource.getInstance();
			
			final String title = resource.getString("close_app__title");
			final String message = resource.getString("close_app__message");
			
			int canClose = JOptionPane.showConfirmDialog(Application.this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if(canClose == JOptionPane.YES_OPTION) {
				
				try {
					
					clientController.stop();
					
					Log.i(Application.class.getName(), "application will be closed");
					System.exit(0);
					
				} catch(Exception exception) {
					
					Log.e(Application.class.getName(), exception.getMessage());
				}
			}
		}
		
	};

	public Application(String title) {
		super(title);
		
		setMinimumSize(new Dimension(600, 400));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
        JTabbedPane tab = new JTabbedPane();
        container.add(tab);
        
        JMenuItem settings = new JMenuItem(Resource.getInstance().getString("settings"));
        settings.addActionListener(this);
        
        JMenu fileMenu = new JMenu(Resource.getInstance().getString("menu_bar"));
        fileMenu.add(settings);
        
        JMenuBar menus = new JMenuBar();
        menus.add(fileMenu);
        setJMenuBar(menus);

		settingsController = new SettingsController(this, Resource.getInstance().getString("settings"));
		settingsController.addSettingsControllerListener(this);
		
        clientController = new ClientController(tab); 
		
		addWindowListener(windowAdapter);
		
		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		settingsController.setVisible(true);
	}

	@Override
	public void visibilityChanged(SettingsController settingsController, boolean shown) {
		
		if(settingsController.valuesUpdated()) {
			
			clientController.stop();
		}
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
				
				Application rootController = new Application(title);
				rootController.setVisible(true);
				
				Log.i(Application.class.getName(), title + " launched");
			}
		});
	}
}
