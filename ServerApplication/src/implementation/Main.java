package implementation;

import utils.*;
import helpers.*;
import javax.swing.*;
import common.annotations.*;

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
				
				Application application = new Application(title);
				application.setVisible(true);
				
				Log.i(Main.class.getName(), title + " launched");
			}
		}); 
	}
}
