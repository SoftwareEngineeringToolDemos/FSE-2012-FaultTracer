package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChiantiAffectingChange {
	static List<String> clist = new ArrayList<String>();
	static List<String> flist = new ArrayList<String>();
	static int failednum = 0;
	static int passnum = 0;
	static HashMap<String, HashSet<String>> changeMap;
	static List<String> changed;

	public static void main(String[] args) throws IOException {
		String[] subs = { "jtopas", "xmlsec", "jmeter", "ant" };
		int[] vs = { 4, 4, 6, 9 };

		for (int i = 0; i < subs.length; i++) {
			trace(subs[i], vs[i]);
		}
	}

	public static void trace(String s, int v) throws IOException {
		String subject = "P:\\HP_D\\FSE_withLCF\\" + s + "\\";

		int vers = v;
		for (int i = 0; i < vers - 1; i++) {
			System.out.println(s+i+".0");
			clist.clear();
			flist.clear();

			String oldCov = subject + "log" + i + i;
			String newCov = subject + "log" + (i + 1) + (i);
			String chiantiDiff = subject + "Chianti_" + s + i + ".0" + "-" + s
					+ (i + 1) + ".0.adiff";
			
			String affchgPath = subject + "Chianti_AffectingChanges" + i
					+ (i + 1);

			// List<String> ch = Chianti.selection(oldCov, chiantiDiff);
			List<String> ft = Chianti.selection(oldCov, chiantiDiff);

			HashMap<String, HashSet<String>> test_affchgs = initialization(
					oldCov, newCov, chiantiDiff, ft);
			Chianti.writeAffectingChanges(test_affchgs, affchgPath);

			// System.out.println(subject+i+(i+1)+ " complete");

		}

	}

	public static HashMap<String, HashSet<String>> initialization(
			String oldCov, String newCov, String diff, List<String> list)
			throws IOException {
		List<String> passbefore = ChiantiAffectingChange.findPassTest(oldCov);
		List<String> passafter = ChiantiAffectingChange.findPassTest(newCov);

		List<String> changed = new ArrayList<String>(passbefore);
		for (String str : passafter) {
			changed.remove(str);
		}

		ChiantiAffectingChange.changed = changed;
//		if (changed.size() == 0)
//			return null;

		// System.out.println(": " + changed.size());

		HashMap<String, HashSet<String>> tests = new HashMap<String, HashSet<String>>();
		BufferedReader reader = new BufferedReader(new FileReader(newCov));
		HashMap<String, HashSet<String>> changeMap = FaultTracer
				.getTypedAtomicChanges2(diff);
		ChiantiAffectingChange.changeMap = changeMap;
		String line = reader.readLine();

		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = FaultTracer.dropTag(line);
				if (list.contains(testName)) {
					HashSet<String> con = new HashSet<String>();
					line = reader.readLine();
					while (line != null && !line.startsWith("<TEST>")) {
						con.add(line);
						line = reader.readLine();
					}
					tests.put(testName, con);
				} else
					line = reader.readLine();
			} else
				line = reader.readLine();
		}
		reader.close();

		HashMap<String, HashSet<String>> test_affchgs = Chianti
				.determineAffectingChanges(tests, diff);
		int failednum = 0;
		int passnum = 0;
		for (String s : test_affchgs.keySet()) {
			if (changed.contains(s))
				failednum++;
		}
		for (String s : passafter) {
			if (list.contains(s))
				passnum++;
		}
		ChiantiAffectingChange.passnum = passnum;
		ChiantiAffectingChange.failednum = failednum;
//		if (failednum == 0)
//			return null;
		return test_affchgs;
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
