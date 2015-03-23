package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class UILogRefine {
	public static void main(String[] args) throws IOException {

		String subject = "F:\\eclipse\\runtime-EclipseApplication\\Xmlsec0.0\\";
		String log = "FaultTracer_log";
		refine(subject, subject, log);
		// int ver = 9;
		// for (int i = 0; i < ver - 1; i++) {
		// reduce(subject, "log" + i + i);
		// reduce(subject, "log" + (i + 1) + i);
		// }

	}

	// public static void main(String[] args) throws IOException {
	//
	// String subject = "G:\\ECOOP_old\\xmlsec\\";
	// String subject2 = "G:\\ECOOP\\xmlsec\\";
	// int ver = 4;
	// for (int i = 0; i < ver - 1; i++) {
	// refine(subject, subject2, "log" + i + i);
	// refine(subject, subject2, "log" + (i+1) + i);
	// }
	// }

	public static void refine(String subject, String subject2, String log)
			throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(subject + log));
		BufferedWriter writer = new BufferedWriter(new FileWriter(subject2
				+ "Red_" + log));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String test = line;
				line = reader.readLine();
				HashSet<String> set = new HashSet<String>();
				boolean pass = false;
				while (line != null && !line.startsWith("<TEST>")) {

					set.add(line);
					if (line.startsWith("<TESTPASS>")) {
						pass = true;
						break;
					}
					line = reader.readLine();
				}
				if (test.substring(test.lastIndexOf(".") + 1)
						.startsWith("test")) {
					writer.write(test);
					writer.newLine();
					for (String toWrite : set) {
						writer.write(toWrite);
						writer.newLine();
					}
				}
			} else
				line = reader.readLine();
		}
		reader.close();
		writer.flush();
		writer.close();

	}
}
