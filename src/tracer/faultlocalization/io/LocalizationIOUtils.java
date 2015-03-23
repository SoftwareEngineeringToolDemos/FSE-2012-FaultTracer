package tracer.faultlocalization.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import tracer.coverage.io.TracerUtils;
import tracer.faultlocalization.core.RankingEdit;
import tracer.test.selection.io.SelectionIOUtils;

public class LocalizationIOUtils {
	static Logger logger = Logger.getLogger(SelectionIOUtils.class);

	public static void writeRankedEditsToFile(String test,
			PriorityQueue<RankingEdit> edits, File dir) {
		logger.debug("write ranking list for test: "+test);
		String fileName = dir.getAbsolutePath() + File.separator + test + ".gz";
		int numRankedEdits = edits.size();
		ObjectOutputStream oos = null;
		String exceptionMessage = "Could not write " + fileName;
		try {
			oos = new ObjectOutputStream(new GZIPOutputStream(
					new FileOutputStream(fileName)));
			oos.writeInt(numRankedEdits);
			while (!edits.isEmpty()) {
				RankingEdit rankingEdit = edits.remove();
				logger.debug("write ranking edit: " + rankingEdit);
				oos.writeUTF(rankingEdit.getEdit());
				oos.writeDouble(rankingEdit.getSusp_val());
				oos.writeInt(rankingEdit.getHeu_val());
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
	

	public static Map<String, List<RankingEdit>> loadRankedEditsFromDir(File dir) {
		Map<String, List<RankingEdit>> result=new HashMap<String, List<RankingEdit>>();
		File[] files=dir.listFiles();
		for(File file:files){
			String testName=TracerUtils.stripGz(file.getName());
			logger.debug("load ranking list for test: "+testName);
			List<RankingEdit> edits=loadRankedEditsFromFile(file);
			result.put(testName, edits);
			for(RankingEdit edit:edits){
				logger.debug("load ranking edit: "+edit);
			}
		}
		return result;
	}
	public static List<RankingEdit> loadRankedEditsFromFile(File file) {
		if (!file.exists()) {
			logger.warn("No files for ranked edit. File does not exist: "
					+ file);
			return null;
		}

		List<RankingEdit> edits = new ArrayList<RankingEdit>();

		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new GZIPInputStream(new FileInputStream(file))));
			int numRankingEdits = ois.readInt();
			for (int i = 0; i < numRankingEdits; i++) {
				String edit = ois.readUTF();
				double susp_val = ois.readDouble();
				int heu_val = ois.readInt();
				RankingEdit rankingEdit = new RankingEdit(edit, susp_val,
						heu_val);
				edits.add(rankingEdit);
			}
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return edits;
	}
}
