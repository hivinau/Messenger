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
public class Message implements Serializable {
	
	private static final long serialVersionUID = 108969423622209854L;

	private String command;
	private Object data;
	
	public Message(String command, Object data) {
		
		this.command = command;
		this.data = data;
	}
	
	public Message() {
		
	}
	
	@Override
	public int hashCode() {
		
		return command.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equals = false;
		
		if(obj instanceof Message) {
			
			Message message = (Message) obj;
			equals = message.command.equals(command) && message.data.equals(data);
		}
		
		return equals;
	}
	
	public void setCommand(String command) {
		
		this.command = command;
	}
	
	public void setData(Object data) {
		
		this.data = data;
	}

	public String getCommand() {
		
		return command;
	}

	public Object getData() {
		
		return data;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		
		stream.defaultReadObject();
	    
	    String command = stream.readUTF();
	    Object data = stream.readObject();
	    
	    this.command = command;
	    this.data = data;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
		stream.writeUTF(command);
		stream.writeObject(data);
	}
}
