package fr.unicaen.info.users.jg_hg.java.chat.helpers.resources;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
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