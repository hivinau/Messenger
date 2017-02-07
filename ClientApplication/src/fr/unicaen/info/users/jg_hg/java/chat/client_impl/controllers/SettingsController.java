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
import java.util.*;
import java.beans.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.prefs.*;
import javax.swing.SwingWorker.*;
import fr.unicaen.info.users.jg_hg.java.chat.utils.*;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.views.*;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.global.*;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.views.SettingsView.*;

/**
 * 
 * @author Jesus GARNICA OLARRA
 */
@SuppressWarnings("serial")
public class SettingsController extends JDialog implements SettingsViewListener, PropertyChangeListener {
	
	public interface SettingsControllerListener {
		
		void visibilityChanged(SettingsController controller, boolean shown);
	}
	
	private SettingsView settingsView = null;

	private final Set<SettingsControllerListener> listeners;
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {
		
		public void componentShown(ComponentEvent e) {
			
			handleState(true);
			
			Log.i(SettingsController.class.getName(), "Fill fields with stored properties");
			
			Preferences preferences = Preferences.userRoot();
	        
	        settingsView.setUsername(preferences.get(Application.USERNAME, null));
	        settingsView.setHostname(preferences.get(Application.HOSTNAME, null));
	        settingsView.setHostport(preferences.getInt(Application.HOSTPORT, -1));
		}
	};

	public SettingsController(Frame frame, String title) {
		super(frame, Dialog.ModalityType.APPLICATION_MODAL);
		
		setPreferredSize(new Dimension(400, 100));
		setResizable(false);
		setUndecorated(true);
        
		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		settingsView = new SettingsView();
		settingsView.addSettingsViewListener(this);
        
        container.add(settingsView);
		
		addComponentListener(componentAdapter);
        
		pack();
		setLocationRelativeTo(frame);

		listeners = new HashSet<>();
	}

	public void addSettingsControllerListener(SettingsControllerListener listener) {

		listeners.add(listener);
	}

	public void removeSettingsControllerListener(SettingsControllerListener listener) {

		listeners.remove(listener);
	}

	@Override
	public void onSaved(SettingsView view) {
		
		Log.i(SettingsController.class.getName(), "onSaved called");
		
		Map<String, Object> properties = new HashMap<>();
		
		properties.put(Application.USERNAME, view.getUsername());
		properties.put(Application.HOSTNAME, view.getHostname());
		properties.put(Application.HOSTPORT, view.getHostPort());
		
		PreferencesWriter writer = new PreferencesWriter(Preferences.userRoot(), properties);
		writer.addPropertyChangeListener(this);
		writer.execute();
		
		onCancelled(view);
	}

	@Override
	public void onCancelled(SettingsView view) {
		
		Log.i(SettingsController.class.getName(), "onCancelled called");
		
		setVisible(false);
		handleState(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		final String property = event.getPropertyName();
		
		if(property != null) {
			
			switch(property) {
			case "state":
				
				Log.i(SettingsController.class.getName(), String.format("Properties saving state: %s", ((StateValue) event.getNewValue()).name()));
				break;
			case "progress":
				
				Log.i(SettingsController.class.getName(), String.format("%d properties have been saved", (Integer) event.getNewValue()));
				break;
				default:
					break;
			}
		}
	}

	private void handleState(boolean shown) {

		for(SettingsControllerListener listener: listeners) {

			listener.visibilityChanged(this, shown);
		}
	}
}
