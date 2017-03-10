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
import javax.swing.border.*;
import java.util.concurrent.locks.*;

import common.annotations.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class FriendsView extends JPanel {

    private final Box box;
    
	private final Lock locker = new ReentrantLock();
	
	public FriendsView() {
		super(new BorderLayout());
		
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
		
		Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		label.setBorder(border);
		
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
	
	public JButton addButton(String name, Color color) {
	
		final JButton button = new JButton(name);
		
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setFocusPainted(false);
		button.setOpaque(false);
		
		Border border = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		button.setBorder(border);
		button.setBorderPainted(true);
		
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
		
		return button;
	}
	
	public void removeButton(JButton button) {

		locker.lock();
		
		synchronized (box.getTreeLock()) {
			
			for(int i = 0; i < box.getComponentCount(); i++) {
				
				if(box.getComponent(i).equals(button)) {

			        box.remove(i); //remove button
			        break;
				}
			}

	        box.revalidate();
		}

		locker.unlock();
	}
	
	private void addCustomComponent(final JComponent component) {

		locker.lock();
		
		synchronized (box.getTreeLock()) {
			
			component.setMaximumSize(new Dimension(Short.MAX_VALUE, component.getPreferredSize().height));
	        box.add(component);
	        box.revalidate();
	        
	        UIThread.run(new Runnable() {
				
				@Override
				public void run() {
					
					component.scrollRectToVisible(component.getBounds());
				}
			});
		}

		locker.unlock();
    } 
}
