package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class LogReduction {
	public static void main(String[] args) throws IOException {

		String subject = "G:\\ECOOP_old\\xmlsec\\";
		// String log = "FaultTracer_log";
		// reduce(subject, log);
		int ver = 4;
		for (int i = 0; i < ver - 1; i++) {
			reduce(subject, "log" + i + i);
			reduce(subject, "log" + (i + 1) + i);
		}

	}

	public static void reduce(String subject, String name) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(subject
				+ name));
		BufferedWriter writer = new BufferedWriter(new FileWriter(subject
				+ "Reduce_" + name));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				writer.write(line);
				writer.newLine();
				line = reader.readLine();
				HashSet<String> set = new HashSet<String>();
				while (line != null && !line.startsWith("<TEST>")) {
					if (!line.contains("-")) {
						set.add(line);
						if (line.startsWith("<TESTPASS>")) {
							break;
						}
					} else if (line.startsWith("<FW>")
							|| line.startsWith("<FR>"))
						set.add(line);
					line = reader.readLine();
				}
				for (String toWrite : set) {
					writer.write(toWrite);
					writer.newLine();
				}

			} else
				line = reader.readLine();
		}
		reader.close();
		writer.flush();
		writer.close();

	}

}
