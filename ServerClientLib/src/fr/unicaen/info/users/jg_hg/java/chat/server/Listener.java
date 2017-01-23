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

package fr.unicaen.info.users.jg_hg.java.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import fr.unicaen.info.users.jg_hg.java.chat.serializable.protocols.object.*;
import fr.unicaen.info.users.jg_hg.java.chat.server.clients.*;
import fr.unicaen.info.users.jg_hg.java.chat.server.clients.Client.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Listener implements ClientHandler {
	
	private static final int MAX_ASK_CONNECTION = 10;
	
	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_THREADS = 8;
	
	private final ServerSocket server;
	private final Set<Client> clients;
	private final ExecutorService executor; 
	private final List<Future<?>> threads;
	
	private boolean listening = false;
	
	public Listener(int port) throws IOException {
		
		server = new ServerSocket(port, Listener.MAX_ASK_CONNECTION);
		clients = new HashSet<>();
		executor= new ThreadPoolExecutor(Listener.CORE_POOL_SIZE, 
				Listener.MAXIMUM_THREADS, 
				1000, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>());
		threads = new ArrayList<>();
		
	}
	
	public void setTimeout(int timeout) throws SocketException {
		
		server.setSoTimeout(timeout);
	}
	
	public void start() throws IOException {
		
		if(listening) {
			
			return;
		}
		
		listening = true;
		
		while(!server.isClosed()) {
			
			Socket socket = server.accept();
			
			Client client = new Client(socket);
			client.setHandler(this);
			
			if(clients.add(client)) {
				
				Future<?> thread = executor.submit(client);
				threads.add(thread);
			}
		}
	}
	
	public void stop() throws IOException {
		
		listening = false;
		
		server.close();
		clients.clear();
		executor.shutdownNow();
		
		for (Iterator<Future<?>> iterator = threads.iterator(); iterator.hasNext();) {
			
			Future<?> thread = iterator.next();
			
			if(!thread.isCancelled()) {
				
				thread.cancel(true);
			}
			
			iterator.remove();
		}
	}

	@Override
	public void messageReceived(final Client client, final Message message) {
		
		System.out.println(message);
	}

	@Override
	public void disconnected(final Client client) {
		
		System.err.println("tess");
		
		InetAddress serverAddress = client.getEndPoint();
		
		if(serverAddress != null) {
			
			System.out.print(serverAddress);
			System.out.println(" disconnected");
		}
	}

	@Override
	public void errorOccured(final Client client, final Exception exception) {
		
		exception.printStackTrace();
	}

	@Override
	public void connected(Client client) {
		
		InetAddress serverAddress = client.getEndPoint();
		
		if(serverAddress != null) {
			
			System.out.print(serverAddress);
			System.out.println(" connected");
		}
	}

}
