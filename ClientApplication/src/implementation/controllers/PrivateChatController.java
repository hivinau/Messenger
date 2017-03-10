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
import java.awt.*;
import javax.swing.*;
import common.annotations.*;
import implementation.views.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class PrivateChatController extends JPanel {
	
	private static final Color ONLINE_COLOR = Color.GREEN;
	private static final Color OFFLINE_COLOR = Color.RED;
	
	private final FriendsView friendsView;
	private final JPanel rightPanel;
	private final JLabel statusLabel;
	
	public PrivateChatController() {
		super(new GridLayout(1, 1));
		
		friendsView = new FriendsView();
        statusLabel = friendsView.addStatusLabel(Resource.getInstance().getString("self"), PrivateChatController.OFFLINE_COLOR);
        
        rightPanel = new JPanel();
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, friendsView, rightPanel);
		split.setContinuousLayout(true);
        split.setResizeWeight(.25);
        split.setDividerSize(2);
        
        add(split);
	}
	
	public void changeConnexionState(boolean state) {
		
		statusLabel.setIcon(new Icon() {
			
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				
				g.setColor(state ? PrivateChatController.ONLINE_COLOR : PrivateChatController.OFFLINE_COLOR);
				g.fillOval(x, y, getIconWidth(), getIconHeight());
                g.drawOval(x, y, getIconWidth(), getIconHeight());
			}
			
			@Override
			public int getIconWidth() {

				return 10;
			}
			
			@Override
			public int getIconHeight() {

				return 10;
			}
		});
	}
}
