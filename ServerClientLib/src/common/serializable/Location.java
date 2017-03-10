package common.serializable;

import java.io.*;

public class Location implements Serializable {
	
	private static final long serialVersionUID = 9143509630573320486L;
	
	private int x;
	private int y;
	
	public Location() {
		this(0, 0);
		
	}
	
	public Location(int x, int y) {
		
		this.x = x;
		this.y = y;
	}
	
	public Location(Location location) {
		
		this.x = location.x;
		this.y = location.y;
	}
	
	@Override
	public int hashCode() {

		return x * y;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equals = false;
		
		if(obj instanceof Location) {
			
			Location location = (Location) obj;
			equals = location.x == x && location.y == y;
		}
		
		return equals;
	}
	
	public void setX(int x) {
		
		this.x = x;
	}
	
	public int getX() {
		
		return x;
	}
	
	public void setY(int y) {
		
		this.y = y;
	}
	
	public int getY() {
		
		return y;
	}
	  
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		
		stream.defaultReadObject();
	    
	    int x = stream.read();
	    int y = stream.read();
	    
	    this.x = x;
	    this.y = y;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		
		stream.write(x);
		stream.write(y);
	}
}
