package com.capgemini.obs.service;

import java.time.LocalDate;
import java.util.List;

import com.capgemini.obs.bean.TransactionsBean;
import com.capgemini.obs.dao.ITransactionDAO;
import com.capgemini.obs.dao.TransactionDAO;
import com.capgemini.obs.exceptions.BankException;

public class TransactionService implements ITransactionService {

	ITransactionDAO transactiondao;

	@Override
	public List<TransactionsBean> viewDailyTransactionDetails(
			LocalDate dateOfTransaction) throws BankException {
		transactiondao = new TransactionDAO();
		List<TransactionsBean> bean = null;

		bean = transactiondao.viewDailyTransactionDetails(dateOfTransaction);
		return bean;
	}

	@Override
	public List<TransactionsBean> viewMyTransactionDetails(LocalDate stdate,
			LocalDate endate) throws BankException {
		transactiondao = new TransactionDAO();
		List<TransactionsBean> bean = null;

		bean = transactiondao.viewMyTransactionDetails(stdate,endate);
		return bean;
	}
}
