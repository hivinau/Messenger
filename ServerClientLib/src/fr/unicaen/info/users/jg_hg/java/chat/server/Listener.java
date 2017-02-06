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
import fr.unicaen.info.users.jg_hg.java.chat.utils.*;
import fr.unicaen.info.users.jg_hg.java.chat.client.*;
import fr.unicaen.info.users.jg_hg.java.chat.server.providers.*;
import fr.unicaen.info.users.jg_hg.java.chat.serializable.objects.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Listener implements IClientHandler {
	
	private static final int MAX_ASK_CONNECTION = 10;
	
	private static final int CORE_POOL_SIZE = 5;
	private static final int MAXIMUM_THREADS = 8;
	
	private final ServerSocket server;
	private final Set<ServiceProvider> serviceProviders;
	private final ExecutorService executor; 
	private final List<Future<?>> threads;
	
	private boolean listening = false;
	
	public Listener(int port) throws IOException {
		
		server = new ServerSocket(port, Listener.MAX_ASK_CONNECTION);
		serviceProviders = new HashSet<>();
		executor= new ThreadPoolExecutor(Listener.CORE_POOL_SIZE, 
				Listener.MAXIMUM_THREADS, 
				1000, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>());
		threads = new ArrayList<>();
		
		listening = false;
	}
	
	public void setTimeout(int timeout) throws SocketException {
		
		server.setSoTimeout(timeout);
	}
	
	public void start() {
		
		if(listening) {
			
			return;
		}
		
		Thread background = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				listening = true;
				
				while(!server.isClosed()) {
					
					try {
						
						Socket socket = server.accept();
						
						ServiceProvider serviceProvider = new ServiceProvider(socket);
						serviceProvider.registerHandler(Listener.this);
						
						if(serviceProviders.add(serviceProvider)) {
							
							Future<?> thread = executor.submit(serviceProvider);
							threads.add(thread);
						}
						
					} catch(Exception exception) {
						
						Log.e(Listener.class.getName(), exception.getMessage());
					}
				}
			}
		});
		
		background.setDaemon(true);
		background.setPriority(Thread.MIN_PRIORITY);
		background.setName(Listener.class.getName());
		background.start();
	}
	
	public void stop() throws IOException {
		
		listening = false;
		
		server.close();
		
		for(ServiceProvider serviceProvider: serviceProviders) {
			
			serviceProvider.unregisterHandler(this);
		}
		serviceProviders.clear();
		
		for (Iterator<Future<?>> iterator = threads.iterator(); iterator.hasNext();) {
			
			Future<?> thread = iterator.next();
			
			if(!thread.isCancelled()) {
				
				thread.cancel(true);
			}
			
			iterator.remove();
		}
		executor.shutdownNow();
	}
	
	public boolean isListening() {
		
		return listening;
	}

	@Override
	public void stateChanged(TcpClient client, int state) {
		
		Log.i(Listener.class.getName(), "state: " + state);
		
		
	}

	@Override
	public void errorOccured(TcpClient client, Exception exception) {
		
		Log.e(Listener.class.getName(), exception.getMessage());
	}

	@Override
	public void messageReceived(TcpClient client, Message message) {
		
		Log.i(Listener.class.getName(), message.toString());
	}

}
