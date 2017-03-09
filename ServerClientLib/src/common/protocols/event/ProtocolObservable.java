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

package common.protocols.event;

import common.annotations.*;

@Developer(name="Hivinau GRAFFE")
public interface ProtocolObservable {

	/**
	 * Register observer
	 * @param observer observer to be registered
	 */
	public void registerObserver(ProtocolObserver observer);
	
	/**
	 * Unregister observer
	 * @param observer observer to be unregistered
	 */
	public void unregisterObserver(ProtocolObserver observer);
	
	/**
	 * Handle a value from source.
	 * @param source source emitter.
	 * @param value value to be handled.
	 */
	public void sendEvent(Object source, Object value);
}
