package tracer.coverage.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class MyAgent {
	private static Logger logger = Logger.getLogger(MyAgent.class);
	public static Set<String> excluded;
	public static void premain(String args, Instrumentation inst) {
		logger.debug("MyAgent start...");
		excluded=readExculdedTestClasses();
	    inst.addTransformer(new ClassTransformer());
	  } 
	public static void main(String[] args){
		logger.debug("main");
		System.out.println("hello world");
	}
	public static Set<String> readExculdedTestClasses(){
		File file=new File("TestClasses.dat");
		Set<String> res=new HashSet<String>();
		if(!file.exists())
			return res;
		try { 
			BufferedReader reader=new BufferedReader(new FileReader(file));
			String line=reader.readLine();
			while(line!=null){
				res.add(line);
				line=reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
}
