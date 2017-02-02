package fr.unicaen.info.users.jg_hg.java.chat.client_impl;

import fr.unicaen.info.users.jg_hg.java.chat.utils.*;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.controllers.*;
import fr.unicaen.info.users.jg_hg.java.chat.helpers.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Main {

	public static void main(String[] args) {
		
		UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				String title = Resource.getInstance().getString("app__name");
				
				RootController rootController = new RootController(title);
				rootController.setVisible(true);
				
				Log.i(Main.class.getName(), title + " is launched");
			}
		});
		
		/*
	
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
		
		*/
	}

}
