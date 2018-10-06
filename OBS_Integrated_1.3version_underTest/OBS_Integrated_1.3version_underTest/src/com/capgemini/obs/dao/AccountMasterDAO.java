                          package com.capgemini.obs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.capgemini.obs.bean.AccountMasterBean;
import com.capgemini.obs.bean.CustomerBean;
import com.capgemini.obs.exceptions.BankException;
import com.capgemini.obs.util.DBUtil;
import com.capgemini.obs.util.IQueryMapper;

public class AccountMasterDAO implements IAccountMasterDAO{

	private static Connection conn;
	public static Logger logger = Logger.getRootLogger();

	/*******************************************************************************************************
	 * - Function Name : addCabRequestDetails(CaBRequest cabRequest) - Input Parameters :
	 * CaBRequest cabRequest - Return Type : int - Throws : {@link CarRentException} -
	 * Author : CAPGEMINI - Creation Date : 29/08/2018 - Description : Adding
	 * Details
	 ********************************************************************************************************/

	@Override
	public long addAccountDetails(AccountMasterBean accountMasterBean)
			throws BankException{
		
		PropertyConfigurator.configure("resource//log4j.properties");

		long id = 0;
		int records = 0;
		ResultSet rs = null;
		conn = DBUtil.getConnection();

		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.INSERT_CREATE_ACC_QUERY);
			
			pstmt.setString(1, accountMasterBean.getAccountType());
			pstmt.setInt(2, accountMasterBean.getAccountBalance());

			records = pstmt.executeUpdate();

			pstmt = conn.prepareStatement(IQueryMapper.ACCID_QUERY_SEQUENCE);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				id = rs.getLong(1);
			}

			pstmt.close();

			if (records != 0) {
				logger.info("Account Details insert successful, 1 record Added");
				conn.close();
				return id;
			}

			return id;

		} catch (SQLException e) {
			throw new BankException(e.getMessage());
		}
		
	}

	
}
