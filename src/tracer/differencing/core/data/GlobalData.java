package tracer.differencing.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IJavaProject;


public class GlobalData {
	public static Map<String, Set<String>> field_scope=new HashMap<String, Set<String>>();
	public static Map<String, String> overridden_rel=new HashMap<String, String>();
	public static List<AtomicChange> data = new ArrayList<AtomicChange>();
	public static List<AtomicChange> atomicData = new ArrayList<AtomicChange>();
	public static HashSet<String> projNames=new HashSet<String>();
	public static IJavaProject proj1;
	public static IJavaProject proj2;
	
	public static String workspace_path;
	
}
