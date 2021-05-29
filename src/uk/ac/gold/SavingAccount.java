/**
 * A programme to create a savings account and set account ID and balance. 
 * The programme supports deposits to and withdrawals from the account as well as the addition of interest.
 */
package uk.ac.gold;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * @author mpeev001
 *
 */
public class SavingAccount extends Account {
	
	// Constructor
	
	/**
	 * Constructor
	 * @param accountID The account ID.
	 * @param balance Account balance.
	 */
	public SavingAccount(String accountID, double balance) { // Create a new savings account with the specified account ID and balance
		super(accountID, balance); // call Account (super) constructor
		ArrayList<Account> accounts = Bank.getListOfAccounts(); // create to a copy of the bank's list of accounts
		
		BigDecimal accountBalance = new BigDecimal(Double.toString(balance)); // set account balance to the specified balance
		BigDecimal minInitialDeposit = new BigDecimal(Double.toString(10.0)); // set the minimum initial deposit to 10.0
		BigDecimal depositBonusAmount = new BigDecimal(Double.toString(1000.0)); // set the deposit amount that receives a bonus to 1000.0
		BigDecimal bonusAmount = new BigDecimal(Double.toString(10.0)); // set the bonus amount to 10.0
		
		try {  // try to set the opening balance to the specified amount
			if (accountBalance.compareTo(minInitialDeposit) == -1) { // if the account balance is less than the minimum initial deposit
			     throw new IllegalArgumentException("The opening balance should be at least £" + minInitialDeposit); // set a balance less than the minimum IllegalArgumentException exception 
			} 
			accounts.add(this); // add this account to the list of accounts
			Bank.setListOfAccounts(accounts); // set the bank's list of accounts to the updated list of accounts
			System.out.printf("Opening balance: %.2f\n", accountBalance); // output opening balance message
			super.setBalance(accountBalance.doubleValue()); // set the account balance to the opening balance
			if(accountBalance.compareTo(depositBonusAmount) >= 0) { // if the account balance is larger than or equal to the bonus amount
				System.out.printf("Bonus: %.2f\n", bonusAmount); // output bonus message
				super.setBalance(super.getBalance().add(bonusAmount).doubleValue()); // add the bonus amount to the account balance
			}
		} catch(IllegalArgumentException e) { // catch a balance less than the minimum IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw a balance less than the minimum IllegalArgumentException exception 
		}
	}
	
	/**
	 * Withdraw a specified amount
	 * @param amount The amount to withdraw.
	 * @return <code>true</code> if withdrawal is successful; <code>false</code> if there are insufficient funds for the transaction;
	 */
	@Override
	public boolean withdraw(double amount) {  // withdraw the specified amount from the account
		BigDecimal fee = new BigDecimal(Double.toString(3)); // set fee to 3.0
		BigDecimal withdrawAmount = new BigDecimal(Double.toString(amount)); // set the amount to withdraw to the amount specified
		BigDecimal minBalance = new BigDecimal(Double.toString(10)); // set the minimum balance to 10.0
		BigDecimal totalAmount = withdrawAmount.add(fee); // set total amount to the amount to withdraw plus the fee
		BigDecimal zero = new BigDecimal(Double.toString(0)); // set zero
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					try { // try to withdraw the specified amount
						if(withdrawAmount.compareTo(zero) == -1) {
							throw new IllegalArgumentException("The withdrawal amount should not be negative."); // set a negative withdrawal IllegalArgumentException exception 
						}
						try { // try to withdraw the specified amount
							if ((super.getBalance().subtract(minBalance)).compareTo(totalAmount) == -1) { // if the available balance is less than the total amount to withdraw
								BigDecimal maxWithdrawal = super.getBalance().subtract(minBalance.add(fee)); // set max withdrawal amount to the account balance minus the minimum balance minus the withdrawal fee
								if(maxWithdrawal.compareTo(zero) == -1) { // if the max withdrawal is negative
									maxWithdrawal = zero; // set max withdrawal to zero
								}
							    throw new IllegalArgumentException("You have insufficient funds for this transaction.\nThe maximum you can withdraw today is £" + maxWithdrawal); // set insufficient funds IllegalArgumentException exception 
							}
							System.out.printf("Withdrawal: %.2f\n", withdrawAmount); // output withdraw message
							super.setBalance((super.getBalance().subtract(withdrawAmount)).doubleValue()); // subtract the amount to withdraw from the account balance
							System.out.printf("Fee: %.2f\n", fee); // output fee message
							super.setBalance((super.getBalance().subtract(fee)).doubleValue()); // subtract the fee amount from the account balance
							return true; // the withdrawal is successful, return true
						} catch(IllegalArgumentException e) { // catch insufficient funds IllegalArgumentException exception 
							System.out.println(e.getMessage()); // throw insufficient funds IllegalArgumentException exception
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
	public void deposit(double amount) { // deposit the specified amount to the account
		BigDecimal depositAmount = new BigDecimal(Double.toString(amount)); // set the deposit amount to the specified amount
		BigDecimal zero = new BigDecimal(Double.toString(0)); // set zero
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					try { //try to deposit the specified amount
						if(depositAmount.compareTo(zero) == -1) { // if the deposit amount is negative
						    throw new IllegalArgumentException("The deposit amount should not be negative."); // set a negative deposit IllegalArgumentException exception
						}
						System.out.printf("Deposit: %.2f\n", depositAmount); // output deposit message
						super.setBalance((super.getBalance().add(depositAmount)).doubleValue()); // add the deposit amount to the account balance
					} catch(IllegalArgumentException e) { // catch a negative deposit IllegalArgumentException exception 
						System.out.println(e.getMessage()); // throw a negative deposit IllegalArgumentException exception 
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
	 * Add interest at the specified rate
	 * @param rate Interest rate.
	 * @return The interest paid.
	 */
	public double addInterest(double rate) { // add interest at the specified rate (%)
		BigDecimal interestPercent = new BigDecimal(Double.toString(rate)); // set interestPercent to the percent value of the rate
		BigDecimal oneHundred = new BigDecimal(Double.toString(100)); // set 100.0
		BigDecimal zero = new BigDecimal(Double.toString(0)); // set zero
		BigDecimal interestRate; // define interest rate
		BigDecimal interest; // define interest
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					try { // try to add interest at the specified rate
						if(interestPercent.compareTo(zero) == -1|| // if the specified interest rate is less than zero or more than 100
						   interestPercent.compareTo(oneHundred) == 1) {
							 throw new IllegalArgumentException("Please provide a rate in percent"); // set a rate not in percent IllegalArgumentException exception
						}
						interestRate = interestPercent.divide(oneHundred); // set interest rate to the decimal value of the rate
						interest = super.getBalance().multiply(interestRate); // calculate the interest
						System.out.printf("Interest: %.2f\n", interest); // output interest message
						super.setBalance((super.getBalance().add(interest)).doubleValue()); // add interest to the account balance
						return interest.doubleValue(); // return the paid interest amount
					} catch(IllegalArgumentException e) { // catch a rate not in percent IllegalArgumentException exception
						System.out.println(e.getMessage()); // throw a rate not in percent IllegalArgumentException exception
					}
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + this.getAccountID()); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		return 0; // the addition of interest is unsuccessful, return 0.0
	}
	
	/**
	 * Generate account information
	 * @return str Account information.
 	 */
	public String toString() { // generate account information
		boolean accountIDInAccounts = false; // set the account ID as unknown
		try { // try to find the account ID in the list of accounts
			for(int i = 0; i < Bank.getListOfAccounts().size(); i++) { // for each account from the list of accounts
				if((Bank.getListOfAccounts().get(i).getAccountID()).equals(this.getAccountID())) { // if the account ID matches the account ID specified
					accountIDInAccounts = true; // set the account ID as known
					String str = super.toString().substring(0, super.toString().length() - 1);
					str += ", Interest= " + Bank.getSavingsInterestRate() +  "%" + "]"; // generate account information, including account ID, balance and the Bank's interest rate 
					return str;
				}
			}
			if(!accountIDInAccounts) { // if the account ID is unknown
			    throw new IllegalArgumentException("Transaction not authorised. Unknown account ID: " + this.getAccountID()); // set unknown account ID IllegalArgumentException exception
			}
		} catch(IllegalArgumentException e) { // catch unknown account ID IllegalArgumentException exception 
			System.out.println(e.getMessage()); // throw unknown account ID IllegalArgumentException exception 
		}
		return "The account does not exist.";
	}
	
}
