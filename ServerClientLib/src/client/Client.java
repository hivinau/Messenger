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

package client;

import common.*;
import helpers.*;
import java.io.*;
import java.net.*;
import java.util.*;
import client.event.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class Client extends ClientObservable implements Runnable {

	private final Socket socket;

    private PrintWriter writer = null;
	private BufferedReader reader = null;
	private boolean isOnline = false;
	
	public Client(String host, int port) throws UnknownHostException, IOException {
		
		socket = new Socket(host, port);
	}
	
	@Override
	public void run() {
		
		boolean identified = false;
		
		while(socket != null && !socket.isClosed()) {
			
			try {
				
				writer = new PrintWriter(socket.getOutputStream());                  
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
	            
	            if(!identified) {

		            //AUTHENTIFICATION PROTOCOL
	            	
	            	//check server request with timeout
		            String request = read();
		            
		            if(request == null) {
		            	
		            	//server is unreachable
		            	handleStatus(false);
		            	break;
		            }
    	            
    	            //transform String content to Message object
		            Message identityRequest = (Message) Serializer.deserialize(request);
		            
		            //check if request command is IDENTITY_REQUEST
	            	if(identityRequest != null && identityRequest.getCommand().equals(Command.IDENTITY_REQUEST)) {
	            		
	            		//ask to retrieve user profile
	            		List<User> users = retrieveClientUser();
	            		
	            		if(users.size() >= 1) {
	            			
	            			User user = users.get(0);
		            		
		            		//client will send user profile to server
							Message identityResponse = new Message(Command.IDENTITY_RESPONSE, user);
				            sendMessage(identityResponse);
				            
				            //client waits for server response
				            String response = read();
		    	            
		    	            //transform String content to Message object
				            Message status = (Message) Serializer.deserialize(response);
				            
				            //check if status is ONLINE
				            if(status != null && status.getCommand().equals(Command.ONLINE)) {
				            	
				            	handleStatus(true);
			            		
			            		identified = true;
			            		isOnline = true;
					            
					            //next loops, cursor will be on READ PROTOCOL
				            }
	            		}
		            	
		            	//client needs to be identified by server, 
	            		//next loops, cursor will be on AUTHENTIFICATION PROTOCOL again
	            	}
	            	
	            } else {
    	            
    	            //READ PROTOCOL
            		
            		//client can listen messages from server on loop
            		
            		while(isOnline) {
        	            
        	            //wait for server message with timeout
            			String content = read();
            			
            			if(content == null) {
    		            	
    		            	//server is unreachable

		            		identified = false;
		            		isOnline = false;
			            	handleStatus(false);
            				break;
            			}
        	            
        	            //transform String content to Message object
                    	Message message = (Message) Serializer.deserialize(content);
                    	
                    	if(message != null) {
                    		
                        	final String command = message.getCommand();
                        	final Object data = message.getData();
                			
                			if(command != null && (command.equals(Command.CONTACT_ONLINE) || command.equals(Command.CONTACT_OFFLINE))) {
                    			
                    			if(data != null && data instanceof User) {
                    				
                    				User user = (User) data;
                        			
                        			handleContactStatus(user, command.equals(Command.CONTACT_ONLINE));
                    			}
                			} else if(command != null && command.equals(Command.SERVER_OFFLINE)) {

    		            		isOnline = false;
    			            	handleStatus(false);
    			            	
                			} else if(command != null && command.equals(Command.PRIVATE_MESSAGE)) {

                    			if(data != null && data instanceof ReceivingPost) {
                    				
                    				ReceivingPost post = (ReceivingPost) data;
                    				
                    				handlePost(post.getSource(), post.getMessage());
                    			}
                			}
                    	}
            		}
	            }
            	
			} catch(Exception exception) {

				handleError(exception);
			}
		}
	}
	
	public void sendMessage(Message message) throws IOException {

		//transform Message object to String content
		String content = Serializer.serialize(message);

		if(writer != null) {

			//client send to server
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
	
	public void yield() {
		
		try {
			
			sendMessage(new Message(Command.OFFLINE, null));
			close();

    		isOnline = false;
			
		} catch (IOException exception) {

			handleError(exception);
		}
	}
	
	private void close() throws IOException {
		
		writer = null;
		reader = null;
		socket.close();
	}
}
