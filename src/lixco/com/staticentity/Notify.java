package lixco.com.staticentity;

import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Notify {
	public static void setWarningMsg(String text) {
		Toolkit.getDefaultToolkit().beep();
		JOptionPane optionPane = new JOptionPane(text, JOptionPane.WARNING_MESSAGE);
		JDialog dialog = optionPane.createDialog("Chú ý!");
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}
}
