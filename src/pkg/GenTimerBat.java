package pkg;

public class GenTimerBat {

	public static void main(String[] args) {
		String[] subs = { "Jtopas", "Xmlsec", "Jmeter", "Ant" };
		int[] vers = { 4, 4, 6, 9 };
		for (int i = 0; i < subs.length; i++) {
			for (int j = 0; j < vers[i]; j++) {
				for (int k = 0; k < 1; k++) {
					System.out.println("cd " + subs[i] + j + ".0");
					System.out
							.println("call ant -f FaultTracer.xml collectStatementCoverage");
					System.out.println("cd ..");
				}
			}
		}
	}
}
