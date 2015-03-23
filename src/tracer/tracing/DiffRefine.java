package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DiffRefine {

	public static void main(String[] args) throws IOException {
		String path = "G:\\ECOOP\\jmeter\\";
		for (int i = 0; i < 5; i++) {
			BufferedReader reader = new BufferedReader(new FileReader(path
					+ "Diff" + i + (i + 1)));
			BufferedWriter writer = new BufferedWriter(new FileWriter(path
					+ "ref_Diff" + i + (i + 1)));
			String line=reader.readLine();
			while(line!=null){
				String[] items=line.split("=>");
				if(!items[0].contains("$Test")){
					writer.write(line+"\n");
				}
				line=reader.readLine();
			}
			reader.close();
			writer.flush();
			writer.close();
		}
	}
}
