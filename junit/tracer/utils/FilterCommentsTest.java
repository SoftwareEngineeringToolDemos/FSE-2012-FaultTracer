package tracer.utils;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.junit.Test;

import tracer.differencing.core.data.GlobalData;

public class FilterCommentsTest {
@Test
	public void test1(){
	String in="	/*\n*\n	 * @Override public boolean visit(SuperFieldAccess node) { IVariableBinding\n	 * bind = node.resolveFieldBinding(); if (bind.isField()) { IField callee =\n */\n	@Override\npublic boolean visit(SimpleName node) {		\n	if(node.resolveBinding()==null)return false;	\n	IBinding bind = node.resolveBinding();	\n	if (bind instanceof IVariableBinding) {		\n		IVariableBinding ibind = (IVariableBinding) bind;		\n	if (ibind.isField()) {			\n		IField callee = (IField) ibind.getJavaElement();			\n		if (callee != null					\n			&& GlobalData.projNames.contains(callee.getPath()							\n					.segment(0))) {				\n		fCallees.add(GetNames.getFieldName(callee));				\n		// System.out.println(\"FieldAccess:\"+				\n		// GetNames.getFieldName(callee));				\n	}			\n}		\n}	\nreturn true;	\n}";
	String out=FilterWhiteSpaces.dropWhiteSpaces(in);
	assertEquals(out, "@Overridepublicbooleanvisit(SimpleNamenode){if(node.resolveBinding()==null)returnfalse;IBindingbind=node.resolveBinding();if(bindinstanceofIVariableBinding){IVariableBindingibind=(IVariableBinding)bind;if(ibind.isField()){IFieldcallee=(IField)ibind.getJavaElement();if(callee!=null&&GlobalData.projNames.contains(callee.getPath().segment(0))){fCallees.add(GetNames.getFieldName(callee));}}}returntrue;}");
}
}
