package pkg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GetTimerData {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(
						"K:\\javalanche\\javalanche-0.3.6\\examples\\FaultTracer-JSME-Time"));
		String line = reader.readLine();
		int count = 0;
		while (line != null) {
			if (line.contains("Time elapsed:")) {
				String time = line.substring(line.indexOf("Time elapsed:")).replace("Time elapsed: ", "").replace(
						" sec", "");
				if (!time.equals("0")) {
					count++;
					System.out.print(Double.parseDouble(time) * 1000 + "\t");
					if(count%10==0)
						System.out.println();
				}
			}
			line = reader.readLine();
		}

	}
}
