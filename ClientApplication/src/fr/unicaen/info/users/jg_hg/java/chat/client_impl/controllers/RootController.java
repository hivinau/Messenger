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

package fr.unicaen.info.users.jg_hg.java.chat.client_impl.controllers;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import fr.unicaen.info.users.jg_hg.java.chat.utils.*;
import fr.unicaen.info.users.jg_hg.java.chat.helpers.*;

public class RootController extends JFrame {

	private static final long serialVersionUID = 10001L;
	
	private WindowAdapter windowAdapter = new WindowAdapter() {
		
		@Override
		public void windowClosing(WindowEvent e) {
			
			Log.i(RootController.class.getName(), "windowClosing called");
			
			Resource resource = Resource.getInstance();
			
			final String title = resource.getString("close_app__title");
			final String message = resource.getString("close_app__message");
			
			int canClose = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			
			if(canClose == JOptionPane.YES_OPTION) {
				
				try {
					
					Log.i(RootController.class.getName(), "application will be closed");
					System.exit(0);
					
				} catch(Exception exception) {
					
					Log.e(RootController.class.getName(), exception.getMessage());
				}
			}
		}
	};

	public RootController(String title) {
		super(title);
		
		initComponents();
	}
	
	private void initComponents() {
		
		setMinimumSize(new Dimension(400, 400));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
		addWindowListener(windowAdapter);
		
		pack();
	}
}
