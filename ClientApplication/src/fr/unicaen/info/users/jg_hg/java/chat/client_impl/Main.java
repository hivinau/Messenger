package fr.unicaen.info.users.jg_hg.java.chat.client_impl;

import java.io.*;
import java.net.*;

import fr.unicaen.info.users.jg_hg.java.chat.client_impl.client.Client;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.client.ClientListener;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Main {

	public static void main(String[] args) {
		
		Client client = new Client(null);
		
		client.addClientListener(new ClientListener() {
			
			@Override
			public void stateChanged(int state) {
				// TODO Auto-generated method stub
			}
		});
		
		Socket socket = null;

		try {

			socket = new Socket("localhost", 8001);
			
			DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
			stream.writeBytes("test" + '\n');
			
		} catch(Exception exception) {
			
		} finally {
			
			if(socket != null) {
				 
				 try {
					 socket.close();
					
				} catch (Exception ignored) {}
			 }
		}
	}

}
