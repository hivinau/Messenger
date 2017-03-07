package implementation;

import utils.*;
import helpers.*;
import javax.swing.*;
import common.annotations.*;
import implementation.controllers.*;

@Developer(name="Jesus GARNICA OLARRA")
public class Main {

	public static void main(String[] args) {
		
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException exception) {
            
        	Log.e(Main.class.getName(), exception.getMessage());
        }
		
		UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				String title = Resource.getInstance().getString("app__name");
				
				RootController rootController = new RootController(title);
				rootController.setVisible(true);
				
				Log.i(Main.class.getName(), title + " launched");
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
