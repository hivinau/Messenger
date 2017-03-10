package common;

import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public interface Command {

	public static final String IDENTITY_REQUEST = "identity_request";
	public static final String IDENTITY_RESPONSE = "identity_response";
	public static final String ONLINE = "online";
	public static final String OFFLINE = "offline";
	public static final String SERVER_OFFLINE = "server_offline";
	public static final String CONTACT_ONLINE = "contact_online";
	public static final String CONTACT_OFFLINE = "contact_offline";
	public static final String PRIVATE_MESSAGE = "private_message";
	public static final String PUBLIC_MESSAGE = "public_message";
}
