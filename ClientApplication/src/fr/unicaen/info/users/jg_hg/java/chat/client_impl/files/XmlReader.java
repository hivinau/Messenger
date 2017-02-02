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

package fr.unicaen.info.users.jg_hg.java.chat.client_impl.files;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public final class XmlReader extends ResourcesReader {

	private final Document document;
	
	public XmlReader(String filename) throws FactoryConfigurationError, ParserConfigurationException, SAXException, IOException {
		super();
		
		document = builder.parse(new File(filename));
	}

	@Override
	public Map<String, Object> configuration() {
		
		Map<String, Object> configuration = new HashMap<>();
		
		configuration.put("version", document.getXmlVersion());
		configuration.put("encodage", document.getXmlEncoding());
		configuration.put("standalone", document.getXmlStandalone());
		
		return configuration;
	}

	@Override
	public String value(String attribute) throws DOMException {
		
		return readStringContent(attribute);
	}
	
	private String readStringContent(String attr) throws DOMException {
		
		Element rootElement = document.getDocumentElement();
		
		NodeList stringsNodes = rootElement.getChildNodes();
		
		int nodesCount = stringsNodes.getLength();
		
		for(int i = 0; i < nodesCount; i++) {
			
			if(stringsNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
				
				final Element stringNode = (Element) stringsNodes.item(i);
				
				if(stringNode != null) {
					
					final String attribute = stringNode.getAttribute("name");
					
					if(attribute.equals(attr)) {
						
						return stringNode.getTextContent();
					}
				}
			}
		}
		
		return null;
	}

}
