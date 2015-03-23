package tracer.tracing;

public class Sample {

	public static void main(String[] args) {
run2();
	}

	public static void run1() {
		double sumc = 0;
		double sumf = 0;
		double[] chianti = { 0, 15, 56, 92, 61, 74, 101, 123, 50, 201, 446,
				133, 512, 229 };
		double[] faultracer = { 0, 15, 56, 92, 53, 67, 101, 123, 50, 203, 446,
				133, 507, 229 };
		double[] all = { 95, 126, 128, 106, 92, 94, 112, 137, 219, 219, 521,
				557, 559, 877 };
		for (int i = 0; i < 14; i++) {
			sumc += chianti[i] / all[i];
			sumf += faultracer[i] / all[i];
			System.out.println(toS(chianti[i] * 100 / all[i]) + "&"
					+ toS(faultracer[i] * 100 / all[i]) + "&0&0&0&0");
		}
		System.out.println(sumc / 14 + "\t" + sumf / 14);
	}

	public static void run2() {
		double[] chianti = { 6.466666667, 1.050847458, 71.53521127,
				40.47945205, 47.5625, 11.93069307, 93.41085271, 3.318471338,
				309.1492537, 10.32987552, 3.821428571, 330.8098859, 2.443127962 };
		double[] faultracer = { 3.866666667, 1.050847458, 23.97183099,
				13.63076923, 24.19298246, 9.584158416, 45.13492063,
				3.159235669, 101.9902439, 9.018672199, 3.564285714,
				197.2843511, 2.424170616 };
		double[] all = {  62, 620, 725, 272, 1238, 2085, 2930, 73, 5693,
				592, 60, 4953, 54 };
		System.out.println("0.0\t 0.0");
		for(int i=0;i<13;i++){
			System.out.println(chianti[i]/all[i]+"\t"+faultracer[i]/all[i]);
		}

	}

	public static String toS(double d) {
		String in = Double.toString(d);
		int s = in.indexOf(".");
		if (in.equals("0.0"))
			return "0.00";
		return in.substring(0, s + 3);
	}
}
