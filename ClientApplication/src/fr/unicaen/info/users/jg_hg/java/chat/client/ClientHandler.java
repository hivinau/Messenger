package fr.unicaen.info.users.jg_hg.java.chat.client;

import java.net.*;
import java.util.*;

public abstract class ClientHandler implements IClient {
	
	protected final Socket socket;
	private final Set<ClientListener> listeners;

	public ClientHandler(Socket socket) {
		
		this.socket = socket;
		listeners = new HashSet<>();
	}
	
	public void addClientListener(ClientListener listener) {
		
		listeners.add(listener);
	}
	
	public void removeListener(ClientListener listener) {
		
		listeners.remove(listener);
	}
	
	protected void handleState(int state) {
		
		for(ClientListener listener: listeners) {
			
			listener.stateChanged(state);
		}
	}
}
