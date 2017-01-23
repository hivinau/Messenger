package fr.unicaen.info.users.jg_hg.java.chat.server_impl;

import java.io.*;
import fr.unicaen.info.users.jg_hg.java.chat.server.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Main {
	
	private static final int PORT = 8001;

	public static void main(String[] args) {
		
		try {
			
			Listener manager = new Listener(PORT);
			manager.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
