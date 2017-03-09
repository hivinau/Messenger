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

import helpers.*;
import server.ClientManager;

import java.io.*;
import java.net.*;
import client.event.*;
import common.protocols.*;
import common.Command;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class Client implements Runnable {

	private final ClientListener clientListener;
	private final User user;
	private final Socket socket;

    private PrintWriter writer = null;
	private BufferedReader reader = null;
	private boolean authenticated = false;
	private boolean closeConnexion = false;
	private boolean isOnline = false;
	private Object message = null;
	
	public Client(ClientListener clientListener, User user, String host, int port) throws UnknownHostException, IOException {

		this.clientListener = clientListener;
		this.user = user;
		socket = new Socket(host, port);
		
		//reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//writer = new PrintWriter(socket.getOutputStream(), true);
	}
	
	public void sendMessage(Message message) throws IOException {

		//transform Message object to String content
		String content = Serializer.serialize(message);
		
		//server send status to client
        writer.println(content);
        writer.flush();
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
	            	
	            	//check server request with timeout
		            String request = read();
		            
		            if(request == null) {
		            	
		            	//server is unreachable
		            	break;
		            }
    	            
    	            //transform String content to Message object
		            Message identityRequest = (Message) Serializer.deserialize(request);
		            
		            //check if request command is IDENTITY_REQUEST
	            	if(identityRequest.getCommand().equals(Command.IDENTITY_REQUEST)) {
	            		
	            		//client will send user profil to server
						Message identityResponse = new Message(Command.IDENTITY_RESPONSE, user);
			            sendMessage(identityResponse);
			            
			            //client waits for server response
			            String response = read();
	    	            
	    	            //transform String content to Message object
			            Message status = (Message) Serializer.deserialize(response);
			            
			            //check if status is ONLINE
			            if(status.getCommand().equals(Command.ONLINE)) {
		            		
		            		identified = true;
		            		isOnline = true;
				            
				            //next loops, cursor will be on READ PROTOCOL
			            }
		            	
		            	//client needs to be identified by server, 
	            		//next loops, cursor will be on AUTHENTIFICATION PROTOCOL again
	            	}
	            	
	            } else {
	            	
	            	System.out.println("Client is online");
    	            
    	            //READ PROTOCOL
            		
            		//client can listen messages from server on loop
            		
            		while(isOnline) {
        	            
        	            //wait for server message with timeout
            			String content = read();
            			
            			if(content == null) {
    		            	
    		            	//server is unreachable
                    		isOnline = false;
            				break;
            			}
        	            
        	            //transform String content to Message object
                    	Message message = (Message) Serializer.deserialize(content);
                		
                    	final String command = message.getCommand();
                		
                		switch (command) {
						default:
							break;
						}
            		}
	            }
            	
			} catch(Exception exception) {

			}
		}
	}
	  
	private String read() throws IOException {

		return reader.readLine();
	}
	
	/*
	@Override
	public void run() {

        while (!socket.isClosed()) {

        	message = null;

    		try {
    			
    			for(int i = 0; i < 5; i++) {
    				
            		Message offlineMessage = new Message(ReceiverProtocol.USER_DISCONNECTED, null);
            		sendMessage(offlineMessage);
    			}

    			String serverMessage = reader.readLine();
        		
        		if(serverMessage == null) {
        	        
        	        //server is offline
        			authenticated = false;
        			break;
        		}
            	
            	//convert to message instance
            	Message request = (Message) Serializer.deserialize(serverMessage);
            	
            	//retrieve server command
            	String command = request.getCommand();
            	
            	//check authentication protocol
            	if(command.equals(AuthenticationProtocol.IDENTITY_REQUEST)) {
            		
            		//send user object as client identity
            		Message response = new Message(AuthenticationProtocol.IDENTITY_RESPONSE, user);
            		sendMessage(response);
            		
            	} else if(command.equals(AuthenticationProtocol.IDENTITY_ACCEPTED)) {

            		//client accepted by server
            		authenticated = true;
        			clientListener.eventOccured(new ClientEvent(Client.this));
            		
            	} else if(command.equals(SenderProtocol.USER_CONNECTED)) {

            		//other client is online
            		message = ((User) request.getData());
        			clientListener.eventOccured(new ClientEvent(Client.this));
            	}
    			
    		} catch(Exception exception) {
    			
    			exception.printStackTrace();
    			clientListener.eventOccured(new ClientEvent(Client.this));
    		}
        }
        
        //server is offline
		clientListener.eventOccured(new ClientEvent(Client.this));
		
		release();
	}
	*/
	
	public boolean isOnline() {
		
		return authenticated;
	}
	
	public boolean hasMessage() {
		
		return message != null;
	}
	
	public Object getMessage() {
		
		return message;
	}
	
	public synchronized void yield() {
		
		try {
			sendMessage(new Message(Command.OFFLINE, null));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public User getLogin() {
		
		return user;
	}
	
	private void release() {
		
		if(reader != null) {
			
			try {
				
				reader.close();
				
			} catch (Exception ignored) {}
		}
		
		if(writer != null) {
			
			try {
				
				writer.close();
				
			} catch (Exception ignored) {}
		}
		
		if(socket != null) {
			
			try {
				
				socket.close();
				
			} catch (Exception ignored) {}
		}
	}
}
