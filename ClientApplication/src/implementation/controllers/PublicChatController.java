package implementation.controllers;

import java.awt.GridLayout;

import javax.swing.*;

public class PublicChatController extends JPanel {

	private final ClientController clientController;
	
	public PublicChatController(ClientController clientController) {
		super(new GridLayout(1, 1));
		
		this.clientController = clientController;
	}

}
