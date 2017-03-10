package common.serializable;

import java.io.*;
import java.util.*;
import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public class SendingPost {

	private ArrayList<User> destinations;
	private String message;
	
	public SendingPost(ArrayList<User> destinations, String message) {
		
		this.destinations = destinations;
		this.message = message;
	}
	
	public ArrayList<User> getDestinations() {
		
		return destinations;
	}
	
	public String getMessage() {
		
		return message;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		
		stream.defaultReadObject();
	    
	    Object object = stream.readObject();
	    String message = stream.readUTF();
	    
		this.destinations = (ArrayList<User>) object;
	    this.message = message;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
		stream.writeObject(destinations);
		stream.writeUTF(message);
	}
}
