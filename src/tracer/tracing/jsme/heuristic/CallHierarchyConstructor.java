package tracer.tracing.jsme.heuristic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CallHierarchyConstructor {

	public static void main(String[] args) throws IOException {
		String path = "D:\\eclipse-SDK-3.6.2-win32-x86_64\\runtime-New_configuration(1)\\";
		String[] subjects = {  "Jtopas3.0" };
		for (String subject : subjects) {
			getCallMap(path + subject + "\\FaultTracer_log", path + subject
					+ "\\CallGraphNew");
			//break;
		}
//		getCallMap(path + subject + "\\FaultTracer_log", path + subject
//				+ "\\CallGraphNew");

	}

	public static void getCallMap(String in, String out) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(in));
		BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String tst = line.replace("<TEST>", "");
				HashSet<String> call_rel = new HashSet<String>();
				line = reader.readLine();
				while (line != null && !line.startsWith("<TEST>")) {
					String caller;
					String callee;
					if (line.contains("-")) {
						caller = line.substring(0, line.indexOf("-"));
						String calleeName = line.substring(line
								.lastIndexOf(".") + 1);
						line = reader.readLine();
						if (line != null && !line.startsWith("<TEST>")) {
							if (line.contains("-"))
								continue;
							else {
								String curName = line.substring(line
										.lastIndexOf(".") + 1);
								if (curName.equals(calleeName)) {
									if (line.startsWith("<"))
										callee = line.substring(line
												.indexOf(">") + 1);
									else
										callee = line;

									call_rel.add(caller + "-" + callee);
								}
								line = reader.readLine();
							}
						}
					} else
						line = reader.readLine();
				}
				writer.write("<TEST>" + tst + "\n");
				for (String rel : call_rel) {
					writer.write(rel + "\n");
				}
			} else
				line = reader.readLine();
		}
		reader.close();
		writer.flush();
		writer.close();

	}

}
