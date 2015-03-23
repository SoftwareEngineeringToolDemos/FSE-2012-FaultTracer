import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tracer.callgraph.parser.Byte2SourceTest;
import tracer.testselection.SelectionTest;
import tracer.tracing.FaultTracerTest;
import tracer.tracing.FaultTracerTest2;
import tracer.utils.FilterCommentsTest;
import catcher.callgraph.core.ClassInstruTest;

@RunWith(Suite.class)
@SuiteClasses({ ClassInstruTest.class, Byte2SourceTest.class,
		SelectionTest.class, FaultTracerTest.class, FaultTracerTest2.class,
		FilterCommentsTest.class })
public class Test {

}
