package edu.ut.ece.bank;

/*
 * Store super account information
 */
public class SuperAccount extends Account {
	// added field
	public double checking = 110; 

	public SuperAccount(String a) {
		super(a);
	}
	
	// get account balance amount
	public double getAmnt() {
		return checking;
	}

	// added method: deposit to the super account
	public void deposit(double v) { 
		checking = checking - v; //fault!!! Should be:checking=checking+v;
		if (v >= 50) {
			checking = checking + 1;
		}
	}
}
