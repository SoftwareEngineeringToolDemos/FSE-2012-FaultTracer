package tracer.utils;

public class FilterWhiteSpaces {

	public static String dropWhiteSpaces(String in) {

//		while (in.contains("/*") && in.contains("*/")) {
//
//			int s = in.indexOf("/*");
//			int e = in.indexOf("*/");
//			if (e + 2 >= in.length() || s > e + 2) {// BUGFIX: should
//													// handle:"*/" "/*"
//				break;
//			}
//			String comment = in.substring(s, e + 2);
//			in = in.replace(comment, "");
//		}
//		while (in.contains("//")) {
//			int s = in.indexOf("//");
//			if (in.substring(s).startsWith("// advance column to next stop")) {
//				String debug = in.substring(s, s + 30);
//				debug.charAt(10);
//			}
//			int i = s + 1;
//			while (i < in.length()) {
//				if (in.charAt(i) == '\n')
//					break;
//				i++;
//			}
//
//			String comment = in.substring(s, i);// BUGFIX: String
//												// comment=in.substring(s,e);
//			in = in.replace(comment, "");// BUGFIX: in.replace(comment, "");
//		}

		return in.replace("\n", "").replace("\r", "").replace(" ", "")
				.replace("\t", "");
	}
}
