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

import java.io.*;
import java.net.*;
import java.util.*;
import common.protocols.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class ClientManager implements Runnable {
	
	public interface ClientManagerListener {

		/**
		 * Map client/User
		 * @param client client identified by socket
		 * @param user profile user to map with client
		 */
		void map(ClientManager client, User user);
		void throwError(String error);
	}
	
	private final Socket socket;
	private final Set<ClientManagerListener> listeners;
	
	public ClientManager(Socket socket) {
		
		this.socket = socket;
		listeners = new HashSet<>();
	}
	
	public boolean registerClientManagerListener(ClientManagerListener listener) {
		
		return listeners.add(listener);
	}
	
	public boolean unregisterClientManagerListener(ClientManagerListener listener) {
		
		return listeners.remove(listener);
	}
	
	public void sendMessage(Message message) throws IOException {
		
		//sendMessageWith(writer, message);
	}
	
	public Socket getSocket() {
		
		return socket;
	}
	
	public void sendUserProfil(User user) {
		
		for(ClientManagerListener listener: listeners) {
			
			listener.map(this, user);
		}
	}
	
	public void sendProcessingError(String error) {
		
		for(ClientManagerListener listener: listeners) {
			
			listener.throwError(error);
		}
	}
	
	public void sendPublicMessage(Message message) throws IOException {
		
		BroadcastProtocol broadcast = new BroadcastProtocol(this, message);
		new Thread(broadcast).start();
	}
	
	@Override
	public void run() {
		
		try {
			
			BaseProtocol authentication = new AuthenticationProtocol(this);
			new Thread(authentication).start();
			
		} catch(Exception exception) {

			sendProcessingError(exception.getMessage());
		}
	}
}

