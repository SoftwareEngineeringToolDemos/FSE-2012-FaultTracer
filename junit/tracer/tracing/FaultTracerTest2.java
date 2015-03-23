package tracer.tracing;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class FaultTracerTest2 {
	public static List<String> selected;

	// test when v1 evolves to v2
	@Test
	public void testSelection1() throws IOException {
		selected = FaultTracer.selection("testInputs/S1Test_log",
				"testInputs/S1-S2.adiff");
		assertEquals(selected.size(), 2);
		assertTrue(selected.contains("ATest.test2:()V"));
		assertTrue(selected.contains("ATest.test3:()V"));
	}

	@Test
	public void testFaultLocalization1() throws IOException {
		double num = FaultTracer.localization("testInputs/S2Test_log",
				"testInputs/S1-S2.adiff", selected);
		assertTrue(num == 1);
	}

	// test when v2 evolves to v1
	@Test
	public void testSelection2() throws IOException {
		List<String> selected = FaultTracer.selection("testInputs/S2Test_log",
				"testInputs/S2-S1.adiff");
		assertEquals(selected.size(), 2);
		assertTrue(selected.contains("ATest.test2:()V"));
		assertTrue(selected.contains("ATest.test3:()V"));
	}

	@Test
	public void testFaultLocalization2() throws IOException {
		double num = FaultTracer.localization("testInputs/S1Test_log",
				"testInputs/S1-S2.adiff", selected);
		assertTrue(num == 1);
	}

}
