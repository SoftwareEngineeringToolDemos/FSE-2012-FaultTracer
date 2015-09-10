package edu.ut.ece.bank;

/*
 * Store basic bank information
 */
public class BankInfo {
	// changed field
	public static String bName = "NName"; 
	// changed field
	public static String bLocation = "NRoad"; 

	// get bank name
	public static String getName() {
		return bName;
	}

	// get bank location
	public static void setLocn(String l) {
		bLocation = l;
	}
}
