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

package fr.unicaen.info.users.jg_hg.java.chat.client;

import java.net.*;
import java.util.*;

import fr.unicaen.info.users.jg_hg.java.chat.serializable.objects.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public abstract class TcpClient {
	
	protected Header header = null;
	protected final Socket socket;
	
	private final List<IClientHandler> handlers;
	
	private final Object lock = new Object();
	
	public abstract void changeState(int state);
	public abstract void send(final Message message);

	public TcpClient(Header header, Socket socket) {
		
		this.header = header;
		this.socket = socket;
		handlers = new ArrayList<>();
	}
	
	public TcpClient(Socket socket) {
		this(null, socket);
		
	}

	@Override
	public int hashCode() {

		return socket.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		boolean equals = false;

		if(obj instanceof TcpClient) {

			TcpClient client = (TcpClient) obj;

			equals = socket.equals(client.socket);
		}

		return equals;
	}

	public Socket getSocket() {
		
		return socket;
	}

	public void setHeader(Header header) {
		
		this.header = header;
	}
	
	public Header getHeader() {
		
		return header;
	}
	public void addHandler(IClientHandler handler) {

		if(!handlers.contains(handler)) {
			
			handlers.add(handler);
		}
	}

	public void removeHandler(IClientHandler handler) {

		if(handlers.contains(handler)) {
			
			handlers.remove(handler);
		}
	}
	
	public void handleState(int state) {

		synchronized (lock) {
			
			for(IClientHandler handler: handlers) {
				
				handler.stateChanged(null, state);
			}
		}
	}

	public void handleError(final Exception exception) {

		synchronized (lock) {
			
			for(IClientHandler handler: handlers) {
				
				handler.errorOccured(null, exception);
			}
		}
	}

	public void handleMessage(final Message message) {
		
		synchronized (lock) {
			
			for(IClientHandler handler: handlers) {
				
				handler.messageReceived(null, message);
			}
		}
	}
}
