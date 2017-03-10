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

package implementation.controllers;

import utils.*;
import server.*;
import java.io.*;
import java.net.*;
import server.event.*;
import server.Server.*;
import common.annotations.*;
import implementation.views.*;
import implementation.views.SettingsView.*;

@Developer(name="Jesus GARNICA OLARRA")
public class ServerController implements SettingsViewListener, ServerListener {

	private final SettingsView settingsView;
	private Server server = null;
	
	public ServerController(SettingsView settingsView) {
		
		this.settingsView = settingsView;
		this.settingsView.addSettingsViewListener(this);
		
		Log.i(ServerController.class.getName(), "SettingsView has registered serverController as SettingsViewListener");
	}

	@Override
	public void stateChanged(SettingsView view, boolean state) {
		
		if(state) {
			
			try {
				
				start();
				
			} catch (Exception exception) {
				
				Log.e(ServerController.class.getName(), exception.getMessage());
			}
			
		} else {
			
			stop();
		}
	}

	@Override
	public void eventOccured(ServerEvent event) {
		
		int count = ((Server) event.getSource()).getClientsCount();
		
		String log = String.format("Server has %d client%s online", count, count > 1 ? "s" : "");
		Log.i(ServerController.class.getName(), log);
	}
	
	public void destroy() {
		
		stop();
		this.settingsView.removeSettingsViewListener(this);
		Log.i(ServerController.class.getName(), "SettingsView has unregistered serverController");
	}
	
	private void start() throws NumberFormatException, IOException {
		
		//stop if needed
		stop();
		
		//set port
		ServerConfiguration configuration = ServerConfiguration.getInstance().setPort(this.settingsView.getPort());
		
		//launch server with port and default configurations
		server = new Server(this, configuration);
		InetAddress address = server.start();
		
		String log = String.format("Server is started on '%s:%d'", address.getHostAddress(), this.settingsView.getPort());
		Log.i(ServerController.class.getName(), log);
	}
	
	private void stop() {

		if(server != null) {
			
			server.yield();
			server = null;
			
			Log.i(ServerController.class.getName(), "Server is aborted");
		}
	}
}
