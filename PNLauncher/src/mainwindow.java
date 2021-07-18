import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class mainwindow {
	private JFrame frmPNL;
	private JTextField txtName, txtID, txtGamePath, txtGameArgs;
	private JCheckBox chckbxID = new JCheckBox();
	private JCheckBox chckbxInj = new JCheckBox();
	private JSeparator sepLine = new JSeparator();
	private JButton btnPlaySP = new JButton("PLAY SP");
	private JButton btnPlayMP = new JButton("PLAY MP");
	private JButton btnPlayZM = new JButton("PLAY ZM");
	private JButton btnPlayEXO = new JButton("PLAY EXO");
	private final JScrollPane verSBAR = new JScrollPane();
	private JTextArea txtArea = new JTextArea();
	private reader readData = new reader();
	private writer writeData = new writer();
	private symlink symData = new symlink();
	private String USR = readData.USRPath, UID = readData.UIDPath, InjArch = "Ayria\\Injector32.exe", AppID = readData.appidPath, GPnull = "Write the game path - Example: C:\\Games\\", GAnull = "< Write your Launch Options here >";
	private int cboSel;
	
	// Launch the application
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainwindow window = new mainwindow();
					window.frmPNL.setVisible(true);
					window.frmPNL.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Create the application
	public mainwindow() {
		readData.iniC();
		readData.txtC();
		initialize();
	}
	
	// Initialize the contents of the frame
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		// Window background
		// ToolTip colors
		frmPNL = new JFrame();
		frmPNL.setIconImage(Toolkit.getDefaultToolkit().getImage(mainwindow.class.getResource("/img/PN-Logo.png")));
		frmPNL.getContentPane().setBackground(Color.decode("#0d1117"));
		frmPNL.setResizable(false);
		frmPNL.setTitle("PNLauncher (v0.1.0)");
		frmPNL.setSize (393, 360);
		frmPNL.setLocationRelativeTo (null);
		frmPNL.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPNL.getContentPane().setLayout(null);
		UIManager.put("ToolTip.border", new LineBorder(Color.decode("#cfd1d4")));
		UIManager.put("ToolTip.background", Color.decode("#292a2d"));
		UIManager.put("ToolTip.foreground", Color.decode("#cfd1d4"));
		UIManager.put("ToolTip.font", new Font("Consolas", Font.PLAIN, 12));
		
		// Label for "Player Name"
		JLabel lblName = new JLabel("Player Name :");
		lblName.setForeground(Color.decode("#c9d1d9"));
		lblName.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblName.setBounds(10, 10, 91, 21);
		frmPNL.getContentPane().add(lblName);
		
		// Text field for "Player Name"
		txtName = new JTextField();
		txtName.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Ignores color codes when counting number of characters.
				// In addition, it will save any name that you enter.
				if (txtName.getText().replaceAll("\\^[0-9;:]", "").chars().allMatch(Character::isWhitespace)) {
					System.out.println("[WARN] [Player Name] Your name must be >= 1 character or you will be renamed to: Ayria");
					txtArea.append("[WARN] [Player Name] Your name must be >= 1 character or you will be renamed to: Ayria\n");
				} else if (txtName.getText().replaceAll("\\^[0-9;:]", "").length() > 15) {
					System.out.println("[WARN] [Player Name] Your name must be <= 15 characters or it will appear clipped");
					txtArea.append("[WARN] [Player Name] Your name must be <= 15 characters or it will appear clipped\n");
				}
				writeData.FileWP = USR;
				writeData.FileWD = txtName.getText();
				writeData.fileW();
				System.out.println("[INFO] [Player Name] Your name has been saved as: " + readData.UsrIDtxt(0));
				txtArea.append("[INFO] [Player Name] Your name has been saved as: " + readData.UsrIDtxt(0) + "\n");
			}
		});
		txtName.setText(readData.UsrIDtxt(0));
		txtName.setCaretColor(Color.decode("#6e7681"));
		txtName.setBorder(new LineBorder(Color.decode("#30363d")));
		txtName.setForeground(Color.decode("#c9d1d9"));
		txtName.setBackground(Color.decode("#161b22"));
		txtName.setHorizontalAlignment(SwingConstants.CENTER);
		txtName.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtName.setBounds(111, 10, 132, 21);
		txtName.setColumns(10);
		frmPNL.getContentPane().add(txtName);
		
		// Label for "Player ID"
		JLabel lblID = new JLabel("Player ID   :");
		lblID.setForeground(Color.decode("#c9d1d9"));
		lblID.setFont(new Font("Consolas", Font.PLAIN, 12));
		lblID.setBounds(10, 41, 91, 21);
		frmPNL.getContentPane().add(lblID);
		
		// Text field for "Player ID"
		txtID = new JTextField();
		txtID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtID.getText().chars().allMatch(Character::isWhitespace)) {
					System.out.println("[WARN] [Player ID] Your ID must be >= 1 character");
					txtArea.append("[WARN] [Player ID] Your ID must be >= 1 character\n");
				} else if (!txtID.getText().matches("^[0-9A-Fa-f]+$")) {
					System.out.println("[WARN] [Player ID] Your ID must be in hexadecimal format");
					txtArea.append("[WARN] [Player ID] Your ID must be in hexadecimal format\n");
				} else if (!txtID.getText().matches("^(?!11000010+$)(?!0+$)[0-9a-fA-F]+$")) {
					// It can't be all 0 and I should think of a better way to detect it. ;-;
					System.out.println("[WARN] [Player ID] Your ID can't be " + txtID.getText());
					txtArea.append("[WARN] [Player ID] Your ID can't be " + txtID.getText() + "\n");
				} else if (chckbxID.isSelected() && !txtID.getText().matches("(?=^1100001.*$)[\\w]{15}$")) {
					System.out.println("[WARN] [Player ID] Your 64-bit ID must start with 1100001 and have 15 characters in total (for example: 110000100000001)");
					txtArea.append("[WARN] [Player ID] Your 64-bit ID must start with 1100001 and have 15 characters in total (for example: 110000100000001)\n");
				} else if (!chckbxID.isSelected() && txtID.getText().length() > 8) {
					System.out.println("[WARN] [Player ID] Your 32-bit ID must be between 1 and 8 characters");
					txtArea.append("[WARN] [Player ID] Your 32-bit ID must be between 1 and 8 characters\n");
				} else {
					writeData.FileWP = UID;
					writeData.FileWD = txtID.getText();
					writeData.fileW();
					if (chckbxID.isSelected()) {
						System.out.println("[INFO] [Player ID] Your 64-bit ID has been saved as: " + readData.UsrIDtxt(1));
						txtArea.append("[INFO] [Player ID] Your 64-bit ID has been saved as: " + readData.UsrIDtxt(1) + "\n");
					} else {
						System.out.println("[INFO] [Player ID] Your 32-bit ID has been saved as: " + readData.UsrIDtxt(1));
						txtArea.append("[INFO] [Player ID] Your 32-bit ID has been saved as: " + readData.UsrIDtxt(1) + "\n");
					}
				}
			}
		});
		txtID.setText(readData.UsrIDtxt(1));
		txtID.setCaretColor(Color.decode("#6e7681"));
		txtID.setBorder(new LineBorder(Color.decode("#30363d")));
		txtID.setForeground(Color.decode("#c9d1d9"));
		txtID.setBackground(Color.decode("#161b22"));
		txtID.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtID.setHorizontalAlignment(SwingConstants.CENTER);
		txtID.setBounds(111, 41, 132, 21);
		txtID.setColumns(10);
		frmPNL.getContentPane().add(txtID);
		
		// Button for "RANDOM"
		JButton btnRndID = new JButton("RANDOM");
		btnRndID.setContentAreaFilled(false);
		btnRndID.setFocusPainted(false);
		btnRndID.setBorder(new LineBorder(Color.decode("#30363d")));
		btnRndID.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * The idea is to generate an ID in 32-bit hex format | https://projectnova.us/community/threads/68
				 * 32-bit : 0x00000000 - 0xFFFFFFFF
				 * 64-bit : 0x110000100000000 - 0x1100001FFFFFFFF
				 * Note   : 0x part can be omitted
				 */
				char[] hex = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
				Random rnd = new Random();
				String hexrslt = "";
				for (int i = 0; i < 8; i++) {
					int resInt = rnd.nextInt(hex.length);
					hexrslt += hex[resInt];
				}
				if (chckbxID.isSelected()) {
					String hexrslt64 = new StringBuilder(hexrslt).insert(0, "1100001").toString();
					txtID.setText(hexrslt64);
					writeData.FileWP = UID;
					writeData.FileWD = txtID.getText();
					writeData.fileW();
					System.out.println("[INFO] [Player ID] Your 64-bit ID has been saved as: " + readData.UsrIDtxt(1));
					txtArea.append("[INFO] [Player ID] Your 64-bit ID has been saved as: " + readData.UsrIDtxt(1) + "\n");
				} else {
					txtID.setText(hexrslt);
					writeData.FileWP = UID;
					writeData.FileWD = txtID.getText();
					writeData.fileW();
					System.out.println("[INFO] [Player ID] Your 32-bit ID has been saved as: " + readData.UsrIDtxt(1));
					txtArea.append("[INFO] [Player ID] Your 32-bit ID has been saved as: " + readData.UsrIDtxt(1) + "\n");
				}
			}
		});
		btnRndID.setBackground(Color.decode("#21262d"));
		btnRndID.setForeground(Color.decode("#c9d1d9"));
		btnRndID.setToolTipText("Generates a random 32-bit ID");
		btnRndID.setBounds(253, 41, 91, 21);
		frmPNL.getContentPane().add(btnRndID);
		
		// CheckBox for "RANDOM"
		//JCheckBox chckbxID = new JCheckBox();
		if (readData.iniEF("x64ID").equals("0")) {
			chckbxID.setEnabled(false);
		} else {
			chckbxID.setEnabled(true);
			chckbxID.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						btnRndID.setToolTipText("Generates a random 64-bit ID");
					} else {
						btnRndID.setToolTipText("Generates a random 32-bit ID");
					}
				}
			});
		}
		chckbxID.setOpaque(false);
		chckbxID.setToolTipText("64-bit ID format?");
		chckbxID.setBounds(350, 41, 21, 21);
		frmPNL.getContentPane().add(chckbxID);
		
		// ComboBox for "Select a game..."
		JComboBox cboGames = new JComboBox(new String[] {"Select a game...",
														 "IW5 - Call of Duty: Modern Warfare 3",
														 "T6  - Call of Duty: Black Ops II",
														 "IW6 - Call of Duty: Ghosts",
														 "S1  - Call of Duty: Advanced Warfare",
														 "H1  - Call of Duty: Modern Warfare Remastered"
														});
		cboGames.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		        //txtArea.getDocument().remove(0, txtArea.getDocument().getLength()); // Clears the txtArea
				readData.gameSel = cboSel = cboGames.getSelectedIndex();
				switch(cboSel) {
				case 1:
					chckbxInj.setSelected(false);
					if (readData.iniGP().chars().allMatch(Character::isWhitespace) && readData.getFile() != null && readData.iniEF("Symlinks").equals("0")) {
						txtGamePath.setText(readData.getPath());
						txtGamePath.postActionEvent();
					} else if (readData.iniGP().chars().allMatch(Character::isWhitespace)) {
						txtGamePath.setText(GPnull + "CoD MW 3");
					} else {
						txtGamePath.setText(readData.iniGP());
					}
					if (readData.iniGA().chars().allMatch(Character::isWhitespace)) {
						txtGameArgs.setText(GAnull);
					} else {
						txtGameArgs.setText(readData.iniGA());
					}
					if (readData.iniEF("Symlinks").equals("1")) {
						symData.setGS("IW5");
						symData.GamePath = readData.iniGP();
						symData.chckPre();
					}
					sepLine.setVisible(true);
					txtGamePath.setVisible(true);
					txtGameArgs.setVisible(true);
					btnPlaySP.setVisible(true);
					btnPlayMP.setVisible(true);
					btnPlayZM.setVisible(false);
					btnPlayEXO.setVisible(false);
					verSBAR.setVisible(true);
					verSBAR.setBounds(10, 201, 357, 110);
					break;
				case 2:
					chckbxInj.setSelected(false);
					if (readData.iniGP().chars().allMatch(Character::isWhitespace) && readData.getFile() != null && readData.iniEF("Symlinks").equals("0")) {
						txtGamePath.setText(readData.getPath());
						txtGamePath.postActionEvent();
					} else if (readData.iniGP().chars().allMatch(Character::isWhitespace)) {
						txtGamePath.setText(GPnull + "CoD BO II");
					} else {
						txtGamePath.setText(readData.iniGP());
					}
					if (readData.iniGA().chars().allMatch(Character::isWhitespace)) {
						txtGameArgs.setText(GAnull);
					} else {
						txtGameArgs.setText(readData.iniGA());
					}
					if (readData.iniEF("Symlinks").equals("1")) {
						symData.setGS("T6");
						symData.GamePath = readData.iniGP();
						symData.chckPre();
					}
					sepLine.setVisible(true);
					txtGamePath.setVisible(true);
					txtGameArgs.setVisible(true);
					btnPlaySP.setVisible(true);
					btnPlayMP.setVisible(true);
					btnPlayZM.setText("PLAY ZM");
					btnPlayZM.setVisible(true);
					btnPlayZM.setBounds(100, 194, 177, 21);
					btnPlayEXO.setVisible(false);
					verSBAR.setVisible(true);
					verSBAR.setBounds(10, 225, 357, 86);
					break;
				case 3:
					chckbxInj.setSelected(true);
					if (readData.iniGP().chars().allMatch(Character::isWhitespace) && readData.getFile() != null && readData.iniEF("Symlinks").equals("0")) {
						txtGamePath.setText(readData.getPath());
						txtGamePath.postActionEvent();
					} else if (readData.iniGP().chars().allMatch(Character::isWhitespace)) {
						txtGamePath.setText(GPnull + "CoD G");
					} else {
						txtGamePath.setText(readData.iniGP());
					}
					if (readData.iniGA().chars().allMatch(Character::isWhitespace)) {
						txtGameArgs.setText(GAnull);
					} else {
						txtGameArgs.setText(readData.iniGA());
					}
					if (readData.iniEF("Symlinks").equals("1")) {
						symData.setGS("IW6");
						symData.GamePath = readData.iniGP();
						symData.chckPre();
					}
					sepLine.setVisible(true);
					txtGamePath.setVisible(true);
					txtGameArgs.setVisible(true);
					btnPlaySP.setVisible(true);
					btnPlayMP.setVisible(true);
					btnPlayZM.setVisible(false);
					btnPlayEXO.setVisible(false);
					verSBAR.setVisible(true);
					verSBAR.setBounds(10, 201, 357, 110);
					break;
				case 4:
					chckbxInj.setSelected(true);
					if (readData.iniGP().chars().allMatch(Character::isWhitespace) && readData.getFile() != null && readData.iniEF("Symlinks").equals("0")) {
						txtGamePath.setText(readData.getPath());
						txtGamePath.postActionEvent();
					} else if (readData.iniGP().chars().allMatch(Character::isWhitespace)) {
						txtGamePath.setText(GPnull + "CoD AW");
					} else {
						txtGamePath.setText(readData.iniGP());
					}
					if (readData.iniGA().chars().allMatch(Character::isWhitespace)) {
						txtGameArgs.setText(GAnull);
					} else {
						txtGameArgs.setText(readData.iniGA());
					}
					if (readData.iniEF("Symlinks").equals("1")) {
						symData.setGS("S1");
						symData.GamePath = readData.iniGP();
						symData.chckPre();
					}
					sepLine.setVisible(true);
					txtGamePath.setVisible(true);
					txtGameArgs.setVisible(true);
					btnPlaySP.setVisible(true);
					btnPlayMP.setVisible(true);
					btnPlayEXO.setVisible(true);
					btnPlayZM.setText("PLAY EXO ZM");
					btnPlayZM.setVisible(true);
					btnPlayZM.setBounds(190, 194, 177, 21);
					verSBAR.setVisible(true);
					verSBAR.setBounds(10, 225, 357, 86);
					break;
				case 5:
					chckbxInj.setSelected(true);
					if (readData.iniGP().chars().allMatch(Character::isWhitespace) && readData.getFile() != null && readData.iniEF("Symlinks").equals("0")) {
						txtGamePath.setText(readData.getPath());
						txtGamePath.postActionEvent();
					} else if (readData.iniGP().chars().allMatch(Character::isWhitespace)) {
						txtGamePath.setText(GPnull + "CoD MW R");
					} else {
						txtGamePath.setText(readData.iniGP());
					}
					if (readData.iniGA().chars().allMatch(Character::isWhitespace)) {
						txtGameArgs.setText(GAnull);
					} else {
						txtGameArgs.setText(readData.iniGA());
					}
					if (readData.iniEF("Symlinks").equals("1")) {
						symData.setGS("H1");
						symData.GamePath = readData.iniGP();
						symData.chckPre();
					}
					sepLine.setVisible(true);
					txtGamePath.setVisible(true);
					txtGameArgs.setVisible(true);
					btnPlaySP.setVisible(true);
					btnPlayMP.setVisible(true);
					btnPlayZM.setVisible(false);
					btnPlayEXO.setVisible(false);
					verSBAR.setVisible(true);
					verSBAR.setBounds(10, 201, 357, 110);
					break;
				default:
					sepLine.setVisible(false);
					txtGamePath.setVisible(false);
					txtGameArgs.setVisible(false);
					btnPlaySP.setVisible(false);
					btnPlayMP.setVisible(false);
					btnPlayZM.setVisible(false);
					btnPlayEXO.setVisible(false);
					verSBAR.setVisible(true);
					verSBAR.setBounds(10, 103, 357, 208);
					break;
				}
			}
		});
		cboGames.setForeground(Color.decode("#c9d1d9"));
		cboGames.setBorder(new LineBorder(Color.decode("#30363d")));
		cboGames.setBackground(Color.decode("#21262d"));
		cboGames.setFont(new Font("Consolas", Font.PLAIN, 12));
		cboGames.setBounds(10, 72, 334, 21);
		frmPNL.getContentPane().add(cboGames);
		
		// CheckBox for "Select a game..."
		//JCheckBox chckbxInj = new JCheckBox();
		chckbxInj.addItemListener(new ItemListener () {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					InjArch = "Ayria\\Injector64.exe";
				} else {
					InjArch = "Ayria\\Injector32.exe";
				}
			}
		});
		chckbxInj.setToolTipText("Use 64-bit Injector?");
		chckbxInj.setOpaque(false);
		chckbxInj.setBounds(350, 72, 21, 21);
		frmPNL.getContentPane().add(chckbxInj);
		
		// Separator Line
		//JSeparator sepLine = new JSeparator();
		sepLine.setVisible(false);
		sepLine.setForeground(Color.decode("#30363d"));
		sepLine.setBackground(Color.decode("#30363d"));
		sepLine.setBounds(10, 103, 357, 2);
		frmPNL.getContentPane().add(sepLine);
		
		// Text field for "Game Path"
		txtGamePath = new JTextField();
		txtGamePath.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtGamePath.getText().trim().startsWith(GPnull)) {
					txtGamePath.setText("");
					//txtGamePath.setForeground(Color.decode("#c9d1d9"));
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (txtGamePath.getText().trim().equals("")) {
					cboSel = cboGames.getSelectedIndex();
					switch(cboSel) {
					case 1:
						txtGamePath.setText(GPnull + "CoD MW 3");
						break;
					case 2:
						txtGamePath.setText(GPnull + "CoD BO II");
						break;
					case 3:
						txtGamePath.setText(GPnull + "CoD G");
						break;
					case 4:
						txtGamePath.setText(GPnull + "CoD AW");
						break;
					case 5:
						txtGamePath.setText(GPnull + "CoD MW R");
						break;
					default:
						break;
					}
					//txtGamePath.setForeground(Color.decode("#8e9297"));
				}
			}
		});
		txtGamePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cboSel = cboGames.getSelectedIndex();
				writeData.Sec = "GamePath";
				switch(cboSel) {
				case 1:
					writeData.Key = "IW5";
					writeData.Val = txtGamePath.getText();
					writeData.iniW();
					break;
				case 2:
					writeData.Key = "T6";
					writeData.Val = txtGamePath.getText();
					writeData.iniW();
					break;
				case 3:
					writeData.Key = "IW6";
					writeData.Val = txtGamePath.getText();
					writeData.iniW();
					break;
				case 4:
					writeData.Key = "S1";
					writeData.Val = txtGamePath.getText();
					writeData.iniW();
					break;
				case 5:
					writeData.Key = "H1";
					writeData.Val = txtGamePath.getText();
					writeData.iniW();
					break;
				default:
					break;
				}
				System.out.println("[INFO] [Game Path] Your game path has been saved as: " + txtGamePath.getText());
				txtArea.append("[INFO] [Game Path] Your game path has been saved as: " + txtGamePath.getText() + "\n");
			}
		});
		txtGamePath.setVisible(false);
		txtGamePath.setCaretColor(Color.decode("#6e7681"));
		txtGamePath.setBorder(new LineBorder(Color.decode("#30363d")));
		txtGamePath.setForeground(Color.decode("#c9d1d9"));
		txtGamePath.setBackground(Color.decode("#161b22"));
		txtGamePath.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtGamePath.setHorizontalAlignment(SwingConstants.CENTER);
		txtGamePath.setBounds(10, 115, 357, 21);
		txtGamePath.setColumns(10);
		frmPNL.getContentPane().add(txtGamePath);
		
		// Text field for "Game Args"
		txtGameArgs = new JTextField();
		txtGameArgs.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (txtGameArgs.getText().trim().equals(GAnull)) {
					txtGameArgs.setText("");
					//txtGameArgs.setForeground(Color.decode("#c9d1d9"));
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (txtGameArgs.getText().trim().equals("")) {
					txtGameArgs.setText(GAnull);
					//txtGameArgs.setForeground(Color.decode("#8e9297"));
				}
			}
		});
		txtGameArgs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cboSel = cboGames.getSelectedIndex();
				writeData.Sec = "GameArgs";
				switch(cboSel) {
					case 1:
						writeData.Key = "IW5";
						writeData.Val = txtGameArgs.getText();
						writeData.iniW();
						break;
					case 2:
						writeData.Key = "T6";
						writeData.Val = txtGameArgs.getText();
						writeData.iniW();
						break;
					case 3:
						writeData.Key = "IW6";
						writeData.Val = txtGameArgs.getText();
						writeData.iniW();
						break;
					case 4:
						writeData.Key = "S1";
						writeData.Val = txtGameArgs.getText();
						writeData.iniW();
						break;
					case 5:
						writeData.Key = "H1";
						writeData.Val = txtGameArgs.getText();
						writeData.iniW();
						break;
					default:
						break;
				}
				System.out.println("[INFO] [Game Args] Your game args has been saved as: " + txtGameArgs.getText());
				txtArea.append("[INFO] [Game Args] Your game args has been saved as: " + txtGameArgs.getText() + "\n");
			}
		});
		txtGameArgs.setText(GAnull);
		txtGameArgs.setVisible(false);
		txtGameArgs.setCaretColor(Color.decode("#6e7681"));
		txtGameArgs.setBorder(new LineBorder(Color.decode("#30363d")));
		txtGameArgs.setForeground(Color.decode("#8e9297"));
		txtGameArgs.setBackground(Color.decode("#161b22"));
		txtGameArgs.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtGameArgs.setHorizontalAlignment(SwingConstants.CENTER);
		txtGameArgs.setBounds(10, 139, 357, 21);
		txtGameArgs.setColumns(10);
		frmPNL.getContentPane().add(txtGameArgs);
		
		// Button for "Play SP"
		//JButton btnPlaySP = new JButton("PLAY SP");
		btnPlaySP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cboSel = cboGames.getSelectedIndex();
				System.out.println("[INFO] [" + e.getActionCommand() + "] Trying to start...");
				txtArea.append("[INFO] [" + e.getActionCommand() + "] Trying to start...\n");
				writeData.FileWP = AppID;
				switch(cboSel) {
				case 1:
					writeData.FileWD = readData.exeAppID[0];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[0], null, null);
					break;
				case 2:
					writeData.FileWD = readData.exeAppID[2];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[2], null, null);
					break;
				case 3:
					writeData.FileWD = readData.exeAppID[5];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[5], null, null);
					break;
				case 4:
					writeData.FileWD = readData.exeAppID[7];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[7], null, null);
					break;
				case 5:
					writeData.FileWD = readData.exeAppID[9];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[9], null, null);
					break;
				default:
					break;
				}
			}
		});
		btnPlaySP.setVisible(false);
		btnPlaySP.setContentAreaFilled(false);
		btnPlaySP.setFocusPainted(false);
		btnPlaySP.setBorder(new LineBorder(Color.decode("#30363d")));
		btnPlaySP.setBackground(Color.decode("#21262d"));
		btnPlaySP.setForeground(Color.decode("#c9d1d9"));
		btnPlaySP.setBounds(10, 170, 177, 21);
		frmPNL.getContentPane().add(btnPlaySP);
		
		// Button for "Play MP"
		//JButton btnPlayMP = new JButton("PLAY MP");
		btnPlayMP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cboSel = cboGames.getSelectedIndex();
				System.out.println("[INFO] [" + e.getActionCommand() + "] Trying to start...");
				txtArea.append("[INFO] [" + e.getActionCommand() + "] Trying to start...\n");
				writeData.FileWP = AppID;
				switch(cboSel) {
				case 1:
					writeData.FileWD = readData.exeAppID[1];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[1], null, null);
					break;
				case 2:
					writeData.FileWD = readData.exeAppID[4];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[4], null, null);
					break;
				case 3:
					writeData.FileWD = readData.exeAppID[6];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[6], null, null);
					break;
				case 4:
					writeData.FileWD = readData.exeAppID[8];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[8], null, null);
					break;
				case 5:
					writeData.FileWD = readData.exeAppID[10];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[10], null, null);
					break;
				default:
					break;
				}
			}
		});
		btnPlayMP.setVisible(false);
		btnPlayMP.setForeground(Color.decode("#c9d1d9"));
		btnPlayMP.setFocusPainted(false);
		btnPlayMP.setContentAreaFilled(false);
		btnPlayMP.setBorder(new LineBorder(Color.decode("#30363d")));
		btnPlayMP.setBackground(Color.decode("#21262d"));
		btnPlayMP.setBounds(190, 170, 177, 21);
		frmPNL.getContentPane().add(btnPlayMP);
		
		// Button for "Play ZM" / "Play EXO ZM"
		//JButton btnPlayZM = new JButton("PLAY ZM");
		btnPlayZM.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cboSel = cboGames.getSelectedIndex();
				System.out.println("[INFO] [" + e.getActionCommand() + "] Trying to start...");
				txtArea.append("[INFO] [" + e.getActionCommand() + "] Trying to start...\n");
				writeData.FileWP = AppID;
				switch(cboSel) {
				case 2:
					writeData.FileWD = readData.exeAppID[3];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[3], null, null);
					break;
				case 4:
					writeData.FileWD = readData.exeAppID[8];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[8], "+zombiesMode 1", null);
					break;
				default:
					break;
				}
			}
		});
		btnPlayZM.setVisible(false);
		btnPlayZM.setForeground(Color.decode("#c9d1d9"));
		btnPlayZM.setFocusPainted(false);
		btnPlayZM.setContentAreaFilled(false);
		btnPlayZM.setBorder(new LineBorder(Color.decode("#30363d")));
		btnPlayZM.setBackground(Color.decode("#21262d"));
		btnPlayZM.setBounds(190, 194, 177, 21);
		frmPNL.getContentPane().add(btnPlayZM);
		
		// Button for "Play EXO"
		//JButton btnPlayEXO = new JButton("PLAY EXO");
		btnPlayEXO.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cboSel = cboGames.getSelectedIndex();
				System.out.println("[INFO] [" + e.getActionCommand() + "] Trying to start...");
				txtArea.append("[INFO] [" + e.getActionCommand() + "] Trying to start...\n");
				writeData.FileWP = AppID;
				switch(cboSel) {
				case 4:
					writeData.FileWD = readData.exeAppID[8];
					writeData.fileW();
					writeData.procRun(readData.gameSel=cboSel, InjArch, readData.exeFile[8], "+survival 1", null);
					break;
				default:
					break;
				}
			}
		});
		btnPlayEXO.setVisible(false);
		btnPlayEXO.setForeground(Color.decode("#c9d1d9"));
		btnPlayEXO.setFocusPainted(false);
		btnPlayEXO.setContentAreaFilled(false);
		btnPlayEXO.setBorder(new LineBorder(Color.decode("#30363d")));
		btnPlayEXO.setBackground(Color.decode("#21262d"));
		btnPlayEXO.setBounds(10, 194, 177, 21);
		frmPNL.getContentPane().add(btnPlayEXO);
		
		// Vertical Scroll Bar + Text Area for "Logs & Info"
		verSBAR.setVisible(true);
		verSBAR.setBorder(null);
		verSBAR.getVerticalScrollBar().setPreferredSize(new Dimension(6, 0));
		verSBAR.getVerticalScrollBar().setBackground(Color.decode("#21262d"));
		verSBAR.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = Color.decode("#32514f");
		    }
		    
		    @Override
	        protected JButton createDecreaseButton(int orientation) {
	            return createZeroButton();
	        }

	        @Override    
	        protected JButton createIncreaseButton(int orientation) {
	            return createZeroButton();
	        }

	        private JButton createZeroButton() {
	            JButton jb = new JButton();
	            jb.setPreferredSize(new Dimension(0, 0));
	            jb.setMinimumSize(new Dimension(0, 0));
	            jb.setMaximumSize(new Dimension(0, 0));
	            return jb;
	        }
		});
		verSBAR.setBounds(10, 103, 357, 208);
		frmPNL.getContentPane().add(verSBAR);
		//JTextArea txtArea = new JTextArea();
		verSBAR.setViewportView(txtArea);
		if (txtName.getText().chars().allMatch(Character::isWhitespace)) {
			System.out.print("Welcome " + System.getProperty("user.name") + " to PNLauncher\n\n");
			txtArea.setText("Welcome " + System.getProperty("user.name") + " to PNLauncher\n\n");
		} else {
			System.out.print("Welcome " + txtName.getText().replaceAll("\\^[0-9;:]", "") + " to PNLauncher\n\n");
			txtArea.setText("Welcome " + txtName.getText().replaceAll("\\^[0-9;:]", "") + " to PNLauncher\n\n");
		}
		if (readData.iniEF("x64ID").equals("1") || readData.iniEF("Symlinks").equals("1")) {
			System.out.println("[WARN] [Experimental Features] Some things can go either very good or very bad, use at your own risk.");
			txtArea.append("[WARN] [Experimental Features] Some things can go either very good or very bad, use at your own risk.\n");
		}
		txtArea.setWrapStyleWord(true);
		txtArea.setLineWrap(true);
		txtArea.setForeground(Color.decode("#8e9297"));
		txtArea.setSelectionColor(Color.decode("#4f545c"));
		txtArea.setSelectedTextColor(Color.decode("#c9d1d9"));
		txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtArea.setEditable(false);
		txtArea.setBorder(new LineBorder(Color.decode("#30363d")));
		txtArea.setBackground(Color.decode("#161b22"));
		
		// Text field for "Player ID"
		if (txtID.getText().equals("CHANG3M3") || txtID.getText().length() == 0) {
			System.out.println("[WARN] [Player ID] I recommend to change your ID");
			txtArea.append("[WARN] [Player ID] I recommend to change your ID\n");
		} else if (readData.iniEF("x64ID").equals("1") && txtID.getText().matches("(?=^1100001.*$)[\\w]{15}$")) {
			chckbxID.setSelected(true);
		}
		
		// ComboBox for "Select a game..."
		// Select the item according to the detected .exe file
		if (readData.iniEF("Symlinks").equals("1") && readData.getFile() != null) {
			cboSel = readData.setGame();
			cboGames.setSelectedIndex(cboSel);
		} else if (readData.getFile() != null) {
			cboSel = readData.setGame();
			cboGames.setSelectedIndex(cboSel);
			cboGames.setEnabled(false);
			UIManager.put("ComboBox.disabledBackground", Color.decode("#21262d"));
			UIManager.put("ComboBox.disabledForeground", Color.decode("#8e9297"));
		}
	}
}
