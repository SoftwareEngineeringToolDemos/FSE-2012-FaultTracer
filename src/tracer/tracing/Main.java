package tracer.tracing;

import java.io.IOException;
import java.util.List;

public class Main {
	static String path="G:\\ECOOP\\jmeter\\";
	static int i=1;
	public static void main(String[] args) throws IOException {
		String old_ECG = path+"log"+i+i;//assign the path to the ecg log file of the older version
		String new_ECG = path+"log"+(i+1)+i;//assign the path to the ecg log file of the newer version

		String adiff =path+"Diff"+i+(i+1);//assign the path to the adiff file for the compared versions

		// test selection, returns a list of selected tests
		List<String> selected = FaultTracer.selection(old_ECG, adiff);

		// fault localization, returns the average number of affecting changes
		// per test case
		double avg_aff_chg = FaultTracer.localization(new_ECG, adiff,
				selected);
		System.out.println("Selected test number:" + selected.size());
		System.out.println("Average affecting change number:" + avg_aff_chg);
	}
}
