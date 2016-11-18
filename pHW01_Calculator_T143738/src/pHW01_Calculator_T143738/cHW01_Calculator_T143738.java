//Resolved Issue #1. The window has been moved after users change display mode
// not happen on Windows. This problem only happen in Linux OS (Ubuntu)
/**
 * 
 */
package pHW01_Calculator_T143738;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.cosh;
import static java.lang.Math.sin;
import static java.lang.Math.sinh;
import static java.lang.Math.tan;
import static java.lang.Math.tanh;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import org.omg.CORBA.CODESET_INCOMPATIBLE;

/**
 * @author Khoa Dao - T143738
 *
 */
public class cHW01_Calculator_T143738 extends JFrame {
	int W = 35, H = 35, D = 10;
	int X = 0, Y = 0;
	double TempNumbers = 0, TempNumbers2 = 0, TempMemory, subMem;
	boolean bAppend = false;
	String sDoors = "", sClick = "",sCurrent = "";
	JButton btnCheckout = new JButton();
	JTextField txtConsole = new JTextField();
	JScrollPane scrConsole;
	JMenuBar mnBar;
	JMenu mnMode, mnHelp, mnEdit;
	JMenuItem mniStand, mniPro, mniScf, mniAbout;
	About panAbout = new About();
	JPanel panStandard = new JPanel();
	JPanel panScientific = new JPanel();
	JPanel panProgrammer = new JPanel();
	JPanel panAU = new JPanel(), panHeDem = new JPanel(), panNknwn = new JPanel();
	ButtonGroup btG = new ButtonGroup(), btGProBox1 = new ButtonGroup(), btGProBox2 = new ButtonGroup();
	// Labes base
	String[][] sLabelNumpad = { { "MC", "MR", "MS", "M+", "M-" }, { "Del", "CE", "C", "\u00B1", "\u221A" },
			{ "7", "8", "9", "/", "%" }, { "4", "5", "6", "*", "1/x" }, { "1", "2", "3", "-", "=" },
			{ "0", ".", "", "+", "" } };
	String[][] sLabelScn = { { "#", "Inv", "ln", "(", ")" }, { "Int", "sinh", "sin", "x\u00B2", "n!" },
			{ "dms", "cosh", "cos", "x\u2071", "\u2071\u221Ax" },
			{ "\u03c0", "tanh", "tan", "x\u00B3", "x\u00B9\u002F\u00B3" }, { "F-E", "Exp", "Mod", "log", "10^x" } };
	String[][] sLabelPro = { { "#", "Mod", "A" }, { "(", ")", "B" }, { "RoL", "RoR", "C" }, { "Or", "Xor", "D" },
			{ "Lsh", "Rsh", "E" }, { "Not", "And", "F" } };
	JButton[][] btnNumbersPad = new JButton[6][5], btnScnpad = new JButton[5][5], btnPropad = new JButton[6][3];
	JRadioButton optDegrees = new JRadioButton("Degrees"), optRadians = new JRadioButton("Radians"),
			optGrads = new JRadioButton("Grads"), optHex = new JRadioButton("Hex"), optDec = new JRadioButton("Dec"),
			optOct = new JRadioButton("Oct"), optBin = new JRadioButton("Bin"), optQword = new JRadioButton("Qword"),
			optDword = new JRadioButton("Dword"), optWord = new JRadioButton("Word"),
			optByte = new JRadioButton("Byte");

	// Menu bar
	public void initializeMenu() {
		mnBar = new JMenuBar();
		mnMode = new JMenu("Mode");
		mnEdit = new JMenu("Edit");
		mnHelp = new JMenu("Help");
		mniStand = new JMenuItem("Standard");
		mniPro = new JMenuItem("Programer");
		mniScf = new JMenuItem("Scientific");
		mniAbout = new JMenuItem("About");
		// add submn to ViewOption
		mnMode.add(mniStand);
		mnMode.add(mniScf);
		mnMode.add(mniPro);
		// add submn to HelpOption
		mnHelp.add(mniAbout);
		// add menu to Menu bar
		mnBar.add(mnMode);
		mnBar.add(mnHelp);
		// Set hotkey for MenuItem
		mniStand.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		mniScf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		mniPro.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		// Set icon for MenuItem
		// mniStand.setIcon(imCaculator);
		// mniAbout.setIcon(imAbout);
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
		CoreProcess();

		// Add component to window
		add(txtConsole);
		// Customize
		txtConsole.setFont(new Font(Font.DIALOG_INPUT, Font.LAYOUT_RIGHT_TO_LEFT, 20));
		txtConsole.setHorizontalAlignment(JTextField.RIGHT);
		txtConsole.setBackground(Color.WHITE);
		txtConsole.setFocusable(true);
		txtConsole.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
		txtConsole.setSelectionColor(Color.WHITE);
		// Function
		MouseListener mlChoosen = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (optDegrees.isSelected()) {
					SciDegMode();
				} else if (optRadians.isSelected()) {
					SciRadMode();
				}
			}
		};
		optDegrees.addMouseListener(mlChoosen);
		optRadians.addMouseListener(mlChoosen);
		optHex.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (optHex.isSelected()) {
					for (int i = 0; i <= 5; i++) {
						btnPropad[i][2].setEnabled(true);
					}
					btnNumbersPad[2][1].setEnabled(true);
					btnNumbersPad[2][2].setEnabled(true);
					btnNumbersPad[2][0].setEnabled(true);
					btnNumbersPad[3][0].setEnabled(true);
					btnNumbersPad[3][1].setEnabled(true);
					btnNumbersPad[3][2].setEnabled(true);
					btnNumbersPad[4][1].setEnabled(true);
					btnNumbersPad[4][2].setEnabled(true);
					btnNumbersPad[5][1].setEnabled(true);
				}
			}
		});
		optDec.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (optDec.isSelected()) {
					for (int i = 0; i <= 5; i++) {
						btnPropad[i][2].setEnabled(false);
					}
					btnNumbersPad[2][1].setEnabled(true);
					btnNumbersPad[2][2].setEnabled(true);
					btnNumbersPad[2][0].setEnabled(true);
					btnNumbersPad[3][0].setEnabled(true);
					btnNumbersPad[3][1].setEnabled(true);
					btnNumbersPad[3][2].setEnabled(true);
					btnNumbersPad[4][1].setEnabled(true);
					btnNumbersPad[4][2].setEnabled(true);
					btnNumbersPad[5][1].setEnabled(true);

				}
			}
		});
		optOct.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (optOct.isSelected()) {
					for (int i = 0; i <= 5; i++) {
						btnPropad[i][2].setEnabled(false);
					}
					btnNumbersPad[2][1].setEnabled(false);
					btnNumbersPad[2][2].setEnabled(false);
					btnNumbersPad[2][0].setEnabled(true);
					btnNumbersPad[3][0].setEnabled(true);
					btnNumbersPad[3][1].setEnabled(true);
					btnNumbersPad[3][2].setEnabled(true);
					btnNumbersPad[4][1].setEnabled(true);
					btnNumbersPad[4][2].setEnabled(true);
					btnNumbersPad[5][1].setEnabled(true);
				}
			}
		});
		optBin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (optBin.isSelected()) {
					for (int i = 0; i <= 5; i++) {
						btnPropad[i][2].setEnabled(false);
					}
					btnNumbersPad[2][1].setEnabled(false);
					btnNumbersPad[2][2].setEnabled(false);
					btnNumbersPad[2][0].setEnabled(false);
					btnNumbersPad[3][0].setEnabled(false);
					btnNumbersPad[3][1].setEnabled(false);
					btnNumbersPad[3][2].setEnabled(false);
					btnNumbersPad[4][1].setEnabled(false);
					btnNumbersPad[4][2].setEnabled(false);
					btnNumbersPad[5][1].setEnabled(false);
				}
			}
		});
		// User only accept to input numbers
		KeyListener klAllow = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				char digit = e.getKeyChar();
				char nChar = '\u002b'; // Symbol "+"
				if (digit < '0' || digit > '9') {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		};
		txtConsole.addKeyListener(klAllow);
		// When user choose option
		ActionListener acMode = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (e.getSource() == mniStand) {
					mStandard();
				} else if (e.getSource() == mniScf) {
					mScnmode();
					SciDegMode();
				} else if (e.getSource() == mniPro) {
					mPromode();
				} else if (e.getSource() == mniAbout) {
					panAbout.setVisible(true);
				}
			}
		};
		// Add function for menu item
		mniStand.addActionListener(acMode);
		mniScf.addActionListener(acMode);
		mniPro.addActionListener(acMode);
		mniAbout.addActionListener(acMode);
		// Recall close command

	}

	// TRIPLE MODE //
	public void displayMode(int mode) {
		int x = this.getX(), y = this.getY();
		if (mode == 1) {
			this.setTitle("Mode: Standard");
			txtConsole.setText("");
			panStandard.setVisible(true);
			panScientific.setVisible(false);
			panHeDem.setVisible(false);
			panNknwn.setVisible(false);
			panAU.setVisible(false);
			panProgrammer.setVisible(false);
			mniStand.setBackground(Color.LIGHT_GRAY);
			mniScf.setBackground(Color.WHITE);
			mniPro.setBackground(Color.WHITE);
			panStandard.setBounds(10, 75, X + W - 45, Y + H - 45);
			this.setSize(5 * W + 4 * D + 25, 6 * H + 4 * D + 120 + 25);
		}
		if (mode == 2) {
			this.setTitle("Mode: Scientific");
			txtConsole.setText("");
			panAU.setVisible(true);
			panStandard.setVisible(true);
			panScientific.setVisible(true);
			panProgrammer.setVisible(false);
			panHeDem.setVisible(false);
			panNknwn.setVisible(false);
			mniStand.setBackground(Color.WHITE);
			mniScf.setBackground(Color.LIGHT_GRAY);
			mniPro.setBackground(Color.WHITE);
			panAU.setBounds(10, 75, X - 10, H);
			panStandard.setBounds(W + X - 25, 75, X + W + 20, Y + H);
			panScientific.setBounds(10, 75 + H + 10, X, Y + H - 40);
			this.setSize(5 * W + 4 * D + X + W - 15, 6 * H + 4 * D + 115 + 27);
		}
		if (mode == 3) {
			this.setTitle("Mode: Programmer");
			txtConsole.setText("");
			panAU.setVisible(false);
			panStandard.setVisible(true);
			panScientific.setVisible(false);
			panProgrammer.setVisible(true);
			panHeDem.setVisible(true);
			panNknwn.setVisible(true);
			mniStand.setBackground(Color.WHITE);
			mniScf.setBackground(Color.WHITE);
			mniPro.setBackground(Color.LIGHT_GRAY);
			panStandard.setBounds(W + X + 85, 75, X + W + 20, Y + H);
			panProgrammer.setBounds(W + 80, 75, X + W + 20, Y + H);
			panHeDem.setBounds(10, 75, 95, 125);
			panNknwn.setBounds(10, 210, 95, 125);
			this.setSize(5 * W + 4 * D + X + W + 100, 6 * H + 4 * D + 115 + 30);
		}
	}

	// STANDARD
	public void mStandard() {
		panStandard.setLayout(null);
		Insets isMargin = new Insets(1, 1, 1, 1);
		Y = 0;
		for (int i = 0; i < 6; i++) {
			X = 0;
			for (int j = 0; j < 5; j++) {
				btnNumbersPad[i][j] = new JButton(sLabelNumpad[i][j]);
				btnNumbersPad[i][j].setBounds(X, Y, W, H);
				btnNumbersPad[i][j].setMargin(isMargin);
				btnNumbersPad[i][j].setBackground(Color.WHITE);
				btnNumbersPad[i][j].setForeground(Color.BLACK);
				btnNumbersPad[i][j].setFocusPainted(false);
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
		txtConsole.setBounds(10, 10, 5 * W + 4 * D, 50);
		// panText.setLayout(null);
		displayMode(1);
		// Show mode

	}

	// SCIENTIFIC
	public void mScnmode() {
		// Resize console screens
		txtConsole.setBounds(10, 10, 5 * W + 4 * D + 270, 50);
		// Angel units panel
		btG.add(optDegrees);
		btG.add(optGrads);
		btG.add(optRadians);
		panAU.setLayout(null);
		panAU.add(optDegrees);
		panAU.add(optGrads);
		panAU.add(optRadians);
		optDegrees.setBounds(5, 5, 90, 25);
		optRadians.setBounds(95, 5, 90, 25);
		optGrads.setBounds(190, 5, 70, 25);
		optGrads.setEnabled(false);
		panAU.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		optDegrees.setSelected(true);
		optDegrees.setFocusPainted(false);
		optRadians.setFocusPainted(false);
		getContentPane().add(panAU);
		// Scientific panel
		panScientific.setLayout(null);
		Insets isMargin = new Insets(1, 1, 1, 1);
		Y = 0;
		for (int i = 0; i < 5; i++) {
			X = 0;
			for (int j = 0; j < 5; j++) {
				btnScnpad[i][j] = new JButton(sLabelScn[i][j]);
				btnScnpad[i][j].setBounds(X, Y, W + 10, H);
				btnScnpad[i][j].setMargin(isMargin);
				btnScnpad[i][j].setFocusPainted(false);
				panScientific.add(btnScnpad[i][j]);
				X = X + W + D + 10;
				// Customize
				btnScnpad[i][j].setBackground(Color.WHITE);
				btnScnpad[i][j].setForeground(Color.black);
			}
			Y = Y + H + D;
		}

		// Disable function
		btnScnpad[0][1].setEnabled(false);
		btnScnpad[1][1].setEnabled(false);
		btnScnpad[3][1].setEnabled(false);
		btnScnpad[4][1].setEnabled(false);
		btnScnpad[2][1].setEnabled(false);
		btnScnpad[1][0].setEnabled(false);
		btnScnpad[2][0].setEnabled(false);
		btnScnpad[4][0].setEnabled(false);
		btnScnpad[0][0].setVisible(false);
		btnScnpad[0][3].setEnabled(false);
		btnScnpad[0][4].setEnabled(false);
		getContentPane().add(panScientific);
		displayMode(2);
	}

	// PROGRAMER
	public void mPromode() {
		// Resize console screens
		txtConsole.setBounds(10, 10, 5 * W + 4 * D + 275, 50);
		// He dem panel
		panHeDem.setLayout(null);
		panNknwn.setLayout(null);
		btGProBox1.add(optBin);
		btGProBox2.add(optDword);
		btGProBox1.add(optOct);
		btGProBox2.add(optQword);
		btGProBox1.add(optDec);
		btGProBox2.add(optByte);
		btGProBox1.add(optHex);
		btGProBox2.add(optWord);
		panHeDem.add(optHex);
		panHeDem.add(optDec);
		panHeDem.add(optOct);
		panHeDem.add(optBin);
		getContentPane().add(panHeDem);
		panHeDem.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		panNknwn.add(optQword);
		panNknwn.add(optDword);
		panNknwn.add(optWord);
		panNknwn.add(optByte);
		getContentPane().add(panNknwn);
		panNknwn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
		// Set position for component sub-panel
		optHex.setSelected(true);
		// optDec.setSelected(true);
		optHex.setBounds(10, 10, 80, 25);
		optHex.setFocusPainted(false);
		optDec.setBounds(10, 37, 80, 25);
		optDec.setFocusPainted(false);
		optOct.setBounds(10, 63, 80, 25);
		optOct.setFocusPainted(false);
		optBin.setBounds(10, 90, 80, 25);
		optBin.setFocusPainted(false);
		// ------
		optQword.setBounds(10, 10, 80, 25);
		optQword.setFocusPainted(false);
		optDword.setBounds(10, 37, 80, 25);
		optDword.setFocusPainted(false);
		optWord.setBounds(10, 63, 80, 25);
		optWord.setFocusPainted(false);
		optByte.setBounds(10, 90, 80, 25);
		optByte.setFocusPainted(false);
		// Scientific panel
		panProgrammer.setLayout(null);
		Insets isMargin = new Insets(1, 1, 1, 1);
		Y = 0;
		for (int i = 0; i < 6; i++) {
			X = 0;
			for (int j = 0; j < 3; j++) {
				btnPropad[i][j] = new JButton(sLabelPro[i][j]);
				btnPropad[i][j].setBounds(X, Y, W + 10, H);
				btnPropad[i][j].setMargin(isMargin);
				panProgrammer.add(btnPropad[i][j]);
				btnPropad[i][j].setBackground(Color.WHITE);
				btnPropad[i][j].setForeground(Color.black);
				btnPropad[i][j].setFocusPainted(false);
				X = X + W + D + 10;
			}
			Y = Y + H + D;
		}
		btnPropad[1][0].setEnabled(false);
		btnPropad[1][1].setEnabled(false);
		btnPropad[3][0].setEnabled(false);
		btnPropad[3][1].setEnabled(false);
		btnPropad[4][1].setEnabled(false);
		btnPropad[4][0].setEnabled(false);
		//
		getContentPane().add(panProgrammer);
		displayMode(3);
	}

	// CALCULATE FUNCTION
	public void CoreProcess() {
		// Numbers input
		ActionListener acMemory = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent c) {
				// TODO Auto-generated method stub
				JButton btnClicked = (JButton) c.getSource();
				String sClick = btnClicked.getText();
				if (sClick.equals("MS")) {

					TempMemory = Double.parseDouble(txtConsole.getText());
					txtConsole.setText("0");

				} else if (sClick.equals("MR")) {
					if (TempMemory % 1 == 0) {
						txtConsole.setText("" + (int) TempMemory);
					} else {
						txtConsole.setText("" + TempMemory);
					}
				} else if (sClick.equals("MC")) {
					TempMemory = 0;
				} else if (sClick.equals("M+")) {
					subMem = Double.parseDouble(txtConsole.getText());
					if (TempMemory % 1 == 0) {
						TempMemory = (int) (TempMemory + subMem);
					} else {
						TempMemory = TempMemory + subMem;
					}

				} else if (sClick.equals("M-")) {
					subMem = Double.parseDouble(txtConsole.getText());
					if (TempMemory % 1 == 0) {
						TempMemory = (int) (TempMemory - subMem);
					} else {
						TempMemory = TempMemory - subMem;
					}

				}

				bAppend = false;
			}

		};
		btnNumbersPad[0][0].addActionListener(acMemory);
		btnNumbersPad[0][1].addActionListener(acMemory);
		btnNumbersPad[0][2].addActionListener(acMemory);
		btnNumbersPad[0][3].addActionListener(acMemory);
		btnNumbersPad[0][4].addActionListener(acMemory);
		// Allow users can click button numbers
		ActionListener acNumbers = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				sCurrent = txtConsole.getText();
				if (bAppend == true) {
					if (sCurrent.equals("0")) {
						txtConsole.setText(sClick);
					} else {
						txtConsole.setText(sCurrent + sClick);
					}
				} else {
					txtConsole.setText(sClick);
					bAppend = true;
				}

			}
		};
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				btnNumbersPad[i][j].addActionListener(acNumbers);
			}
		}
		// Operation of process
		ActionListener acDoors = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				sCurrent = txtConsole.getText();
				TempNumbers = Double.parseDouble(sCurrent);
				sDoors = sClick;
				bAppend = false;
			}
		};
		btnNumbersPad[5][3].addActionListener(acDoors);
		btnNumbersPad[4][3].addActionListener(acDoors);
		btnNumbersPad[3][3].addActionListener(acDoors);
		btnNumbersPad[2][3].addActionListener(acDoors);
		/// btnScnpad[2][3].addActionListener(acDoors);

		// Basic calculator
		ActionListener acBasicMath = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sCurrent = txtConsole.getText();
				TempNumbers2 = Double.parseDouble(sCurrent);
				if (sDoors.equals("+")) {
					TempNumbers2 = TempNumbers + TempNumbers2;
					if (TempNumbers2 % 1 == 0) {
						int iDone = (int) TempNumbers2;
						txtConsole.setText("" + iDone);
					} else {
						txtConsole.setText("" + TempNumbers2);
					}
				}
				if (sDoors.equals("-")) {
					TempNumbers2 = TempNumbers - TempNumbers2;
					if (TempNumbers2 % 1 == 0) {
						int iDone = (int) TempNumbers2;
						txtConsole.setText("" + iDone);
					} else {
						txtConsole.setText("" + TempNumbers2);
					}
				}
				if (sDoors.equals("*")) {
					TempNumbers2 = TempNumbers * TempNumbers2;
					if (TempNumbers2 % 1 == 0) {
						int iDone = (int) TempNumbers2;
						txtConsole.setText("" + iDone);
					} else {
						txtConsole.setText("" + TempNumbers2);
					}
				}
				if (sDoors.equals("/")) {
					TempNumbers2 = TempNumbers / TempNumbers2;
					if (TempNumbers2 % 1 == 0) {
						int iDone = (int) TempNumbers2;
						txtConsole.setText("" + iDone);
					} else {
						txtConsole.setText("" + TempNumbers2);
					}
				}
				if (sDoors.equals("x\u2071")) {
					double dXnY = 0;
					dXnY = Math.pow(TempNumbers, TempNumbers2);
					if (dXnY % 1 == 0) {
						int iDone = (int) dXnY;
						txtConsole.setText("" + iDone);
					} else {
						txtConsole.setText("" + dXnY);
					}
				}
				if (sDoors.equals("\u2071\u221Ax")) {
					double dYsquareX = 0;
					dYsquareX = Math.pow(TempNumbers2, (1.0 / TempNumbers));
					if (dYsquareX % 1 == 0) {
						int iDone = (int) dYsquareX;
						txtConsole.setText("" + iDone);
					} else {
						txtConsole.setText("" + dYsquareX);
					}
				}
				if (sDoors.equals("Mod")) {
					double dMOD = 0;
					dMOD = TempNumbers % TempNumbers2;
					if (dMOD % 1 == 0) {
						int iDone = (int) dMOD;
						txtConsole.setText("" + iDone);
					} else {
						txtConsole.setText("" + dMOD);
					}
				}
				bAppend = false;
			}
		};
		btnNumbersPad[4][4].addActionListener(acBasicMath);
		btnNumbersPad[4][4].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				txtConsole.requestFocus();
				txtConsole.selectAll();
			}
		});
		// Positive & Non positive
		ActionListener acPnNP = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (txtConsole.getText().startsWith("-")) {
					txtConsole.setText(txtConsole.getText().substring(1, txtConsole.getText().length()));
				} else {
					txtConsole.setText("-" + txtConsole.getText());
				}
				bAppend = false;
			}
		};
		btnNumbersPad[1][3].addActionListener(acPnNP);

		// 1/x
		ActionListener ac1pX = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("1/x")) {
					double d1DivX = Double.parseDouble(txtConsole.getText());
					double d1 = 1 / d1DivX;
					if (d1 % 1 == 0) {
						int i1 = (int) d1;
						txtConsole.setText("" + i1);
					} else {
						txtConsole.setText("" + d1);
					}
				}
				bAppend = false;

			}
		};
		btnNumbersPad[3][4].addActionListener(ac1pX);
		ActionListener acSQRT = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				int rResult = 0;
				if (sClick.equals("\u221A")) {
					double dSQRT = Double.parseDouble(txtConsole.getText());
					double dSResult = Math.sqrt(dSQRT);
					if (dSResult % 1 == 0) {
						rResult = (int) dSResult;
						txtConsole.setText("" + rResult);
					} else {
						txtConsole.setText("" + dSResult);
					}

				}
				bAppend = false;

			}
		};
		btnNumbersPad[1][4].addActionListener(acSQRT);

		// %
		ActionListener acDiv = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("%")) {
					double dDiv = Double.parseDouble(txtConsole.getText());
					double dDivRe = dDiv / 100;
					if (dDivRe % 1 == 0) {
						int iDiv = (int) dDivRe;
						txtConsole.setText("" + iDiv);
					} else {
						txtConsole.setText("" + dDivRe);
					}
				}
				bAppend = false;

			}
		};
		btnNumbersPad[2][4].addActionListener(acDiv);

		// Clear console function
		ActionListener acRM = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton btnClicked = (JButton) e.getSource();
				String sClick = btnClicked.getText();
				if (sClick.equals("C")) {
					txtConsole.setText("0");
				} else if (sClick.equals("CE")) {
					txtConsole.setText("0");
				} else if (sClick.equals("Del")) {
					if (txtConsole.getText().equals("Math ERROR")) {
						txtConsole.setText("0");
					} else if (txtConsole.getText().length() > 1) {
						txtConsole.setText(txtConsole.getText().substring(0, txtConsole.getText().length() - 1));
					} else {
						txtConsole.setText("0");
					}
				}
			}
		};
		btnNumbersPad[1][2].addActionListener(acRM);
		btnNumbersPad[1][1].addActionListener(acRM);
		btnNumbersPad[1][0].addActionListener(acRM);

	}

	// SCIENTIFIC CALCULATE FUNCTION
	public void SciDegMode() {
		txtConsole.setText("");
		TempNumbers = 0;
		TempNumbers2 = 0;
		ActionListener acDoors = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				sCurrent = txtConsole.getText();
				TempNumbers = Double.parseDouble(sCurrent);
				sDoors = sClick;
				bAppend = false;
			}
		};
		btnScnpad[2][3].addActionListener(acDoors);
		btnScnpad[2][4].addActionListener(acDoors);
		btnScnpad[4][2].addActionListener(acDoors);
		ActionListener acPI = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				TempNumbers2 = Math.PI;
				txtConsole.setText("" + TempNumbers2);
			}
		};
		btnScnpad[3][0].addActionListener(acPI);
		ActionListener acSQR = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("x\u00B2")) {
					double dTempSQR = Double.parseDouble(txtConsole.getText());
					double dSQR = dTempSQR * dTempSQR;
					if (dSQR % 1 == 0) {
						int iSQR = (int) dSQR;
						txtConsole.setText("" + iSQR);
					} else {
						txtConsole.setText("" + dSQR);
					}
					bAppend = false;
				}

			}

		};
		btnScnpad[1][3].addActionListener(acSQR);
		ActionListener acSQR3 = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent c) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) c.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("x\u00B3")) {
					double dTempSQR = Double.parseDouble(txtConsole.getText());
					double dSQR3 = dTempSQR * dTempSQR * dTempSQR;
					if (dSQR3 % 1 == 0) {
						int iSQR3 = (int) dSQR3;
						txtConsole.setText("" + iSQR3);
					} else {
						txtConsole.setText("" + dSQR3);
					}
					bAppend = false;
				}

			}

		};
		btnScnpad[3][3].addActionListener(acSQR3);
		ActionListener acSQR13 = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent c) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) c.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("x\u00B9\u002F\u00B3")) {
					double dTempSQR = Double.parseDouble(txtConsole.getText());
					double dSQR13 = Math.pow(dTempSQR, (1.0 / 3.0));
					if (dSQR13 % 1 == 0) {
						int iSQR13 = (int) dSQR13;
						txtConsole.setText("" + iSQR13);
					} else {
						txtConsole.setText("" + dSQR13);
					}
					bAppend = false;
				}

			}

		};
		btnScnpad[3][4].addActionListener(acSQR13);
		ActionListener acLN = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent c) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) c.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("ln")) {
					double dTempLn = Double.parseDouble(txtConsole.getText());
					double dLn = Math.log(dTempLn);
					if (dLn > 0) {
						if (dLn % 1 == 0) {
							int iLN = (int) dLn;
							txtConsole.setText("" + iLN);
						} else {
							txtConsole.setText("" + dLn);
						}
					} else {
						txtConsole.setText("Math ERROR");
					}
					bAppend = false;
				}

			}

		};
		btnScnpad[0][2].addActionListener(acLN);
		ActionListener acLOG = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent c) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) c.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("log")) {
					double dTempLog = Double.parseDouble(txtConsole.getText());
					double dLOG = Math.log(dTempLog);
					if (dLOG > 0) {
						if (dLOG % 1 == 0) {
							int iLOG = (int) dLOG;
							txtConsole.setText("" + iLOG);
						} else {
							txtConsole.setText("" + dLOG);
						}
					} else {
						txtConsole.setText("0");
					}

					bAppend = false;
				}

			}

		};
		btnScnpad[0][2].addActionListener(acLOG);
		ActionListener acFactorial = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent c) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) c.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("n!")) {
					double dTempFact = Double.parseDouble(txtConsole.getText());
					double dFact = 1;
					if (dTempFact != 0 && dTempFact == 1) {
						dFact = 1;
					} else if (dTempFact != 0) {
						for (int i = 0; i < dTempFact; i++) {
							dFact = 1.0 * (dFact * ((dTempFact - i)));
						}
						if (dFact % 1 == 0) {
							int iFact = (int) dFact;
							txtConsole.setText("" + iFact);
						} else {
							txtConsole.setText("Math ERROR");
						}
					}
					bAppend = false;
				}
			}
		};
		btnScnpad[1][4].addActionListener(acFactorial);
		ActionListener acSIN = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				double eSin = 0;
				if (sClick.equals("sin")) {
					double dTempSin = Double.parseDouble(txtConsole.getText());
					double dSin = sin(dTempSin * PI / 180);
						eSin = Math.round(dSin);
					if (dTempSin == 0 || dTempSin == 90 || dTempSin == 180 || dTempSin == -180 || dTempSin == -90) {
						if (eSin % 1.0 == 0) {
							int iSin = (int) eSin;
							txtConsole.setText("" + iSin);
						} else {
							txtConsole.setText("" + eSin);
						}
					} else {
						txtConsole.setText("" + dSin);
					}

					bAppend = false;
				}
			}
		};
		btnScnpad[1][2].addActionListener(acSIN);
		btnScnpad[1][1].addActionListener(acSIN);
		ActionListener acCOS = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				double eCos = 0;
				if (sClick.equals("cos")) {
					double dTempCos = Double.parseDouble(txtConsole.getText());
					double dCos = cos(dTempCos * PI / 180);
					txtConsole.setText("" + Math.round(dCos));
						eCos = Math.round(dCos);
					if (dTempCos == 0 || dTempCos == 90 || dTempCos == 180 || dTempCos == -180 || dTempCos == -90) {
						if (eCos % 1.0 == 0) {
							int iCos = (int) eCos;
							txtConsole.setText("" + iCos);
						} else {
							txtConsole.setText("" + eCos);
						}
					} else {
						txtConsole.setText("" + dCos);
					}
					bAppend = false;
				}
			}
		};
		btnScnpad[2][2].addActionListener(acCOS);
		ActionListener acTAN = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) e.getSource();
				sClick = btnCheckout.getText();
				double dCTan = 0;
				if (sClick.equals("tan")) {
					double dTempTan = Double.parseDouble(txtConsole.getText());
					double dTan = tan(dTempTan * PI / 180);
					
					if (dTempTan == 90 || dTempTan == -90) {
						txtConsole.setText("INFINITY");
					} else {
						if (dTempTan == 180 || dTempTan == -180 || dTempTan == 360 || dTempTan == -360) {
							txtConsole.setText("0");
						}else if (dTempTan == 45) {
							txtConsole.setText("1");
						}else if(dTempTan == 135) {
							txtConsole.setText("-1");
						}else if(dTempTan == -45) {
							txtConsole.setText("-1");
						}else if(dTempTan == -135) {
							txtConsole.setText("1");
						}else {
							if (dTan % 1 == 0) {
								dCTan = Math.round(dTan);
								int iCTan = (int) dCTan;
								txtConsole.setText("" + iCTan);
							} else {
								txtConsole.setText("" + dTan);
							}
						}
					}
					bAppend = false;
				}
			}
		};
		btnScnpad[3][2].addActionListener(acTAN);
		btnScnpad[3][1].addActionListener(acTAN);
		ActionListener acTenX = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent c) {
				// TODO Auto-generated method stub
				btnCheckout = (JButton) c.getSource();
				sClick = btnCheckout.getText();
				if (sClick.equals("10^x")) {
					double dTempTenX = Double.parseDouble(txtConsole.getText());
					double dTenX = Math.pow(10, dTempTenX);
					if (dTenX % 1 == 0) {
						int iTenX = (int) dTenX;
						txtConsole.setText("" + iTenX);
					} else {
						txtConsole.setText("" + dTenX);
					}
					bAppend = false;
				}
			}
		};
		btnScnpad[4][4].addActionListener(acTenX);
	}

	public void SciRadMode() {
			txtConsole.setText("0");
			TempNumbers = 0;
			TempNumbers2 = 0;		
			
			
			ActionListener acSINRad = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnCheckout = (JButton) e.getSource();
					sClick = btnCheckout.getText();
					double eSin = 0;
					if (sClick.equals("sin")) {
						double dTempSin = Double.parseDouble(txtConsole.getText());
						double dSin = sin(dTempSin * PI / 180);
							eSin = Math.round(dSin);
						if (dTempSin == 0 || dTempSin == 90 || dTempSin == 180 || dTempSin == -180 || dTempSin == -90) {
							if (eSin % 1.0 == 0) {
								int iSin = (int) eSin;
								txtConsole.setText("" + iSin);
							} else {
								txtConsole.setText("" + eSin);
							}
						} else {
							txtConsole.setText("" + dSin);
						}

						bAppend = false;
					}
				}
			};
			//btnScnpad[1][2].addActionListener(acSINRad);
			//btnScnpad[1][1].addActionListener(acSINRad);
			ActionListener acCOSRad = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnCheckout = (JButton) e.getSource();
					sClick = btnCheckout.getText();
					double eCos = 0;
					if (sClick.equals("cos")) {
						double dTempCos = Double.parseDouble(txtConsole.getText());
						double dCos = cos(dTempCos * PI / 180);
						txtConsole.setText("" + Math.round(dCos));
							eCos = Math.round(dCos);
						if (dTempCos == 0 || dTempCos == 90 || dTempCos == 180 || dTempCos == -180 || dTempCos == -90) {
							if (eCos % 1.0 == 0) {
								int iCos = (int) eCos;
								txtConsole.setText("" + iCos);
							} else {
								txtConsole.setText("" + eCos);
							}
						} else {
							txtConsole.setText("" + dCos);
						}
						bAppend = false;
					}
				}
			};
			btnScnpad[2][2].addActionListener(acCOSRad);
			ActionListener acTANRad = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					btnCheckout = (JButton) e.getSource();
					sClick = btnCheckout.getText();
					double dCTan = 0;
					if (sClick.equals("tan")) {
						double dTempTan = Double.parseDouble(txtConsole.getText());
						double dTan = tan(dTempTan * PI / 180);
						
						if (dTempTan == 90 || dTempTan == -90) {
							txtConsole.setText("INFINITY");
						} else {
							if (dTempTan == 180 || dTempTan == -180 || dTempTan == 360 || dTempTan == -360) {
								txtConsole.setText("0");
							}else if (dTempTan == 45) {
								txtConsole.setText("1");
							}else if(dTempTan == 135) {
								txtConsole.setText("-1");
							}else if(dTempTan == -45) {
								txtConsole.setText("-1");
							}else if(dTempTan == -135) {
								txtConsole.setText("1");
							}else {
								if (dTan % 1 == 0) {
									dCTan = Math.round(dTan);
									int iCTan = (int) dCTan;
									txtConsole.setText("" + iCTan);
								} else {
									txtConsole.setText("" + dTan);
								}
							}
						}
						bAppend = false;
					}
				}
			};
			btnScnpad[3][2].addActionListener(acTANRad);
			btnScnpad[3][1].addActionListener(acTANRad);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		cHW01_Calculator_T143738 GUI = new cHW01_Calculator_T143738();
		GUI.setDefaultCloseOperation(EXIT_ON_CLOSE);
		GUI.setVisible(true);
	}

}
