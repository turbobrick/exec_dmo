package exec_demo;

import java.io.FileOutputStream;
import java.io.IOException;

public class SortOfFfmpeg {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream("test_byte_file");
		while (System.in.read() != -1)
			fos.write(System.in.read());
		fos.close();
	}
}