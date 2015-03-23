package pkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetAffectingChange {

	public static void main(String[] args) throws IOException {
		String[] subjects = { "jtopas", "xmlsec", "jmeter", "Ant" };
		int[] vs = { 4, 4, 6, 9 };
		for (int j = 0; j < subjects.length; j++) {
			String subject = subjects[j];
			for (int i = 1; i < vs[j]; i++) {
				
				String ftPath = "P:\\HP_D\\FSE_withLCF\\" + subject
						+ "\\affectingChanges" + (i - 1) + i;
				String chiantiPath = "P:\\HP_D\\FSE_withLCF\\" + subject
						+ "\\Chianti_AffectingChanges" + (i - 1) + i;
				Map<String, Integer> cm = readAffChg(chiantiPath);
				Map<String, Integer> fm = readAffChg(ftPath);

				for (String test : cm.keySet()) {
					if (fm.containsKey(test))
						{//System.out.println(cm.get(test) + "\t" + fm.get(test));
					if(fm.get(test)>1000)
						System.out.println(subject+i+": "+test+"-"+fm.get(test));
						}
				}
			}
		}

	}

	public static Map<String, Integer> readAffChg(String path)
			throws IOException {
		Map<String, Integer> res = new HashMap<String, Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith("<TEST>")) {
				String test = line.substring(line.indexOf(">") + 1);
				int count = 0;
				line = reader.readLine();
				while (line != null && !line.startsWith("<TEST>")) {
					count++;
					line = reader.readLine();
				}
				res.put(test, count);
			} else
				line = reader.readLine();
		}
		return res;
	}
}
