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

package common.protocols;

import server.*;
import helpers.*;
import java.io.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class AuthenticationProtocol extends BaseProtocol {
	
	public static final String IDENTITY_REQUEST = "Who are you?";
	public static final String IDENTITY_RESPONSE = "I am one client";
	
	private final ClientManager clientManager;
	
	public AuthenticationProtocol(ClientManager clientManager) throws IOException {
		super(clientManager.getSocket());

		this.clientManager = clientManager;
	}

	@Override
	public void run() {
		
		try {
			
			String serverMessage;
            String clientMessage;
			
			//server needs user profile to map client identity
			//init first request
			Message request = new Message(AuthenticationProtocol.IDENTITY_REQUEST, null);
			
			//send request to client
			serverMessage = Serializer.serialize(request);
            writer.println(serverMessage);

            while ((clientMessage = reader.readLine()) != null) {
            	
            	//retrieve client response
            	Message response = (Message) Serializer.deserialize(clientMessage);
            	
            	if(response.getCommand().equals(AuthenticationProtocol.IDENTITY_RESPONSE)) {
            		
            		Object user = response.getData();
            		
            		if(user instanceof User) {

            			//handle user profile of client
                		clientManager.sendUserProfil((User) user);
            		}
            		break;
            	}
            }
			
		} catch(Exception exception) {
			
			//handle error for client
			clientManager.sendProcessingError(exception.getMessage());
			
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
}
