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

package fr.unicaen.info.users.jg_hg.java.chat.client_impl.ui.helpers;

import fr.unicaen.info.users.jg_hg.java.chat.client_impl.files.*;

public class Resources {

	private static Resources instance = null;
	private static final String STRINGS_FILE = "res/strings.xml";
	private static final String INTEGERS_FILE = "res/integers.xml";
	
	private Resources() {
		
	}
	
	/**
	 * Récupère un singleton de cette classe.
	 * @return {@link lifegame.librairies.utils.Resources}.
	 */
	public static Resources getInstance() {
		
		if(instance == null) {
			
			instance = new Resources();
		}
		
		return instance;
	}
	
	/**
	 * Il est interdit de cloner une instance de {@link Resources}
	 */
	@Override
    public Object clone() throws CloneNotSupportedException {

        throw new CloneNotSupportedException();
    }

	/**
	 * Récupère une chaîne de caractères à partir d'un identifiant depuis strings.xml.
	 * @param id identifiant relatif à une chaîne de caractères.
	 * @return une chaîne de caractères.
	 */
	public String getString(String id) {
		
		String result = null;
		
		try {
			
			XmlReader reader = new XmlReader(Resources.STRINGS_FILE);
			
			result = reader.value(id);
			
		} catch (Exception exception) {
			
			exception.printStackTrace();
		}
		
		return result;
	}

	/**
	 * Récupère un entier à partir d'un identifiant depuis integers.xml.
	 * @param id identifiant relatif à un entier.
	 * @return entier.
	 */
	public int getInt(String id) {
		
		int result = -1;
		
		try {
			
			XmlReader reader = new XmlReader(Resources.INTEGERS_FILE);
			
			result = Integer.parseInt(reader.value(id));
			
		} catch (Exception exception) {
			
			exception.printStackTrace();
		}
		
		return result;
	}
}
