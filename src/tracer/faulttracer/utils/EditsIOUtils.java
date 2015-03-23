package tracer.faulttracer.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EditsIOUtils {
	public static List<String> getAtomicChanges(String diff) throws IOException {
		List<String> res = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		while (line != null) {
			if (FaultTracerProperties.CONSIDER_LCF || !line.startsWith("LCF:")) {
				int s = line.indexOf(":");
				int e = line.indexOf("=>");
				String elem = line.substring(s + 1, e);
				res.add(elem);
			}
			line = reader.readLine();
		}
		reader.close();
		// System.out.println("atomic changes:" + res.size());
		return res;
	}

	public static List<String> getTypedAtomicChanges(String diff)
			throws IOException {
		List<String> res = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		while (line != null) {
			if (FaultTracerProperties.CONSIDER_LCF || !line.startsWith("LCF:")) {
				int e = line.indexOf("=>");
				String elem = line.substring(0, e);
				res.add(elem);
			}
			line = reader.readLine();
		}
		reader.close();
		// System.out.println("atomic changes:" + res.size());
		return res;
	}
	public static Map<String, Set<String>> getTypedAtomicChangesWithRelation(
			String diff) throws IOException {
		HashMap<String, Set<String>> res = new HashMap<String, Set<String>>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		while (line != null) {
			if (FaultTracerProperties.CONSIDER_LCF || !line.startsWith("LCF:")) {
				int e = line.indexOf("=>");
				String elem = line.substring(0, e);
				String prel = line.substring(e + 2);
				HashSet<String> set = new HashSet<String>();
				if (prel.length() > 0) {
					String[] items = prel.split(", ");
					for (String item : items)
						set.add(item);
				}
				res.put(elem, set);
			}
			line = reader.readLine();
		}
		reader.close();
		// System.out.println("atomic changes:" + res.size());
		return res;
	}
	

}
