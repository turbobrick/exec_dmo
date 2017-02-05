package exec_demo;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

public class MainApp {

	public static void main(String[] args) {
		initStream("ffmpeg/bin");
	}
	
	public static void initStream(String path) {
		File file = new File(path);
		for (File s : file.listFiles()) {
			if (s.getName().equals("ffmpeg.exe")) {
				try {
					DefaultExecutor executor = new DefaultExecutor();
					executor.setWorkingDirectory(file);
					
					CommandLine commandLine = new CommandLine(s.getParentFile().getAbsolutePath());
					System.out.println("Path: " + commandLine.toString());
					
					displayMixers();
					Scanner reader = new Scanner(System.in);
					
					System.out.print("\n Select a Mixer: ");
					int n = reader.nextInt();
					reader.close();
					
					String executeMe = "java -jar app2.jar " + n; // Execute .jar to start sending sound bytes to stdout.
					commandLine = CommandLine.parse(executeMe);
					
					PipedOutputStream output = new PipedOutputStream(); // Fill this with bytes.
					PipedInputStream input = new PipedInputStream();
					output.connect(input); // Send audio bytes from output to this input stream.
					
					executor.setStreamHandler(new PumpStreamHandler(output, null)); // Stream all the bytes to the output.
					executor.execute(commandLine, new DefaultExecuteResultHandler()); // Execute APP2 (The one which captures the live audio bytes).

					// Icecast URL is set to the default parameters.
					String feedMe = "ffmpeg -f s16le -ar 48000 -ac 2 -i - -f ogg -content_type application/ogg icecast://source:hackme@localhost:8000/stream";
					commandLine = CommandLine.parse(feedMe);

					executor.setStreamHandler(new PumpStreamHandler(null, null, input)); // Send the bytes being received in real time to FFMPEG.
					executor.execute(commandLine, new DefaultExecuteResultHandler()); // Execute FFMPEG.
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Quick method for checking your System's mixers so you can choose one for testing.
	 */
	public static void displayMixers() {
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();

		int mixnum = 0;

		for (Mixer.Info m : mixers) {
			Mixer mix = AudioSystem.getMixer(m);
			if (mix.getTargetLineInfo().length != 0) {
				System.out.println("ID [" + mixnum + "] - " + m.getName());
				Line.Info[] lines = mix.getTargetLineInfo();
				if (mix.getTargetLineInfo().length > 0) {
					for (Line.Info lineInfo : lines) {
						Line line;
						
						try {
							line = AudioSystem.getLine(lineInfo);
							if (line instanceof TargetDataLine) {
								System.out.println("\t" + line);
							}
						} catch (LineUnavailableException e) {
							System.out.println("Line is unavailable.");
							e.printStackTrace();
						}
					}
				}
			}
			mixnum++;
		}
	}
}
