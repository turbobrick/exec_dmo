package exec_demo;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat.Encoding;

public class SecondApp {
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Mixer mixer = AudioSystem.getMixer(AudioSystem.getMixerInfo()[Integer.parseInt(args[0])]);
		AudioFormat af = new AudioFormat(Encoding.PCM_SIGNED, 48000f, 16, 2, 4, 48000f, false);
		
		TargetDataLine line;
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
		if (!AudioSystem.isLineSupported(info))
			System.out.println("Line is not supported.");

		try {
			line = (TargetDataLine) mixer.getLine(info);
			line.open(af);

			int bytesRead, CHUNK_SIZE = 4096;
			byte[] data = new byte[line.getBufferSize() / 5];

			line.start();

			while (true) {
				bytesRead = line.read(data, 0, CHUNK_SIZE);
				System.out.write(data, 0, bytesRead);
				System.out.flush();
			}

		} catch (LineUnavailableException ex) {
			System.out.println("Line is unavailable.");
			ex.printStackTrace();
		}
	}
}
