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

package fr.unicaen.info.users.jg_hg.java.chat.client_impl.views;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * 
 * @author Jesus GARNICA OLARRA
 */
@SuppressWarnings("serial")
public class FriendsView extends JPanel {
	
	public interface FriendsViewListener {
		
		void componentChanged(final FriendsView view, final Component component);
	}

    private final Box box;
    private final Component glue;
	
	private final Set<FriendsViewListener> listeners;
	
	private ActionListener clientStateActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			handleComponent((Component) e.getSource());
		}
	};
	
	public FriendsView() {
		super(new BorderLayout());
		
		glue = Box.createVerticalGlue();
		
		box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		
		JScrollPane scroll = new JScrollPane(box);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        
        add(scroll);
		
		listeners = new HashSet<>();
	}
	

	
	public void addFriendsViewListener(FriendsViewListener listener) {
		
		listeners.add(listener);
	}
	
	public void removeFriendsViewListener(FriendsViewListener listener) {
		
		listeners.remove(listener);
	}
	
	public void addSelf(String name, Color color) {
	
		JToggleButton button = new JToggleButton(name);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.addActionListener(clientStateActionListener);
		button.setIcon(new Icon() {
			
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				
				g.setColor(color);
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
	
		addCustomComponent(button);
	}
	
	public void addFriend(String name, Color color) {
	
		JButton button = new JButton(name);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.setIcon(new Icon() {
			
			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				
				g.setColor(color);
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
	
		addCustomComponent(button);
	}
	
	private void addCustomComponent(final JComponent component) {
		
		component.setMaximumSize(new Dimension(Short.MAX_VALUE, component.getPreferredSize().height));
        box.remove(glue);
        box.add(Box.createVerticalStrut(5));
        box.add(component);
        box.add(glue);
        box.revalidate();
        
        EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				component.scrollRectToVisible(component.getBounds());
			}
		});
    } 
	
	private void handleComponent(final Component component) {
		
		for(FriendsViewListener listener: listeners) {
			
			listener.componentChanged(this, component);
		}
	}
}
