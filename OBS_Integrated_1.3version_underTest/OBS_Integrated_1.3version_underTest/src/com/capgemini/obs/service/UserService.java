package com.capgemini.obs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.obs.bean.UserBean;
import com.capgemini.obs.dao.CustomerDAO;
import com.capgemini.obs.dao.IUserDAO;
import com.capgemini.obs.dao.UserDAO;
import com.capgemini.obs.exceptions.BankException;

public class UserService implements IUserService {

	IUserDAO userDao;
	UserBean userBean;
	UserBean loginBean;
	
	//--------------------Add User Details------------------------------------
	
	@Override
	public UserBean UserDetails(UserBean userBean, long accId) throws BankException {
		
		userDao = new UserDAO();

		try {		
			userBean = userDao.UserDetails(userBean, accId);

		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userBean;
	}
	
	//--------------------Validate User Details while SignUp------------------------------------
	
	@Override
	public boolean validateUserCredentials(UserBean userBean) throws BankException {
		
		List<String> validationErrors = new ArrayList<String>();

		// Validating TransactionPassword
		if (!(isValidTransactionPassword(userBean.getTransactionPassword()))) {
			validationErrors.add("\n Invalid Transaction Password! \n");
		}
		if (!validationErrors.isEmpty())
			throw new BankException(validationErrors + "");
		else
			return true;
	}

	private static boolean isValidTransactionPassword(String transPassword) {
		Pattern namePattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Za-z])(?=.*[@#$%^&+=]).{6,}");
		Matcher nameMatcher = namePattern.matcher(transPassword);
		return nameMatcher.matches();
	}
	
	//--------------------Validate User Details while LoginIn------------------------------------
	
	public UserBean authenticUser(UserBean loginBean) throws BankException {

		userDao = new UserDAO();

		try {		
			loginBean= userDao.authenticUser(loginBean);			

		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return loginBean;	
		}
/*
	@Override
	public boolean validateLoginCredentials(UserBean loginBean) throws BankException {
		List<String> validationErrors = new ArrayList<String>();

		// Validating UserId
		if (!(isValidUserId(loginBean.getUserId()))) {
						validationErrors.add("\n UserId should have digits upto 10 only! \n");
		}
			
		// Validating LoginPassword
		if (!(isValidLoginPassword(loginBean.getLoginPassword()))) {
			validationErrors.add("\n Login Password Format Error! \n");
		}
		if (!validationErrors.isEmpty())
			throw new BankException(validationErrors + "");
		else
			return true;
	}

	private boolean isValidUserId(long userId) {
		return (userId > 4 && userId < 10);
		
	}

	private static boolean isValidLoginPassword(String loginPassword) {
		Pattern namePattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Za-z])(?=.*[@#$%^&+=]).{6,}");
		Matcher nameMatcher = namePattern.matcher(loginPassword);
		return nameMatcher.matches();
	}*/

	@Override
	public void setLock(UserBean loginBean, String setLockStatus) throws BankException {

		try {		
			userDao.setLock(loginBean, setLockStatus);

		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void setNewLoginPassword(UserBean loginBean, String loginPassword)
			throws BankException {
		
		userDao = new UserDAO();
		
		try {	
			userDao.setNewLoginPassword(loginBean, loginPassword);

		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public UserBean forgotPassword(UserBean userForgotPass) throws BankException {
		userDao = new UserDAO();

		try {		
			userForgotPass = userDao.forgotPassword(userForgotPass);

		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userForgotPass;
	}


}