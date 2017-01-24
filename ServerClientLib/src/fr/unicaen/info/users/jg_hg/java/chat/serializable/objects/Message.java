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

package fr.unicaen.info.users.jg_hg.java.chat.serializable.objects;

import java.util.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Message {

	private final String command;
	private final String content;
	
	public Message(String command, String content) {

		this.command = command;
		this.content = content;
	}
	
	public String getCommand() {
		
		return command;
	}

	public String getContent() {
		
		return content;
	}
	
	@Override
	public String toString() {
		
		String description = "";
		
		if(command != null) {
			
			description += String.format(Locale.FRANCE, "command: %s\n", command);
		}
		
		if(content != null) {
			
			description += String.format(Locale.FRANCE, "content: %s\n", content);
		}
		
		return description;
	}
}
