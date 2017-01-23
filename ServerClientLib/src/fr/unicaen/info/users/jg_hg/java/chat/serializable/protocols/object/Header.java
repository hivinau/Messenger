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

package fr.unicaen.info.users.jg_hg.java.chat.serializable.protocols.object;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Header {

	private final boolean broadcasting;
	
	public Header(boolean broadcasting) {
		
		this.broadcasting = broadcasting;
	}
	
	public boolean isBroadcasting() {
		
		return broadcasting;
	}
}
