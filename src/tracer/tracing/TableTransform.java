package tracer.tracing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TableTransform {
	public static void main(String[] args) throws IOException {
		double a=(329+2071*5+5752*3+586*2+5019*13)/24.0;
		System.out.println(a);
		BufferedReader reader = new BufferedReader(new FileReader(
				"D:\\data.txt"));
		String line = reader.readLine();
		while (line != null) {
			if (!line.contains("&") || line.startsWith("%"))
				System.out.println(line);
			else {
				String[] items = line.split("&");
				String toWrite = items[0] + "&" + items[1] + "&" +"5019"+ "&" + print(items[10]);
				
				for(int i=2;i<10;i++)
					toWrite=toWrite+"&" + print(items[i]);
				System.out.println(toWrite+"\\\\");

			}
			line = reader.readLine();
		}
	}

	public static String print(String s) {
		String out="";
		if(s.contains("\\\\"))
			out=s.replace("\\\\", "");
		else
		if(s.contains("/"))
		out= s.substring(0, s.indexOf("/"));
		else out=s;
		return out;
	}

}
