package implementation.views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import common.annotations.*;
import helpers.Resource;

@Developer(name="Jesus GARNICA OLARRA")
@SuppressWarnings("serial")
public class ChatAreaView extends JPanel {
	
	private static final int PADDING = 10;

	private final JTextArea readArea;
	private final JTextArea writeArea;
	private final JButton sendButton;
	
	public ChatAreaView() {
		super(new SpringLayout());

		SpringLayout layout = (SpringLayout) getLayout();
		
		readArea = new JTextArea();
		readArea.setLineWrap(true);
		readArea.setWrapStyleWord(true);
		readArea.setEditable(false);
		
		JScrollPane readScroll = new JScrollPane(readArea);
		readScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		readScroll.getVerticalScrollBar().setUnitIncrement(25);
		
		writeArea = new JTextArea();
		writeArea.setLineWrap(true);
		writeArea.setWrapStyleWord(true);
		writeArea.setEditable(true);
		
		sendButton = new JButton(Resource.getInstance().getString("send"));
		
		JScrollPane writeScroll = new JScrollPane(writeArea);
		writeScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		writeScroll.getVerticalScrollBar().setUnitIncrement(25);
		writeScroll.setPreferredSize(new Dimension(100, 80));
        
        add(readScroll);
        add(writeScroll);
        add(sendButton);

        //sendButton constraints
        layout.putConstraint(SpringLayout.SOUTH, sendButton, -ChatAreaView.PADDING, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, sendButton, -ChatAreaView.PADDING, SpringLayout.EAST, this);

        //writeScroll constraints
		layout.putConstraint(SpringLayout.SOUTH, writeScroll, 0, SpringLayout.SOUTH, sendButton);
		layout.putConstraint(SpringLayout.EAST, writeScroll, -ChatAreaView.PADDING, SpringLayout.WEST, sendButton);
		layout.putConstraint(SpringLayout.WEST, writeScroll, ChatAreaView.PADDING, SpringLayout.WEST, this);
		
        //readScroll constraints
		layout.putConstraint(SpringLayout.SOUTH, readScroll, -ChatAreaView.PADDING, SpringLayout.NORTH, writeScroll);
		layout.putConstraint(SpringLayout.NORTH, readScroll, ChatAreaView.PADDING, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, readScroll, 0, SpringLayout.WEST, writeScroll);
		layout.putConstraint(SpringLayout.EAST, readScroll, -ChatAreaView.PADDING, SpringLayout.EAST, this);

        //sendButton constraints
		layout.putConstraint(SpringLayout.NORTH, sendButton, 0, SpringLayout.NORTH, writeScroll);
	}
	
	public void registerButtonListener(ActionListener listener) {
		
		if(sendButton != null) {
			
			sendButton.addActionListener(listener);
		}
	}
	
	public void unregisterButtonListener(ActionListener listener) {
		
		if(sendButton != null) {
			
			sendButton.removeActionListener(listener);
		}
	}
	
	public String getWriterText() {
		
		String text = writeArea.getText();
		
		writeArea.setText(null);
		
		return text;
	}
	
	public void setReaderText(String senderName, String text) {
		
		String template = Resource.getInstance().getString("template_writer");
		String message = String.format(template, senderName, text);
		setReaderText(message);
	}
	
	private void setReaderText(String text) {
		
		String buffer = readArea.getText();
		buffer = buffer + (buffer.length() == 0 ? "" : "\n");
		
		readArea.setText(buffer + text);
	}
}
