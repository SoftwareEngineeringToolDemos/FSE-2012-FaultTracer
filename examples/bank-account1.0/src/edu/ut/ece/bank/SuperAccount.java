package edu.ut.ece.bank;

/*
 * Store super account information
 */
public class SuperAccount extends Account {
	public SuperAccount(String a) {
		super(a);
	}
	// get account balance amount
	public double getAmnt() {
		return checking;
	}
}
