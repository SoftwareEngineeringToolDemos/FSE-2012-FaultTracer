package tracer.tracing.heuristic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class CallHierarchyConstructor {

	public static HashSet<String> callmap=new HashSet<String>();
	public static void initialize(String path, String test) throws IOException{
		callmap.clear();
		BufferedReader reader=new BufferedReader(new FileReader(path));
		
		String line=reader.readLine();
		while(!line.startsWith("<TEST>"+test))
			line=reader.readLine();
		line=reader.readLine();
		while(line!=null&&!line.startsWith("<TEST>")){
			String caller;
			String callee;
			
			if(line.contains("-")){
				caller=line.substring(0, line.indexOf("-"));
				String calleeName=line.substring(line.lastIndexOf(".")+1);
				line=reader.readLine();
				if(line!=null&&!line.startsWith("<TEST>")){
					if(line.contains("-"))
						continue;
					else{
						String curName=line.substring(line.lastIndexOf(".")+1);
						if(curName.equals(calleeName)){
							if(line.startsWith("<"))
								callee=line.substring(line.indexOf(">")+1);
							else callee=line;
							
							callmap.add(caller+"-"+callee);
						}
						line=reader.readLine();
					}
				}
				
			
			}else line=reader.readLine();
			
		}


		
	}
	
}
