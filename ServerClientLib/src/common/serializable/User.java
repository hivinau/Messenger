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

package common.serializable;

import java.io.*;
import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public class User implements Serializable {

	private static final long serialVersionUID = 5103123805993570136L;
	
	private String name;

	public User(String name) {
		
		this.name = name;
	}

	public User() {
		
	}
	
	@Override
	public int hashCode() {
		
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equals = false;
		
		if(obj instanceof User) {
			
			User user = (User) obj;
			equals = user.equals(this);
		}
		
		return equals;
	}
	
	public String getName() {
		
		return name;
	}
	  
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		
		stream.defaultReadObject();
	    
	    String name = (String) stream.readObject();
	    this.name = name;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
		stream.writeObject(name);
	}
}
