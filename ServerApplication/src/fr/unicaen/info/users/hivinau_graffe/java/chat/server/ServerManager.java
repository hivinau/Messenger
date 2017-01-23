package fr.unicaen.info.users.hivinau_graffe.java.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import fr.unicaen.info.users.hivinau_graffe.java.chat.server.clients.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class ServerManager {
	
	private static final int CORE_POOL_SIZE = 1;
	private static final int MAXIMUM_THREADS = 5;
	
	private final ServerSocket server;
	private final Set<ClientRunnable> clients;
	private final ExecutorService clientsExecutor; 
	
	private boolean running = false;

	public ServerManager(int port) throws IOException {
		
		server = new ServerSocket(port);
		clients = new HashSet<>();
		clientsExecutor = new ThreadPoolExecutor(ServerManager.CORE_POOL_SIZE, ServerManager.MAXIMUM_THREADS, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	public void listen() throws IOException {
		
		running = true;
		
		System.out.println("Listening on port : " + server.getLocalPort());
		
		while(running) {
			
			Socket socket = server.accept();
			
			ClientRunnable client = new ClientRunnable(socket);
			
			clients.add(client);
			clientsExecutor.submit(client);
		}
	}
	
	public void stop() {
		
		running = false;
	}
}
