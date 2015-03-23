package pkg1;
import pkg1.ClassUnderTest;
import junit.framework.TestCase;


public class Tests extends TestCase{

	public void test1(){
		assertEquals(2,ClassUnderTest.mid(2, 2, 3));
	}
	public void test2(){
		assertEquals(2,ClassUnderTest.mid(1, 2, 3));
	}
	public void test3(){
		assertEquals(3,ClassUnderTest.mid(4, 5, 3));
	}
}
