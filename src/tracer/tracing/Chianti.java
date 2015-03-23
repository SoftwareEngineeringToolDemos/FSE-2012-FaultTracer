package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chianti {
	// static List<String> list = new ArrayList<String>();

	public static List<String> selection(String coverage, String diff)
			throws IOException {
		List<String> selected = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		List<String> changes = getAtomicChanges(diff);
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = dropTag(line);
				line = reader.readLine();
				while (line != null && !line.startsWith("<TEST>")) {

					if (!line.contains("-")) {
						if (!line.startsWith("<class ")) {
							if (changes.contains(line)) {
								selected.add(testName);
								break;
							}
						} else {
							String receiver = getReceiver(line);
							String method = getMethod(line);
							String change = receiver + "." + method;
							if (changes.contains(change)
									|| changes.contains(line.substring(line
											.indexOf(">") + 1))) {
								selected.add(testName);
								break;
							}
						}
					}
					line = reader.readLine();
				}

			} else
				line = reader.readLine();
		}
		// System.out.println(selected.size());
		// list = selected;
		return selected;
	}

	public static double localization2(String coverage, String diff,
			List<String> list, BufferedWriter writer) throws IOException {
		double count = 0.0;
		double res = 0.0;
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		List<String> changes = getTypedAtomicChanges(diff);
		String line = reader.readLine();
		double sum = 0.0;
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = dropTag(line);
				if (list.contains(testName)) {
					count++;

					HashSet<String> affectingChanges = new HashSet<String>();
					line = reader.readLine();
					while (line != null && !line.startsWith("<TEST>")) {
						if (!line.contains("-")) {
							if (!line.startsWith("<class")) {
								for (String change : changes) {
									if (change.contains(line)) {
										affectingChanges.add(change);
									}
								}

							} else {
								String smethod = line.substring(line
										.indexOf(">") + 1);
								String receiver = getReceiver(line);
								String meth = getMethod(line);
								String method = receiver + "." + meth;
								for (String change : changes) {
									if (change.contains(method)
											|| change.contains(smethod)) {
										affectingChanges.add(change);
									}
								}
							}
						}
						line = reader.readLine();
					}
					HashSet<String> toAdd = new HashSet<String>();
					int ams = 0;
					for (String s : affectingChanges) {
						if (s.startsWith("AM:")) {
							ams++;
							toAdd.add(s.replace("AM:", "CM:")); // TODO
						}
					}
					affectingChanges.addAll(toAdd);
					HashSet<String> tChanges = transitiveChanges(
							affectingChanges, diff);
					HashSet<String> lcs = new HashSet<String>();
					for (String change : tChanges) {
						if (change.startsWith("LC:")
								|| change.startsWith("LCF:"))
							lcs.add(change);
					}
					tChanges.removeAll(lcs);
					sum += tChanges.size() - ams;
					writer.write((tChanges.size() - ams) + "\n");
				} else
					line = reader.readLine();

			} else
				line = reader.readLine();
		}
		res = sum / count;
		return res;
	}

	public static HashMap<String, HashSet<String>> determineAffectingChanges(
			HashMap<String, HashSet<String>> tests, String diff)
			throws IOException {
		HashMap<String, HashSet<String>> test_affchgs = new HashMap<String, HashSet<String>>();
		List<String> changes = getTypedAtomicChanges(diff);
		for (String testName : tests.keySet()) {
			HashSet<String> affectingChanges = new HashSet<String>();
			for (String line : tests.get(testName)) {
				if (!line.contains("-")) {
					if (!line.startsWith("<class")) {
						for (String change : changes) {
							if (change.contains(line)) {
								affectingChanges.add(change);
							}
						}

					} else {
						String smethod = line.substring(line.indexOf(">") + 1);
						String receiver = getReceiver(line);
						String meth = getMethod(line);
						String method = receiver + "." + meth;
						for (String change : changes) {
							if (change.contains(method)
									|| change.contains(smethod)) {
								affectingChanges.add(change);
							}
						}
					}
				}
			}
			HashSet<String> toAdd = new HashSet<String>();
			HashSet<String> ams=new HashSet<String>();
			for (String s : affectingChanges) {
				if (s.startsWith("AM:")) {
					toAdd.add(s.replace("AM:", "CM:")); // TODO
					ams.add(s.replace("AM:", "CM:"));
				}
			}
			affectingChanges.addAll(toAdd);
			HashSet<String> tChanges = transitiveChanges(affectingChanges, diff);
			HashSet<String> lcs = new HashSet<String>();
			for (String change : tChanges) {
				if (change.startsWith("LC:") || change.startsWith("LCF:"))
					lcs.add(change);
			}
			tChanges.removeAll(lcs);
			tChanges.removeAll(ams);
			test_affchgs.put(testName,tChanges);
		}

		return test_affchgs;
	}

	
	public static double localization(String coverage, String diff,
			List<String> list) throws IOException {
		double count = 0.0;
		double res = 0.0;
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		List<String> changes = getTypedAtomicChanges(diff);
		String line = reader.readLine();
		double sum = 0.0;
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = dropTag(line);
				if (list.contains(testName)) {
					count++;

					HashSet<String> affectingChanges = new HashSet<String>();
					line = reader.readLine();
					while (line != null && !line.startsWith("<TEST>")) {
						if (!line.contains("-")) {
							if (!line.startsWith("<class")) {
								for (String change : changes) {
									if (change.contains(line)) {
										affectingChanges.add(change);
									}
								}

							} else {
								String smethod = line.substring(line
										.indexOf(">") + 1);
								String receiver = getReceiver(line);
								String meth = getMethod(line);
								String method = receiver + "." + meth;
								for (String change : changes) {
									if (change.contains(method)
											|| change.contains(smethod)) {
										affectingChanges.add(change);
									}
								}
							}
						}
						line = reader.readLine();
					}
					HashSet<String> toAdd = new HashSet<String>();
					int ams = 0;
					for (String s : affectingChanges) {
						if (s.startsWith("AM:")) {
							ams++;
							toAdd.add(s.replace("AM:", "CM:")); // TODO
						}
					}
					affectingChanges.addAll(toAdd);
					HashSet<String> tChanges = transitiveChanges(
							affectingChanges, diff);
					sum += tChanges.size() - ams;

				} else
					line = reader.readLine();

			} else
				line = reader.readLine();
		}
		res = sum / count;
		return res;
	}

	public static HashSet<String> transitiveChanges(
			HashSet<String> affectingChanges, String diff) throws IOException {
		HashSet<String> tChanges = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		HashMap<String, String> map = new HashMap<String, String>();
		while (line != null) {
			if (!line.startsWith("LCF:")) {
				if (line.startsWith("AM:"))
					line = line.replaceFirst("AM:", "CM:");
				String[] items = line.split("=>");
				if (items.length == 1)
					map.put(items[0], "");
				else
					map.put(items[0], items[1]);
			}
			line = reader.readLine();
		}
		reader.close();

		List<String> worklist = new ArrayList<String>();
		worklist.addAll(affectingChanges);
		while (!worklist.isEmpty()) {
			String cur = worklist.remove(0);
			tChanges.add(cur);
			if (cur.startsWith("AM:"))
				continue;

			String depend = map.get(cur);
			if (depend != null && !depend.equals("")) {
				String[] dependItems = depend.split(", ");
				for (String dependItem : dependItems) {
					if (!tChanges.contains(dependItem)) {
						worklist.add(dependItem);
					}
				}
			}

		}

		return tChanges;

	}

	public static List<String> getAtomicChanges(String diff) throws IOException {
		List<String> res = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		while (line != null) {
			if (!line.startsWith("LCF:")) {
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
			if (!line.startsWith("LCF:")) {
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

	public static String dropTag(String in) {
		return in.substring(in.indexOf(">") + 1);
	}

	public static String getReceiver(String in) {
		int e = in.indexOf(">");
		int s = 7;
		return in.substring(s, e);
	}

	public static String getMethod(String in) {
		int s = in.lastIndexOf(".");
		return in.substring(s + 1);
	}

	public static List<String> selection2(String coverage, String diff,
			List<String> list) throws IOException {
		List<String> selected = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		List<String> changes = getAtomicChanges(diff);
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = dropTag(line);
				if (list.contains(testName)) {
					System.out.println("<TEST>" + testName);
					line = reader.readLine();
					while (line != null && !line.startsWith("<TEST>")) {

						if (!line.contains("-")) {
							if (!line.startsWith("<class ")) {
								if (changes.contains(line)) {
									System.out.println(line);
									selected.add(testName);
									break;
								}

							} else {
								String receiver = getReceiver(line);
								String method = getMethod(line);
								String change = receiver + "." + method;
								if (changes.contains(change)
										|| changes.contains(line.substring(line
												.indexOf(">") + 1))) {
									System.out.println(line);
									selected.add(testName);
									break;
								}
							}
						}
						line = reader.readLine();
					}
				} else
					line = reader.readLine();
			} else
				line = reader.readLine();
		}
		// System.out.println(selected.size());
		// list = selected;
		return selected;
	}
	public static void writeAffectingChanges(HashMap<String, HashSet<String>> test_affchgs, String path) throws IOException{
		BufferedWriter writer=new BufferedWriter(new FileWriter(path));
		for(String test:test_affchgs.keySet()){
			writer.write("<TEST>"+test+"\n");
			for(String chg:test_affchgs.get(test)){
				writer.write(chg+"\n");
			}
		}
		writer.flush();
		writer.close();
	
	}

}
