package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CIA {
	static List<String> clist = new ArrayList<String>();
	static List<String> flist = new ArrayList<String>();
	static BufferedWriter faultTracer;
	static BufferedWriter chianti;

	public static void main(String[] args) throws IOException {
		chianti = new BufferedWriter(new FileWriter(
				"D:\\chianti_AffectingChangeNumber"));
		faultTracer = new BufferedWriter(new FileWriter(
				"D:\\faultTracer_AffectingChangeNumber"));
		String[] subs = { "jtopas", "xmlsec", "jmeter", "ant" };
		int[] vs = { 4, 4, 6, 9 };
		for (int i = 0; i < 4; i++) {
			trace(subs[i], vs[i]);
		}
		faultTracer.flush();
		faultTracer.close();
		chianti.flush();
		chianti.close();
	}

	public static void trace(String s, int v) throws IOException {
		String subject = "M:\\HP_D\\FSE_withLCF\\" + s + "\\";
		int vers = v;
		for (int i = 0; i < vers - 1; i++) {

			clist.clear();
			flist.clear();

			String oldCov = subject + "log" + i + i;
			String diff = subject + s + i + ".0" + "-" + s + (i + 1)
					+ ".0.adiff";
			String chiantiDiff = subject + "Chianti_" + s + i + ".0" + "-" + s
					+ (i + 1) + ".0.adiff";
			List<String> ft = FaultTracer.selection(oldCov, diff);
			List<String> ch = Chianti.selection(oldCov, chiantiDiff);

			String newCov = subject + "log" + (i + 1) + (i);

			// List<String> passbefore = findPassTest(oldCov);
			// List<String> passafter = findPassTest(newCov);
			//
			// List<String> changed = passbefore;
			// for (String s : passafter) {
			// changed.remove(s);
			// }
			// //System.out.println(changed.size());
			//
			// int unselc = 0;
			// int unself = 0;
			// System.out.println("chianti:");
			// for (String s : changed) {
			// if (!ch.contains(s))
			// System.out.println(s);
			// }
			// System.out.println("faulttracer:");
			// for (String s : changed) {
			// if (!ft.contains(s))
			// System.out.println(s);
			// }

			double ftNum = FaultTracer.localization3(newCov, diff, ft,
					faultTracer);
			double chNum = Chianti.localization2(newCov, chiantiDiff, ch,
					chianti);
			// System.out.print(ft.size()+"\t");
			// System.out.print( compare(ch, ft)+"\t");
			// System.out.print("0\t 0\t ");
			System.out.print(chNum + "\t" + ftNum + "\t");
			System.out.println();
			/*
			 * System.out.println("------v" + i + " vs v" + (i + 1) +
			 * "-------");
			 * 
			 * List<String> clist = new ArrayList<String>(ch); List<String>
			 * res=new ArrayList<String>(); for (String t : clist) { if
			 * (!ft.contains(t)) res.add(t); } System.out.println(res.size());
			 * Chianti.selection2(oldCov, chiantiDiff, res);
			 */

			// System.out.println("------v"+i+" vs v"+(i+1)+"-------");
			// System.out.println("chianti:");
			// for(String s:clist){
			// System.out.println(s);
			// }
			// System.out.println("faulttracer:");
			// for(String s:flist){
			// System.out.println(s);
			// }
		
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
