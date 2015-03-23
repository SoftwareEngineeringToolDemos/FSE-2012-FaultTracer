package manual.debug;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Timer {

	public static void main(String[] args) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader("D:\\time.txt"));
		String line=reader.readLine();
		while(line!=null){
			String[] times=line.split("\t");
			for(int i=0;i<10;i++){
			double chianti=Double.parseDouble(times[i]);
			double faulttracer=Double.parseDouble(times[i+10]);
			System.out.println(chianti+"\t"+faulttracer);
			}
			line=reader.readLine();
		}
	}
}
