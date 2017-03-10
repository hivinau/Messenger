package implementation.events;

import common.event.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Jesus GARNICA OLARRA")
public abstract class AbstractChatObserver implements IObserver {

	public abstract void messageReady(User[] user);
}
