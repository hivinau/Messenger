package fr.unicaen.info.users.jg_hg.java.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import fr.unicaen.info.users.jg_hg.java.chat.server.clients.*;

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
	
	public int getPort() {
		
		return server.getLocalPort();
	}

	public void listen() throws IOException {
		
		running = true;
		
		while(running) {
			
			try {
				
				Socket socket = server.accept();
				
				ClientRunnable client = new ClientRunnable(socket);
				
				clients.add(client);
				clientsExecutor.submit(client);
				
			} catch (IOException ignored) {
				
				throw ignored;
			}
		}
	}
	
	public void stop() {
		
		running = false;
	}
}
