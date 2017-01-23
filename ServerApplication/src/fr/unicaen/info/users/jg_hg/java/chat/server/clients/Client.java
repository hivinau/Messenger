package fr.unicaen.info.users.jg_hg.java.chat.server.clients;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Client implements Runnable {
	
	private final Socket socket;
	private final List<ClientHandler> handlers;

	public Client(Socket socket) {
		super();
		
		this.socket = socket;
		handlers = new ArrayList<>();
	}
	
	public void addHandler(ClientHandler handler) {
		
		if(!handlers.contains(handler)) {
			
			handlers.add(handler);
		}
	}

	@Override
	public void run() {
		
		while(socket.isConnected()) {
			
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
	
	public interface ClientHandler {
		
		void messageReceived();
	}
}
