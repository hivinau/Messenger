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

package fr.unicaen.info.users.jg_hg.java.chat.server.clients;

import java.io.*;
import java.net.*;
import fr.unicaen.info.users.jg_hg.java.chat.serializable.protocols.object.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Client implements Runnable {

	private final Socket socket;
	
	private final Object lock = new Object();
	
	private ClientHandler handler;

	public Client(Socket socket) {
		super();

		this.socket = socket;
	}

	public Client(Client client) {
		this(client.socket);

	}

	public void setHandler(ClientHandler handler) {

		this.handler = handler;
	}

	@Override
	public int hashCode() {

		return socket.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		boolean equals = false;

		if(obj instanceof Client) {

			Client client = (Client) obj;

			equals = socket.equals(client.socket);
		}

		return equals;
	}

	public InetAddress getEndPoint() {

		return socket.getInetAddress();
	}

	@Override
	public void run() {

		handleConnectedState();
		
		while(!socket.isClosed()) {
			
			BufferedReader reader = null;
			
			try {

				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String content = "";
				
				String line = reader.readLine();
	            
	            do {
	            	
	            	if(line != null && line.length() > 0) {
	            		
	                	content += line;
	            	}
	            	
	            	line = reader.readLine();
	            	
	            } while(line != null);
				
				final Message message = new Message("test", content);
				handleMessage(message);
				

			} catch(Exception exception) {

				handleError(exception);

			} finally {

				if(reader != null) {

					try {

						reader.close();

					} catch (Exception exception) {

						handleError(exception);
					}
				}
			}
		}
		
		handleDisconnectedState();
	}

	private void handleConnectedState() {

		synchronized (lock) {
			
			if(handler != null) {

				final Client client = new Client(this);
				handler.connected(client);
			}
		}
	}

	private void handleError(final Exception exception) {

		synchronized (lock) {
			
			if(handler != null) {

				final Client client = new Client(this);
				handler.errorOccured(client, exception);
			}
		}
	}

	private void handleMessage(final Message message) {
		
		synchronized (lock) {
			
			if(handler != null) {

				final Client cl = new Client(this);
				handler.messageReceived(cl, message);
			}
		}
	}

	private void handleDisconnectedState() {

		synchronized (lock) {
			
			if(handler != null) {

				final Client client = new Client(this);
				handler.disconnected(client);
			}
		}
	}

	public interface ClientHandler {

		void connected(final Client client);
		void errorOccured(final Client client, final Exception exception);
		void messageReceived(final Client client, final Message message);
		void disconnected(final Client client);
	}
}
