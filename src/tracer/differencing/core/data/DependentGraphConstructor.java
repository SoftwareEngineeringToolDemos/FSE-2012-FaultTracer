package tracer.differencing.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DependentGraphConstructor {

	public static List<AtomicChange> constructDependenceGraph(
			List<AtomicChange> data) {

		HashMap<String, AtomicChange> map = new HashMap<String, AtomicChange>();
		for (AtomicChange a1 : data) {
			if (map.get(a1.toString()) != null)
				a1 = map.get(a1.toString());
			if (a1.getType().equals("AM")) {
				if (a1.fCallees.size() > 0 || a1.mCallees.size() > 0) {
					for (AtomicChange a2 : data) {
						if (a2.getType().equals("AM")) {
							if (a1.mCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String overridden : a1.mCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("AM")) {
											if (a3.getName().equals(overridden)) {
												tag2 = true;
												break;
											}
										}
									}
									if (!tag2) {
										tag = false;
										GlobalData.overridden_rel.put(
												a2.toString(), overridden);
										break;
									}
								}
								if (tag)
									a1.dependOns.add(a2.toString());
							}

						} else if (a2.getType().equals("AF")) {
							if (a1.fCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String hidden : a1.fCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("AF")) {
											if (a3.getName().equals(hidden)) {
												tag2 = true;
												break;
											}
										}

									}
									if (!tag2) {
										tag = false;
										GlobalData.overridden_rel.put(
												a2.toString(), hidden);
										break;
									}
								}
								if (tag)
									a1.dependOns.add(a2.toString());
							}
						}

					}

				}
			} else if (a1.getType().equals("DM")) {
				if (a1.fCallees.size() > 0 || a1.mCallees.size() > 0) {
					for (AtomicChange a2 : data) {
						if (a2.getType().equals("DM")) {
							if (a1.mCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String overridden : a1.mCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("DM")) {
											if (a3.getName().equals(overridden)) {
												tag2 = true;
												break;
											}
										}
									}
									if (!tag2) {
										tag = false;
										break;
									}
								}
								if (tag) {
									if (map.get(a2.toString()) != null)
										a2 = map.get(a2.toString());
									a2.dependOns.add(a1.toString());
									map.put(a2.toString(), a2);
								}
							}
						} else if (a2.getType().equals("DF")) {
							if (a1.fCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String hidden : a1.fCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("DF")) {
											if (a3.getName().equals(hidden)) {
												tag2 = true;
												break;
											}
										}

									}
									if (!tag2) {
										tag = false;
										break;
									}
								}
								if (tag) {
									if (map.get(a2.toString()) != null)
										a2 = map.get(a2.toString());
									a2.dependOns.add(a1.toString());
									map.put(a2.toString(), a2);
								}
							}
						}

					}

				}

			} else if (a1.getType().equals("CM")) {
				if (a1.fCallees.size() > 0 || a1.mCallees.size() > 0) {
					for (AtomicChange a2 : data) {
						if (a2.getType().equals("DF")) {// BUGFIX: if
														// (a2.getName().equals("DF"))
														// {
							if (a1.fCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String hidden : a1.fCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("DF")) {
											if (a3.getName().equals(hidden)) {
												tag2 = true;
												break;
											}
										}
									}
									if (!tag2) {
										tag = false;
										break;
									}
								}
								if (tag) {
									if (map.get(a2.toString()) != null)
										a2 = map.get(a2.toString());
									a2.dependOns.add(a1.toString());
									map.put(a2.toString(), a2);
								}
							}
						} else if (a2.getType().equals("DM")) {
							if (a1.mCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String overridden : a1.mCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("DM")) {
											if (a3.getName().equals(overridden)) {
												tag2 = true;
												break;
											}
										}
									}
									if (!tag2) {
										tag = false;
										break;
									}
								}
								if (tag) {
									if (map.get(a2.toString()) != null)
										a2 = map.get(a2.toString());
									a2.dependOns.add(a1.toString());
									map.put(a2.toString(), a2);
								}
							}
						} else if (a2.getType().equals("AF")) {
							if (a1.fCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String hidden : a1.fCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("AF")) {
											if (a3.getName().equals(hidden)) {
												tag2 = true;
												break;
											}
										}

									}
									if (!tag2) {
										tag = false;
										GlobalData.overridden_rel.put(
												a2.toString(), hidden);
										break;
									}
								}
								if (tag)
									a1.dependOns.add(a2.toString());
							}
						} else if (a2.getType().equals("AM")) {
							if (a1.mCallees.keySet().contains(a2.getName())) {
								boolean tag = true;
								for (String overridden : a1.mCallees.get(a2
										.getName())) {
									boolean tag2 = false;
									for (AtomicChange a3 : data) {
										if (a3.getType().equals("AM")) {
											if (a3.getName().equals(overridden)) {
												tag2 = true;
												break;
											}

										}
									}
									if (!tag2) {
										tag = false;
										GlobalData.overridden_rel.put(
												a2.toString(), overridden);
										break;
									}
								}
								if (tag)
									a1.dependOns.add(a2.toString());
							}
						}

					}
				}
			}
			map.put(a1.toString(), a1);
		}
		List<AtomicChange> res = new ArrayList<AtomicChange>(map.values());
		return res;
	}
}
