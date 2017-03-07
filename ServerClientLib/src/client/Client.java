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
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Set;
import common.annotations.*;
import common.protocols.AuthenticationProtocol;
import common.protocols.BroadcastProtocol;
import common.serializable.*;
import helpers.Serializer;

@Developer(name="Hivinau GRAFFE")
public class Client implements Runnable {

	private final User user;
	private final Socket socket;
	private final Set<LogEventListener> logs;

    private PrintWriter writer = null;
	private BufferedReader reader = null;
	
	public Client(String name, String host, int port) throws UnknownHostException, IOException {
		
		logs = new HashSet<>();
		
		user = new User(name);
		socket = new Socket(host, port);
	}
	
	public boolean addLogEventListener(LogEventListener listener) {
		
		return logs.add(listener);
	}
	
	public boolean removeLogEventListener(LogEventListener listener) {
		
		return logs.remove(listener);
	}
	
	@Override
	public void run() {
		
		try {
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			
			String serverMessage;
            String clientMessage;

            while ((serverMessage = reader.readLine()) != null) {
            	
            	//convert to message instance
            	Message message = (Message) Serializer.deserialize(serverMessage);
            	
            	//retrieve server command
            	String command = message.getCommand();
            	
            	//check authentication protocol
            	if(command.equals(AuthenticationProtocol.IDENTITY_REQUEST)) {
            		
            		//send user object as client identity
            		Message response = new Message(AuthenticationProtocol.IDENTITY_RESPONSE, user);
            		clientMessage = Serializer.serialize(response);
            		
            		if(clientMessage != null) {
            			
            			writer.println(clientMessage);
            		}
            	} else if(command.equals(Message.Command.AUTHENTICATION_COMPLETED)) {

        			log("authentication success", false);
            		break;
            	} else if(command.equals(BroadcastProtocol.USER_CONNECTED)) {

            		String m = String.format("'%s' connected", ((User) message.getData()).getName());
        			log(m, false);
            		break;
            	}
            }
			
		} catch(Exception exception) {

			log("authentication failed", true);
			
		} finally {
			
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
		}
	}
	
	public void yield()  {
		
		
	}
	
	public User getLogin() {
		
		return user;
	}
	
	private Message getMessageFrom(ObjectInputStream reader) throws ClassNotFoundException, IOException {

		return (Message) reader.readObject();
	}
	
	private void sendMessageWith(ObjectOutputStream writer, Message message) throws IOException {
		
		writer.writeObject(message);
		writer.flush();
	}
	
	private void log(String message, boolean isError) {
		
		for(LogEventListener listener: logs) {
			
			listener.log(message, isError);
		}
	}
}
