package com.capgemini.obs.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.capgemini.obs.bean.FundTransferBean;
import com.capgemini.obs.bean.PayeeBean;
import com.capgemini.obs.exceptions.BankException;
import com.capgemini.obs.util.DBUtil;
import com.capgemini.obs.util.IQueryMapper;

public class FundTransferDAO implements IFundTransferDAO {

	static Logger logger = Logger.getLogger("logfile");
	
	Connection conn = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	int curr_bal,payee_curr_bal;
	int max_limit=0;
	
	public FundTransferDAO(){
		PropertyConfigurator.configure("resource//log4j.properties");
		
	}
	
	
	@Override
	public int checkPayee(PayeeBean payeeBean) throws BankException {
		
		conn = DBUtil.getConnection();
		
		
		try {
			preparedStatement = conn.prepareStatement(IQueryMapper.SELECT_PAYEE_ID);
			
			
			preparedStatement.setLong(1, payeeBean.getAccountId());
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			
			ResultSetMetaData meta = resultSet.getMetaData();
			int colCount = meta.getColumnCount();
			int col,set1 = 0,set2 = 0;
			int arr;
			String nick;
			
			
			while(resultSet.next()){
				
				for (col=1; col <= colCount; col++) 
			    {
			        	if(col==1){
			        		int value = resultSet.getInt(col);
			        		
			        		
			        		//arr= ((BigInteger) value).intValueExact();
				        	if(value == payeeBean.getPayeeAccountId())
				        		set1 = 1;
				        	else
				        		set1 = 0;
			        	}else if(col ==2){
			        		nick = resultSet.getString(col);
			        		
			        		
			        		if(nick.equals(payeeBean.getNickName()))
			        			set2 = 1;
			        		else
			        			set2 = 0;
			        	}
			        	
			          
			        
			    }//end of for
				if(set1 ==1 && set2 == 1){
					return 0;
				}	
				else if(set1 ==1 && set2 == 0){
					return 2;
				}	
				else if(set1 == 0 && set2 ==0)
					continue;
				
			}
			return 1;
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new BankException("Tehnical problem occured. Refer log");
		}
		finally{
			try {
				
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
				throw new BankException("Error in closing db connection");
				
			}
		}
		
	}

	
	@Override
	public PayeeBean addPayee(PayeeBean payeeBean) throws BankException {
		
		conn = DBUtil.getConnection();
		
		try {
			preparedStatement = conn.prepareStatement(IQueryMapper.INSERT_PAYEE);
			
			preparedStatement.setLong(1,payeeBean.getAccountId());
			preparedStatement.setLong(2,payeeBean.getPayeeAccountId());
			preparedStatement.setString(3,payeeBean.getNickName());
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println("Payee Registration failed");
			logger.error(e.getMessage());
			throw new BankException("Tehnical problem occured. Refer log");
		}
		finally{
			try {
				preparedStatement.close();
				conn.close();
			}catch (SQLException e) {
				logger.error(e.getMessage());
				throw new BankException("Error in closing db connection");
				
			}
		}
		return payeeBean;
	}


	
	@Override
	public FundTransferBean payPayee(FundTransferBean fundTransfer) throws BankException {
		
		conn = DBUtil.getConnection();
		
		try {
			preparedStatement = conn.prepareStatement(IQueryMapper.FUND_TRANSFER_ACCOUNT);
			preparedStatement.setLong(1, fundTransfer.getAccountId());
			preparedStatement.setLong(2, fundTransfer.getPayeeAccountId());
			preparedStatement.setInt(3, fundTransfer.getTransferAmount());
			
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println("Error in payment");
			logger.error(e.getMessage());
			throw new BankException("Tehnical problem occured. Refer log");
		}
		finally{
			try {
				
				preparedStatement.close();
				conn.close();
			}catch (SQLException e) {
				logger.error(e.getMessage());
				throw new BankException("Error in closing db connection");
				
			}
		}
		return fundTransfer;
	}

	@Override
	public boolean checkTransactionPwd(long account_Id, String tranPwd) throws BankException {
		conn = DBUtil.getConnection();
		ResultSet resultSet = null;
		try {
			
			preparedStatement = conn.prepareStatement(IQueryMapper.CHECK_TRANSACTION_PWD);
			
			preparedStatement.setLong(1, account_Id);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				
				if(tranPwd.equals(resultSet.getString(1))){
					
					return true;
				}else{
					
					return false;
				}
			}
			else{
				
				return false;
			}
			
		} catch (SQLException e) {
			logger.error("Invalid Transaction Password");
			System.err.println("Invalid transaction Password");
			throw new BankException("Tehnical problem occured. Refer log");
			
		}
		finally{
			try {
				resultSet.close();
				preparedStatement.close();
				conn.close();
			}catch (SQLException e) {
				logger.error(e.getMessage());
				throw new BankException("Error in closing db connection");
				
			}
		}
		
	}

	
	@Override
	public boolean checkAccountBalance(long account_ID, int other_bank_amount) throws BankException {
		
		conn = DBUtil.getConnection();
		
		try {
			preparedStatement = conn.prepareStatement(IQueryMapper.CHECK_ACCOUNT_BALANCE);
			preparedStatement.setLong(1, account_ID);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				
				if(other_bank_amount <= resultSet.getInt(1)){
					return true;
				}else{
					return false;
				}
			}else{
				
				return false;
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			throw new BankException("Tehnical problem occured. Refer log");
		}
		finally{
			try {
				preparedStatement.close();
				conn.close();
			}catch (SQLException e) {
				logger.error(e.getMessage());
				throw new BankException("Error in closing db connection");
				
			}
		}
		
	}

	
	@Override
	public boolean checkMaxFunds(long account_ID, int other_bank_amount) throws BankException {
		
		System.out.println("In check");
		conn = DBUtil.getConnection();
		System.out.println("In check");
		try {
			
			//Date today = LocalDate.now();
			LocalDate date = LocalDate.now();
			Date today = Date.valueOf(date);
			
			preparedStatement = conn.prepareStatement(IQueryMapper.CHECK_MAX_LIMIT);
			preparedStatement.setLong(1, account_ID);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			
			while(resultSet.next()){
				
				
				Date dateTransfer = resultSet.getDate(1);
				
				if(today.equals(dateTransfer)){
				
					max_limit += resultSet.getInt(2);
				}
			}
			
			
			if(max_limit>=1000000)
				return false;
			else
				return true;
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			System.err.println("Error in checking for the total fund transferred today");
			throw new BankException("Tehnical problem occured. Refer log");
		}
		finally{
			try {
				preparedStatement.close();
				conn.close();
			}catch (SQLException e) {
				logger.error(e.getMessage());
				throw new BankException("Error in closing db connection");
				
			}
		}
		
	}

	@Override
	public boolean updateTransaction(long account_ID,
			long payee_Account_Id,String tran_desc,int other_bank_amount) throws BankException {
		
		conn = DBUtil.getConnection();
		int records_accHolder=0;
		int records_payeeHolder=0;
		int records_updated = 0;
		int flag1=0; int flag2=0;
		

		try {
			preparedStatement = conn.prepareStatement(IQueryMapper.INSERT_TRANSFER_TABLE);
			
			preparedStatement.setString(1,tran_desc);
			preparedStatement.setString(2,"d");
			preparedStatement.setInt(3,other_bank_amount);
			preparedStatement.setLong(4, account_ID);
			
			records_accHolder = preparedStatement.executeUpdate();
			
			
			preparedStatement = conn.prepareStatement(IQueryMapper.CHECK_ACCOUNT_BALANCE);
			preparedStatement.setLong(1, account_ID);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				curr_bal = resultSet.getInt(1);
			}
			
			preparedStatement = conn.prepareStatement(IQueryMapper.UPDATE_ACCOUNT_BAL);
			int update_bal = curr_bal - other_bank_amount;
			preparedStatement.setInt(1, update_bal);
			preparedStatement.setLong(2, account_ID);
			
			preparedStatement.executeUpdate();
			
			if(records_accHolder>0 && records_updated>0)
				flag1 = 1;
			
		} catch (SQLException e) {
			logger.error("Database Problem : Data not stored");
			System.err.println("Error in updating in transaction table");
			throw new BankException("Tehnical problem occured. Refer log" +e.getMessage());
		}
		
		try {
						
			preparedStatement = conn.prepareStatement(IQueryMapper.INSERT_TRANSFER_TABLE);
			
			preparedStatement.setString(1,tran_desc);
			preparedStatement.setString(2,"c");
			preparedStatement.setInt(3,other_bank_amount);
			preparedStatement.setLong(4, payee_Account_Id);
			
			records_payeeHolder = preparedStatement.executeUpdate();
			
			preparedStatement = conn.prepareStatement(IQueryMapper.CHECK_ACCOUNT_BALANCE);
			preparedStatement.setLong(1, payee_Account_Id);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				
				payee_curr_bal = resultSet.getInt(1);
			}
			
			
			preparedStatement = conn.prepareStatement(IQueryMapper.UPDATE_ACCOUNT_BAL);
			int update_payee_bal = payee_curr_bal + other_bank_amount;
			preparedStatement.setInt(1, update_payee_bal);
			preparedStatement.setLong(2, payee_Account_Id);
			
			preparedStatement.executeUpdate();
			if(records_payeeHolder>0)
				flag2 = 1;
			
		} catch (SQLException e) {
			
			logger.error("Database Problem : Data not stored");
			System.err.println("Could't update record in Transaction table of Payee Account");
			throw new BankException("Tehnical problem occured. Refer log" +e.getMessage());
			
		}
		finally{
			try {
				preparedStatement.close();
				conn.close();
			}catch (SQLException e) {
				logger.error(e.getMessage());
				throw new BankException("Error in closing db connection");
				
			}
		}
		if(flag1==1&&flag2==1)
			return true;
		else
			return false;
	}


	@Override
	public FundTransferBean TransferSameAccount(FundTransferBean fundTransferBean) throws BankException {
		
		conn = DBUtil.getConnection();
		int status = 0;
		try {
			preparedStatement = conn.prepareStatement(IQueryMapper.FUND_TRANSFER_ACCOUNT);
			
			preparedStatement.setLong(1, fundTransferBean.getAccountId());
			preparedStatement.setLong(2, fundTransferBean.getPayeeAccountId());
			preparedStatement.setInt(3, fundTransferBean.getTransferAmount());
			
			status = preparedStatement.executeUpdate();
		
		} catch (SQLException e) {
			logger.error("Database Problem : Data not stored");
			throw new BankException("DBProblem : data not stored" + e.getMessage());
		}
		finally{
			try {
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				System.err.println("Connection not closed");
			}
		}
		return fundTransferBean;
	}


}
