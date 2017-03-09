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

package implementation.controllers;

import helpers.*;
import java.io.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import common.annotations.*;
import implementation.views.*;
import implementation.views.FriendsView.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class PrivateChatController extends JPanel implements FriendsViewListener {

	private final ClientController clientController;
	private final SettingsController settingsController;
	private final FriendsView friendsView;
	private final JPanel rightPanel;
	private final JToggleButton connexionButton;
	
	public PrivateChatController(ClientController clientController) {
		super(new GridLayout(1, 1));
		
		this.clientController = clientController;
		
		settingsController = new SettingsController(null, Resource.getInstance().getString("settings"));
		
		friendsView = new FriendsView();
        friendsView.addFriendsViewListener(this);
        connexionButton = friendsView.addSelf(Resource.getInstance().getString("self"), Color.RED);
        
        rightPanel = new JPanel();
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, friendsView, rightPanel);
		split.setContinuousLayout(true);
        split.setResizeWeight(.25);
        split.setDividerSize(2);
        
        add(split);
	}
	
	@Override
	public void componentChanged(FriendsView view, Component component) {
		
		if(component instanceof JToggleButton) {
			
			if(((JToggleButton) component).isSelected()) {
				
			}
		}
	}
	
	public void changeConnexionState(boolean state) {
		
		connexionButton.setSelected(state);
	}
}
