import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.ini4j.Wini;

public class reader {
	String USRPath = "Ayria/Assets/Platformwrapper/Username.txt", UIDPath = "Ayria/Assets/Platformwrapper/UserID.txt", appidPath = "ayria_appid.txt", iniPath = "PNLauncher.ini",
					GamePath, GameArgs, EFx64ID, EFSymlinks;
	String[] exeFile = {"iw5sp.exe", "iw5mp.exe",				// [0], [1]
					   "t6sp.exe", "t6zm.exe", "t6mp.exe",		// [2], [3], [4]
					   "iw6sp64_ship.exe", "iw6mp64_ship.exe",	// [5], [6]
					   "s1_sp64_ship.exe", "s1_mp64_ship.exe",	// [7], [8]
					   "h1_sp64_ship.exe", "h1_mp64_ship.exe"},	// [9], [10]
			exeAppID = {"42680", "42690",
						"202970", "212910", "202990",
						"209160", "209170",
						"209650", "209660",
						"393080", "393100"};
	int gameSel;
	
	
	
	
	/* ****************************************************************
	 * 
	 * Check PNLauncher.ini, Username.txt and UserID.txt now to avoid future problems.
	 * 
	 * ****************************************************************/
	public void iniC() {
		File iniFile = new File(iniPath);
		if (!iniFile.exists() || iniFile.length() == 0) {
		     InputStream RsrcFile = (getClass().getResourceAsStream(iniFile.toString()));
		     try {
				Files.copy(RsrcFile, iniFile.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				RsrcFile.close();
			} catch (IOException e) {
				System.err.println("[ERROR] " + e);
				writer.WinMsg("[ERROR] PNLauncher", "PNLauncher.ini not found or incomplete.\nCheck the \"Troubleshooting\" part on GitHub", "ERROR");
				System.exit(0);
			}
		}
	}
	
	public void txtC() {
		File FLR = new File("Ayria");
		File USRfile = new File(USRPath);
		File UIDfile = new File(UIDPath);
		if (!FLR.isDirectory()) {
			System.err.println("[ERROR] Ayria folder not found. Please, download and install ProjectNova.");
			writer.WinMsg("[ERROR] PNLauncher", "Ayria folder not found. Please, download and install ProjectNova.", "ERROR");
			System.exit(0);
		} else if (!USRfile.exists() || !UIDfile.exists()) {
			try {
				USRfile.getParentFile().mkdirs();
				USRfile.createNewFile();
				UIDfile.createNewFile();
			} catch (Exception e) {
				if (!USRfile.getParentFile().exists()) {
					System.err.println("[ERROR] Ayria folder is incomplete. Please, download and install ProjectNova.");
					writer.WinMsg("[ERROR] PNLauncher", "Ayria folder is incomplete. Please, download and install ProjectNova.", "ERROR");
					System.exit(0);
				}
				System.err.println("[ERROR] " + e);
			}
		}
	}
	
	public boolean logC() {
		File LogsFlr = new File("Ayria/PNL/Logs");
		if (!LogsFlr.exists()) {
			return LogsFlr.mkdirs();
		}
		return true;
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * Read the contents of USRPath / UIDPath
	 * 
	 * ****************************************************************/
	public String UsrIDtxt(int UIDSel) {
		File txtFile;
		if (UIDSel == 0) {
			txtFile = new File(USRPath);
		} else {
			txtFile = new File(UIDPath);
		}
		byte[] txtBytes = null;
		int tryNum = 2;
		while (true) {
			try {
				txtBytes = Files.readAllBytes(txtFile.toPath());
				break;
			} catch (IOException e) {
				txtC();
				if (--tryNum == 0) {
					System.err.println("[ERROR] " + e + " not found. Please, reinstall ProjectNova or create the missing file manually.");
					writer.WinMsg("[ERROR] " + e, e.getMessage() + " not found.\nPlease, reinstall ProjectNova or create the missing file manually.", "ERROR");
					System.exit(0);
				}
			}
		}
		return new String(txtBytes);
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * PNLauncher.ini [GamePath]
	 * 
	 * ****************************************************************/
	public String iniGP() {
		try {
			Wini iniReader = new Wini(new File(iniPath));
			switch (gameSel) {
			case 1:
				return GamePath = iniReader.get("GamePath", "IW5");
			case 2:
				return GamePath = iniReader.get("GamePath", "T6");
			case 3:
				return GamePath = iniReader.get("GamePath", "IW6");
			case 4:
				return GamePath = iniReader.get("GamePath", "S1");
			case 5:
				return GamePath = iniReader.get("GamePath", "H1");
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		return "";
	}
	
	/* ****************************************************************
	 * 
	 * PNLauncher.ini [GameArgs]
	 * 
	 * ****************************************************************/
	public String iniGA() {
		try {
			Wini iniReader = new Wini(new File(iniPath));
			switch (gameSel) {
			case 1:
				return GameArgs = iniReader.get("GameArgs", "IW5");
			case 2:
				return GameArgs = iniReader.get("GameArgs", "T6");
			case 3:
				return GameArgs = iniReader.get("GameArgs", "IW6");
			case 4:
				return GameArgs = iniReader.get("GameArgs", "S1");
			case 5:
				return GameArgs = iniReader.get("GameArgs", "H1");
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		return "";
	}
	
	/* ****************************************************************
	 * 
	 * PNLauncher.ini [ExperimentalFeatures]
	 * 
	 * ****************************************************************/
	public String iniEF(String EFSel) {
		try {
			Wini iniReader = new Wini(new File(iniPath));
			if (EFSel.equals("x64ID")) {
				return EFx64ID = iniReader.get("ExperimentalFeatures", "x64ID");
			} else if (EFSel.equals("Symlinks")) {
				return EFSymlinks = iniReader.get("ExperimentalFeatures", "Symlinks");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		return "";
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * Get the current path and check which String[] exeFile exists...
	 * ...everything to make sure that the JComboBox is already selected and save the user 5 seconds.
	 * 
	 * ****************************************************************/
	public String getPath() {
		String currPath = Paths.get("").toAbsolutePath().toString();
		return currPath;
	}
	
	public String getFile() {
		File exeGame;
		String currFile = null;
		for (String exe : exeFile) {
			exeGame = new File(exe);
			if (exeGame.exists()) {
				currFile = exeGame.toString();
				break;
			}
		}
		return currFile;
	}
	
	public int setGame() {
		int cboSel = 0;
		if (getFile() == null) {
			return cboSel;
		} else if (getFile().equals(exeFile[0]) || getFile().equals(exeFile[1])) {
			return cboSel = 1;
		} else if (getFile().equals(exeFile[2]) || getFile().equals(exeFile[3]) || getFile().equals(exeFile[4])) {
			return cboSel = 2;
		} else if (getFile().equals(exeFile[5]) || getFile().equals(exeFile[6])) {
			return cboSel = 3;
		} else if (getFile().equals(exeFile[7]) || getFile().equals(exeFile[8])) {
			return cboSel = 4;
		} else if (getFile().equals(exeFile[9]) || getFile().equals(exeFile[10])) {
			return cboSel = 5;
		}
		return cboSel;
	}
	
	
	
	
}
