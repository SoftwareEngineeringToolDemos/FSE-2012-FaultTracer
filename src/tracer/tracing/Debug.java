package tracer.tracing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Debug {

	public static void debugAll(String loc, String tar, String sbi, String jac,
			String och, String heu) throws IOException {
		HashMap<String, List<String>> locinfo = getInfo(loc);
		HashMap<String, List<String>> tarinfo = getInfo(tar);
		HashMap<String, List<String>> sbiinfo = getInfo(sbi);
		HashMap<String, List<String>> jacinfo = getInfo(jac);
		HashMap<String, List<String>> ochinfo = getInfo(och);

		for (String test : locinfo.keySet()) {
			List<String> locs = locinfo.get(test);
			List<String> tarres = tarinfo.get(test);
			List<String> sbires = sbiinfo.get(test);
			List<String> jacres = jacinfo.get(test);
			List<String> ochres = ochinfo.get(test);

			for (String c : locs) {

				int tar_loc = 1;
				int jac_loc = 1;
				int och_loc = 1;
				int sbi_loc = 1;
				String tar_s = "";
				String sbi_s = "";
				String jac_s = "";
				String och_s = "";

				for (String t : tarres) {
					if (t.startsWith(c)) {
						tar_s = t;
						break;

					} else
						tar_loc++;
				}
				for (String s : sbires) {
					if (s.startsWith(c)) {
						sbi_s = s;
						break;
					} else
						sbi_loc++;
				}
				for (String j : jacres) {
					if (j.startsWith(c)) {
						jac_s = j;
						break;
					} else
						jac_loc++;
				}

				for (String o : ochres) {
					if (o.startsWith(c)) {
						och_s = o;
						break;
					} else
						och_loc++;
				}

				System.out.println(test.replace("org.apache.tools.ant.", "")
						+ "-" + c.replace("org.apache.tools.ant.", "") + " : "
						+ tarres.size() + "&\t" + printS(tar_s) + "&\t"
						+ tar_loc + "&\t" + printS(sbi_s) + "&\t" + sbi_loc
						+ "&\t" + printS(jac_s) + "&\t" + jac_loc + "&\t"
						+ printS(och_s) + "&\t" + och_loc);
			}
		}

	}

	public static void debugCM(String loc, String tar, String sbi, String jac,
			String och, String heu) throws IOException {
		HashMap<String, List<String>> locinfo = getInfo(loc);
		HashMap<String, List<String>> tarinfo = getInfo(tar);
		HashMap<String, List<String>> sbiinfo = getInfo(sbi);
		HashMap<String, List<String>> jacinfo = getInfo(jac);
		HashMap<String, List<String>> ochinfo = getInfo(och);
		HashMap<String, List<String>> heuinfo = getInfo(heu);

		for (String test : locinfo.keySet()) {
			List<String> locs = locinfo.get(test);
			List<String> tarres = tarinfo.get(test);
			List<String> sbires = sbiinfo.get(test);
			List<String> jacres = jacinfo.get(test);
			List<String> ochres = ochinfo.get(test);
			List<String> heures = heuinfo.get(test);
			int mc = 0;
			for (String t : tarres) {

				if (t.startsWith("CM:") || t.startsWith("AM:"))
					mc++;
			}
			for (String c : locs) {
				if (!c.startsWith("CM:") && !c.startsWith("AM:")) {
					System.out.println(test
							.replace("org.apache.tools.ant.", "")
							+ "-"
							+ c.replace("org.apache.tools.ant.", "") + " : ");
					continue;

				}
				int tar_loc = 1;
				int sbi_loc = 1;
				int jac_loc = 1;
				int och_loc = 1;
				int heu_loc = 1;

				for (String t : tarres) {
					if (t.startsWith(c))
						break;
					else {
						if (t.startsWith("CM:") || t.startsWith("AM:"))
							tar_loc++;
					}
				}
				for (String s : sbires) {
					if (s.startsWith(c))
						break;
					else {
						if (s.startsWith("CM:") || s.startsWith("AM:"))
							sbi_loc++;
					}
				}
				for (String j : jacres) {
					if (j.startsWith(c))
						break;
					else {
						if (j.startsWith("CM:") || j.startsWith("AM:"))
							jac_loc++;
					}
				}

				for (String o : ochres) {
					if (o.startsWith(c))
						break;
					else {
						if (o.startsWith("CM:") || o.startsWith("AM:"))
							och_loc++;
					}
				}

				for (String h : heures) {
					if (h.startsWith(c))
						break;
					else
						heu_loc++;
				}
				System.out.println(test.replace("org.apache.tools.ant.", "")
						+ "-" + c.replace("org.apache.tools.ant.", "")
						+ " :\t " + tar_loc + "\t" + sbi_loc + "\t" + jac_loc
						+ "\t" + och_loc + "\t" + heu_loc + "\t"
						+ mc);
			}
		}

	}

	public static HashMap<String, List<String>> getInfo(String path)
			throws IOException {
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();

		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String test = line.substring(line.indexOf(">") + 1);
				if (test.contains(" -"))
					test = line.substring(line.indexOf(">") + 1,
							line.lastIndexOf(" -"));
				line = reader.readLine();

				List<String> contents = new ArrayList<String>();
				while (line != null && !line.startsWith("<TEST>")) {
					contents.add(line);
					line = reader.readLine();
				}
				map.put(test, contents);
			} else
				line = reader.readLine();
		}

		return map;
	}

	public static String printS(String s) {
		s = s.substring(s.indexOf(": ") + 2);
		double susp = Double.parseDouble(s.substring(0, s.indexOf(" - ")));
		String heu = s.substring(s.indexOf(" - ") + 3);
		return PrintData.PrintDouble2(susp);
		// return PrintData.PrintDouble2(susp) + "/" + heu;
	}
}
