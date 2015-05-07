package tracer.coverage.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import tracer.coverage.core.ECGCoverageListener;
import tracer.coverage.core.Properties;
import tracer.coverage.junit.JUnit4TestDriver;
import tracer.coverage.junit.JUnitTestDriver;
import tracer.differencing.core.data.GlobalData;

public class TracerUtils {
	static Logger logger = Logger.getLogger(TracerUtils.class);
	static boolean testResult;

	public static void writeMethodTrace(Map<String, Integer> coverageMap,
			String testName, String fileName) {
		int numEntities = coverageMap.size();
		int countEntities = 0;
		ObjectOutputStream oos = null;
		String exceptionMessage = "Could not write " + fileName;
		try {
			oos = new ObjectOutputStream(new GZIPOutputStream(
					new FileOutputStream(fileName)));
			if (Properties.JUNIT4)
				oos.writeBoolean(JUnit4TestDriver.pass);
			else
				oos.writeBoolean(JUnitTestDriver.pass);
			oos.writeInt(numEntities);

			for (String method : coverageMap.keySet()) {
				countEntities++;
				oos.writeUTF(method);
				int freq = coverageMap.get(method);
				oos.writeInt(freq);
			}
		} catch (IOException e) {
			ECGCoverageListener.logger.warn(exceptionMessage, e);
			throw new RuntimeException(exceptionMessage, e);
		} finally {
			if (oos != null) {
				try {
					oos.flush();
					oos.close();
				} catch (IOException e) {
					logger.warn(exceptionMessage, e);
					throw new RuntimeException(exceptionMessage, e);
				}
			}
		}
		if (countEntities != numEntities) {
			logger.warn("Different number of total entities (Writing again) "
					+ countEntities + " " + numEntities + " " + coverageMap);
			writeMethodTrace(coverageMap, testName, fileName);
		}
	}

	public static Map<String, Map<String, Integer>> loadMethodTracesFromDirectory()
			throws IOException {
		File dir = new File(Properties.TRACER_COV_DIR);
		logger.debug("Loading from " + dir);
		if (!dir.exists()) {
			logger.warn("No files for ECG coverage. Directory does not exist: "
					+ dir);
			return null;
		}
		File[] tests = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("gz");
			}
		});
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		Map<String, String> id_test = loadTestMapping(Properties.TRACER_COV_DIR);
		for (File f : tests) {
			Map<String, Integer> classMap = loadMethodTrace(f);
			String key = stripGz(f.getName());
			result.put(id_test.get(key) + "-" + testResult, classMap);
		}
		return result;
	}

	public static Map<String, Map<String, Integer>> loadMethodTracesFromDirectory(
			String dirPath) throws IOException {
		File dir = new File(dirPath);
		logger.debug("Loading from " + dir);
		if (!dir.exists()) {
			logger.warn("No files for ECG coverage. Directory does not exist: "
					+ dir);
			return null;
		}
		File[] tests = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("gz");
			}
		});
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		Map<String, String> id_test = loadTestMapping(dirPath);

		for (File f : tests) {
			Map<String, Integer> classMap = loadMethodTrace(f);
			String key = stripGz(f.getName());
			result.put(id_test.get(key) + "-" + testResult, classMap);
		}
		return result;
	}

	public static Map<String, Map<String, Integer>> loadMethodTracesFromDirectoryForNewVersion()
			throws IOException {
		File dir = new File(GlobalData.workspace_path + File.separator
				+ GlobalData.proj2.getElementName() + File.separator
				+ Properties.TRACER_COV_DIR);
		logger.debug("Loading from " + dir);
		if (!dir.exists()) {
			logger.warn("No files for ECG coverage. Directory does not exist: "
					+ dir);
			return null;
		}
		File[] tests = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("gz");
			}
		});
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		Map<String, String> id_test = loadTestMapping(GlobalData.workspace_path
				+ File.separator + GlobalData.proj2.getElementName()
				+ File.separator + Properties.TRACER_COV_DIR);

		for (File f : tests) {
			Map<String, Integer> classMap = loadMethodTrace(f);
			String key = stripGz(f.getName());
			result.put(id_test.get(key) + "-" + testResult, classMap);
		}
		return result;
	}

	public static Map<String, Map<String, Integer>> loadMethodTracesFromDirectoryForOldVersion()  {
		File dir = new File(GlobalData.workspace_path + File.separator
				+ GlobalData.proj1.getElementName() + File.separator
				+ Properties.TRACER_COV_DIR);
		logger.debug("Loading from " + dir);
		if (!dir.exists()) {
			logger.warn("No files for ECG coverage. Directory does not exist: "
					+ dir);
			return null;
		}
		File[] tests = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("gz");
			}
		});
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		Map<String, String> id_test=null;
		try {
			id_test = loadTestMapping(GlobalData.workspace_path + File.separator
					+ GlobalData.proj1.getElementName() + File.separator
					+ Properties.TRACER_COV_DIR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (File f : tests) {
			Map<String, Integer> classMap = loadMethodTrace(f);
			String key = stripGz(f.getName());
			result.put(id_test.get(key) + "-" + testResult, classMap);
		}
		return result;
	}

	public static Map<String, String> loadTestMapping(String dirPath)
			throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new FileReader(dirPath
				+ File.separator + Properties.TEST_ID_FILE));
		String line = reader.readLine();
		while (line != null) {
			String[] items = line.split(" ");
			map.put(items[0], items[1]);
			line = reader.readLine();
		}
		return map;
	}

	public static Map<String, Integer> loadMethodTrace(File file) {
		Map<String, Integer> mfMap = new HashMap<String, Integer>();
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new GZIPInputStream(new FileInputStream(file))));

			testResult = ois.readBoolean();
			int numClasses = ois.readInt();
			for (int i = 0; i < numClasses; i++) {
				String methodfield = Byte2Source.run(ois.readUTF());
				int freq = ois.readInt();
				mfMap.put(methodfield, freq);
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mfMap;
	}

	public static String stripGz(String test) {
		String key = test;
		if (test.endsWith(".gz")) {
			key = test.substring(0, test.length() - 3);
		}
		key = key.replace("test-", "");
		return key;
	}

}
