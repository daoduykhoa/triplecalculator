/**
 * 
 */
package pHW01_Calculator_T143738;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author ddkhoa
 *
 */
public class changelogs extends JDialog {
	JScrollPane scr;
	JTextArea txaChangelog = new JTextArea();
	public changelogs(){
		setTitle("Change log");
		setSize(179,100);
		setLayout(null);
		setLocationRelativeTo(null);
		scr = new JScrollPane(txaChangelog);
		txaChangelog.setEditable(false);
		add(scr);
		scr.setBounds(0, 0, 200, 300);
		getContentPane().add(scr);
		txaChangelog.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
		txaChangelog.setText("\n"+"      "+"Author: Khoa Dao" 
							+ "\n         Van Lang"
							+ "\n        University"
							+ "\n      Triple Calculator");
	}

}
