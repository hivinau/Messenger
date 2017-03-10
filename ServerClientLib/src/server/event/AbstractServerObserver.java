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
public abstract class AbstractServerObserver implements IObserver {
	
	/**
	 * Handle server that a client is online or offline.
	 * @param client client has status that changed to online or offline.
	 * @param object object mapped with this event.
	 */
	public abstract void clientStatusChanged(ClientManager client, Object object);
}
