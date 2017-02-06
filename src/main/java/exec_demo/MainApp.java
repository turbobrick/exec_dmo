package exec_demo;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

public class MainApp {

	public static void main(String[] args) {
		initStream(".", "app2.jar");
	}

	public static void initStream(String path, String fileName) {
		File file = new File(path);
		for (File s : file.listFiles()) {
			if (s.getName().equals(fileName)) {
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

					String executeMe = "java -jar app2.jar " + n;
					commandLine = CommandLine.parse(executeMe);

					PipedOutputStream output = new PipedOutputStream(); 
					PipedInputStream input = new PipedInputStream();
					output.connect(input);

					executor.setStreamHandler(new PumpStreamHandler(output, null));
					executor.execute(commandLine, new DefaultExecuteResultHandler()); 

					String feedMe = "java -jar app3.jar";
					commandLine = CommandLine.parse(feedMe);

					executor.setStreamHandler(new PumpStreamHandler(null, null, input));
					executor.execute(commandLine, new DefaultExecuteResultHandler());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Quick method for checking your System's mixers so you can choose one for
	 * testing.
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
						showLineInfo(lineInfo);
					}
				}
			}
			mixnum++;
		}
	}

	private static void showLineInfo(final Line.Info lineInfo) {
		System.out.println("  " + lineInfo.toString());
		if (lineInfo instanceof DataLine.Info) {
			DataLine.Info dataLineInfo = (DataLine.Info) lineInfo;

			AudioFormat[] formats = dataLineInfo.getFormats();
			for (final AudioFormat format : formats)
				System.out.println("    " + format.toString());
		}
	}
}
