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

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import fr.unicaen.info.users.jg_hg.java.chat.serializable.objects.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Client extends TcpClient {
	
	public static final int CONNECTING = 0;
	public static final int CONNECTED = 1;
	public static final int DISCONNECTING = 2;
	public static final int DISCONNECTED = 3;
	public static final int MESSAGE_SENDING = 4;
	public static final int MESSAGE_SENDED = 5;
	public static final int MESSAGE_RECEIVED = 6;

	public Client(Header header, Socket socket) {
		super(header, socket);
		
	}

	public Client(Socket socket) {
		super(socket);
		
	}
	
	public Client(Header header, String host, int port) throws UnknownHostException, IOException {
		super(header, host, port);
	}
	
	public Client(String host, int port) throws UnknownHostException, IOException {
		super(host, port);
	}

	@Override
	public void changeState(int state) {
		
	}

	@Override
	public void send(Message message) {
		
	}
	
	public void close() throws IOException {
		
		if(socket != null) {
			
			socket.close();
		}
	}

}
