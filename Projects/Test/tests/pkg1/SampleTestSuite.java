package pkg1;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SampleTestSuite {

	public static Test suite(){
		TestSuite result=new TestSuite();
		result.addTestSuite(Tests.class);
		return result;
	}
}
