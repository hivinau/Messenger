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

package fr.unicaen.info.users.jg_hg.java.chat.server.providers;

import java.io.*;
import java.net.*;
import fr.unicaen.info.users.jg_hg.java.chat.client.*;
import fr.unicaen.info.users.jg_hg.java.chat.serializable.objects.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class ServiceProvider implements Runnable {

	private final Client client;

	public ServiceProvider(Socket socket) {
		super();

		this.client = new Client(socket);
	}

	public ServiceProvider(ServiceProvider serviceProvider) {
		this(serviceProvider.client.getSocket());

	}
	
	public void registerHandler(IClientHandler handler) {
		
		client.addHandler(handler);
	}
	
	public void unregisterHandler(IClientHandler handler) {
		
		client.removeHandler(handler);
	}
	
	@Override
	public int hashCode() {
		
		return client.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		boolean equals = false;
				
		if(obj instanceof ServiceProvider) {
			
			ServiceProvider provider = (ServiceProvider) obj;
			equals = client.equals(provider.client);
		}
		
		return equals;
	}

	@Override
	public void run() {

		client.handleState(Client.CONNECTED);
		
		Socket socket = client.getSocket();
		
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
				client.handleMessage(message);
				

			} catch(Exception exception) {

				client.handleError(exception);

			} finally {

				if(reader != null) {

					try {

						reader.close();

					} catch (Exception exception) {

						client.handleError(exception);
					}
				}
			}
		}

		client.handleState(Client.DISCONNECTED);
	}

	
}
