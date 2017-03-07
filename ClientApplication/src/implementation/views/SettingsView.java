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
import utils.ui.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import utils.validators.*;
import common.annotations.*;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class SettingsView extends JPanel implements ActionListener {

	public interface SettingsViewListener {

		void onSaved(final SettingsView view);
		void onCancelled(final SettingsView view);
	}

	private static final int PORT_LENGTH = 5;
	private static final int PADDING = 10;

	private JLabel nameLabel = null;
	private JLabel hostLabel = null;
	private JLabel portLabel = null;
	private JTextField nameTextField = null; 
	private JTextField hostTextField = null; 
	private JTextField portTextField = null; 

	private JButton cancelButton = null;
	private JButton saveButton = null;

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
				}
			}
		}
	};

	public SettingsView() {
		super(new SpringLayout());
		
		/*----- USER -------*/

		SpringLayout layout = (SpringLayout) getLayout();

		nameLabel = new JLabel(Resource.getInstance().getString("name"));
		nameTextField = new JTextField();
		nameTextField.setUI(new HintTextFieldUI(Resource.getInstance().getString("name_hint")));
		
		add(nameLabel);
		add(nameTextField);

		layout.putConstraint(SpringLayout.NORTH, nameLabel, SettingsView.PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, nameTextField, 0, SpringLayout.VERTICAL_CENTER, nameLabel);
		layout.putConstraint(SpringLayout.WEST, nameLabel, SettingsView.PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, nameTextField, SettingsView.PADDING, SpringLayout.EAST, nameLabel);
		layout.putConstraint(SpringLayout.EAST, nameTextField, -SettingsView.PADDING, SpringLayout.EAST, this);

		/*----- HOST -------*/
		
		hostLabel = new JLabel(Resource.getInstance().getString("hostname"));
		hostTextField = new JTextField();
		hostTextField.setUI(new HintTextFieldUI(Resource.getInstance().getString("hostname_hint")));
		portLabel = new JLabel(Resource.getInstance().getString("hostport"));
		portTextField = new JTextField(SettingsView.PORT_LENGTH);
		portTextField.setUI(new HintTextFieldUI(Resource.getInstance().getString("hostport_hint")));
		
		add(hostLabel);
		add(hostTextField);
		add(portLabel);
		add(portTextField);

		layout.putConstraint(SpringLayout.NORTH, hostLabel, SettingsView.PADDING, SpringLayout.SOUTH, nameLabel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, hostTextField, 0, SpringLayout.VERTICAL_CENTER, hostLabel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, portLabel, 0, SpringLayout.VERTICAL_CENTER, hostLabel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, portTextField, 0, SpringLayout.VERTICAL_CENTER, hostLabel);
		layout.putConstraint(SpringLayout.WEST, hostLabel, SettingsView.PADDING, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, portTextField, 0, SpringLayout.EAST, nameTextField);
		layout.putConstraint(SpringLayout.EAST, portLabel, -SettingsView.PADDING, SpringLayout.WEST, portTextField);
		layout.putConstraint(SpringLayout.EAST, hostTextField, -SettingsView.PADDING, SpringLayout.WEST, portLabel);
		layout.putConstraint(SpringLayout.WEST, hostTextField, 0, SpringLayout.WEST, nameTextField);
		
		/*----- BUTTONS -------*/

		cancelButton = new JButton(Resource.getInstance().getString("cancel"));
		saveButton = new JButton(Resource.getInstance().getString("login"));

		add(cancelButton);
		add(saveButton);

		layout.putConstraint(SpringLayout.NORTH, saveButton, SettingsView.PADDING, SpringLayout.SOUTH, portTextField);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, cancelButton, 0, SpringLayout.VERTICAL_CENTER, saveButton);
		layout.putConstraint(SpringLayout.EAST, saveButton, 0, SpringLayout.EAST, portTextField);
		layout.putConstraint(SpringLayout.EAST, cancelButton, -SettingsView.PADDING, SpringLayout.WEST, saveButton);

		portTextField.addKeyListener(portValidatorAdapter);
		cancelButton.addActionListener(this);
		saveButton.addActionListener(this);

		listeners = new HashSet<>();
	}

	public void addSettingsViewListener(SettingsViewListener listener) {

		listeners.add(listener);
	}

	public void removeSettingsViewListener(SettingsViewListener listener) {

		listeners.remove(listener);
	}

	public void setUsername(String username) {

		nameTextField.setText(username);
	}

	public String getUsername() {

		return nameTextField.getText();
	}

	public void setHostname(String hostname) {

		hostTextField.setText(hostname);
	}

	public String getHostname() {

		return hostTextField.getText();
	}

	public void setHostport(int port) {
		
		if(port >= 0) {

			portTextField.setText(String.format(Locale.FRANCE, "%d", port));
		}
	}

	public int getHostPort() throws NumberFormatException {

		int port = Integer.parseInt(portTextField.getText());
		return port;
	}

	private void handleAction(boolean saving) {

		for(SettingsViewListener listener: listeners) {

			if(saving) {

				if(validateFields()) {

					listener.onSaved(this);
				}

			} else {

				listener.onCancelled(this);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		if(source instanceof JButton) {

			JButton button = (JButton) source;

			handleAction(button.equals(saveButton));
		}
	}

	private boolean validateFields() {

		boolean usernameValid = nameTextField.getText() != null && new StandardValidator().validate(nameTextField.getText());
		boolean hostnameValid = hostTextField.getText() != null && new IPValidator().validate(hostTextField.getText());
		boolean hostportValid = portTextField.getText() != null && new PortValidator().validate(portTextField.getText());

		return usernameValid && hostnameValid && hostportValid;
	}
}
