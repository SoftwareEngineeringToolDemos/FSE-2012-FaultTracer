package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordAffectedTests {
	static List<String> clist = new ArrayList<String>();
	static List<String> flist = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		String[] subs = { "jtopas", "xmlsec", "jmeter" ,"ant"};
		int[] vs = { 4, 4, 6, 9 };
		for (int i = 0; i < 4; i++) {
			trace(subs[i], vs[i]);
		}
	}

	public static void trace(String s, int v) throws IOException {
		String subject = "G:\\FSE_withLCF\\" + s + "\\";
		System.out.println("subject:"+subject);
		
		int vers = v;
		for (int i = 0; i < vers - 1; i++) {
			BufferedWriter writer=new BufferedWriter(new FileWriter(subject+"affectedTests"+i+(i+1)));
			clist.clear();
			flist.clear();

			String oldCov = subject + "log" + i + i;
			String diff = subject + s + i + ".0" + "-" + s + (i + 1)
					+ ".0.adiff";
			List<String> ft = FaultTracer.selection(oldCov, diff);

			String newCov = subject + "log" + (i + 1) + (i);

			double ftNum = FaultTracer.localization2(newCov, diff, ft, writer);
			writer.flush();
			writer.close();
			
		}
	}

	public static String compare(List<String> chianti, List<String> faultTracer) {
		List<String> c = new ArrayList<String>();
		List<String> f = new ArrayList<String>();
		for (String s : chianti) {
			c.add(s);
		}
		for (String s : faultTracer) {
			f.add(s);
		}
		for (String s : faultTracer) {
			if (c.contains(s))
				c.remove(s);
		}
		for (String s : chianti) {
			if (f.contains(s))
				f.remove(s);
		}

		clist = c;
		flist = f;
		return c.size() + "\t" + f.size();

	}

	public static List<String> findPassTest(String cov) throws IOException {
		List<String> res = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(cov));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {

				String test = line.substring(line.indexOf(">") + 1);

				line = reader.readLine();
				while (line != null && !line.startsWith("<TEST>")) {
					if (line.startsWith("<TESTPASS>")) {
						// if (test.substring(test.lastIndexOf(".") + 1)
						// .startsWith("test"))
						res.add(test);
						break;
					}
					line = reader.readLine();
				}
			} else
				line = reader.readLine();
		}

		return res;
	}
}
