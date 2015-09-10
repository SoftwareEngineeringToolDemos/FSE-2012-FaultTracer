package edu.ut.ece.bank.tests.junit3;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestDriver extends TestCase {
	public static Test suite() {

		TestSuite suite = new TestSuite("Bank Account Tests");
		suite.addTestSuite(BankTests.class);
		return suite;
	}
}