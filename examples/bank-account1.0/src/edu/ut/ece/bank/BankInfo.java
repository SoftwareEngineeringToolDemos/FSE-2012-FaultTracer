package edu.ut.ece.bank;

/*
 * Store basic bank information
 */
public class BankInfo {
	public static String bName = "OName";
	public static String bLocation = "ORoad";

	// get bank name
	public static String getName() {
		return bName;
	}

	// get bank location
	public static void setLocn(String l) {
		bLocation = l;
	}
}
