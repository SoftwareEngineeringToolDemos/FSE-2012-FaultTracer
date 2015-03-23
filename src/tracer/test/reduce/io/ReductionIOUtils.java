package tracer.test.reduce.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import tracer.faulttracer.utils.FaultTracerProperties;

public class ReductionIOUtils {
	static Logger logger = Logger.getLogger(ReductionIOUtils.class);
	public static void writeReducedTests(String pathRoot, Map<String, Set<String>> tests)  {
		File tracer_dir = new File(pathRoot + FaultTracerProperties.TRACER_DIR);
		if (!tracer_dir.exists())
			tracer_dir.mkdir();
		String fileName = pathRoot + FaultTracerProperties.REDUCE_TEST_FILE;

		int numReducedTests = tests.size();
		ObjectOutputStream oos = null;
		String exceptionMessage = "Could not write " + fileName;
		try {
			oos = new ObjectOutputStream(new GZIPOutputStream(
					new FileOutputStream(fileName)));
			oos.writeInt(numReducedTests);

			for (String test : tests.keySet()) {
				oos.writeUTF(test);
				oos.writeInt(tests.get(test).size());
				for(String redun:tests.get(test))
					oos.writeUTF(redun);
			}
		} catch (IOException e) {
			throw new RuntimeException(exceptionMessage, e);
		} finally {
			if (oos != null) {
				try {
					oos.flush();
					oos.close();
				} catch (IOException e) {
					throw new RuntimeException(exceptionMessage, e);
				}
			}
		}
	}

	public static Map<String, Set<String>> loadReducedTests() {
		File file = new File(FaultTracerProperties.REDUCE_TEST_FILE);
		if (!file.exists()) {
			logger.warn("No files for reduced tests. File does not exist: "
					+ file);
			return null;
		}

		Map<String, Set<String>> reduced_tests = new HashMap<String, Set<String>>();
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new GZIPInputStream(new FileInputStream(file))));
			int numSelectedTests = ois.readInt();
			for (int i = 0; i < numSelectedTests; i++) {
				String test = ois.readUTF();
				reduced_tests.put(test, new HashSet<String>());
				int numRedundant=ois.readInt();
				for(int j=0;j<numRedundant;j++)
					reduced_tests.get(test).add(ois.readUTF());
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reduced_tests;
	}
	
}
