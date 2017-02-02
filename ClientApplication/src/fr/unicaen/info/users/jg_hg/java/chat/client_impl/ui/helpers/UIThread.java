package fr.unicaen.info.users.jg_hg.java.chat.client_impl.ui.helpers;

import javax.swing.*;

public class UIThread {

	public static void run(Runnable runnable) {
		
		SwingUtilities.invokeLater(runnable);
	}
}
