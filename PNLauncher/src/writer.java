import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class writer {
	/* ****************************************************************
	 * RA pls no forget @,@
	 * 
	 * FileWP : FileWritePath
	 * FileWD : FileWriteData
	 * 
	 * ****************************************************************/
	private reader readData = new reader();
	String iniPath = readData.iniPath, Sec, Key, Val, FileWP, FileWD;
	
	
	
	
	/* ****************************************************************
	 * 
	 * Write to *.txt / PNLauncher.ini file
	 * 
	 * ****************************************************************/
	public void fileW() {
		try {
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FileWP, false));
			fileWriter.write(FileWD);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
	}
	
	public void iniW() {
		try {
			Wini iniWriter = new Wini(new File(iniPath));
			iniWriter.put(Sec, Key, Val);
			iniWriter.store();
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * All this to create a process... ;-;!
	 * 
	 * ****************************************************************/
	public void procRun(int GameSel, String InjFile, String GameFile, String GameArgs, String GamePath) {
		readData.gameSel = GameSel;
		if (readData.iniEF("Symlinks").equals("1")) {
			InjFile = readData.getPath()+"\\"+InjFile;
			GameFile = readData.getPath()+"\\"+GameFile;
			GamePath = readData.getPath();
		} else {
			GamePath = readData.iniGP();
		}
		if (GameArgs == null) {
			GameArgs = readData.iniGA();
		} else {
			GameArgs = GameArgs + " " + readData.iniGA();
		}
		
		
		try {
			Process proc = Runtime.getRuntime().exec(new String[] {"CMD", "/C", InjFile, GameFile, GameArgs}, null, new File(GamePath));	// Runs the Injector##.exe with everything the user has typed in.
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));										// And this is just to read what Injector##.exe says. 
			String str = null;
			System.out.println("\n---------------- " + InjFile.replaceAll("^[/\\\\\\\\]?(?:.+[/\\\\\\\\]+?)?(.+?)[/\\\\\\\\]?$", "$1") + " ----------------\n"); // All to say 32 or 64 . _  .
			while ((str = stdInput.readLine()) != null) {
				System.out.println(str);
			}
			System.out.println("\n------------------------------------------------\n");
			stdInput.close();
			Thread.sleep(1500);
			if (readData.iniEF("Symlinks").equals("0") && proc.exitValue() == 0) {
				System.exit(0);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * And this racing stripe here I feel is pretty sharp.
	 * 
	 * ****************************************************************/
	public static void WinMsg(String title, String msg, String type) {
		UIManager.put("OptionPane.background", Color.decode("#0d1117"));
		UIManager.put("Panel.background", Color.decode("#0d1117"));
		UIManager.put("OptionPane.messageForeground", Color.decode("#cfd1d4"));
		UIManager.put("Button.background", Color.decode("#21262d"));
		UIManager.put("Button.focus", Color.decode("#21262d"));
		UIManager.put("Button.foreground", Color.decode("#c9d1d9"));
		UIManager.put("Button.select", Color.decode("#21262d"));
		switch(type) {
		case "ERROR":
			JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
			break;
		case "WARN":
			JOptionPane.showMessageDialog(null, msg, title, JOptionPane.WARNING_MESSAGE);
			break;
		case "INFO":
			JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
			break;
		case "QUESTION":
			JOptionPane.showMessageDialog(null, msg, title, JOptionPane.QUESTION_MESSAGE);
			break;
		default:
			JOptionPane.showMessageDialog(null, msg, title, JOptionPane.PLAIN_MESSAGE);
			break;
		}
	}
	
	
	
	
}
