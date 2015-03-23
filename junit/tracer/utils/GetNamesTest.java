package tracer.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class GetNamesTest {

	@Test
	public void test1(){
		String oracle="org.apache.jmeter.save.old.xml.XmlHandler.endElement:(QString;QString;QString;)V";
		String in="org.apache.jmeter.save.old.xml.XmlHandler.endElement:(Qjava.lang.String;Qjava.lang.String;Qjava.lang.String;)V";
		String out=GetNames.getLCName(in);
		assertEquals(out,oracle);
	}
}
