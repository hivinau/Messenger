package fr.unicaen.info.users.jg_hg.java.chat.client_impl;

import fr.unicaen.info.users.jg_hg.java.chat.client_impl.ui.*;
import fr.unicaen.info.users.jg_hg.java.chat.client_impl.ui.helpers.*;
import fr.unicaen.info.users.jg_hg.java.chat.utils.Log;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Main {

	public static void main(String[] args) {
		
		UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				String title = Resources.getInstance().getString("app__name");
				
				MainForm mainForm = new MainForm(title);
				mainForm.setVisible(true);
				
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
