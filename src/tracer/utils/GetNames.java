package tracer.utils;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class GetNames {
	public static String getLCName(String name) {
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(name.substring(0, name.lastIndexOf(":")));
		nameBuilder
				.append(":"
						+ transform(tripGeneric(name.substring(name
								.lastIndexOf(":") + 1))));// BUGFIX:
															// substring(index)=>substring(index+1)
		return nameBuilder.toString();
	}

	public static String getLCFName(String name) {
		StringBuilder nameBuilder = new StringBuilder();
		nameBuilder.append(name.substring(0, name.lastIndexOf(":")));
		nameBuilder
				.append(":"
						+ tripGeneric(unitTran(name.substring(name
								.lastIndexOf(":") + 1))));
		return nameBuilder.toString();
	}

	public static String getMethodName(IMethod iMethod) {

		StringBuilder name = new StringBuilder();
		name.append(iMethod.getDeclaringType().getFullyQualifiedName());
		name.append(".");
		name.append(iMethod.getElementName());

		try {
			String sig = iMethod.getSignature();// BUGFIX: without Qde.susebox.*
												// process
			sig = tripGeneric(sig);
			name.append(":" + transform(sig));
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name.toString();

	}

	public static String getShortMethodName(IMethod iMethod) {

		StringBuilder name = new StringBuilder();
		name.append(iMethod.getElementName());

		try {
			String sig = iMethod.getSignature();// BUGFIX: without Qde.susebox.*
												// process
			sig = tripGeneric(sig);
			name.append(":" + transform(sig));
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name.toString();

	}

	public static String getFieldName(IField iField) {

		StringBuilder name = new StringBuilder();
		name.append(iField.getDeclaringType().getFullyQualifiedName());
		name.append(".");
		name.append(iField.getElementName());
		try {
			String sig = iField.getTypeSignature();// BUGFIX: without
													// Qde.susebox.* process
			sig = tripGeneric(sig);// BUGFIX: get rid of generic, such as
									// QList<QRule;>;

			name.append(":" + unitTran(sig));
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name.toString();

	}

	public static String transform(String in) {
		StringBuilder out = new StringBuilder();

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

		return out.toString();
	}

	public static String unitTran(String in) {
		StringBuilder out = new StringBuilder();
		if (in.contains("Q")) {
			int i = in.indexOf('Q');
			String pre = in.substring(0, i);
			String sub = in.substring(i + 1);
			out.append(pre);
			if (!sub.contains(".")) {
				out.append("Q" + sub);
			} else {
				int j = sub.lastIndexOf(".");
				out.append("Q" + sub.substring(j + 1));
			}
		} else
			out.append(in);
		return out.toString();
	}

	public static String tripGeneric(String in) {
		while (in.contains("<") && in.contains(">")) {
			//System.out.println(in);
			int l1 = in.indexOf("<");
			int l2 = l1 + 1;
			int left = 1;
			int right = 0;
			while (l2 < in.length()) {

				if (in.charAt(l2) == '<')
					left++;
				else if (in.charAt(l2) == '>')
					right++;
				if(left==right)break;
				l2++;
			}

			String gene = in.substring(l1, l2 + 1);
			in = in.replace(gene, "");
		}
		return in;
	}

}
