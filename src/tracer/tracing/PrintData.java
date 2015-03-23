package tracer.tracing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PrintData {

	public static void main(String[] args) throws IOException {
		String[] pairs = new String[] { "<$j_0$, $j_1$>", "<$j_1$, $j_2$>",
				"<$j_2$, $j_3$>", "<$x_0$, $x_1$>", "<$x_1$, $x_2$>",
				"<$x_2$, $x_3$>", "<$m_0$, $m_1$>", "<$m_1$, $m_2$>",
				"<$m_2$, $m_3$>", "<$m_3$, $m_4$>", "<$m_4$, $m_5$>",
				"<$a_0$, $a_1$>", "<$a_1$, $a_2$>", "<$a_2$, $a_3$>",
				"<$a_3$, $a_4$>", "<$a_4$, $a_5$>", "<$a_5$, $a_6$>",
				"<$a_6$, $a_7$>", "<$a_7$, $a_8$>", "Average" };
		BufferedReader reader = new BufferedReader(new FileReader(
				"D:\\data.txt"));
		String line = reader.readLine();
		double chPercSum = 0.0;
		double ftPercSum = 0.0;
		double chSum = 0.0;
		double ftSum = 0.0;
		double allSum = 0.0;
		double diff = 0.0;
		int count = 18;
		double av = 0.0;
		int i = 0;
		while (line != null) {
			String[] datas = line.split("\t");
			//double allchanges = Double.parseDouble(datas[2]);
			double ch = Double.parseDouble(datas[1]);
			double ft = Double.parseDouble(datas[0]);
			if (i != 0) {
				//chPercSum += ch / allchanges;
				//ftPercSum += ft / allchanges;
				chSum += ch;
				ftSum += ft;
				//allSum += allchanges;
				diff += (ch - ft);
				av += (1 - ft / ch);
			}
			i++;
			System.out.println("&" + PrintDouble2(ch) + "&" + PrintDouble2(ft));
//			System.out.println("&" + PrintDouble2(ch) + "&" + PrintDouble2(ft)
//					+ "&" + PrintDouble(ch / allchanges) + "&"
//					+ PrintDouble(ft / allchanges) + "&"
//					+ PrintDouble2(ch - ft) + "&" + PrintDouble(1 - ft / ch)
//					+ "\\\\");
			// System.out.println("\\hline");
			line = reader.readLine();
		}
		// System.out.println("\\hline");
		System.out.println("&" + PrintDouble2(chSum / count) + " &"
				+ PrintDouble2(ftSum / count) +"&"+ PrintDouble(chPercSum / count)
				+ "&" + PrintDouble(ftPercSum / count) + "&"
				+ PrintDouble2(diff / count) + "&"
				+ PrintDouble(1 - ftSum / chSum) + "\\\\");
		System.out.println("\\hline");
		System.out.println(PrintDouble(1 - ftSum / chSum));
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
