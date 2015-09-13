package edu.ut.ece.bank.tests.junit3;

import junit.framework.TestCase;
import edu.ut.ece.bank.Account;
import edu.ut.ece.bank.BankInfo;
import edu.ut.ece.bank.SuperAccount;

/*
 * Tests for the bank account program
 */
public class BankTests extends TestCase {
	public void test1() {
		BankInfo.setLocn("XXX");
	}

	public void test2() {
		assertTrue(BankInfo.getName() != null);
	}

	public void test3() {
		Account acnt = new Account("account1");
		acnt.withdraw(20);
		assertTrue(acnt.checking == 80);
	}

	public void test4() {
		SuperAccount acnt = new SuperAccount("account2");
		assertTrue(acnt.getAmnt() >= 100);
	}

	public void test5() {
		Account acnt1 = new Account("account3");
		SuperAccount acnt2 = new SuperAccount("account4");
		double amount = acnt1.withdraw(80);
		acnt2.deposit(amount);
		assertTrue(acnt2.checking >= 180);
	}
}