package fr.unicaen.info.users.hivinau_graffe.java.chat.client;

import fr.unicaen.info.users.hivinau_graffe.java.chat.serializable.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public interface IClient {

	public void connect(final int timeout);
	public void disconnect();
	public void send(final Message message);
}
