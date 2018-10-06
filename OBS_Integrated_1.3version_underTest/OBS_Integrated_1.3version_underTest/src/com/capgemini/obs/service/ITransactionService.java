package com.capgemini.obs.service;

import java.time.LocalDate;
import java.util.List;

import com.capgemini.obs.bean.TransactionsBean;
import com.capgemini.obs.exceptions.BankException;

public interface ITransactionService {


	List<TransactionsBean> viewDailyTransactionDetails(LocalDate dateOfTransaction) throws BankException;

	List<TransactionsBean> viewMyTransactionDetails(LocalDate stdate,
			LocalDate endate) throws BankException ;

}
