package tracer.testselection;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tracer.tracing.FaultTracer;

public class SelectionTest {
	@Test
	public void testGetReceiver1() {
		String oracle = "de.susebox.java.util.TokenizerProperty";
		String in = "<class de.susebox.java.util.TokenizerProperty>de.susebox.java.util.TokenizerProperty.setType:(I)V";
		String out = FaultTracer.getReceiver(in);
		assertTrue(oracle.equals(out));
	}
	@Test
	public void testGetReceiver2() {
		String oracle = "B";
		String in = "<class B>B.get:()I";
		String out = FaultTracer.getReceiver(in);
		assertTrue(oracle.equals(out));
	}
	@Test
	public void testGetReceiver3() {
		String oracle = "org.apache.xml.security.Init$FuncHereLoader";
		String in = "<class org.apache.xml.security.Init$FuncHereLoader>org.apache.xml.security.Init$FuncHereLoader.getName:()QString;";
		String out = FaultTracer.getReceiver(in);
		assertTrue(oracle.equals(out));
	}
	@Test
	public void testGetReceiver4() {
		String oracle = "org.apache.xml.security.c14n.helper.NamespaceSearcher";
		String in = "<class org.apache.xml.security.c14n.helper.NamespaceSearcher>org.apache.xml.security.c14n.helper.NamespaceSearcher.visible:(QNode;)Z";
		String out = FaultTracer.getReceiver(in);
		assertTrue(oracle.equals(out));
	}

}
