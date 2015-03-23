package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class FaultTracer {
	// static List<String> list;
	static boolean considerLCF = true;

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
					} else if (line.startsWith("<FR>")) {
						String field = line.substring(line.indexOf("-") + 1);
						if (changes.contains(field)) {
							selected.add(testName);
							break;
						}

					} else if (line.startsWith("<FW>")) {

					}
					line = reader.readLine();
				}

			} else
				line = reader.readLine();
		}
		// System.out.println(selected.size());
		// list = selected;
		/*
		 * for (String s : selected) { System.out.println(s); }
		 */
		return selected;
	}

	public static double localization(String coverage, String diff,
			List<String> list) throws IOException {
		double res = 0.0;
		double count = 0.0;
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		List<String> changes = getTypedAtomicChanges(diff);
		String line = reader.readLine();
		double sum = 0.0;
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = dropTag(line);
				if (list.contains(testName)) {
					// System.out.println("testName:"+testName);
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
						} else if (line.startsWith("<FR>")) {
							String field = line
									.substring(line.indexOf("-") + 1);
							for (String change : changes) {
								if (change.contains(field))
									affectingChanges.add(change);
							}

						} else if (line.startsWith("<FW>")) {

						}
						line = reader.readLine();
					}
					sum += affectingChanges.size();
					// System.out.println("test:"+testName+"-"+affectingChanges.size());
				} else
					line = reader.readLine();

			} else
				line = reader.readLine();
		}
		res = sum / count;
		return res;
	}

	public static double localization2(String coverage, String diff,
			List<String> list, BufferedWriter r) throws IOException {
		double res = 0.0;
		double count = 0.0;
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		HashMap<String, HashSet<String>> changes = getTypedAtomicChanges2(diff);
		String line = reader.readLine();
		double sum = 0.0;
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = dropTag(line);
				if (list.contains(testName)) {
					r.write(line + "\n");
					// System.out.println("testName:"+testName);
					count++;
					HashSet<String> affectingChanges = new HashSet<String>();
					line = reader.readLine();
					while (line != null && !line.startsWith("<TEST>")) {
						if (!line.contains("-")) {
							if (!line.startsWith("<class")) {
								for (String change : changes.keySet()) {
									if (change.contains(line)) {
										if (!change.startsWith("LC:"))
											affectingChanges.add(change);
										else {
											for (String s : changes.get(change)) {
												affectingChanges.add(s);
											}
										}
									}
								}

							} else {
								String smethod = line.substring(line
										.indexOf(">") + 1);
								String receiver = getReceiver(line);
								String meth = getMethod(line);
								String method = receiver + "." + meth;
								for (String change : changes.keySet()) {
									if (change.contains(method)
											|| change.contains(smethod)) {
										if (!change.startsWith("LC:"))
											affectingChanges.add(change);
										else {
											for (String s : changes.get(change)) {
												affectingChanges.add(s);
											}
										}
									}
								}
							}
						} else if (line.startsWith("<FR>")) {
							String field = line
									.substring(line.indexOf("-") + 1);
							for (String change : changes.keySet()) {
								if (change.contains(field)) {
									if (!change.startsWith("LCF:"))
										affectingChanges.add(change);
									else {
										for (String s : changes.get(change)) {
											affectingChanges.add(s);
										}
									}
								}
							}

						} else if (line.startsWith("<FW>")) {

						}
						line = reader.readLine();
					}
					for (String c : affectingChanges) {
//						if (c.startsWith("LC:"))
//							System.out.println("haha");
						r.write(c + "\n");
					}
					sum += affectingChanges.size();
					// System.out.println("test:"+testName+"-"+affectingChanges.size());
				} else
					line = reader.readLine();

			} else
				line = reader.readLine();
		}
		res = sum / count;
		return res;
	}

	public static double localization3(String coverage, String diff,
			List<String> list, BufferedWriter r) throws IOException {
		double res = 0.0;
		double count = 0.0;
		BufferedReader reader = new BufferedReader(new FileReader(coverage));
		HashMap<String, HashSet<String>> changes = getTypedAtomicChanges2(diff);
		String line = reader.readLine();
		double sum = 0.0;
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String testName = dropTag(line);
				if (list.contains(testName)) {
					// r.write(line + "\n");
					// System.out.println("testName:"+testName);
					count++;
					HashSet<String> affectingChanges = new HashSet<String>();
					line = reader.readLine();
					while (line != null && !line.startsWith("<TEST>")) {
						if (!line.contains("-")) {
							if (!line.startsWith("<class")) {
								for (String change : changes.keySet()) {
									if (change.contains(line)) {
										if (!change.startsWith("LC:")) {
											affectingChanges.add(change);
										} else {
											for (String s : changes.get(change)) {
												// System.out.println("haha");
												affectingChanges.add(s);
											}
										}
									}
								}

							} else {
								String smethod = line.substring(line
										.indexOf(">") + 1);
								String receiver = getReceiver(line);
								String meth = getMethod(line);
								String method = receiver + "." + meth;
								for (String change : changes.keySet()) {
									if (change.contains(method)
											|| change.contains(smethod)) {
										if (!change.startsWith("LC:")) {
											affectingChanges.add(change);
										} else {
											for (String s : changes.get(change)) {
												// System.out.println("haha");
												affectingChanges.add(s);
											}
										}
									}
								}
							}
						} else if (line.startsWith("<FR>")) {
							String field = line
									.substring(line.indexOf("-") + 1);
							for (String change : changes.keySet()) {
								if (change.contains(field)) {
									if (!change.startsWith("LCF:")) {
										affectingChanges.add(change);
									} else {
										for (String s : changes.get(change)) {
											affectingChanges.add(s);
										}
									}
								}
							}

						} else if (line.startsWith("<FW>")) {

						}
						line = reader.readLine();
					}
					r.write(affectingChanges.size() + "\n");
					sum += affectingChanges.size();
					// System.out.println("test:"+testName+"-"+affectingChanges.size());
				} else
					line = reader.readLine();

			} else
				line = reader.readLine();
		}
		res = sum / count;
		return res;
	}

	public static HashMap<String, HashSet<String>> determineAffectingChanges(
			HashMap<String, HashSet<String>> tests,
			HashMap<String, HashSet<String>> changeMap) {
		HashMap<String, HashSet<String>> test_affchgs = new HashMap<String, HashSet<String>>();
		for (String testName : tests.keySet()) {
			HashSet<String> affectingChanges = new HashSet<String>();
			HashSet<String> cons = tests.get(testName);
			for (String con : cons) {
				if (!con.contains("-")) {
					if (!con.startsWith("<class")) {
						for (String change : changeMap.keySet()) {
							if (change.contains(con)) {
								if (!change.startsWith("LC:")) {
									affectingChanges.add(change);
								} else {
									for (String s : changeMap.get(change)) {
										// System.out.println("haha");
										affectingChanges.add(s);
									}
								}
							}
						}

					} else {
						String smethod = con.substring(con.indexOf(">") + 1);
						String receiver = FaultTracer.getReceiver(con);
						String meth = FaultTracer.getMethod(con);
						String method = receiver + "." + meth;
						for (String change : changeMap.keySet()) {
							if (change.contains(method)) {
								//System.out.println("haha");
							} else if (change.contains(smethod)) {
								//System.out.println("xixi");
							}
							if (change.contains(method)
									|| change.contains(smethod)) {
								if (!change.startsWith("LC:")) {
									affectingChanges.add(change);
								} else {
									for (String s : changeMap.get(change)) {
										// System.out.println("haha");
										affectingChanges.add(s);
									}
								}
							}
						}
					}
				} else if (con.startsWith("<FR>")) {
					String field = con.substring(con.indexOf("-") + 1);
					for (String change : changeMap.keySet()) {
						if (change.contains(field)) {
							if (!change.startsWith("LCF:")) {
								affectingChanges.add(change);
							} else {
								for (String s : changeMap.get(change)) {
									affectingChanges.add(s);
								}
							}
						}
					}

				} else if (con.startsWith("<FW>")) {

				}
			}
			test_affchgs.put(testName, affectingChanges);
		}
		return test_affchgs;
	}

	/*
	 * public static boolean isConsFieldAccess(String line) { int s =
	 * line.indexOf(">"); int e = line.indexOf("-"); String method =
	 * line.substring(s + 1, e); String type = method.substring(0,
	 * method.lastIndexOf(".")); String mName =
	 * method.substring(method.lastIndexOf(".") + 1, method.indexOf(":"));
	 * String tName = type.substring(type.lastIndexOf(".") + 1); String
	 * tShortName = tName.substring(tName.indexOf("$") + 1);
	 * if(mName.equals("<clinit>")){
	 * System.out.println("static field access in constructors, cancelled");
	 * return true; } if (mName.equals("<init>")||tShortName.equals(mName)) {
	 * //System.out.println("field access in constructors, cancelled"); return
	 * true; } return false;
	 * 
	 * }
	 */

	public static List<String> getAtomicChanges(String diff) throws IOException {
		List<String> res = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		while (line != null) {
			if (considerLCF || !line.startsWith("LCF:")) {
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
			if (considerLCF || !line.startsWith("LCF:")) {
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

	public static HashMap<String, HashSet<String>> getTypedAtomicChanges2(
			String diff) throws IOException {
		HashMap<String, HashSet<String>> res = new HashMap<String, HashSet<String>>();
		BufferedReader reader = new BufferedReader(new FileReader(diff));
		String line = reader.readLine();
		while (line != null) {
			if (considerLCF || !line.startsWith("LCF:")) {
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

}
