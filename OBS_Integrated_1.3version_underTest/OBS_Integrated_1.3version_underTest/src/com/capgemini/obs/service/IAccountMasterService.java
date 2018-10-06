package com.capgemini.obs.service;

import com.capgemini.obs.bean.AccountMasterBean;
import com.capgemini.obs.exceptions.BankException;

public interface IAccountMasterService {

	public long addAccountDetails(AccountMasterBean accountMasterBean)
			throws BankException;
	
	public boolean validateAccount(AccountMasterBean accountMasterBean)  throws BankException;
}
