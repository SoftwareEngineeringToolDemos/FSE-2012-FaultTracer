package tracer.callgraph.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParserRunner {

	public static void main(String[] args) throws IOException{
		
		BufferedReader reader=new BufferedReader(new FileReader("d:\\log"));
		String line =reader.readLine();
		while(line!=null){
			StringBuilder out=new StringBuilder();
			
			if(line.contains("-")){
				String[] items=line.split("-");
				out.append(Byte2Source.transform(items[0]));
				out.append("-");
				out.append(Byte2Source.transform(items[1]));
			}
			else out.append(Byte2Source.transform(line));
			System.out.println(out.toString());
			line=reader.readLine();
		}
		reader.close();
	}
}
