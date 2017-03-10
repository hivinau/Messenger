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

package server;

import common.*;
import java.io.*;
import helpers.*;
import java.net.*;
import server.event.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class ClientManager extends ServerObservable implements Runnable {
	
	public interface ClientManagerListener {

		/**
		 * Map client/User
		 * @param client client identified by socket
		 * @param user profile user to map with client
		 */
		void map(ClientManager client, User user);
		void throwError(String error);
	}
	
	private Socket socket = null;
	private PrintWriter writer = null;
	private BufferedReader reader = null;
	private boolean isOnline = false;
	
	public ClientManager(Socket socket) throws SocketException {
		
		this.socket = socket;
	}
	
	public Socket getSocket() {
		
		return socket;
	}
	
	@Override
	public int hashCode() {
		
		return socket.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equals = false;
		
		if(obj instanceof ClientManager) {
			
			ClientManager clientManager = (ClientManager) obj;
			equals = clientManager.socket.equals(socket);
		}
		
		return equals;
	}
	
	@Override
	public void run() {
		
		boolean identified = false;
		
		while(!socket.isClosed()) {
			
			try {
				
				writer = new PrintWriter(socket.getOutputStream());                  
				reader = new BufferedReader( new InputStreamReader(socket.getInputStream()));    
	            
	            if(!identified) {

		            //AUTHENTIFICATION PROTOCOL
					
					//server needs user profile to map client identity
					//init first request
					Message identityRequest = new Message(Command.IDENTITY_REQUEST, null);
		            sendMessage(identityRequest);
		            
		            //check client response with timeout
		            String response = read();
		            
		            if(response == null) {
		            	
		            	//client is unreachable
    	    			handleStatus(ClientManager.this, null);
		            	break;
		            }
    	            
    	            //transform String content to Message object
		            Message identityResponse = (Message) Serializer.deserialize(response);
	            	
	            	//check if response command is IDENTITY_RESPONSE
	            	//and response contains user profil
	            	if(identityResponse != null && identityResponse.getCommand().equals(Command.IDENTITY_RESPONSE) &&
	            			identityResponse.getData() != null && identityResponse.getData() instanceof User) {
	            		
	            		//server prevent client that status is online
			            Message status = new Message(Command.ONLINE, null);
			            sendMessage(status);
			            
	            		//server will map user profil to this client
            			handleStatus(ClientManager.this, identityResponse.getData());
	            		
	            		identified = true;
	            		isOnline = true;
			            
			            //next loops, cursor will be on READ PROTOCOL
	            	}
	            	
	            	//server needs identity, 
            		//next loops, cursor will be on AUTHENTIFICATION PROTOCOL again
	            	
	            } else {
    	            
    	            //READ PROTOCOL
            		
            		//server can listen messages from this client on loop
            		
            		while(isOnline) {
        	            
        	            //wait for client message with timeout
            			String content = read();
            			
            			if(content == null) {
    		            	
    		            	//client is unreachable

                    		isOnline = false;
    	            		identified = false;
        	    			handleStatus(ClientManager.this, null);
            				break;
            			}
        	            
        	            //transform String content to Message object
                    	Message message = (Message) Serializer.deserialize(content);
                		
                    	final String command = message.getCommand();
                    	
                    	if(command != null && command.equals(Command.OFFLINE)) {

                			//client needs to disconnect from server

                    		isOnline = false;
    	            		identified = false;
        	    			handleStatus(ClientManager.this, null);
        	    			
                			close();
                    	}
            		}
	            }
            	
			} catch(Exception exception) {

				handleError(exception);
			}
		}
	}
	
	/**
	 * Send message to client.
	 * @param message message to send.
	 */
	public void sendMessage(Message message) {

		//transform Message object to String content
		String content = Serializer.serialize(message);

		if(writer != null) {

			//server send to client
	        writer.println(content);
	        writer.flush();
		}
	}
	  
	private String read() throws IOException {
		
		String result = null;
		
		while(reader != null && !reader.ready());
		
		if(reader != null) {

			result = reader.readLine();
		}
		
		return result;
	}
	
	private void close() throws IOException {
		
		writer = null;
		reader = null;
		socket.close();
	}
}

