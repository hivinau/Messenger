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

package implementation.views;

import helpers.*;
import java.awt.*;
import javax.swing.*;
import common.annotations.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class FriendsView extends JPanel {

    private final Box box;
    private final Component glue;
	
	public FriendsView() {
		super(new BorderLayout());
		
		glue = Box.createVerticalGlue();
		
		box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		
		JScrollPane scroll = new JScrollPane(box);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        
        add(scroll);
	}
	
	public JLabel addStatusLabel(String name, Color color) {
	
		JLabel label = new JLabel(name);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setOpaque(false);
		label.setIcon(new Icon() {
			
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
	
		addCustomComponent(label);
		
		return label;
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
        
        UIThread.run(new Runnable() {
			
			@Override
			public void run() {
				
				component.scrollRectToVisible(component.getBounds());
			}
		});
    } 
}
