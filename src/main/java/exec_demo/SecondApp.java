package exec_demo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class SecondApp {
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
                File in = new File(args[0]);
		try (InputStream input = new FileInputStream(in)) {
			int bytesRead, CHUNK_SIZE = 4096;
			byte[] data = new byte[CHUNK_SIZE];

			while (true) {
				bytesRead = input.read(data, 0, CHUNK_SIZE);
                                if (bytesRead > 0) {
                                    System.out.write(data, 0, bytesRead);
                                    System.out.flush();
                                } else if (bytesRead == -1) {
                                    System.exit(0);
                                }
			}

		}
	}
}
