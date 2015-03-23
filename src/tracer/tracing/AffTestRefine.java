package tracer.tracing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AffTestRefine {
	static List<String> clist = new ArrayList<String>();
	static List<String> flist = new ArrayList<String>();

	public static void main(String[] args) throws IOException {
		String[] subs = { "jtopas", "xmlsec", "jmeter", "ant" };
		int[] vs = { 4, 4, 6, 9 };
		for (int i = 0; i < 4; i++) {
			trace(subs[i], vs[i]);
		}
	}

	public static void trace(String s, int v) throws IOException {
		String subject = "G:\\FSE_withLCF\\" + s + "\\";
		int vers = v;
		for (int i = 0; i < vers - 1; i++) {
			BufferedReader reader=new BufferedReader(new FileReader(subject+"affectedTests"+i+(i+1)));
			BufferedWriter writer=new BufferedWriter(new FileWriter(subject+"affectingChanges"+i+(i+1)));
			String line=reader.readLine();
			while(line!=null){
				if(line.startsWith("<TEST>")){
					String test=line;
					line=reader.readLine();
					if(line==null)break;
					if(line.startsWith("<TEST>"))
						continue;
					writer.write(test+"\n");
					while(line!=null&&!line.startsWith("<TEST>")){
						writer.write(line+"\n");
						line=reader.readLine();
					}
					if(line==null)break;
					if(line.startsWith("<TEST>"))continue;
				}
				line=reader.readLine();
			}
			writer.flush();
			writer.close();
			reader.close();
		}

	}


}
