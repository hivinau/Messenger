/**
 * Copyright 2017
 *
 *
 * Sous licence Apache, Version 2.0 (la "Licence");
 * Vous ne pouvez pas utiliser ce fichier sauf en conformité avec la licence.
 * Vous pouvez obtenir une copie de la licence à l'adresse :
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Sauf si requis par la loi en vigueur ou accord écrit, le logiciel
 * Distribué sous licence est distribué «TEL QUEL»,
 * SANS GARANTIES OU CONDITIONS D'AUCUNE SORTE, express ou implicite.
 * Voir la licence pour les autorisations spécifiques aux différentes langues et
 * Limitations sous la licence.
 *
 * Contribué par : Jesus GARNICA OLARRA, Hivinau GRAFFE
 */

package server;

import java.io.*;
import java.net.*;
import java.util.*;
import server.event.*;
import common.Command;
import common.annotations.*;
import common.serializable.*;
import java.util.concurrent.*;

@Developer(name="Hivinau GRAFFE")
public class Server extends AbstractServerObserver {
	
	public static class ServerConfiguration {
		
		private static ServerConfiguration configuration = null;
		
		private int port = 5000;
		private boolean reuseAddress = false;
		private int receiveBufferSize = 4098;
		private int timeout = 4102;
		
		public static ServerConfiguration getInstance() {
			
			if(configuration == null) {
				
				configuration = new ServerConfiguration();
			}
			
			return configuration;
		}
		
		/**
		 * Set port number, or {@code 0} to use a port number that is automatically allocated.
		 * 
		 * @param port The port must be between 0 and 65535, inclusive.
		 * A port number of {@code 0} means that the port number is
		 * automatically allocated, typically from an ephemeral port range.
		 * This port number can then be retrieved by calling
		 * {@link #getLocalPort getLocalPort}.
		 */
		public ServerConfiguration setPort(int port) {
			
			this.port = port;
			
			return this;
		}
		
		/**
	     * Enable/disable the {@link SocketOptions#SO_REUSEADDR SO_REUSEADDR}
	     * socket option.
	     * <p>
	     * When a TCP connection is closed the connection may remain
	     * in a timeout state for a period of time after the connection
	     * is closed (typically known as the {@code TIME_WAIT} state
	     * or {@code 2MSL} wait state).
	     * For applications using a well known socket address or port
	     * it may not be possible to bind a socket to the required
	     * {@code SocketAddress} if there is a connection in the
	     * timeout state involving the socket address or port.
	     * <p>
	     * Enabling {@link SocketOptions#SO_REUSEADDR SO_REUSEADDR} prior to
	     * binding the socket using {@link #bind(SocketAddress)} allows the socket
	     * to be bound even though a previous connection is in a timeout state.
	     * <p>
	     * When a {@code ServerSocket} is created the initial setting
	     * of {@link SocketOptions#SO_REUSEADDR SO_REUSEADDR} is not defined.
	     * Applications can use {@link #getReuseAddress()} to determine the initial
	     * setting of {@link SocketOptions#SO_REUSEADDR SO_REUSEADDR}.
	     * <p>
	     * The behaviour when {@link SocketOptions#SO_REUSEADDR SO_REUSEADDR} is
	     * enabled or disabled after a socket is bound (See {@link #isBound()})
	     * is not defined.
	     *
	     * @param on  whether to enable or disable the socket option
	     * 
	     */
		public ServerConfiguration setReuseAddress(boolean reuseAddress) {
			
			this.reuseAddress = reuseAddress;
			
			return this;
		}
		
		/**
	     * Sets a default proposed value for the
	     * {@link SocketOptions#SO_RCVBUF SO_RCVBUF} option for sockets
	     * accepted from this {@code ServerSocket}. The value actually set
	     * in the accepted socket must be determined by calling
	     * {@link Socket#getReceiveBufferSize()} after the socket
	     * is returned by {@link #accept()}.
	     * <p>
	     * The value of {@link SocketOptions#SO_RCVBUF SO_RCVBUF} is used both to
	     * set the size of the internal socket receive buffer, and to set the size
	     * of the TCP receive window that is advertized to the remote peer.
	     * <p>
	     * It is possible to change the value subsequently, by calling
	     * {@link Socket#setReceiveBufferSize(int)}. However, if the application
	     * wishes to allow a receive window larger than 64K bytes, as defined by RFC1323
	     * then the proposed value must be set in the ServerSocket <B>before</B>
	     * it is bound to a local address. This implies, that the ServerSocket must be
	     * created with the no-argument constructor, then setReceiveBufferSize() must
	     * be called and lastly the ServerSocket is bound to an address by calling bind().
	     * <p>
	     * Failure to do this will not cause an error, and the buffer size may be set to the
	     * requested value but the TCP receive window in sockets accepted from
	     * this ServerSocket will be no larger than 64K bytes.
	     *
	     * @param size the size to which to set the receive buffer size.
	     * 
	     */
		public ServerConfiguration setReceiveBufferSize(int receiveBufferSize) {
			
			this.receiveBufferSize = receiveBufferSize;
			
			return this;
		}
		
		/**
	     * Enable/disable {@link SocketOptions#SO_TIMEOUT SO_TIMEOUT} with the
	     * specified timeout, in milliseconds.  With this option set to a non-zero
	     * timeout, a call to accept() for this ServerSocket
	     * will block for only this amount of time.  If the timeout expires,
	     * a <B>java.net.SocketTimeoutException</B> is raised, though the
	     * ServerSocket is still valid.  The option <B>must</B> be enabled
	     * prior to entering the blocking operation to have effect.  The
	     * timeout must be {@code > 0}.
	     * A timeout of zero is interpreted as an infinite timeout.
	     * @param timeout the specified timeout, in milliseconds
	     */
		public ServerConfiguration setTimeout(int timeout) {
			
			this.timeout = timeout;
			
			return this;
		}
		
		public int getPort() {
			
			return port;
		}
		
		public boolean isReuseAddress() {
			
			return reuseAddress;
		}
		
		public int getReceiveBufferSize() {
			
			return receiveBufferSize;
		}
		
		public int getTimeout() {
			
			return timeout;
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			
			throw new CloneNotSupportedException();
		}
	}

	private final ServerListener serverListener;
	private final ExecutorService executor; 
	private final List<Future<?>> threads;
	private final Map<ClientManager, User> clients;

	private ServerSocket serverSocket = null;
	private boolean running = false;
	
	/**
	 * Creates an unbound server socket
	 * @param serverListener listener to retrieve events
	 * @throws IOException IO error when opening the socket
	 */
	public Server(ServerListener serverListener) throws IOException {
		
		this(serverListener, ServerConfiguration.getInstance());
	}
	
	/**
	 * Creates an unbound server socket
	 * @param serverListener listener to retrieve events
	 * @param configuration configuration for server
	 * @throws IOException IO error when opening the socket
	 */
	public Server(ServerListener serverListener, ServerConfiguration configuration) throws IOException {
		
		this.serverListener = serverListener;
		
		serverSocket = new ServerSocket(configuration.getPort());
		serverSocket.setReuseAddress(configuration.isReuseAddress());
		serverSocket.setReceiveBufferSize(configuration.getReceiveBufferSize());
		serverSocket.setSoTimeout(configuration.getTimeout());
		
		executor = new ThreadPoolExecutor(5, 
				8, 
				60, TimeUnit.SECONDS, 
				new LinkedBlockingQueue<Runnable>());
		
		threads = Collections.synchronizedList(new LinkedList<>());
		clients = Collections.synchronizedMap(new HashMap<>());
	}
	
	/**
     * Listens for a connection to be made to this socket and accepts
     * it. The method blocks until a connection is made.
     *
     * <p>A new Socket {@code s} is created <p>
     * 
     * @return server addresses
     * 
	 * @throws UnknownHostException 
     *
     */
	public synchronized InetAddress start() throws UnknownHostException {
		
		running = true;

		Thread background = new Thread(new Runnable() {

			@Override
			public void run() {

				while(running) {
					
					try {
						
						Socket socket = serverSocket.accept();
						socket.setSoTimeout(serverSocket.getSoTimeout());
						
						ClientManager clientManager = new ClientManager(socket);
						
						clientManager.registerObserver(Server.this);

						Future<?> thread = executor.submit(clientManager);
						threads.add(thread);
						
					} catch (IOException exception) {
						
						//eventOccured(Server.this, exception);
					}
				}
				
				try {
					
					serverSocket.close();
					
				} catch (IOException exception) {
					
					//eventOccured(Server.this, exception);
					serverSocket = null;
				}
			}
			
		});
		
		background.setPriority(Thread.MIN_PRIORITY);
		background.start();
		
		return InetAddress.getLocalHost();
	}
	
	/**
     * Closes this socket.
     *
     * Any thread currently blocked in {@link #accept()} will throw
     * a {@link SocketException}.
     *
     * <p> If this socket has an associated channel then the channel is closed
     * as well.
     *
     */
	public synchronized void yield()  {
		
		//prevent all clients that server will be closed
		Message offline = new Message(Command.SERVER_OFFLINE, null);
		broadcastMessage(offline);
		
		running = false;
		
		for(Map.Entry<ClientManager, User> entry: clients.entrySet()) {
			
			entry.getKey().unregisterObserver(this);
		}
		
		clients.clear();
		
		for (Iterator<Future<?>> iterator = threads.iterator(); iterator.hasNext();) {
			
			Future<?> thread = iterator.next();

			thread.cancel(true);
			
			iterator.remove();
		}
	}
	
	@Override
	public synchronized void clientStatusChanged(ClientManager client, Object object) {
		
		if(object == null) {
			//client is offline
			
			User user = clients.get(client);
			
			//notify all clients for that event
			Message contactOffline = new Message(Command.CONTACT_OFFLINE, user); 
			broadcastMessage(contactOffline);

			//remove client 
			removeClient(client);
			
			serverListener.eventOccured(new ServerEvent(this));
			
		} else if(object instanceof User) {
			//client is online
			
			//add new client to clients list
			User user = (User) object; 
			
			if(addClient(client, (User) object) == null) { //check if user not exist on clients list
				
				serverListener.eventOccured(new ServerEvent(this));
				
				//notify all clients for that event without new client
				//do not use broadcastMessage() to avoid notify client itself
				
				Message contactOnline = new Message(Command.CONTACT_ONLINE, user); 
				for(Map.Entry<ClientManager, User> entry: clients.entrySet()) {
					
					if(!entry.getValue().equals(user)) {

						entry.getKey().sendMessage(contactOnline);
						
						//send all client status to new client
						client.sendMessage(new Message(Command.CONTACT_ONLINE, entry.getValue()));
					}
				}
			}
		}
	}
	
	@Override
	public synchronized void postReceived(ClientManager client, SendingPost post, boolean isPublic) {
		
		if(post != null) {
			
			if(isPublic) {
				
			} else {

				//retrieve sender
				User sender = clients.get(client);
				
				//retrieve all receivers
				ArrayList<User> receivers = post.getDestinations();
				
				if(receivers != null && receivers.size() > 0) {
					
					//sender send post to receivers
					//create message to send
					ReceivingPost receivingPost = new ReceivingPost(sender, post.getMessage());
					Message messagePost = new Message(Command.PRIVATE_MESSAGE, receivingPost);

					for(Map.Entry<ClientManager, User> entry: clients.entrySet()) {
						
						for(User receiver: receivers) {
							
							if(!entry.getValue().equals(receiver)) {

								entry.getKey().sendMessage(messagePost);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public synchronized void errorOccured(Throwable error) {
		
		error.printStackTrace();
	}
	
	/**
	 * Returns the number of clients connected in network.
     *
     * @return  number of clients connected in network.
	 */
	public int getClientsCount() {
		
		return clients.size();
	}
	
	private synchronized void broadcastMessage(Message message) {

		for(Map.Entry<ClientManager, User> entry: clients.entrySet()) {
			
			entry.getKey().sendMessage(message);
		}
	}
	
	private User addClient(ClientManager client, User user) {
		
		return clients.put(client, user);
	}
	
	private boolean removeClient(ClientManager client) {
		
		User user = clients.get(client);
		
		if(user != null) {

			return clients.remove(client, user);
		}
		
		return false;
	}

}