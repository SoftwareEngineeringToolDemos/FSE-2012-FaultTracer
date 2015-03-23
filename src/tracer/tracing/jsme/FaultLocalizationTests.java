package tracer.tracing.jsme;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

public class FaultLocalizationTests {

	@Test
	public void testReadSeededFaults() throws IOException {
		HashMap<String, HashSet<String>> fault_tests = FaultLocalization
				.readSeededFaults("G:\\FSE_withLCF\\xmlsec\\SeededFaultLoc23");
		for(String fault:fault_tests.keySet()){
			System.out.println("fault:"+fault);
			for(String test:fault_tests.get(fault)){
				System.out.println("test:"+test);
			}
			
		}
	}
}
