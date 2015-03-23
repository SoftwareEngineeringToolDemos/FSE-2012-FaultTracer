package tracer.callgraph.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class Byte2SourceTest {
	@Test
	public void test1() {
		String oracle = "de.susebox.java.lang.TestExceptionList.assertTrue:(QString;Z)V";
		String input = "de/susebox/java/lang/TestExceptionList:assertTrue:(Ljava/lang/String;Z)V";
		String output = Byte2Source.transform(input);
		assertEquals(oracle, output);
	}

	@Test
	public void test2() {
		String oracle = "<FR>de.susebox.java.util.AbstractTokenizer.isSequenceCommentOrString:(I)QTokenizerProperty;";
		String input = "<FR>de/susebox/java/util/AbstractTokenizer:isSequenceCommentOrString:(I)Lde/susebox/java/util/TokenizerProperty;";
		String output = Byte2Source.transform(input);
		assertEquals(oracle, output);
	}
	@Test
	public void test3() {
		String oracle = "<FW>de.susebox.java.util.TokenizerProperty.setCompanion:(QObject;)V-de.susebox.java.util.TokenizerProperty._companion:QObject;";
		String input = "<FW>de/susebox/java/util/TokenizerProperty:setCompanion:(LObject;)V-de/susebox/java/util/TokenizerProperty:_companion:LObject;";
		String output = Byte2Source.run(input);
		assertEquals(oracle, output);
	}
	@Test
	public void test4() {
		String oracle = "<class de.susebox.java.util.InputStreamTokenizer>de.susebox.java.util.InputStreamTokenizer.read:([CII)I";
		String input = "<class de.susebox.java.util.InputStreamTokenizer>de/susebox/java/util/InputStreamTokenizer:read:([CII)I";
		String output = Byte2Source.run(input);
		assertEquals(oracle, output);
	}

}
