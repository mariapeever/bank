/**
 * A programme to create a bank, set a bank name and savings interest rate, 
 * maintain a list of accounts, deposit to and withdraw an amount from an account, 
 * and transfer a specified amount from one account to another 
 */
package uk.ac.gold; // set package to uk.ac.gold

import java.math.BigDecimal;
import java.util.ArrayList; // import ArrayList

/**
 * @author mpeev001
 */
class Bank {
	
	// fields
	
	private String name;
	private static double savingsInterestRate;
	private static ArrayList<Account> listOfAccounts;
	
	// getters and setters
	
	/**
	 * Get the name of the bank.
	 * @return name Bank name.
	 */
	String getName() { // get bank name
		String name = this.name; 
		return name;
	}

	/**
	 * Set the name of the bank.
	 * @param name Bank name.
	 */
	void setName(String name) { // set bank name
		this.name = name; 
	}

	/**
	 * Get a copy of the list of accounts. 
	 * @return copy A copy of the list of accounts.
	 */
	static ArrayList<Account> getListOfAccounts() { // get list of accounts
		if(listOfAccounts == null) { // if the list of accounts is not initialised
		   listOfAccounts = new ArrayList<>(); // initialise list of accounts
		} 
		ArrayList<Account> copy = new ArrayList<>(listOfAccounts.size()); // create an ArrayList of length equal to that of the list of account ArrayList
		copy.addAll(listOfAccounts); // add all accounts from list of accounts to the copy
		return copy; // return the copy
	}

	/**
	 * Set the list of accounts.
	 * @param accounts The list of accounts.
	 */
	static void setListOfAccounts(ArrayList<Account> accounts) { // set list of accounts 
		ArrayList<Account> copy = new ArrayList<>(accounts.size()); // create an ArrayList of length equal to that of the list of account ArrayList 
		copy.addAll(accounts); // add all accounts from the specified list of accounts to the copy
		listOfAccounts = copy; // set the bank's list of accounts to the copy
	}

	/**
	 * Set the savings interest rate.
	 * @param rate The savings interest rate.
	 */
	static void setSavingsInterestRate(double rate) { // set the bank's savings interest rate
		savingsInterestRate = rate;
	}

	/**
	 * Get the savings interest rate.
	 * @return savingsInterestRate The savings interest rate.
	 */
	static double getSavingsInterestRate() { // get the bank's savings interest rate
		return savingsInterestRate;
	}
	
	// constructor
	
	/**
	 * Constructor
	 * @param name Bank name.
	 * @param rate Savings interest rate.
	 */
	Bank(String name, double rate) {
		this.setName(name);; // set the bank's name to the specified name
		setSavingsInterestRate(rate); // set the bank's savings interest rate to the specified rate
	}
	
	// methods
	
	/**
	 * Deposit a specified amount into the account with the specified account ID.
	 * @param accountID Account ID.
	 * @param amount Amount.
	 * @return <code>true</code> if the deposit is successful; <code>false</code> if the account ID is unknown;
	 */
	boolean deposit(String accountID, double amount) { // deposit the specified amount into the account with the ID specified
		BigDecimal minDeposit = new BigDecimal(Double.toString(1));
		BigDecimal depositAmount = new BigDecimal(Double.toString(amount));
		boolean accountIDInAccounts = false; // set account ID as unknown
		BigDecimal zero = new BigDecimal(Double.toString(0)); // set zero
		try { //try to find the specified accountID in the list of accounts
			for(int i = 0; i < getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((getListOfAccounts().get(i).getAccountID()).equals(accountID)) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set account ID as known
					try { // try to deposit the specified amount into the account with the specified account ID
						if(getListOfAccounts().get(i) instanceof CurrentAccount && // if the account is a current account and the deposit amount is under the minimum deposit amount
						   depositAmount.compareTo(minDeposit) == -1) {
							throw new IllegalArgumentException("The deposit amount should be more than £" + minDeposit + "."); // set deposit less than the minimum IllegalArgumentException exception
						} else if (getListOfAccounts().get(i) instanceof SavingAccount && // if the account is a current account and the deposit amount is under the minimum deposit amount
								  depositAmount.compareTo(zero) == -1 ) { 
							throw new IllegalArgumentException("The deposit amount should not be negative."); // set a negative deposit IllegalArgumentException exception
						}
						getListOfAccounts().get(i).deposit(amount); // deposit the specified amount into the account
						return true;  // the deposit is successful, return true
					} catch(IllegalArgumentException e) { // catch deposit less than the minimum/zero IllegalArgumentException exception 
						System.out.println(e.getMessage()); // throw deposit less than the minimum/zero IllegalArgumentException exception 
					}
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + accountID); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception
		}
		return false; // the deposit is unsuccessful, return false
	}
	
	/**
	 * Withdraw a specified amount from the account with the specified account ID.
	 * @param accountID Account ID.
	 * @param amount Amount.
	 * @return <code>true</code> if the withdrawal is successful; <code>false</code> if the account ID is unknown;
	 */
	boolean withdraw(String accountID, double amount) { // withdraw the specified amount from the account with the specified account ID
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((getListOfAccounts().get(i).getAccountID()).equals(accountID)) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					if(getListOfAccounts().get(i).withdraw(amount)) { // try to withdraw the specified amount from the account
						return true; //withdrawal is successful, return true
					}
					return false; // the withdrawal is unsuccessful, return false
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + accountID); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		return false; // the withdrawal is unsuccessful, return false
	}
	
	/**
	 * Transfer a specified amount from the account with the specified fromAccountID to the account with the specified toAccountID.
	 * @param fromAccountID Account ID to withdraw the specified amount from.
	 * @param toAccountID Account ID to deposit the specified amount to.
	 * @param amount Amount.
	 * @return <code>true</code> if the withdrawal and deposit are successful; <code>false</code> if the withdrawal is unsuccessful or one of the the account IDs is unknown;
	 */
	boolean transfer(String fromAccountID, String toAccountID, double amount) { // transfer the specified amount from the account with ID fromAccountID to the account with ID toAccountID
		int size = getListOfAccounts().size(); // get the number of accounts in the list of accounts
		boolean fromAccountIDInAccounts = false; // set the Account ID to transfer the amount from as unknown
		boolean toAccountIDInAccounts = false; // set the Account ID to transfer the amount to as unknown
		BigDecimal minDeposit = new BigDecimal(Double.toString(1)); // set minimum deposit for current accounts
		BigDecimal transferAmount = new BigDecimal(Double.toString(amount));
		try { //try to find the Account ID to transfer the amount from in List of Accounts
			for(int i = 0; i < size; i++) { // for each account from the list of accounts
				if((getListOfAccounts().get(i).getAccountID()).equals(fromAccountID)) { // if the account ID in the list equals the specified Account ID to transfer the amount from
					fromAccountIDInAccounts = true; // set the account ID to transfer the amount from as known
					try { // try to find the account ID to transfer the amount to in the List of Accounts
						for(int j = 0; j < size; j++) { // for each account from the list of accounts
							if((getListOfAccounts().get(j).getAccountID()).equals(toAccountID)) { // if the account ID in the list equals the Account ID to transfer the amount to
								toAccountIDInAccounts = true; // set the account ID to transfer the amount to as known
								try { // try to transfer the specified amount
									if(getListOfAccounts().get(j) instanceof CurrentAccount && transferAmount.compareTo(minDeposit) == -1) { // if the account to transfer to is a Current Account and the amount is less than the minimum deposit amount allowed
										throw new IllegalArgumentException("You can only transfer amounts over £" + minDeposit); // set deposit less than the minimum IllegalArgumentException exception
									}
									if(this.withdraw(getListOfAccounts().get(i).getAccountID(),amount) && // if the withdrawal and the deposit are successful
									   this.deposit(getListOfAccounts().get(j).getAccountID(),amount)) { 
										return true; // the transfer is successful, return true
									}
								} catch(IllegalArgumentException e) { // catch deposit less than the minimum IllegalArgumentException exception 
									System.out.println(e.getMessage()); // deposit less than the minimum IllegalArgumentException exception 
								}
								return false; // the withdrawal is unsuccessful, return false
							}
						}
						if(!toAccountIDInAccounts) { // if the account ID to transfer to is unknown
						    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID:" + toAccountID); // set unknown account ID IllegalArgumentException exception
						}
					} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
						System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
					}
				}
			}
			if(!fromAccountIDInAccounts) { // if the account ID to transfer from is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + fromAccountID); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		return false; // the transfer is unsuccessful, return false
	}

}
