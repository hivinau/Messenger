package fr.unicaen.info.users.jg_hg.java.chat.client;

import java.net.*;
import fr.unicaen.info.users.jg_hg.java.chat.serializable.*;

public class Client extends ClientHandler {
	
	public static final int CONNECTING = 0;
	public static final int CONNECTED = 1;
	public static final int DISCONNECTING = 2;
	public static final int DISCONNECTED = 3;
	public static final int MESSAGE_SENDING = 4;
	public static final int MESSAGE_SENDED = 5;
	public static final int MESSAGE_RECEIVED = 6;
	
	public Client(Socket socket) {
		super(socket);
	}

	@Override
	public void connect(int timeout) {
		
		handleState(CONNECTING);
		
		//...
		
		handleState(CONNECTED);
	}

	@Override
	public void disconnect() {
		
		handleState(DISCONNECTING);
		
		//...
		
		handleState(DISCONNECTED);
	}

	@Override
	public void send(Message message) {
		
		handleState(MESSAGE_SENDING);
		
		//...
		
		handleState(MESSAGE_SENDED);
	}

}
