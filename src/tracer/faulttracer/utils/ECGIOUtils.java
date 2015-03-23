package tracer.faulttracer.utils;

public class ECGIOUtils {
	public static String stripOutCome(String line) {
		if(line.contains("-"))
		return line.substring(0, line.lastIndexOf("-"));
		return line;
	}
	public static boolean getOutCome(String line) {
		return Boolean.parseBoolean(line.substring(line.lastIndexOf("-")+1));
	}
	public static String getReceiver(String in) {
		int e = in.indexOf(">");
		int s = 7;
		return in.substring(s, e);
	}

	public static String getMethod(String in) {
		int s = in.lastIndexOf(".");
		return in.substring(s + 1);
	}
	
}
