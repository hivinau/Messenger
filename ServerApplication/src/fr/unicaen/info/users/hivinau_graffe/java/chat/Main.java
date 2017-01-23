package fr.unicaen.info.users.hivinau_graffe.java.chat;

import java.io.*;
import fr.unicaen.info.users.hivinau_graffe.java.chat.server.*;

/**
 * 
 * @author Hivinau GRAFFE
 */
public class Main {

	public static void main(String[] args) {
		
		try {
			ServerManager manager = new ServerManager(8001);
			manager.listen();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
