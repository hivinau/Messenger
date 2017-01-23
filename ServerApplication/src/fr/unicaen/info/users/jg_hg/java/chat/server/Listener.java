package fr.unicaen.info.users.jg_hg.java.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import fr.unicaen.info.users.jg_hg.java.chat.server.clients.*;
import fr.unicaen.info.users.jg_hg.java.chat.server.clients.Client.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Listener implements ClientHandler {
	
	private static final int MAX_ASK_CONNECTION = 10;
	
	private static final int CORE_POOL_SIZE = 1;
	private static final int MAXIMUM_THREADS = 5;
	
	private final ServerSocket server;
	private final Set<Client> clients;
	private final ExecutorService executor; 
	
	private final List<Future<?>> threads;
	
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
		
		boolean listening = !server.isClosed();
		
		if(listening) {
			
			return;
		}
		
		while(!server.isClosed()) {
			
			Socket socket = server.accept();
			
			Client client = new Client(socket);
			client.addHandler(this);
			
			if(clients.add(client)) {
				
				Future<?> thread = executor.submit(client);
				threads.add(thread);
			}
		}
	}
	
	public void stop() throws IOException {
		
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
	public void messageReceived() {
		// TODO Auto-generated method stub
		
	}
}
