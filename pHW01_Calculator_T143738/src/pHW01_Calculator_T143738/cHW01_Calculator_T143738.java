/**
 * 
 */
package pHW01_Calculator_T143738;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * @author Khoa Dao - T143738
 *
 */
public class cHW01_Calculator_T143738 extends JFrame {
	int W = 35, H = 35, D = 10;
	int X = 0, Y = 0;
	JTextField txaConsole = new JTextField();
	JMenuBar mnBar;
	JMenu mnView, mnHelp, mnEdit;
	JMenuItem mniStand, mniPro, mniScf, mniAbout;
	String[][] sLabel = { { "MC", "MR", "MS", "M+", "M-" },
			{ "Del", "CE", "C", "#", "\u221A" }, { "7", "8", "9", "/", "%" },
			{ "4", "5", "6", "*", "1/x" }, { "1", "2", "3", "-", "=" },
			{ "0", ".", "", "+", "" } };
	JButton btnNumbersPad[][] = new JButton[6][5];
	JPanel panStandard = new JPanel();
	JPanel panScientific = new JPanel();
	JPanel panProgrammer = new JPanel();
	JPanel panText = new JPanel();
	private static final long serialVersionUID = 1L;

	// Menu bar
	public void initializeMenu() {
		mnBar = new JMenuBar();
		mnView = new JMenu("View");
		mnEdit = new JMenu("Edit");
		mnHelp = new JMenu("Help");
		mniStand = new JMenuItem("Standard");
		mniPro = new JMenuItem("Programer");
		mniScf = new JMenuItem("Scientific");
		mniAbout = new JMenuItem("About");
		// add submn to ViewOption
		mnView.add(mniStand);
		mnView.add(mniScf);
		mnView.add(mniPro);
		// add submn to HelpOption
		mnHelp.add(mniAbout);
		// add menu to menu bar
		mnBar.add(mnView);
		mnBar.add(mnHelp);
		setJMenuBar(mnBar);
	}

	// Build Up
	public cHW01_Calculator_T143738() {
		// Constructor
		setSize(300, 500);
		setTitle("T143738 - Calculator");
		setLayout(null);
		setLocationRelativeTo(null);
		initializeMenu();
		setResizable(false);
		mStandard();
		txaConsole.setBackground(Color.WHITE);
		// Add component to window
		add(txaConsole);
		// Set postition
		// Function
						//User only accept to input numbers
						txaConsole.addKeyListener(new KeyAdapter() {
							@Override
							public void keyTyped(KeyEvent e) {
								char ch = e.getKeyChar();
								if (ch < '0' || ch > '9' ) {
										e.consume();
								}
							}
						});
		
		mniAbout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(null, "Java Swing - Simple Calculator"
						+ "\nAuthor: Khoa Dao");
			}
		});
		
	this.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
			closeApplication();
		}
	});
}
	public void closeApplication() {
		int result = JOptionPane.showConfirmDialog(null,
				"Do you want to exit the program?", "Confirm",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	public void displayMode(int mode) {
		if (mode == 1) {
			panStandard.setVisible(true);
			panStandard.setBounds(10, 75, X + W - 45, Y + H - 45);
			
			this.setSize(5 * W + 4 * D + 20, 6 * H + 4 * D + 120);
		}
		}
	// Standard Mode
	
				public void mStandard() {
					panStandard.setLayout(null);
					Insets isMargin = new Insets(1, 1, 1, 1);
					Y = 0;
					for (int i = 0; i < 6; i++) {
						X = 0;
						for (int j = 0; j < 5; j++) {
							btnNumbersPad[i][j] = new JButton(sLabel[i][j]);
							btnNumbersPad[i][j].setBounds(X, Y, W, H);
							btnNumbersPad[i][j].setMargin(isMargin);
							btnNumbersPad[i][j].setBackground(Color.WHITE);
							btnNumbersPad[i][j].setForeground(Color.BLACK);
							panStandard.add(btnNumbersPad[i][j]);
							X = X + W + D;
						}
						Y = Y + H + D;
					}
					btnNumbersPad[4][4].setSize(W, H + D + H);
					btnNumbersPad[5][0].setSize(W + D + W, H);
					btnNumbersPad[5][1].setLocation(W + D + W + D, Y - H - D);
					btnNumbersPad[5][4].setVisible(false);
					getContentPane().add(panStandard);
					txaConsole.setBounds(10, 10, 5* W + 4 *D , 50);
					panText.setLayout(null);
					getContentPane().add(panText);
					displayMode(1);
				}
				
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		cHW01_Calculator_T143738 GUI = new cHW01_Calculator_T143738();
		GUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
		GUI.setVisible(true);
	}

}
