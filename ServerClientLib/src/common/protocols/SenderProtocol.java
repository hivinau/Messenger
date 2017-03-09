package common.protocols;

import server.*;
import java.io.*;
import helpers.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class SenderProtocol extends BaseProtocol {
	
	public static final String USER_CONNECTED = "new user is online";

	private final ClientManager clientManager;
	private final Message message;
	
	public SenderProtocol(ClientManager clientManager, Message message) throws IOException {
		super(clientManager.getSocket());

		this.clientManager = clientManager;
		this.message = message;
	}

	@Override
	public void run() {

		try {
			
			//server send message to client
            writer.println(Serializer.serialize(message));
			writer.flush();
			
		} catch(Exception exception) {
			
			clientManager.sendEvent(clientManager, exception);
		}
	}

}
