package tracer.coverage.io;

import java.util.Stack;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import tracer.coverage.core.ECGCoverageListener;

public class Tracer {

	private static Logger logger = Logger.getLogger(Tracer.class);

	private static Tracer trace = null;

	public AtomicBoolean tracingDeacivated = new AtomicBoolean();

	public static Stack<String> call_stack = new Stack<String>();

	private Tracer() {
	}

	public static Tracer getInstance() {
		if (trace == null) {
			trace = new Tracer();
		}
		return trace;
	}

	public void deactivateTrace() {
		tracingDeacivated.set(true);
	}

	public void activateTrace() {
		tracingDeacivated.set(false);
	}

	public void logMethodInfo(String receiver, String mfName) {
		if (tracingDeacivated.get()) {
			logger.debug("Excluding entitiy: " + "<" + receiver + ">" + mfName);
			return;
		}
		logger.debug("Recording entitiy: " + "<" + receiver + ">" + mfName);

		String key = mfName;
		if (receiver.length() > 0) {
			key = "<" + receiver + ">" + key;
		}
		ConcurrentMap<String, Integer> methodMap = ECGCoverageListener.methodCoverageMap
				.get();
		if (!methodMap.containsKey(key)) {
			methodMap.put(key, 1);
		} else
			methodMap.put(key, methodMap.get(key) + 1);
	}

	public void logCallRelationInfo(String caller, String callee) {

		if (tracingDeacivated.get()) {
			logger.debug("Excluding entitiy: " + caller + "-" + callee);
			return;
		}
		logger.debug("Recording entitiy: " + caller + "-" + callee);

		String key = "<CALL>" + caller + "-" + callee;
		ConcurrentMap<String, Integer> methodMap = ECGCoverageListener.methodCoverageMap
				.get();
		if (!methodMap.containsKey(key)) {
			methodMap.put(key, 1);
		} else
			methodMap.put(key, methodMap.get(key) + 1);
	}

	public static void stack_push(String toWrite) {
		logger.debug("push: " + toWrite);
		call_stack.push(toWrite);
	}

	public static String stack_top() {
		if (!call_stack.isEmpty())
			return call_stack.peek();
		else
			return "StartFakeCaller";
	}

	public static void stack_pop() {
		// if (!call_stack.isEmpty())
		logger.debug("pop: " + call_stack.pop());
	}
}
