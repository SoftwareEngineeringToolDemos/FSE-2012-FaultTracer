package tracer.callgraph.instr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphConstructor {
	public static void ConstructGraph(File file) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		List<String> list=new ArrayList<String>();
		while(line!=null){
			list.add(line);
			line=reader.readLine();
		}
		reader.close();
		
	}

}
