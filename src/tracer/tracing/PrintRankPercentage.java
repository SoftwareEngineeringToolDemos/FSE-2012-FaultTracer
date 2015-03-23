package tracer.tracing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PrintRankPercentage {
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				"G:\\data.txt"));
		String line = reader.readLine();
		double[] tar = new double[24];
		double[] sbi = new double[24];
		double[] jac = new double[24];
		double[] och = new double[24];
		double[] heu = new double[24];
		double[] all = new double[24];
		int i = 0;
		while (line != null) {
			String[] items = line.split("\t");
			tar[i] = Double.parseDouble(items[0]);
			sbi[i] = Double.parseDouble(items[1]);
			jac[i] = Double.parseDouble(items[2]);
			och[i] = Double.parseDouble(items[3]);
			heu[i] = Double.parseDouble(items[4]);
			all[i] = Double.parseDouble(items[5]);

			System.out.println(PrintDouble(tar[i] / all[i]) + "\t"
					+ PrintDouble(sbi[i] / all[i]) + "\t"
					+ PrintDouble(jac[i] / all[i]) + "\t"
					+ PrintDouble(och[i] / all[i]) + "\t"
					+ PrintDouble(heu[i] / all[i]));
			line = reader.readLine();
		}

	}

	public static String PrintDouble(double d) {

		d = d * 100;
		String s = d + "000";
		int i = s.indexOf(".");
		return s.substring(0, i + 3);
	}

}
