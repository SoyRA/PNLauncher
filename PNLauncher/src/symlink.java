import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class symlink {
	/* ****************************************************************
	 * 
	 * The idea is to make a "Symbolic Link Clone" like LSE does
	 *   * Someday I'll do it with pure Java and multiplatform :P
	 * 
	 * - LSE : https://schinagl.priv.at/nt/hardlinkshellext/linkshellextension.html
	 * - Ln  : https://schinagl.priv.at/nt/ln/ln.html
	 * 
	 * ****************************************************************/
	private reader readData = new reader();
	String GamePath, TargetPath = readData.getPath(), gameSel;
	private String lnexe = "Ayria/PNL/ln.exe", ExclFile = "Ayria/PNL/ExclFileList.txt", ExclFlr = "Ayria/PNL/ExclFlrList.txt", lnArgs = "--output Ayria/PNL/Logs/ln.exe.log -s -o";
	
	
	
	
	/* ****************************************************************
	 * 
	 * Some checks before doing anything
	 * 
	 * ****************************************************************/
	private String cur = gameSel;
	private String old;
	
	/*public String getGS() {
		return cur;
	}*/
	
	public void setGS(String s) {
		old = cur;
		cur = s;
	}
	
	public void chckPre() {
		if (GamePath.chars().allMatch(Character::isWhitespace)) {
			System.out.println("[WARN] Game Path is required to continue. Once saved, select the same game again or restart PNLauncher.");
			return;
		}
		
		
		if (!readData.logC()) {
			System.out.println("[WARN] It will not be possible to generate " + lnArgs.replaceAll("(?:--output | -s -o)", "") + ", but we will continue.");
		}
		
		
		File FLR = new File("Ayria");
		File iniFile = new File(readData.iniPath);
		File lnFile = new File(lnexe);
		File lnExclFile = new File(ExclFile);
		File lnExclFlr = new File(ExclFlr);
		if (!lnFile.exists() || !lnExclFile.exists() || !lnExclFlr.exists()) {
			InputStream RsrclnFile = (getClass().getResourceAsStream("libs/lnstatic/ln.exe"));
			InputStream RsrclnExclFile = (getClass().getResourceAsStream(ExclFile));
			InputStream RsrclnExclFlr = (getClass().getResourceAsStream(ExclFlr));
			try {
				Files.copy(RsrclnFile, lnFile.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				Files.copy(RsrclnExclFile, lnExclFile.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				Files.copy(RsrclnExclFlr, lnExclFlr.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("[ERROR] " + e);
				System.err.println("[ERROR] Uhh we'll need that to live. If we can't extract \"" + lnexe + "\", \"" + ExclFile + "\" and \"" + ExclFlr + "\" we can't continue. =(");
				return;
			}
		} else if (lnExclFile.length() > 137 || lnExclFlr.length() > 70) {
			// Oh yes, what a way to revert the changes made by H1PrtlUpd
			InputStream RsrclnExclFile = (getClass().getResourceAsStream(ExclFile));
			InputStream RsrclnExclFlr = (getClass().getResourceAsStream(ExclFlr));
			try {
				Files.copy(RsrclnExclFile, lnExclFile.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				Files.copy(RsrclnExclFlr, lnExclFlr.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("[ERROR] " + e);
				System.out.println("[WARN] \"" + ExclFile + "\" and/or \"" + ExclFlr + "\" are modified and we couldn't replace them, let's hope nothing goes wrong anyway.");
			}
		}
		
		
		// Checks if the current directory has only PNLauncher.bat, PNLauncher.jar, PNLauncher.ini and Ayria
		// Check if the current directory has any symlinks
		List<Path> chckFiles = null, chckSymlink = null;
		try {
			chckFiles = Files.walk(Paths.get(""), 1).filter(F -> Files.isRegularFile(F) && !F.equals(Paths.get("PNLauncher.bat")) && !F.equals(Paths.get("PNLauncher.jar")) && !F.equals(iniFile.toPath()) && !F.equals(lnFile.toPath()) || Files.isDirectory(F) && !F.equals(Paths.get("")) && !F.equals(FLR.toPath())).collect(Collectors.toList());
			chckSymlink = Files.walk(Paths.get("")).filter(Files::isSymbolicLink).collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		
		
		if (chckFiles.isEmpty()) {
			if (cur.equals("H1")) {
				H1PrtlUpd();
			}
			procRun();
			return;
		}
		
		
		if (old == null || cur.equals(old)) {
			// Do nothing a,a
		} else {
			if (!chckSymlink.isEmpty() && !getFlr().isEmpty()) {
				moveFiles(0, old);
				if (cur.equals("H1")) {
					H1PrtlUpd();
				}
				chckPost();
			} else {
				moveFiles(1, cur);
				if (cur.equals("H1")) {
					H1PrtlUpd();
				}
				chckPost();
			}
		}
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * Since I already made a Backup, I can now delete and recreate everything.
	 * 
	 * ****************************************************************/
	public void chckPost() {
		cleanDir();
		procRun();
	}
	
	
	// The only reason to use it together with ln.exe is because of a problem:
	// Run -> IW5 -> ln -> T6 -> ln -> It works, but steam_api.dll (and any files that exist in both) points to IW5 instead of T6
	private void cleanDir() {
		Path src = Paths.get(TargetPath);
		try (Stream<File> files = Files.walk(src).filter(Files::isSymbolicLink).map(Path::toFile)) {
			//files.forEach(System.out::println);
			files.forEach(File::delete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		// It should delete ONLY empty directories!
		// And dammit, I failed to exclude the Ayria folder.
		try (Stream<File> files = Files.walk(src).filter(Files::isDirectory).filter(F -> !F.equals(src)).map(Path::toFile).sorted(Comparator.reverseOrder())) {
			//files.forEach(System.out::println);
			files.forEach(File::delete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		// Fuck you iw5sp.exe / t6xx.exe file >:c!
		try (Stream<File> files = Files.walk(src, 1).filter(F -> Files.isRegularFile(F) && F.getFileName().toString().equals(readData.exeFile[0]) || F.getFileName().toString().equals(readData.exeFile[2]) || F.getFileName().toString().equals(readData.exeFile[3]) || F.getFileName().toString().equals(readData.exeFile[4])).map(Path::toFile)) {
			files.forEach(File::delete);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * Save player-generated files (ok, only some of them).
	 *   * If it's not in ExclFile / ExclFlr, ln.exe will delete it!
	 * 
	 * ****************************************************************/
	private void moveFiles(int time, String gameSel) {
		switch (time) {
		// Move the files from "./" to "Ayria/PNL/gameSel"
		case 0:
			for (String ExclArray : getFlr()) {
				Path src = Paths.get(ExclArray);
				Path destn = Paths.get("Ayria/PNL/" + gameSel);
				destn.toFile().mkdirs();
				// All except "main/*.dcache"
				try (Stream<Path> files = Files.walk(src).filter(Files::isRegularFile).filter(F -> !F.getFileName().toString().endsWith("dcache"))) {
					//files.forEach(F -> System.out.println(src.resolve(F.getFileName()) + " -> " + destn.resolve(F.getParent()).resolve(F.getFileName())))
					files.forEach(F -> {
						try {
							Files.createDirectories(destn.resolve(F.getParent()));
							Files.move(src.resolve(F.getFileName()), destn.resolve(F.getParent()).resolve(F.getFileName()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.err.println("[ERROR] " + e);
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("[ERROR] " + e);
				}
				// Just "main/*.dcache"
				try (Stream<Path> dcacheFiles = Files.walk(src).filter(Files::isRegularFile).filter(F -> F.getFileName().toString().endsWith("dcache"))) {
					//dcacheFiles.forEach(F -> System.out.println(src.resolve(F.getFileName()) + " -> " + destn.resolve(F.getParent()).resolve(F.getFileName())));
					dcacheFiles.forEach(F -> {
						try {
							Files.createDirectories(destn.resolve(F.getParent()));
							Files.move(src.resolve(""), destn.resolve(F));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.err.println("[ERROR] " + e);
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("[ERROR] " + e);
				}
				// It should delete ONLY empty directories!
				try (Stream<File> Flrs = Files.walk(src).filter(Files::isDirectory).map(Path::toFile)) {
					Flrs.forEach(File::delete);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("[ERROR] " + e);
				}
			}
			break;
		// Move the files from "Ayria/PNL/gameSel" to "./"
		case 1:
			Path src = Paths.get("Ayria/PNL/" + gameSel);
			Path destn = Paths.get("");
			if (!src.toFile().exists()) {
				break;
			}
			try (Stream<Path> files = Files.walk(src, 1).filter(F -> Files.exists(F) && Files.isDirectory(F) && !F.equals(src))) {
				//files.forEach(F -> System.out.println(src.resolve(F.getFileName()) + " -> " + destn.resolve(F.getFileName())));
				files.forEach(F -> {
					try {
						Files.move(src.resolve(F.getFileName()), destn.resolve(F.getFileName()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.err.println("[ERROR] " + e);
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("[ERROR] " + e);
			}
			break;
		default:
			break;
		}
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * Searches if a folder of ExclArray() exists in the current directory
	 * 
	 * ****************************************************************/
	private List<String> getFlr() {
		List<String> gameFlr = ExclArray(), FlrList = new ArrayList<>();;
		File Flr;
		for (String str : gameFlr) {
			Flr = new File(str);
			if (Flr.exists()) {
				FlrList.add(Flr.toString());
			}
		}
		return FlrList;
	}
	
	private List<String> ExclArray() {
		List<String> FlrList = null;
		Path ExclFlrPath = Paths.get(ExclFlr), srcMain = Paths.get("main");
		// Literally ExclFlr
		try (Stream<String> lines = Files.lines(ExclFlrPath).filter(F -> !F.equals("Ayria")).map(M -> M.replace("\\\\", "\\"))) {
			FlrList = lines.collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		// Just main/*.dcache
		if (srcMain.toFile().isDirectory()) {
			try (Stream<Object> dcacheFiles = Files.walk(srcMain,  1).filter(F -> F.getFileName().toString().endsWith("dcache")).map(F -> srcMain.resolve(F.getFileName()))) {
				for (Object obj: dcacheFiles.collect(Collectors.toList())) {
					FlrList.add(String.valueOf(obj));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("[ERROR] " + e);
			}
		}
		return FlrList;
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * Since H1 1.4 Update has some files that are NOT the same as the last version...
	 * Well, better to use ~170 MB than ~80 GB. :P
	 * 
	 * ****************************************************************/
	private void H1PrtlUpd() {
		if (!cur.equals("H1")) {
			return;
		}
		
		
		File H1Prtl = new File("Ayria/PNL/H1/PrtlUpd");
		if (!H1Prtl.exists()) {
			System.out.println("[INFO] No files have been detected in \"" + H1Prtl + "\", I'll assume that your game already has the 1.4 Update installed.");
			return;
		}
		
		
		List<Path> chckH1 = null;
		try {
			chckH1 = Files.walk(Paths.get(H1Prtl.toURI()), 1).filter(F -> Files.isRegularFile(F) && F.getFileName().toString().equals(readData.exeFile[9]) || F.getFileName().toString().equals(readData.exeFile[10])).map(M -> M.getFileName()).collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
		if (chckH1.isEmpty()) {
			System.out.println("[WARN] No .exe files have been detected in \"" + H1Prtl + "\", I'll assume it's empty and that your game already has the 1.4 Update installed.");
			return;
		}
		
		
		List<String> getH1Prtl = null;
		try {
			getH1Prtl = Files.walk(Paths.get(H1Prtl.toURI())).filter(F -> Files.isRegularFile(F)).map(M -> M.toString().replaceAll("^(.+[\\/\\\\]"+H1Prtl.getName()+"[\\/\\\\])", "").replace("\\", "\\\\")).collect(Collectors.toList());
		} catch (IOException e) {
			System.err.println("[ERROR] " + e);
			return;
		}
		
		
		File ExclFileList = new File(ExclFile);
		try {
			BufferedWriter FileWriter = new BufferedWriter(new FileWriter(ExclFileList, true));
			for (int i = 0; i < getH1Prtl.toArray().length; i++) {
				FileWriter.newLine();
				FileWriter.write(getH1Prtl.toArray()[i]+"");
			}
			FileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
			return;
		}
		
		
		Path src = Paths.get(H1Prtl.toURI());
		Path destn = Paths.get("");
		try {
			Files.walk(src)
				 .filter(F -> !F.equals(src))
				 //.forEach(F -> System.out.println(F + " -> " + destn.resolve(src.relativize(F))));
				 .forEach(F -> {
					try {
						Files.copy(F, destn.resolve(src.relativize(F)), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.err.println("[ERROR] " + e);
					}
				});
			System.out.println("[INFO] The files from \"" + H1Prtl + "\" has been added to ExclFileList.txt and copied to the current directory.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
			System.out.println("[WARN] The files from \"" + H1Prtl + "\" has been added to ExclFileList.txt but they couldn't be copied to the current directory, we will continue with the plan but you may like to copy and paste them manually.");
			return;
		}
		
		
		
		
	}
	
	
	
	
	/* ****************************************************************
	 * 
	 * Please PC, make everything work so I don't have to think anymore.
	 * 
	 * 									:v
	 * 
	 * ****************************************************************/
	private void procRun() {
		File lnFile = new File(lnexe);
		while (!lnFile.exists()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println("[ERROR] " + e);
			}
		}
		
		
		// After the first start, the .exe is deleted...so you have to copy it.
		if (cur.equals("IW5") || cur.equals("T6")) {
			Path src = Paths.get(GamePath);
			Path destn = Paths.get(TargetPath);
			try (Stream<Path> copyFile = Files.walk(Paths.get(src.toUri()), 1).filter(F -> Files.isRegularFile(F) && F.getFileName().toString().equals(readData.exeFile[0]) || F.getFileName().toString().equals(readData.exeFile[2]) || F.getFileName().toString().equals(readData.exeFile[3]) || F.getFileName().toString().equals(readData.exeFile[4]))) {
				copyFile.forEach(F -> {
					try {
						Files.copy(F, destn.resolve(src.relativize(F)), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.err.println("[ERROR] " + e);
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("[ERROR] " + e);
			}
		}
		
		
		try {
			/*
			 * ln.exe doesn't like quotes between commands.
			 * - Good ending : ln.exe -x ie.txt -x moreie.txt -X ie1 -X ie2\\ie3 -s -o "GamePath" "TargetPath"
			 * - Bad ending  : ln.exe "-x ie.txt -x moreie.txt" "-X ie1 -X ie2\\ie3" "-s -o" "GamePath" "TargetPath" 
			 * 
			 * ----------------------------------------------------------------
			 * 
			 * - CMD : PowerShell.exe -Command "Start-Process -FilePath '.\Ayria\PNL\ln.exe' -ArgumentList '-x @Ayria/PNL/ExclFileList.txt -X @Ayria/PNL/ExclFlrList.txt --output Ayria/PNL/Logs/ln.exe.log -s -o ""GamePath"""" ""TargetPath""""' -WorkingDirectory 'TargetPath' -verb RunAs"
			 *         * It works perfectly from CMD but in PS it doesn't put the "quotes" ("GamePath" / "TargetPath")
			 * - PS  : Start-Process -FilePath ".\Ayria\PNL\ln.exe" -ArgumentList "-x @Ayria/PNL/ExclFileList.txt -X @Ayria/PNL/ExclFlrList.txt --output Ayria/PNL/Logs/ln.exe.log -s -o `GamePath`" `"TargetPath`"" -WorkingDirectory "TargetPath" -verb RunAs
			 *         * It works perfectly from PS but in CMD it doesn't support -o because it's ambiguous
			 * 
			 * ---------------------------------------------------------------- 
			 */
			Process proc = Runtime.getRuntime().exec("PowerShell.exe -NonInteractive -InputFormat None -OutputFormat None -Command \"Start-Process -FilePath '.\\"+lnexe+"' -ArgumentList '-x @"+ExclFile+" -X @"+ExclFlr+" "+lnArgs+" \"\""+GamePath+"\"\"\"\" \"\""+TargetPath+"\"\"\"\"' -WorkingDirectory '"+TargetPath+"' -verb RunAs\"", null, new File(TargetPath));
			proc.waitFor();
			System.out.println("\n-------------------- ln.exe --------------------\n");
			System.out.println("  " + lnArgs.replaceAll("(?:--output | -s -o)", "") + " has been generated");
			System.out.println("\n------------------------------------------------\n");
			Thread.sleep(500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.err.println("[ERROR] " + e);
		}
	}
	
	
	
	
}
