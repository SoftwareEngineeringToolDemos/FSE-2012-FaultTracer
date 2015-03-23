package tracer.tracing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PrintData4FaultLocalization {

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				"G:\\FaultLocalization.txt"));
		String line = reader.readLine();
		double tarSum = 0.0;
		double sbiSum = 0.0;
		double jacSum = 0.0;
		double ochSum=0.0;
		double aff=0.0;
		int count = 24;
		int i = 0;
		while (line != null) {
			String[] datas = line.split("&");
			aff+=Double.parseDouble(datas[3]);
			tarSum+= Double.parseDouble(datas[5]);
			sbiSum+= Double.parseDouble(datas[7]);
			jacSum+= Double.parseDouble(datas[9]);
			ochSum+= Double.parseDouble(datas[11].replace("\\", ""));

			line = reader.readLine();
		}
System.out.println(PrintDouble2(aff/24)+"\t"+PrintDouble2(tarSum/24)+"\t"+PrintDouble2(sbiSum/24)+"\t"+PrintDouble2(jacSum/24)+"\t"+PrintDouble2(ochSum/24)+"\t");
		

	}

	public static String PrintDouble(double d) {

		d = d * 100;
		String s = d + "000";
		int i = s.indexOf(".");
		return s.substring(0, i + 3);
	}

	public static String PrintDouble2(double d) {

		String s = d + "000";
		int i = s.indexOf(".");
		return s.substring(0, i + 3);

	}
}
