package tracer.tracing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ChangeStatistic {
	static int lc = 0;
	static int lcf=0;
	static int cm = 0;
	static int am = 0;
	static int dm = 0;
	static int cfi = 0;
	static int csfi = 0;
	static int af = 0;
	static int df = 0;
	static int sum = 0;
	public static void main(String[] args) throws IOException {

		String[] progs = { "jtopas", "xmlsec","jmeter", "ant" };
		String subject = "G:\\FSE_withLCF\\";
		int[] num = { 4, 4, 6,9 };
		
//		String[] progs = { "jmeter" };
//		String subject = "G:\\ECOOP_withoutDF\\";
//		int[] num = { 6};
		double count=19;
		for (int i = 0; i < num.length; i++) {
			for (int j = 0; j < num[i] - 1; j++) {
				collect(subject + progs[i] + "\\" + progs[i] + j+".0-" +progs[i] + (j + 1)+".0.adiff");
			}
		}
		System.out.println(sum/count + "&" + am/count + "&" + dm/count + "&" + cm/count + "&" + af/count
				+ "&" + df/count + "&" + cfi/count + "&" + csfi/count + "&" + lc/count+ "&" + lcf/count+"\\\\");
		System.out.println("\\hline");
	}

	public static void collect(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		int lc = 0;
		int lcf=0;
		int cm = 0;
		int am = 0;
		int dm = 0;
		int cfi = 0;
		int csfi = 0;
		int af = 0;
		int df = 0;
		int sum = 0;
		while (line != null) {
			sum++;
			if (line.startsWith("LC:"))
				lc++;
			else if (line.startsWith("LCF:"))
				lcf++;
			else if (line.startsWith("CM:"))
				cm++;
			else if (line.startsWith("AM:"))
				am++;
			else if (line.startsWith("DM:"))
				dm++;
			else if (line.startsWith("CFI:"))
				cfi++;
			else if (line.startsWith("CSFI:"))
				csfi++;
			else if (line.startsWith("AF:"))
				af++;
			else if (line.startsWith("DF:"))
				df++;
			line = reader.readLine();
		}
		ChangeStatistic.sum+=sum;
		ChangeStatistic.lc+=lc;
		ChangeStatistic.lcf+=lcf;
		ChangeStatistic.cm+=cm;
		ChangeStatistic.am+=am;
		ChangeStatistic.dm+=dm;
		ChangeStatistic.cfi+=cfi;
		ChangeStatistic.csfi+=csfi;
		ChangeStatistic.af+=af;
		ChangeStatistic.df+=df;
		
//		System.out.println(sum + "&" + am + "&" + dm + "&" + cm + "&" + af
//				+ "&" + df + "&" + cfi + "&" + csfi + "&" + lc+ "&" + lcf+"\\\\");
//		System.out.println("\\hline");

		System.out.println(sum+"\t"+am + "\t" + dm + "\t" + cm + "\t" + af
				+ "\t" + df + "\t" + cfi + "\t" + csfi + "\t" + lc+"\t"+lcf);

	}
}
