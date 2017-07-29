package org.xtext.labs.ui.extend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

//TODO only source code. not yet associate with eclipse. maybe for next. log watcher.
public class MydslLogWatcher {

	static private String strRtn;
	static private Process ps;
	static private BufferedReader br;
	static private BufferedWriter bw;
	static private String logPath;

	public void init(String logPath) throws InterruptedException{
		
		MydslLogWatcher.logPath = logPath;
		
		//Eclipse will be locked when you do not run FileWatcher in Another Thread. kim
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					FlagFileWatcher();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		});
		t1.start();
	}

	private void FlagFileWatcher() throws IOException, InterruptedException {

		Path p = Paths.get(logPath);
		String file = p.getFileName().toString();
		String directoryPath = logPath.replace(file, "");
		String logFilename = file.replace(".dsl", ".log");
		
		Path path = Paths.get(directoryPath);
		try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
			final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, 
					StandardWatchEventKinds.ENTRY_CREATE);
			
			while (true) {
				final WatchKey wk = watchService.take();
				Thread.sleep(1500);
				
				for (WatchEvent<?> event : wk.pollEvents()) {
					final Path changed = (Path) event.context();
					
					if (changed.endsWith(logFilename)) {
		                BufferedReader br = new BufferedReader(new InputStreamReader(
		                	    new FileInputStream(directoryPath + logFilename), "SJIS"));

					    StringBuffer lines = new StringBuffer();
					    lines.setLength(0);
					    lines.append("");

					    while (br.readLine() != null) {
					    	String line = br.readLine();
					        lines.append(line + "\n");
					    }
						
					    //MydslPrintConsole.PrintConsoleLog(lines.toString());
		            }
				}
				// reset the key
				boolean valid = wk.reset();
				if (!valid) {
					System.out.println("Key has been unregisterede");
				}
			}
		}
	}
}
