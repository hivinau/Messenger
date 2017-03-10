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

package implementation.controllers;

import client.*;
import helpers.*;

import java.io.IOException;
import java.util.*;
import javax.swing.*;
import client.event.*;
import java.util.prefs.*;

import common.Command;
import common.annotations.*;
import common.serializable.*;
import java.util.concurrent.*;

import implementation.events.AbstractChatObserver;
import implementation.global.*;
import implementation.helpers.*;

@Developer(name="Jesus GARNICA OLARRA")
public class ClientController extends AbstractClientObserver {
	
	private final PrivateChatController privateChatController;
	private final PublicChatController publicChatController;
	private final ExecutorService executor; 
	private final List<Future<?>> threads;
	
	private Client client = null;
	
	private AbstractChatObserver chatObserver = new AbstractChatObserver() {
		
		@Override
		public void errorOccured(Throwable arg0) {
			// TODO Auto-generated method stub
			
		}
		
		public void sendPost(ArrayList<User> user, String post) {
			
			try {
				
				SendingPost sendingPost = new SendingPost(user, post);
				Message message = new Message(Command.PRIVATE_MESSAGE, sendingPost);
				
				client.sendMessage(message);
			} catch (IOException exception) {
				
				exception.printStackTrace();
			}
		}
		
	};
	
	public ClientController(JTabbedPane tab) {
		
		privateChatController = new PrivateChatController();
		publicChatController = new PublicChatController(this);
		
		privateChatController.observable.registerObserver(chatObserver);
		
        tab.addTab(Resource.getInstance().getString("private_section"), privateChatController);
        tab.addTab(Resource.getInstance().getString("public_section"), publicChatController);
		
		executor = new ThreadPoolExecutor(5, 
				8, 
				60, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>());
		
		threads = Collections.synchronizedList(new LinkedList<>());
        
        start();
	}
	
	@Override
	public void postReceived(User user, String post) {

		privateChatController.addPost(user, post);
	}
	
	@Override
	public void errorOccured(Throwable error) {
		
		error.printStackTrace();
	}
	
	@Override
	public void statusChanged(boolean status) {

		privateChatController.changeConnexionState(status);
		
		if(!status) {

			privateChatController.removeAllUser();
			restart();
		}
	}
	
	
	@Override
	public User getUser() {

		Preferences preferences = Preferences.userRoot();
		
        String name = preferences.get(Constant.USERNAME, null);
        Location location = LocationHelper.randomLocation(0, 10);
        
		User user = new User(name, location);
		
		return user;
	}

	@Override
	public void contactStatusChanged(User user, boolean status) {
		
		if(status) {
			
			privateChatController.addUser(user);
		} else {
			
			privateChatController.removeUser(user);
		}
	}
	
	public synchronized void start()  {
		
		Preferences preferences = Preferences.userRoot();

		final String name = preferences.get(Constant.USERNAME, null);
        final String host = preferences.get(Constant.HOSTNAME, null);
        final int port = preferences.getInt(Constant.HOSTPORT, -1);
        
        if(name != null && host != null && port != -1) { //check if settings are set before connexion

        	new Thread(new Runnable() {
				
				@Override
				public void run() {
			        
			        while(client == null) {

			            try {
			        		
			        		client = new Client(host, port);
			        		client.registerObserver(ClientController.this);
			        		
							Future<?> thread = executor.submit(client);
			    			threads.add(thread);
			    			
			    		} catch (Exception ignored) {}
			        }
				}
				
			}).start();
        }
	}
	
	public synchronized void stop() {

		if(client != null) {
			
			client.yield();
		}
		
		for (Iterator<Future<?>> iterator = threads.iterator(); iterator.hasNext();) {
			
			Future<?> thread = iterator.next();

			thread.cancel(true);
			
			iterator.remove();
		}
	}

	public synchronized void restart() {
		
		stop();
		
		try {
			
			Thread.sleep(3000); //needs to wait, server has to notify all clients first
			
		} catch (InterruptedException ignored) {}
		
		client = null;
		
		start();
	}

}
