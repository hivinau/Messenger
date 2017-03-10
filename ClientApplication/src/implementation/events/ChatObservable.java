package implementation.events;

import java.util.*;
import common.event.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Jesus GARNICA OLARRA")
public class ChatObservable extends AbstractObservable {
	
	public ChatObservable() {
		super();
		
	}
	
	public void handlePost(ArrayList<User> user, String message) {
		
		for(IObserver ob: observers) {
			
			AbstractChatObserver observer = (AbstractChatObserver) ob;
			observer.sendPost(user, message);
		}
	}
}
