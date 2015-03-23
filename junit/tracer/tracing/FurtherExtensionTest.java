package tracer.tracing;

import org.junit.Test;

public class FurtherExtensionTest {
	@Test
	public void testSort() {
String[] names={"a","b","c","d","e","f"};
double[] values={3,2,5,2,0,1};
int[] heuValues={1,0,1,1,1,1};
Tarantula.sort(names, values,heuValues, 0, 5);
for(int i=0;i<names.length;i++){
	System.out.println(names[i]+": "+values[i]+"-"+heuValues[i]);
}
	}
}
