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
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import common.annotations.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class SettingsView extends JPanel {
	
	public interface SettingsViewListener {
		
		void stateChanged(final SettingsView view, final boolean state);
	}
	
	private static final int PORT_LENGTH = 4;

	private JLabel portLabel = null;
	private JTextField portTextField = null;
	private JToggleButton activateButton = null;
	
	private final Set<SettingsViewListener> listeners;
	
	private KeyAdapter portValidatorAdapter = new KeyAdapter() {
		
		@Override
		public void keyTyped(KeyEvent e) {
			
			Object object = e.getSource();
			
			if(object instanceof JTextField) {
				
				JTextField textField = (JTextField) object;
				
				if(textField.equals(portTextField)) {

					boolean limitReached = textField.getText().length() >= SettingsView.PORT_LENGTH;
					
					char c = e.getKeyChar();
					boolean forbiddenCharacter = !((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE));
					
					if (limitReached || forbiddenCharacter) {

			            e.consume(); 
					}

					activateButton.setEnabled(textField.getText().length() >= SettingsView.PORT_LENGTH - 1);
				}
			}
		}
	};
	
	private ActionListener hostStateActionListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object object = e.getSource();
			
			if(object instanceof JToggleButton) {
				
				JToggleButton button = (JToggleButton) object;
				
				if(button.equals(activateButton)) {
					
					boolean state = button.isSelected();
					
					portTextField.setEnabled(!state);
					handleState(state);
				}
			}
		}
	};

	public SettingsView() {
		
		setLayout(new FlowLayout());

		portLabel = new JLabel(Resource.getInstance().getString("host_port"));
		
		portTextField = new JTextField(SettingsView.PORT_LENGTH);
		portTextField.addKeyListener(portValidatorAdapter);
		
		activateButton = new JToggleButton(Resource.getInstance().getString("host_state"));
		
		activateButton.addActionListener(hostStateActionListener);
		activateButton.setEnabled(false);
		
		add(portLabel);
		add(portTextField);
		add(activateButton);
		
		listeners = new HashSet<>();
	}
	
	public void addSettingsViewListener(SettingsViewListener listener) {
		
		listeners.add(listener);
	}
	
	public void removeSettingsViewListener(SettingsViewListener listener) {
		
		listeners.remove(listener);
	}
	
	public int getPort() throws NumberFormatException {
		
		int port = Integer.parseInt(portTextField.getText());
		return port;
	}
	
	private void handleState(final boolean state) {
		
		for(SettingsViewListener listener: listeners) {
			
			listener.stateChanged(this, state);
		}
	}
}
