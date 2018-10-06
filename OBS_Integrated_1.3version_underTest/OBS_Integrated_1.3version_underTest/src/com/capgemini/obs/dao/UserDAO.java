package com.capgemini.obs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.capgemini.obs.bean.UserBean;
import com.capgemini.obs.exceptions.BankException;
import com.capgemini.obs.util.DBUtil;
import com.capgemini.obs.util.IQueryMapper;

public class UserDAO implements IUserDAO{

	private static Connection conn;
	public static Logger logger = Logger.getRootLogger();

	/*******************************************************************************************************
	 * - Function Name : addCabRequestDetails(CaBRequest cabRequest) - Input Parameters :
	 * CaBRequest cabRequest - Return Type : int - Throws : {@link CarRentException} -
	 * Author : CAPGEMINI - Creation Date : 29/08/2018 - Description : Adding
	 * Details
	 ********************************************************************************************************/

	@Override
	public UserBean UserDetails(UserBean userBean, long accountId) throws BankException {
		
		long id = 0;
		long up = 0;
		int records = 0;
		ResultSet rs = null;		
		conn = DBUtil.getConnection();
		
		PropertyConfigurator.configure("resource//log4j.properties");
		

		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.INSERT_lOGIN_QUERY);
			
			pstmt.setLong(1, accountId);			
			//pstmt.setString(2, userBean.getLoginPassword());
			pstmt.setString(2, "xyz");
			pstmt.setString(3, userBean.getSecretQuestion());	
			//pstmt.setString(3, "secret");
			pstmt.setString(4, userBean.getTransactionPassword());
			//pstmt.setString(4, "pass");
			//pstmt.setString(5, userBean.getLockStatus());
			pstmt.setString(5, "U");
			pstmt.setString(6, userBean.getSecretAnswer());

			records = pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(IQueryMapper.LOGIN_USERID_QUERY_SEQUENCE);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				id = rs.getLong(1);
			}
			
			rs=null;
			if (records != 0) {
				logger.info("Details regarding login inserted successfully, 1 user Id created");
				pstmt = conn.prepareStatement(IQueryMapper.UPDATE_lOGIN_QUERY);
				
				pstmt.setLong(1, accountId);
				pstmt.setLong(2, id);
				
				up = pstmt.executeUpdate();				
				
				if (up==1) {
					pstmt = conn.prepareStatement(IQueryMapper.SELECT_lOGIN_QUERY);		
					
					pstmt.setLong(1, id);
					
					rs = pstmt.executeQuery();					
					if (rs.next()) {
						userBean.setAccountId(rs.getLong(1));
						userBean.setUserId(rs.getLong(2));						
						userBean.setLoginPassword(rs.getString(3));
						userBean.setSecretQuestion(rs.getString(4));
						userBean.setTransactionPassword(rs.getString(5));
						userBean.setLockStatus(rs.getString(6));	
						userBean.setSecretAnswer(rs.getString(7));
					}
				}
			}			
			pstmt.close();
			return userBean;

		} catch (SQLException e) {
			throw new BankException(e.getMessage());
		}
		
	}

	@Override
	public UserBean authenticUser(UserBean loginBean) throws BankException{
		 
		long up = 0;
		ResultSet rs = null;		
		conn = DBUtil.getConnection();
		
		PropertyConfigurator.configure("resource//log4j.properties");
		

		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.SELECT_CHECKlOGIN_QUERY);
			
			pstmt.setLong(1, loginBean.getUserId());		
			pstmt.setString(2, loginBean.getLoginPassword());					
		
			up = pstmt.executeUpdate();				
			
			if (up==1) {
				pstmt = conn.prepareStatement(IQueryMapper.SELECT_lOGIN_QUERY);		
				
				pstmt.setLong(1,  loginBean.getUserId() );
				
				rs = pstmt.executeQuery();					
				while (rs.next()) {
					loginBean.setAccountId(rs.getLong(1));
					loginBean.setUserId(rs.getLong(2));						
					loginBean.setLoginPassword(rs.getString(3));
					loginBean.setSecretQuestion(rs.getString(4));
					loginBean.setTransactionPassword(rs.getString(5));
					loginBean.setLockStatus(rs.getString(6));	
					loginBean.setSecretAnswer(rs.getString(7));
				}
			}else{				
				return null;
			}
			pstmt.close();	

	} catch (SQLException e) {
		throw new BankException(e.getMessage());
	}
	return loginBean;
		
		}

	@Override
	public void setLock(UserBean loginBean, String LockStatus) throws BankException{

		ResultSet rs = null;		
		conn = DBUtil.getConnection();
		
		PropertyConfigurator.configure("resource//log4j.properties");
		

		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.UPDATE_lOCK_QUERY);
			
			pstmt.setString(1, LockStatus);		
			pstmt.setLong(2, loginBean.getUserId());		
		
			rs = pstmt.executeQuery();					
			
			}catch (SQLException e) {
				throw new BankException(e.getMessage());
			}
	
	}

	@Override
	public void setNewLoginPassword(UserBean loginBean, String loginPassword)
			throws BankException {

		int n;
		conn = DBUtil.getConnection();
		
		PropertyConfigurator.configure("resource//log4j.properties");
		

		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.UPDATE_LOGIN_PASSWORD_QUERY);
			
			pstmt.setString(1, loginPassword);		
			pstmt.setLong(2, loginBean.getUserId());		
		
			n = pstmt.executeUpdate();					
			}catch (SQLException e) {
				throw new BankException(e.getMessage());
			}
		
	}

	@Override
	public UserBean forgotPassword(UserBean userForgotPass) throws BankException {
		
		long up = 0;
		ResultSet rs = null;		
		conn = DBUtil.getConnection();
		
		PropertyConfigurator.configure("resource//log4j.properties");
		

		try {
			PreparedStatement pstmt = conn
					.prepareStatement(IQueryMapper.SELECT_FORGOT_PASSWORD_QUERY);
								
			pstmt.setLong(1, userForgotPass.getUserId());
		
			up = pstmt.executeUpdate();	
			if (up==1) {
				pstmt = conn.prepareStatement(IQueryMapper.SELECT_lOGIN_QUERY);		
				
				pstmt.setLong(1,  userForgotPass.getUserId() );
				
				rs = pstmt.executeQuery();					
				while (rs.next()) {
					userForgotPass.setAccountId(rs.getLong(1));
					userForgotPass.setUserId(rs.getLong(2));						
					userForgotPass.setLoginPassword(rs.getString(3));
					userForgotPass.setSecretQuestion(rs.getString(4));
					userForgotPass.setTransactionPassword(rs.getString(5));
					userForgotPass.setLockStatus(rs.getString(6));	
					userForgotPass.setSecretAnswer(rs.getString(7));
				}
			}else{				
				return null;
			}
			pstmt.close();	

	} catch (SQLException e) {
		throw new BankException(e.getMessage());
	}
		return userForgotPass;
	}
}
