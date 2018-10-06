package com.capgemini.obs.dao;

import com.capgemini.obs.bean.UserBean;
import com.capgemini.obs.exceptions.BankException;

public interface IUserDAO {

	public UserBean UserDetails(UserBean userBean, long accountId) throws BankException;

	public UserBean authenticUser(UserBean loginBean) throws BankException;

	public void setLock(UserBean loginBean, String setLockStatus) throws BankException;

	public void setNewLoginPassword(UserBean loginBean, String loginPassword) throws BankException;

	public UserBean forgotPassword(UserBean userForgotPass) throws BankException;

}
