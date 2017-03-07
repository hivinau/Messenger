package common.protocols;

import server.*;
import java.io.*;
import helpers.*;
import common.serializable.*;

public class BroadcastProtocol extends BaseProtocol {
	
	public static final String USER_CONNECTED = "new user is online";

	private final ClientManager clientManager;
	private final Message message;
	
	public BroadcastProtocol(ClientManager clientManager, Message message) throws IOException {
		super(clientManager.getSocket());

		this.clientManager = clientManager;
		this.message = message;
	}

	@Override
	public void run() {

		try {
			
			//server send message to client
			String broadcastMessage = Serializer.serialize(message);
            writer.println(broadcastMessage);
			
		} catch(Exception exception) {
			
			//handle error for client
			clientManager.sendProcessingError(exception.getMessage());
			
		} finally {
			
			if(reader != null) {
				
				try {
					reader.close();
				} catch (Exception ignored) {}
			}
			
			if(writer != null) {
				
				try {
					writer.close();
				} catch (Exception ignored) {}
			}
		}
	}

}
