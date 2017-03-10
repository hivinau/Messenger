package common.serializable;

import java.io.*;
import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public class ReceivingPost {

	private User source;
	private String message;
	
	public ReceivingPost(User source, String message) {
		
		this.source = source;
		this.message = message;
	}
	
	public User getSource() {
		
		return source;
	}
	
	public String getMessage() {
		
		return message;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		
		stream.defaultReadObject();
	    
	    Object source = stream.readObject();
	    String message = stream.readUTF();
	    
	    this.source = (User) source;
	    this.message = message;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
		stream.writeObject(source);
		stream.writeUTF(message);
	}
}
