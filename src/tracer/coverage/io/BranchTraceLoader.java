package tracer.coverage.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tracer.coverage.core.Properties;

public class BranchTraceLoader {
	
	public static void main(String[] args) throws IOException{
		Map<String, Map<String, Integer>> trace=TracerUtils.loadMethodTracesFromDirectory(Properties.TRACER_BRAN_FILES);
		trace=removeMethInfo(trace);
		for(String test:trace.keySet()){
			System.out.println("<Test>"+test);
			for(String elem:trace.get(test).keySet()){
				System.out.println(elem+"-"+trace.get(test).get(elem));
			}
		}
		CoverageSerializer.serialize("branch-coverage", trace);
	}
	
	public static Map<String, Map<String, Integer>> removeMethInfo(Map<String, Map<String, Integer>> trace){
		Map<String, Map<String, Integer>> result=new HashMap<String, Map<String, Integer>>();
		for(String key:trace.keySet()){
			Map<String, Integer> map=trace.get(key);
			Map<String, Integer> newMap=new HashMap<String, Integer>();
			for(String k:map.keySet()){
				int freq=map.get(k);
				String[] item=k.split(":");
				String newK=item[0].substring(0, item[0].lastIndexOf("."))+":"+item[2]+":"+item[3];
				newMap.put(newK, freq);
			}
			result.put(key, newMap);
		}
		return result;
	}

}
