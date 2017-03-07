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

package utils;

import java.util.*;
import javax.swing.*;
import java.util.prefs.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class PreferencesWriter extends SwingWorker<Integer, String> {
	
	private final Preferences preferences;
	private final Map<String, Object> properties;
	
	public PreferencesWriter(Preferences preferences, Map<String, Object> properties) {
		
		this.preferences = preferences;
		this.properties = properties;
	}

	@Override
	protected Integer doInBackground() throws Exception {
		
		int progress = 1;
		
		for(Map.Entry<String, Object> entry: properties.entrySet()) {

			setProgress(progress);
			
			String key = entry.getKey();
			Object value = entry.getValue();
			
			if(value instanceof String) {
				
				preferences.put(key, (String) value);
				
			} else if(value instanceof Integer) {
				
				preferences.putInt(key, (int) value);
				
			} else if(value instanceof Boolean) {
				
				preferences.putBoolean(key, (boolean) value);
			}
			
			progress++;
		}
		
		return progress;
	}
	

}
