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
import java.awt.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.prefs.*;
import common.annotations.*;
import implementation.views.*;
import implementation.global.*;
import javax.swing.SwingWorker.*;
import implementation.views.SettingsView.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class SettingsController extends JDialog implements SettingsViewListener, PropertyChangeListener {
	
	public interface SettingsControllerListener {
		
		void visibilityChanged(SettingsController controller, boolean shown);
	}

	private final Set<SettingsControllerListener> listeners;
	
	private boolean valuesChanged = false;
	private SettingsView settingsView = null;
	
	private ComponentAdapter componentAdapter = new ComponentAdapter() {
		
		public void componentShown(ComponentEvent e) {
			
			handleState(true);
			
			Log.i(SettingsController.class.getName(), "Fill fields with stored properties");
			
			Preferences preferences = Preferences.userRoot();
	        
	        settingsView.setUsername(preferences.get(Constant.USERNAME, null));
	        settingsView.setHostname(preferences.get(Constant.HOSTNAME, null));
	        settingsView.setHostport(preferences.getInt(Constant.HOSTPORT, -1));
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

	@Override
	public void onSaved(SettingsView view) {
		
		Log.i(SettingsController.class.getName(), "onSaved called");
		
		Map<String, Object> properties = new HashMap<>();
		
		properties.put(Constant.USERNAME, view.getUsername());
		properties.put(Constant.HOSTNAME, view.getHostname());
		properties.put(Constant.HOSTPORT, view.getHostPort());
		
		PreferencesWriter writer = new PreferencesWriter(Preferences.userRoot(), properties);
		writer.addPropertyChangeListener(this);
		writer.execute();
		
		valuesChanged = true;
		close();
	}

	@Override
	public void onCancelled(SettingsView view) {
		
		Log.i(SettingsController.class.getName(), "onCancelled called");
		
		valuesChanged = false;
		close();
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

	public boolean addSettingsControllerListener(SettingsControllerListener listener) {

		return listeners.add(listener);
	}

	public boolean removeSettingsControllerListener(SettingsControllerListener listener) {

		return listeners.remove(listener);
	}
	
	public boolean valuesUpdated() {
		
		return valuesChanged;
	}
	
	private void close() {
		
		setVisible(false);
		handleState(false);
	}

	private void handleState(boolean shown) {

		for(SettingsControllerListener listener: listeners) {

			listener.visibilityChanged(this, shown);
		}
	}
}
