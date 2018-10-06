package com.capgemini.obs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.capgemini.obs.bean.TransactionsBean;
import com.capgemini.obs.exceptions.BankException;
import com.capgemini.obs.util.DBUtil;
import com.capgemini.obs.util.IQueryMapper;

public class TransactionDAO implements ITransactionDAO{

	private static Connection conn;
	public static Logger logger = Logger.getRootLogger();

	@Override
	public List<TransactionsBean> viewDailyTransactionDetails(
			LocalDate dateOfTransaction) throws BankException {
		ResultSet records = null;
		TransactionsBean transactionsbean = null;
		PreparedStatement pstmt = null;
		List<TransactionsBean> transactionlist = new ArrayList<TransactionsBean>();
		conn = DBUtil.getConnection();
		try {
			pstmt = conn
					.prepareStatement(IQueryMapper.GET_DAILY_TRANSACTION_QUERY);
			pstmt.setDate(1, Date.valueOf(dateOfTransaction));
			records = pstmt.executeQuery();
			while (records.next()) {
				transactionsbean = new TransactionsBean();
				// transactionbean.setDateOfTransaction(records.getDate("DateofTransaction"));
				transactionsbean.setTransactionId(records.getInt(1));
				transactionsbean.setTranDescription(records.getString(2));
				transactionsbean.setDateOfTransaction(records.getDate(3)
						.toLocalDate());
				transactionsbean.setTransactionType(records.getString(4));
				transactionsbean.setTranAmount(records.getInt(5));
				transactionsbean.setAccountId(records.getLong(6));
				transactionlist.add(transactionsbean);
			}
			if (!transactionlist.isEmpty()) {
				logger.info("Transaction Details Found Successfully");
				return transactionlist;
			} else {
				logger.info("Transaction Details Not Found");
				throw new BankException(
						"There are no Transaction details associated with the given date "
								+ dateOfTransaction);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			throw new BankException(e.getMessage());
		}
	}

	@Override
	public List<TransactionsBean> viewMyTransactionDetails(LocalDate stdate,
			LocalDate endate) throws BankException {
		ResultSet records = null;
		TransactionsBean transactionsbean = null;
		PreparedStatement pstmt = null;
		List<TransactionsBean> transactionlist = new ArrayList<TransactionsBean>();
		conn = DBUtil.getConnection();
		try {
			pstmt = conn
					.prepareStatement(IQueryMapper.GET_MY_TRANSACTION_QUERY);
			pstmt.setDate(1, Date.valueOf(stdate));
			pstmt.setDate(2, Date.valueOf(endate.plusDays(1)));
			records = pstmt.executeQuery();
			while (records.next()) {
				transactionsbean = new TransactionsBean();
				transactionsbean.setTransactionId(records.getInt(1));
				transactionsbean.setTranDescription(records.getString(2));
				transactionsbean.setDateOfTransaction(records.getDate(3)
						.toLocalDate());
				transactionsbean.setTransactionType(records.getString(4));
				transactionsbean.setTranAmount(records.getInt(5));
				transactionsbean.setAccountId(records.getLong(6));
				transactionlist.add(transactionsbean);
			}
			if (!transactionlist.isEmpty()) {
				logger.info("Transaction Details Found Successfully");
				return transactionlist;
			} else {
				logger.info("Transaction Details Not Found");
				throw new BankException(
						"There are no Transaction details associated between the given dates "
								+ stdate + " and " + endate);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			throw new BankException(e.getMessage());
		}
	}
}
