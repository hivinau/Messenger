package implementation.events;

import java.util.*;
import common.event.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Jesus GARNICA OLARRA")
public abstract class AbstractChatObserver implements IObserver {

	public abstract void sendPost(ArrayList<User> user, String message);
}
