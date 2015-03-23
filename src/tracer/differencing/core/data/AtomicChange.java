package tracer.differencing.core.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;

import tracer.utils.GetNames;

public class AtomicChange implements Constants {

	int type;
	IJavaElement elem;
	IJavaElement oldElem;
	public String LCName;
	public String LCFName;
	public String oldContent;
	public String newContent;
	public HashMap<String,List<String>> mCallees = null;
	public HashMap<String,List<String>> fCallees = null;
	public HashSet<String> dependOns = null;

	public AtomicChange(int type, IJavaElement elem) {
		this.type = type;
		this.elem = elem;
		dependOns = new HashSet<String>();
		mCallees = new HashMap<String,List<String>>();
		fCallees = new HashMap<String,List<String>>();
	}

	public AtomicChange(int type, IJavaElement elem1, IJavaElement elem2, String oldContent,
			String newContent) {
		this.type = type;
		this.elem = elem2;
		this.oldElem=elem1;
		this.oldContent = oldContent;
		this.newContent = newContent;
		dependOns = new HashSet<String>();
		mCallees = new HashMap<String,List<String>>();
		fCallees = new HashMap<String,List<String>>();
	}

	@Override
	public String toString() {
		if (type == LC)
			return this.getType() + ":" + GetNames.getLCName(LCName);// BUGFIX:
																		// return
																		// this.getType()+":"+LCName;
		
		else if (type == LCF)
			return this.getType() + ":" + GetNames.getLCFName(LCFName);
		
		else if (this.elem instanceof IMethod)
			return this.getType() + ":"
					+ GetNames.getMethodName((IMethod) this.elem);

		else if (this.elem instanceof IField)
			return this.getType() + ":"
					+ GetNames.getFieldName((IField) this.elem);
		else
			return this.getType() + ":" + this.elem.getPath() + "-"
					+ this.elem.getElementName();
	}

	public String getName() {

		if (type == LC)
			return GetNames.getLCName(LCName);
		
		if (type == LCF)
			return GetNames.getLCName(LCFName);

		if (this.elem instanceof IMethod)
			return GetNames.getMethodName((IMethod) this.elem);

		else if (this.elem instanceof IField)
			return GetNames.getFieldName((IField) this.elem);
		else
			return this.elem.getPath() + "-" + this.elem.getElementName();
	}

	public String getType() {

		if (type == APR)
			return "APR";
		else if (type == DPR)
			return "DPR";
		else if (type == AP)
			return "AP";
		else if (type == DP)
			return "DP";
		else if (type == AC)
			return "AC";
		else if (type == DC)
			return "DC";
		else if (type == AT)
			return "AT";
		else if (type == DT)
			return "DT";
		else if (type == AM)
			return "AM";
		else if (type == DM)
			return "DM";
		else if (type == CM)
			return "CM";
		else if (type == AF)
			return "AF";
		else if (type == DF)
			return "DF";
		else if (type == CFI)
			return "CFI";
		else if (type == CSFI)
			return "CSFI";
		else if (type == AI)
			return "AI";
		else if (type == DI)
			return "DI";
		else if (type == CI)
			return "CI";
		else if (type == ASI)
			return "ASI";
		else if (type == DSI)
			return "DSI";
		else if (type == CSI)
			return "CSI";
		else if(type==LC)
			return "LC";
		else return "LCF";
	}

	public IJavaElement getJavaElement() {
		return this.elem;
	}
	public IJavaElement getOldJavaElement() {
		return this.oldElem;
	}
}
