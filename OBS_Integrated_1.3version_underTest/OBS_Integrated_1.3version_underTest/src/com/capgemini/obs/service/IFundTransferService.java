package com.capgemini.obs.service;

import com.capgemini.obs.bean.FundTransferBean;
import com.capgemini.obs.bean.PayeeBean;
import com.capgemini.obs.exceptions.BankException;

public interface IFundTransferService {
	

	public int checkPayee(PayeeBean payeeBean) throws BankException;
	public FundTransferBean TransferSameAccount(FundTransferBean fundTransfer) throws BankException;
	public PayeeBean addPayee(PayeeBean payeeBean) throws BankException;
	public FundTransferBean payPayee(FundTransferBean fundTransfer) throws BankException;
	public boolean checkTransactionPwd(long account_Id,String tranPwd) throws BankException;
	public boolean checkAccountBalance(long account_ID, int other_bank_amount) throws BankException;
	public boolean updateTransaction(long account_ID, long payee_Account_Id, String tran_desc, int other_bank_amount) throws BankException;
	public boolean checkMaxFunds(long account_ID, int other_bank_amount) throws BankException;
	
	
}
