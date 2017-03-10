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

package server.event;

import server.*;
import common.event.*;
import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public class ServerObservable extends AbstractObservable {
	
	public ServerObservable() {
		super();
		
	}
	
	/**
	 * Prevent server that a client is now online or offline.
	 * @param client client which status changed.
	 * @param object if object is null, client is offline.
	 */
	public void handleStatus(ClientManager client, Object object) {
		
		for(IObserver ob: observers) {
			
			AbstractServerObserver observer = (AbstractServerObserver) ob;
			
			observer.clientStatusChanged(client, object);
		}
	}

}
