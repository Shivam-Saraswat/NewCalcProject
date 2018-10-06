package com.capgemini.obs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.obs.bean.FundTransferBean;
import com.capgemini.obs.bean.PayeeBean;
import com.capgemini.obs.bean.TransactionsBean;
import com.capgemini.obs.dao.FundTransferDAO;
import com.capgemini.obs.dao.IFundTransferDAO;
import com.capgemini.obs.exceptions.BankException;

public class FundTransferService implements IFundTransferService{

	IFundTransferDAO fundTransferDao = null;
	FundTransferBean fundTransferBean = null;
	PayeeBean payee = null;
	TransactionsBean transactionBean = null;
	@Override
	public FundTransferBean TransferSameAccount(FundTransferBean fundTransferBean) throws BankException {
		fundTransferDao = new FundTransferDAO();
		fundTransferBean = fundTransferDao.TransferSameAccount(fundTransferBean);  
		return fundTransferBean;
	}

	
	@Override
	public int checkPayee(PayeeBean payeeBean) throws BankException {
		fundTransferDao = new FundTransferDAO();
		int result = fundTransferDao.checkPayee(payeeBean);
		return result;
	}


	@Override
	public PayeeBean addPayee(PayeeBean payeeBean) throws BankException {
		fundTransferDao = new FundTransferDAO();
		payee = fundTransferDao.addPayee(payeeBean);
		return payee;
	}

	
	@Override
	public FundTransferBean payPayee(FundTransferBean fundTransfer) throws BankException {
		
		fundTransferDao = new FundTransferDAO();
		fundTransferBean = fundTransferDao.payPayee(fundTransfer);
		return fundTransferBean;
	}


	@Override
	public boolean checkTransactionPwd(long account_Id,String tranPwd) throws BankException {
		fundTransferDao = new FundTransferDAO();
		boolean checkPwd = fundTransferDao.checkTransactionPwd(account_Id,tranPwd);
		return checkPwd;
	}


	@Override
	public boolean checkAccountBalance(long account_ID, int other_bank_amount) throws BankException {
		fundTransferDao = new FundTransferDAO();
		boolean checkBal = fundTransferDao.checkAccountBalance(account_ID,other_bank_amount);
		return checkBal;
	}

	
	@Override
	public boolean checkMaxFunds(long account_ID, int other_bank_amount) throws BankException {
		fundTransferDao = new FundTransferDAO();
		boolean checkLimit = fundTransferDao.checkMaxFunds(account_ID,other_bank_amount);
		return checkLimit;
	}


	@Override
	public boolean updateTransaction(long account_ID,long payee_Account_Id,String tran_desc,int other_bank_amount) throws BankException {
		fundTransferDao = new FundTransferDAO();
		boolean update = fundTransferDao.updateTransaction(account_ID,payee_Account_Id,tran_desc,other_bank_amount);
		return update;
	}
	
	public void validateDetails(FundTransferBean bean) throws BankException
	{
		List<String> validationErrors = new ArrayList<String>();

		//Validating Account Id
		if(!(isValidAccountId(bean.getAccountId()))) {
			validationErrors.add("\n AccountId should be in digits and should have minimum of 4 digits\n");
		}
		//Validating Payee Account Id
		if(!(isValidAccountId(bean.getPayeeAccountId()))){
			validationErrors.add("\n PayeeAccountId should be in digits and should have minimum of 4 digits\n");
		}
		//Validating transfer Amount
		if(!(isValidAmount(bean.getTransferAmount()))){
			validationErrors.add("\n Amount Should be a positive Number \n" );
		}
		
		if(!validationErrors.isEmpty())
			throw new BankException(validationErrors +"");
	}

	public boolean isValidAccountId(long l){
		String str = Long.toString(l);
		Pattern namePattern=Pattern.compile("^[0-9]{4,}$");
		Matcher nameMatcher=namePattern.matcher(str);
		return nameMatcher.matches();
	}
	
	public boolean isValidAmount(int amount){
		return (amount>0);
	}
	
}
