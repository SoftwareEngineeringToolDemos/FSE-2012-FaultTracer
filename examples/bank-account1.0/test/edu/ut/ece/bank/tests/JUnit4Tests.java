package edu.ut.ece.bank.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ut.ece.bank.Account;
import edu.ut.ece.bank.SuperAccount;

/*
 * Tests for the bank account program
 */
public class JUnit4Tests{
	
	@Test
	public void check1() {
		Account acnt1 = new Account("account3");
		SuperAccount acnt2 = new SuperAccount("account4");
		double amount = acnt1.withdraw(80);
		acnt2.deposit(amount);
		assertTrue(false);
	}
	@Test
	public void check2() {
		Account acnt1 = new Account("account3");
		SuperAccount acnt2 = new SuperAccount("account4");
		double amount = acnt1.withdraw(80);
		acnt2.deposit(amount);
	}
	@Test
	public void check5() {
		Account acnt1 = new Account("account3");
		SuperAccount acnt2 = new SuperAccount("account4");
		double amount = acnt1.withdraw(80);
		acnt2.deposit(amount);
	}
}