package com.capgemini.obs.dao;

import com.capgemini.obs.bean.AccountMasterBean;
import com.capgemini.obs.exceptions.BankException;

public interface IAccountMasterDAO {

	public long addAccountDetails(AccountMasterBean accountMasterBean)
			throws BankException;

}
