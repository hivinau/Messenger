package fr.unicaen.info.users.jg_hg.java.chat.server_impl;

import fr.unicaen.info.users.jg_hg.java.chat.utils.*;
import fr.unicaen.info.users.jg_hg.java.chat.helpers.*;
import fr.unicaen.info.users.jg_hg.java.chat.server_impl.controllers.*;

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
		
		try {
			
			Listener manager = new Listener(PORT);
			manager.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		*/
	}
}
