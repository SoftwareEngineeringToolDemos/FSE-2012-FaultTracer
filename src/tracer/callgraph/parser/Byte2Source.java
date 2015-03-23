package tracer.callgraph.parser;

public class Byte2Source {
	public static void main(String[] args) {
		// testMe(42, false);
		System.out.println("!!!!!!!!!!!!!!! Start Testing! ");
		run("");
	}

	public static String run(String in) {
		StringBuilder out = new StringBuilder();

		if (in.contains("-")) {
			String[] items = in.split("-");
			out.append(Byte2Source.transform(items[0]));
			out.append("-");
			out.append(Byte2Source.transform(items[1]));
		} else
			out.append(Byte2Source.transform(in));
		return out.toString();
	}

	public static String transform(String in) {
		StringBuilder out = new StringBuilder();
		if (!in.contains(":"))
			return in;
		String[] segs = in.split(":");
		out.append(segs[0].replace("/", "."));
		if (!segs[1].contains("<init>")) {
			out.append("." + segs[1] + ":");
		} else {
			int i = segs[0].lastIndexOf("/");
			String type = segs[0].substring(i + 1);
			String method;
			if (type.contains("$")) {
				method = type.substring(type.indexOf("$") + 1);
			} else
				method = type.substring(type.indexOf(">") + 1);
			out.append("." + method + ":");
		}

		if (!segs[2].contains("("))
			out.append(unitTran(segs[2]));
		else {
			int start = in.indexOf("(");
			int end = in.indexOf(")");
			String paraStr = in.substring(start + 1, end);
			String[] paras = paraStr.split(";");
			out.append("(");
			for (String para : paras) {
				out.append(unitTran(para) + ";");
			}
			if (!(in.charAt(end - 1) == ';'))
				out.deleteCharAt(out.length() - 1);
			out.append(")");

			String retn = unitTran(in.substring(end + 1));
			out.append(retn);

		}

		return out.toString();
	}

	public static String unitTran(String in) {
		StringBuilder out = new StringBuilder();
		if (in.contains("L")) {
			int i = in.indexOf('L');
			String pre = in.substring(0, i);
			String sub = in.substring(i + 1);
			out.append(pre);
			if (!sub.contains("/")) {
				out.append("Q" + sub);
			} else {
				int j = sub.lastIndexOf("/");
				out.append("Q" + sub.substring(j + 1));
			}
		} else
			out.append(in);
		return out.toString();
	}

}
