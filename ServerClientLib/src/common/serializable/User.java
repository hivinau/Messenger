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
import java.text.*;
import java.util.*;
import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public class User implements Serializable {

	private static final long serialVersionUID = 5103123805993570136L;
	private static final String PATTERN = "###";
	
	private String name;
	private Location location;

	public User(String name) {
		this(name, 0, 0);
		
	}

	public User(String name, Location location) {
		this(name, location.getX(), location.getY());
		
	}

	public User(String name, int x, int y) {
		
		Date createdDate = new Date();
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
		
		this.name = String.format("%s%s%s", formater.format(createdDate), User.PATTERN, name);
		this.location = new Location(x, y);
	}
	
	@Override
	public int hashCode() {
		
		return name.hashCode() + location.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equals = false;
		
		if(obj instanceof User) {
			
			User user = (User) obj;
			equals = user.name.equals(name) && user.location.equals(location);
		}
		
		return equals;
	}
	
	public void setLocation(Location location) {
		
		setLocation(location.getX(), location.getY());
	}
	
	public void setLocation(int x, int y) {
		
		this.location.setX(x);
		this.location.setY(y);
	}
	
	/**
	 * 
	 * @return Name is merged with creation date
	 */
	public String getRawName() {
		
		return name;
	}
	
	/**
	 * 
	 * @return Name without creation date
	 */
	public String getFormattedName() {
		
		return name.split(User.PATTERN)[1];
	}
	  
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		
		stream.defaultReadObject();
	    
	    String name = (String) stream.readUTF();
	    Location location = (Location) stream.readObject();
	    
	    this.name = name;
	    this.location = location;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
		stream.writeUTF(name);
		stream.writeObject(location);
	}
}
