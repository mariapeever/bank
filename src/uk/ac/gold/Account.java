/**
 * A programme to create an account with specified Account ID 
 * and balance and print information about the account. The programme includes abstract 
 * withdraw and deposit methods.
 */
package uk.ac.gold;

import java.math.BigDecimal;

/**
 * @author mpeev001
 */
public abstract class Account {
	
	// fields
	
	private String accountID;
	private double balance;
	
	// getters and setters
	
	/**
	 * Get the account ID
	 * @return accountID The account ID.
	 */
	public String getAccountID() { // get account ID
		String accountID = this.accountID;
		return accountID;
	}

	/**
	 * Set the account ID
	 * @param accountID The account ID to set.
	 */
	public void setAccountID(String accountID) { // set account ID
		this.accountID = accountID;
	}

	/**
	 * Get the balance
	 * @return balance The balance.
	 */
	public BigDecimal getBalance() { // get balance
		BigDecimal balance = new BigDecimal(Double.toString(this.balance));
		return balance;
	}

	/**
	 * Set the balance
	 * @param balance The balance to set.
	 */
	public void setBalance(double balance) { // set balance
		this.balance = balance;
	}
	
	// constructor
	
	/**
	 * Constructor
	 * @param accountID The account ID.
	 * @param balance The account balance.
	 */
	public Account(String accountID, double balance) {
		this.setAccountID(accountID); // set account ID
	}
	
	// methods
	
	/**
	 * @param amount Amount to withdraw.
	 * @return <code>true</code> if withdrawal is successful; <code>false</code> otherwise
	 */
	public abstract boolean withdraw(double amount); // withdraw the specified amount from the account
	
	/**
	 * @param amount Amount to deposit.
	 */
	public abstract void deposit(double amount); // deposit the specified amount to the account
	
	/**
	 * Generate account information
	 * @return str Account information.
 	 */
	public String toString() { 
		String str = "Account [accountID= " + getAccountID() + ", Balance= " + getBalance() + "]"; // account information
		return str;
	}

}
