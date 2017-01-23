package fr.unicaen.info.users.jg_hg.java.chat.client;

import fr.unicaen.info.users.jg_hg.java.chat.serializable.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public interface IClient {

	public void connect(final int timeout);
	public void disconnect();
	public void send(final Message message);
}
