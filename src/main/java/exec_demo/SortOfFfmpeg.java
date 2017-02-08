package exec_demo;

import java.io.FileOutputStream;
import java.io.IOException;

public class SortOfFfmpeg {

	public static void main(String[] args) throws ClassNotFoundException, IOException, InterruptedException {
		FileOutputStream fos = new FileOutputStream("test_byte_file");
                fos.flush();
                int b;
		while ((b = System.in.read()) != -1)
			fos.write(b);
		fos.close();
	}
}