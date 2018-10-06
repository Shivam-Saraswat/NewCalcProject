package com.capgemini.obs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.obs.bean.AccountMasterBean;
import com.capgemini.obs.bean.UserBean;
import com.capgemini.obs.dao.AccountMasterDAO;
import com.capgemini.obs.dao.CustomerDAO;
import com.capgemini.obs.dao.IAccountMasterDAO;
import com.capgemini.obs.dao.ICustomerDAO;
import com.capgemini.obs.dao.IUserDAO;
import com.capgemini.obs.dao.UserDAO;
import com.capgemini.obs.exceptions.BankException;

public class AccountMasterService implements IAccountMasterService{

	IAccountMasterDAO accountMasterDao;
	
	@Override
	public long addAccountDetails(AccountMasterBean accountMasterBean) throws BankException {
		
		long accId=0;		
		accountMasterDao = new AccountMasterDAO();

		try {
			accId = accountMasterDao.addAccountDetails(accountMasterBean);

		} catch (BankException e) {
			
			e.printStackTrace();
		}		
		
		return accId;
	}

	public boolean validateAccount(AccountMasterBean accountMasterBean)  throws BankException {
		
		List<String> validationErrors = new ArrayList<String>();

		// Validating Opening Balance
		if (!(isValidAccountBalance(accountMasterBean.getAccountBalance()))) {
			validationErrors
					.add("\n Please keep a minimum balance of Rs.2000 \n");
		}

		if (!validationErrors.isEmpty()) {
			throw new BankException(validationErrors + "");
		} else
			return true;
		}
		
		public boolean isValidAccountBalance(int i) {
			return (i >= 2000);
		}

}
