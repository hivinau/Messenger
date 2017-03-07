package helpers;

import java.io.*;
import java.util.*;
import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public class Serializer {
 
    public static String serialize(Object object) {
    	
    	ObjectOutputStream stream = null;
    	String serialized = null;
    	
    	try {

    		ByteArrayOutputStream converter = new ByteArrayOutputStream();
            stream = new ObjectOutputStream(converter);
            
            stream.writeObject(object);
            
            byte[] buffer = converter.toByteArray();
            
            serialized = Base64.getEncoder().encodeToString(buffer);
            
    	} catch(Exception ignored) {} finally {
    		
    		if(stream != null) {
    			
    			try {
    				
    	            stream.flush();
					stream.close();
				} catch (IOException ignored) {}
    		}
    	}
    		
        return serialized;
    }
 
    public static Object deserialize(String encoded) throws IOException, ClassNotFoundException {

    	ObjectInputStream stream = null;
    	Object object = null;
    	
    	try {
    		
    		byte [] buffer = Base64.getDecoder().decode(encoded);
    		
    		stream = new ObjectInputStream(new ByteArrayInputStream(buffer));
    		object = stream.readObject();
    		
    	} catch(Exception ignored) {} finally {
    		
    		if(stream != null) {
    			
    			try {
					stream.close();
				} catch (IOException ignored) {}
    		}
    	}
    	
        return object;
    }
}
