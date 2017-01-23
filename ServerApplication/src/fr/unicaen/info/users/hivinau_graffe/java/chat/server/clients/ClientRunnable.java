package fr.unicaen.info.users.hivinau_graffe.java.chat.server.clients;

import java.io.*;
import java.net.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class ClientRunnable implements Runnable {
	
	private final Socket socket;

	public ClientRunnable(Socket socket) {
		super();
		
		this.socket = socket;
	}

	@Override
	public void run() {
		
		BufferedReader reader = null;
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String content = "";
			String line = reader.readLine();
            
            do {
            	
            	if(line != null) {
            		
                	content += line;
            	}
            	
            	line = reader.readLine();
            	
            } while(line != null);
            
            System.out.println(content);
			
		} catch(Exception exception) {
			
		}
		 finally {
			 
			 if(reader != null) {
				 
				 try {
					reader.close();
					
				} catch (Exception ignored) {}
			 }
		 }
	}
}
