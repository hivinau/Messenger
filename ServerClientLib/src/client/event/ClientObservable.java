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

package client.event;

import helpers.*;
import java.util.*;
import common.event.*;
import common.annotations.*;
import common.serializable.*;

@Developer(name="Hivinau GRAFFE")
public class ClientObservable extends AbstractObservable {
	
	public ClientObservable() {
		super();
		
	}
	
	/**
	 * Prevent client that its status is on/off.
	 * @param status status of client connexion.
	 */
	public void handleStatus(final boolean status) {
		
		UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				for(IObserver ob: observers) {
					
					AbstractClientObserver observer = (AbstractClientObserver) ob;
					
					observer.statusChanged(status);
				}
			}
		});
	}
	
	/**
	 * Prevent client that contact status is on/off.
	 * @param user user profile of contact.
	 * @param status status of contact connexion.
	 */
	public void handleContactStatus(User user, boolean status) {
		
		UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				for(IObserver ob: observers) {
					
					AbstractClientObserver observer = (AbstractClientObserver) ob;
					
					observer.contactStatusChanged(user, status);
				}
			}
		});
	}
	
	/**
	 * List profiles of user
	 * @return list of user profile
	 */
	public List<User> retrieveClientUser() {
		
		List<User> users = new ArrayList<>();
		
		for(IObserver ob: observers) {
			
			AbstractClientObserver observer = (AbstractClientObserver) ob;
			
			users.add(observer.getUser());
		}
		
		return users;
	}

}
