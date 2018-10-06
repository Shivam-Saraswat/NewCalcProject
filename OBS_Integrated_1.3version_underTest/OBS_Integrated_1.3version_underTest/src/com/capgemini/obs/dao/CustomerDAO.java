package com.capgemini.obs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.capgemini.obs.bean.CustomerBean;
import com.capgemini.obs.exceptions.BankException;
import com.capgemini.obs.util.DBUtil;
import com.capgemini.obs.util.IQueryMapper;

public class CustomerDAO implements ICustomerDAO {
	private static Connection conn=null;
	public static Logger logger = Logger.getRootLogger();
	PreparedStatement pstmt;

	/*******************************************************************************************************
	 * - Function Name : addCabRequestDetails(CaBRequest cabRequest) - Input Parameters :
	 * CaBRequest cabRequest - Return Type : int - Throws : {@link CarRentException} -
	 * Author : CAPGEMINI - Creation Date : 29/08/2018 - Description : Adding
	 * Details
	 ********************************************************************************************************/

	@Override
	public CustomerBean addCustomerDetails(CustomerBean customerbean, long accountId)
			throws BankException {
		
		PropertyConfigurator.configure("resource//log4j.properties");

		long id = 0;
		int records = 0;
		ResultSet rs = null;
		conn = DBUtil.getConnection();

		try {
			 pstmt = conn
					.prepareStatement(IQueryMapper.INSERT_QUERY);
			
			pstmt.setLong(1, accountId);
			pstmt.setString(2, customerbean.getCustomerName());
			pstmt.setString(3, customerbean.getEmail());
			pstmt.setString(4, customerbean.getAddress());			
			pstmt.setString(5, customerbean.getPanCard());

			records = pstmt.executeUpdate();

			pstmt = conn.prepareStatement(IQueryMapper.SELECT_CUSTOMER_QUERY);
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				customerbean.setAccountId(rs.getLong(1));
				customerbean.setCustomerName(rs.getString(2));						
				customerbean.setEmail(rs.getString(3));
				customerbean.setAddress(rs.getString(4));
				customerbean.setPanCard(rs.getString(5));							
			}				
			
			return customerbean;
			
			} catch(SQLException e) {
				throw new BankException(e.getMessage());
				
			}finally{
				try {
					logger.info("Customer Personal details insert successful, 1 record Added");
					pstmt.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
	}
	
	public CustomerBean viewAddressDetails(Long AccountId) throws BankException {
		// TODO Auto-generated method stub
		//System.out.println(AccountId);
		ResultSet records = null;
		CustomerBean customerbean = null;
			PreparedStatement pstmt = null;
			conn=DBUtil.getConnection();
			try {
				pstmt = conn
						.prepareStatement(IQueryMapper.GET_ADDRESS_QUERY);
				pstmt.setLong(1, AccountId);
				//System.out.println(AccountId);
				records = pstmt.executeQuery();
				if(records.next())
				{
					//System.out.println(AccountId);
					customerbean = new CustomerBean();
					//System.out.println(AccountId);
					customerbean.setAddress(records.getString("ADDRESS"));
					
				}
				if (customerbean != null) {
					logger.info("Address Found Successfully");
					return customerbean;
				} 
				else {
					logger.info("Address Not Found");
					return null;
				} 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				throw new BankException(e.getMessage());
			}
			
			
	}

	@Override
	public CustomerBean viewEmailDetails(long AccountId) throws BankException {
		// TODO Auto-generated method stub
		ResultSet records = null;
		CustomerBean customerbean = null;
			PreparedStatement pstmt = null;
			conn=DBUtil.getConnection();
			try {
				pstmt = conn
						.prepareStatement(IQueryMapper.GET_EMAIL_QUERY);
				pstmt.setLong(1, AccountId);
				//System.out.println(AccountId);
				records = pstmt.executeQuery();
				if(records.next())
				{
					//System.out.println(AccountId);
					customerbean = new CustomerBean();
					//System.out.println(AccountId);
					customerbean.setEmail(records.getString("EMAIL"));
					
				}
				if (customerbean != null) {
					logger.info("email Found Successfully");
					return customerbean;
				} 
				else {
					logger.info("email Not Found");
					return null;
				} 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
				throw new BankException(e.getMessage());
			}
	}
	public int updateCustomerAddress(CustomerBean customerbean) throws BankException {
		// TODO Auto-generated method stub
		PropertyConfigurator.configure("resource//log4j.properties");

		int id = 0;
		int records = 0;
		ResultSet rs = null;
		conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.UPDATE_ADDRESS_QUERY);

			
			//System.out.println(customerbean.getAddress());
			pstmt.setString(1, customerbean.getAddress());
			pstmt.setLong(2, customerbean.getAccountId());
			
			records = pstmt.executeUpdate();
			return records;

		} 
		catch (SQLException e) {
			throw new BankException(e.getMessage());
		}
	}
	public int updateEmail(CustomerBean customerbean) throws BankException{
		// TODO Auto-generated method stub
		PropertyConfigurator.configure("resource//log4j.properties");

		int id = 0;
		int records = 0;
		ResultSet rs = null;
		conn = DBUtil.getConnection();
		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.UPDATE_QUERY_EMAIL);

			
			
			pstmt.setString(1, customerbean.getEmail());
			pstmt.setLong(2, customerbean.getAccountId());
			
			records = pstmt.executeUpdate();
			return records;

		} 
		catch (SQLException e) {
			throw new BankException(e.getMessage());
		}
	}

}
