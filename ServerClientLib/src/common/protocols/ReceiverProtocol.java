package common.protocols;

import server.*;
import java.io.*;
import helpers.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class ReceiverProtocol extends BaseProtocol {
	
	public static final String USER_DISCONNECTED = "user is offline";

	private final ClientManager clientManager;
	
	public ReceiverProtocol(ClientManager clientManager) throws IOException {
		super(clientManager.getSocket());

		this.clientManager = clientManager;
	}

	@Override
	public void run() {

        while (!clientManager.getSocket().isClosed()) {
    		System.out.println("listen message");
        	
        	try {
    			
    			//client send message to server
                String clientMessage = reader.readLine();
                
                if(clientMessage == null) {
                    
                    //client is offline
                	break;
                }
                
                //retrieve client response
            	Message response = (Message) Serializer.deserialize(clientMessage);
        		System.out.println(response.getCommand());
            	
            	if(response.getCommand().equals(ReceiverProtocol.USER_DISCONNECTED)) {
            		
            		//client wants to disconnect
            		break;
            	}
            	
            	clientManager.sendEvent(clientManager, response);
    			
    		} catch(IOException exception) {
        		System.out.println(exception.getMessage());
    			
    			clientManager.sendEvent(clientManager, exception);
    			
    		} catch (ClassNotFoundException exception) {
    			
    			clientManager.sendEvent(clientManager, exception);
			}
        }
		
        //client is offline because it is unreachable
    	clientManager.sendEvent(clientManager, new Message(ReceiverProtocol.USER_DISCONNECTED, null));
		
		release();
		clientManager.release();
	}

}
