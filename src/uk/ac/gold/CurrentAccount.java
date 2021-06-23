/**
 * A programme to create a current account and set account ID, balance and number of checks used. 
 * The programme supports deposits to the account, withdrawals (ATM/Electronic or by check) and 
 * an option to reset the number of checks used.
 */
package uk.ac.gold;

// import BigDecimal, BigInteger and ArrayList

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author mpeev001
 */
class CurrentAccount extends Account {
	
	// fields 
	
	private int numberOfChecksUsed;

	// getters and setters 
	
	/**
	 * Get the number of checks used
	 * @return The number of checks used.
	 */
	int getNumberOfChecksUsed() { // get the number of checks used
		return this.numberOfChecksUsed;
	}

	/**
	 * Set the number of checks used
	 * @param numberOfChecksUsed The number of checks used to set.
	 */
	void setNumberOfChecksUsed(int numberOfChecksUsed) { // set the number of checks used
		this.numberOfChecksUsed = numberOfChecksUsed;
	}
	
	// constructor
	
	/**
	 * Constructor
	 * @param accountID The account ID.
	 * @param balance The account balance.
	 * @param checks The number of checks used.
	 */
	CurrentAccount(String accountID, double balance, int checks) { // constructor to create a new current account with and ID accountID, the specified balance and the specified number of checks used
		super(accountID, balance); // call Account (super) constructor
		ArrayList<Account> accounts = Bank.getListOfAccounts(); // make a copy of the bank's list of accounts
		BigDecimal accountBalance = new BigDecimal(Double.toString(balance)); // convert balance to a BigDecimal
		BigDecimal zero = new BigDecimal(Double.toString(0)); // set zero
		try {  // try to set the account balance to the specified balance
			if (accountBalance.compareTo(zero) == -1) { // if the specified balance is negative
			     throw new IllegalArgumentException("The account balance should not be negative."); // set a negative balance IllegalArgumentException exception 
			}
			accounts.add(this); // add this account to the list of accounts
			Bank.setListOfAccounts(accounts); // set the bank's list of accounts to the updated accounts list
			System.out.printf("Opening balance: %.2f\n", accountBalance); // Output opening balance message
			super.setBalance(balance); // set the account balance to the specified balance
		} catch(IllegalArgumentException e) { // catch a negative balance IllegalArgumentException
			System.out.println(e.getMessage()); // throw a negative balance IllegalArgumentException 
		}
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(accountID)) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					try { // try to set the number of checks used to the specified number of checks
						if (checks < 0) { // if the specified number of checks is negative
						     throw new IllegalArgumentException("The number of checks used should not be negative."); // set a negative checks number IllegalArgumentException exception 
						}
						this.setNumberOfChecksUsed(checks); // set the number of checks used to the number of checks specified
					} catch(IllegalArgumentException e) { // catch a negative number of checks IllegalArgumentException
						System.out.println(e.getMessage()); // throw a negative number of checks IllegalArgumentException
					}
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("The number of checks used cannot be set. Unknown account ID: " + accountID); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		
	}
	
	// methods
	
	/**
	 * Withdraw a specified amount
	 * @param amount The amount to withdraw.
	 * @return <code>true</code> if withdrawal is successful; <code>false</code> if there are insufficient funds for the transaction;
	 */
	@Override
	boolean withdraw(double amount) { // withdraw the specified amount from the account
		BigDecimal fee = new BigDecimal(Double.toString(1)); // set fee to 1.0
		BigDecimal withdrawAmount = new BigDecimal(Double.toString(amount)); // set withdraw amount to the amount specified
		BigDecimal totalAmount = withdrawAmount.add(fee); // set the total amount to the amount specified plus the fee
		BigDecimal zero = new BigDecimal(Double.toString(0)); // set zero
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					try { // try to withdraw the specified amount
						if(withdrawAmount.compareTo(zero) == -1) { // if the specified amount is negative
							throw new IllegalArgumentException("The withdrawal amount should not be negative."); // set a negative withdrawal IllegalArgumentException exception 
						}
						try { // try to withdraw the specified amount
							if(super.getBalance().compareTo(totalAmount) == -1) { // if the account balance is less than the total amount after fees
								BigDecimal maxWithdrawal = super.getBalance().subtract(fee); // set max withdrawal amount to the the balance minus the fee
								if(maxWithdrawal.compareTo(zero) == -1) { // if the max withdrawal is negative
									maxWithdrawal = zero; // set the max withdrawal amount to zero
								}
								throw new IllegalArgumentException("You have insufficient funds for this transaction.\nThe maximum you can withdraw today is £" + maxWithdrawal); // set an insufficient funds IllegalArgumentException exception 
							} 
							System.out.printf("Withdrawal: %.2f\n", withdrawAmount); // output withdraw message
							super.setBalance((super.getBalance().subtract(withdrawAmount)).doubleValue()); // subtract the specified amount from the account balance
							System.out.printf("Fee: %.2f\n", fee); // output fee message
							super.setBalance((super.getBalance().subtract(fee)).doubleValue()); // subtract the fee from the account balance 
							return true; // the withdrawal is successful, return true
						} catch(IllegalArgumentException e) { // catch an insufficient funds IllegalArgumentException exception 
							System.out.println(e.getMessage()); // throw an insufficient funds IllegalArgumentException exception
						}
					} catch(IllegalArgumentException e) { // catch a negative withdrawal IllegalArgumentException exception
						System.out.println(e.getMessage()); // throw a negative withdrawal IllegalArgumentException exception
					}
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + this.getAccountID()); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		return false; // the withdrawal is unsuccessful, return false
	}
	
	/**
	 * Deposit a specified amount to the account
	 * @param amount The amount to deposit.
	 */
	@Override
	void deposit(double amount) { // deposit the specified amount to the account
		BigDecimal fee = new BigDecimal(Double.toString(1)); // set the fee to 1.0
		BigDecimal depositAmount = new BigDecimal(Double.toString(amount)); // set the deposit amount to the amount specified
		BigDecimal minDeposit = new BigDecimal(Double.toString(1)); // set the minimum deposit to 1.0
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					try { //try to deposit the specified amount
						if(depositAmount.compareTo(minDeposit) == -1) { // if the deposit amount is less than the minimum deposit amount allowed
						    throw new IllegalArgumentException("The minimum amount that can be deposited is £" + minDeposit); // set deposit less than the minimum IllegalArgumentException exception
						}
						System.out.printf("Deposit: %.2f\n", depositAmount); // output deposit message
						super.setBalance((super.getBalance().add(depositAmount)).doubleValue()); // add the amount to the account balance
						System.out.printf("Fee: %.2f\n", fee); // output fee message
						super.setBalance((super.getBalance().subtract(fee)).doubleValue()); // subtract the fee from the account balance
					} catch(IllegalArgumentException e) { // catch deposit less than the minimum IllegalArgumentException exception 
						System.out.println(e.getMessage()); // deposit less than the minimum IllegalArgumentException exception 
					}
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + this.getAccountID()); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
	}
	
	/**
	 * Reset the number of checks used to 0
	 */
	void resetChecksUsed() { // reset the number of checks used to 0
		this.setNumberOfChecksUsed(0); // set the number of checks used to 0
	}
	
	/**
	 * Withdraw a specified amount using check
	 * @param amount The amount to withdraw.
	 * @return <code>true</code> if withdrawal is successful; <code>false</code> if there are insufficient funds for the transaction;
	 */
	boolean withdrawUsingCheck(double amount) { // withdraw the specified amount using check 
		BigDecimal fee = new BigDecimal(Double.toString(2)); // set the fee to 2.0
		BigInteger freeChecks = new BigInteger(Integer.toString(3)); // set the number of free checks to 3
		BigDecimal withdrawAmount = new BigDecimal(Double.toString(amount)); // set the withdraw amount to the specified amount
		BigDecimal overdraft = new BigDecimal(Double.toString(10)); // set the overdraft amount to 10.0
		BigDecimal availableBalance = super.getBalance().add(overdraft); // set available balance to the account balance plus the overdraft amount
		BigDecimal zero = new BigDecimal(Double.toString(0)); // set zero
		boolean applyFee = (new BigInteger(Integer.toString(this.getNumberOfChecksUsed())).compareTo(freeChecks) >= 0); // set whether to apply a fee to true if the number of checks used is more than the number of free checks; set to false, otherwise 
		if(applyFee) { // if a fee should be applied
			availableBalance = availableBalance.subtract(fee); // set the available balance to the available balance minus the fee
		}
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					try { // check if the withdraw amount is negative
						if(withdrawAmount.compareTo(zero) == -1) { // if the amount to withdraw is negative
							throw new IllegalArgumentException("The withdrawal amount should not be negative."); // set a negative withdrawal IllegalArgumentException exception 
						}
						try { // try to withdraw the specified amount
							if(availableBalance.compareTo(withdrawAmount) == -1) { // if the available balance is less than the amount to withdrawn
								throw new IllegalArgumentException("You have insufficient funds for this transaction.\nThe maximum you can withdraw today is £" + availableBalance); // set insufficient funds IllegalArgumentException exception
							}
							System.out.printf("Withdrawal: %.2f\n", withdrawAmount); // output withdraw message
							super.setBalance((super.getBalance().subtract(withdrawAmount)).doubleValue()); // subtract the withdraw amount from the account balance
							if(applyFee) { // if a fee should be applied
								System.out.printf("Fee: %.2f\n", fee); // output fee message
								super.setBalance((super.getBalance().subtract(fee)).doubleValue()); //subtract the fee from the account balance
							}
							this.setNumberOfChecksUsed(this.getNumberOfChecksUsed() + 1); // increment the number of checks used by 1
							return true; // the withdrawal is successful, return true
						} catch (IllegalArgumentException e) { // catch insufficient funds IllegalArgumentException exception
							System.out.println(e.getMessage()); // throw insufficient funds IllegalArgumentException exception
						}
					} catch (IllegalArgumentException e) { // catch a negative withdrawal IllegalArgumentException exception 
						System.out.println(e.getMessage());  // throw a negative withdrawal IllegalArgumentException exception 
					}
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + this.getAccountID()); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		return false; // the withdrawal is unsuccessful, return false
	}
	
	/**
	 * Output account information, including account ID, balance and number of checks used
	 * @return str Account information.
	 */
	String toString() { // generate account information
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					String str = super.toString().substring(0, super.toString().length() - 1);
					str += ", Number of checks= " + this.getNumberOfChecksUsed() + "]"; // generate account information, including account ID, balance and the Bank's interest rate 
					return str;
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Unknown account ID: " + this.getAccountID()); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		return "The account does not exist.";
	}

}
