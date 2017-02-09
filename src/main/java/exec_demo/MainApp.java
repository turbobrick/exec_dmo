package exec_demo;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

public class MainApp {

	public static void main(String[] args) {
		initStream();
	}

	public static void initStream() {
            try {

                    PipedOutputStream output = new PipedOutputStream();
                    PipedInputStream input = new PipedInputStream();
                    output.connect(input);

                    runFfmpeg(input);
                    runSecond(output);

            } catch (IOException e) {
                    e.printStackTrace();
            }
	}

        private static void runFfmpeg(PipedInputStream input) throws IOException {
            DefaultExecutor executor = new DefaultExecutor();

            CommandLine commandLine;
            String feedMe = "java -cp target/exec_dmo-0.0.1-SNAPSHOT-jar-with-dependencies.jar exec_demo.SortOfFfmpeg";
            commandLine = CommandLine.parse(feedMe);

            executor.setStreamHandler(new PumpStreamHandler(null, null, input));
            executor.execute(commandLine, new DefaultExecuteResultHandler());
        }

        private static void runSecond(PipedOutputStream output) throws IOException {
            DefaultExecutor executor = new DefaultExecutor();

            CommandLine commandLine;
            String executeMe = "java -cp target/exec_dmo-0.0.1-SNAPSHOT-jar-with-dependencies.jar exec_demo.SecondApp pom.xml";
            commandLine = CommandLine.parse(executeMe);


            executor.setStreamHandler(new PumpStreamHandler(output, null));
            executor.execute(commandLine, new DefaultExecuteResultHandler());
        }
}
