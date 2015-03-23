package edu.ut.ece.bank;

/*
 * Store standard account information
 */
public class Account extends BankInfo {
	public String account;
	public double checking = 100;
	public double yRate = 0.01;
	public double cRate = 0.001;

	public Account(String a) {
		account = a;
	}

	// get account balance amount
	public double getAmnt() {
		return checking;
	}

	// withdraw from the account
	public double withdraw(double v) {
		if (checking >= v) {
			checking = checking - v;
			return v;
		} else
			return 0;
	}

	// deposit to the account
	public void deposit(double v) {
		checking = checking + v;
	}

	// get interest rate
	double getRate() {
		return cRate;
	}
}
