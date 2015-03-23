package tracer.test.prioritization.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import tracer.differencing.core.data.GlobalData;
import tracer.test.selection.core.SelectionProperties;

public class PrioritizationIOUtils {
	static Logger logger = Logger.getLogger(PrioritizationIOUtils.class);

	public static void writePrioritizedTests(List<String> tests) {
		String fileName = SelectionProperties.PROJECT_NEW_VERSION
				+ File.separator + SelectionProperties.PRIORITIZE_TEST_FILE;
		int numSelectedTests = tests.size();
		ObjectOutputStream oos = null;
		String exceptionMessage = "Could not write " + fileName;
		try {
			oos = new ObjectOutputStream(new GZIPOutputStream(
					new FileOutputStream(fileName)));
			oos.writeInt(numSelectedTests);

			for (String test : tests) {
				logger.debug("selecting test: " + test);
				oos.writeUTF(test);
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

	public static List<String> loadPrioritizedTests() {
		File file = new File(SelectionProperties.PRIORITIZE_TEST_FILE);
		if (!file.exists()) {
			logger.warn("No files for prioritized tests. File does not exist: "
					+ file);
			return null;
		}

		List<String> selected_tests = new ArrayList<String>();
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new GZIPInputStream(new FileInputStream(file))));
			int numSelectedTests = ois.readInt();
			for (int i = 0; i < numSelectedTests; i++) {
				String test = ois.readUTF();
				selected_tests.add(test);
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return selected_tests;
	}

	public static List<String> loadSelectedTestsForEclipse() {
		File file = new File(GlobalData.workspace_path + File.separator
				+ GlobalData.proj2.getElementName() + File.separator
				+ SelectionProperties.SELECT_TEST_FILE);
		if (!file.exists()) {
			logger.warn("No files for selected tests. File does not exist: "
					+ file);
			return null;
		}

		List<String> selected_tests = new ArrayList<String>();
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new GZIPInputStream(new FileInputStream(file))));
			int numSelectedTests = ois.readInt();
			for (int i = 0; i < numSelectedTests; i++) {
				String test = ois.readUTF();
				selected_tests.add(test);
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return selected_tests;
	}
}
