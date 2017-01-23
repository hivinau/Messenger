package fr.unicaen.info.users.jg_hg.java.chat;

import java.io.*;

import fr.unicaen.info.users.jg_hg.java.chat.server.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Main {

	public static void main(String[] args) {
		
		try {
			
			Listener manager = new Listener(8001);
			manager.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
