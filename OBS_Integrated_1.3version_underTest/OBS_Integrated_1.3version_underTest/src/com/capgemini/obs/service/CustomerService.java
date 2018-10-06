package com.capgemini.obs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.capgemini.obs.bean.CustomerBean;
import com.capgemini.obs.bean.UserBean;
import com.capgemini.obs.dao.CustomerDAO;
import com.capgemini.obs.dao.ICustomerDAO;
import com.capgemini.obs.dao.IUserDAO;
import com.capgemini.obs.dao.UserDAO;
import com.capgemini.obs.exceptions.BankException;

public class CustomerService implements ICustomerService {

	ICustomerDAO CustomerDao;
	IUserDAO userDao;
	UserBean userBean;

	public CustomerBean addCustomerDetails(CustomerBean customerBean, long accountId) {
		CustomerDao = new CustomerDAO();

		try {
			CustomerDao.addCustomerDetails(customerBean, accountId);

		} catch (BankException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customerBean;
	}

	public boolean validateCustomer(CustomerBean customerBean)
			throws BankException {
		List<String> validationErrors = new ArrayList<String>();

		// Validating customer name
		if (!(isValidName(customerBean.getCustomerName()))) {
			validationErrors
					.add("\n Donar Name Should Be In Alphabets and minimum 3 characters long ! \n");
		}
		// Validating address
		if (!(isValidAddress(customerBean.getAddress()))) {
			validationErrors
					.add("\n Address Should Be Greater Than 2 Characters \n");
		}
		// Validating email
		if (!(isValidEmail(customerBean.getEmail()))) {
			validationErrors.add("\n Email should be valid \n");
		}
		// Validating pancard
		if (!(isValidPanCard(customerBean.getPanCard()))) {
			validationErrors.add("\n Pan card number should be valid \n");
		}

		if (!validationErrors.isEmpty()) {
			throw new BankException(validationErrors + "");
		} else
			return true;
	}

	public boolean isValidName(String CustomerName) {
		Pattern namePattern = Pattern.compile("^[A-Za-z]{3,}$");
		Matcher nameMatcher = namePattern.matcher(CustomerName);
		return nameMatcher.matches();
	}

	public boolean isValidAddress(String address) {
		return (address.length() > 2);
	}

	public boolean isValidEmail(String Email) {
		Pattern EmailPattern = Pattern.compile(
				"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		Matcher EmailMatcher = EmailPattern.matcher(Email);
		return EmailMatcher.matches();

	}

	public boolean isValidPanCard(String PanCard) {
		Pattern PanCardPattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
		Matcher PanCardMatcher = PanCardPattern.matcher(PanCard);
		return PanCardMatcher.matches();
	}
	
	@Override
	public int updateCustomerAddress(CustomerBean customerbean) throws BankException {
		// TODO Auto-generated method stub
		CustomerDAO AddressUpdateDao;
		/*System.out.println(customerbean.getAccountId());
		System.out.println(customerbean.getAddress());*/
		AddressUpdateDao = new CustomerDAO();
		int customerSeq;
		customerSeq = AddressUpdateDao.updateCustomerAddress(customerbean);
		return customerSeq;
	}

	@Override
	public int updateEmail(CustomerBean customerbean) throws BankException {
		// TODO Auto-generated method stub
		CustomerDAO AddressUpdateDao;
		/*System.out.println(customerbean.getAccountId());
		System.out.println(customerbean.getAddress());*/
		AddressUpdateDao = new CustomerDAO();
		int customerSeq;
		customerSeq = AddressUpdateDao.updateEmail(customerbean);
		return customerSeq;
	}

	public CustomerBean viewAddressDetails(long AccountId) throws BankException {
		// System.out.println(AccountId);
		CustomerDao = new CustomerDAO();
		CustomerBean bean = null;

		bean = CustomerDao.viewAddressDetails(AccountId);
		return bean;
	}
	
	public void validateAddress(CustomerBean customerbean) throws BankException {
		//System.out.println(customerbean.getAddress());
		if (customerbean.getAddress().length() < 3) {
			throw new BankException(
					"\n Address Should Be Greater Than 2 Characters \n");
		}
	}

	@Override
	public CustomerBean viewEmailDetails(long AccountId) throws BankException {
		// TODO Auto-generated method stub
		CustomerDao = new CustomerDAO();
		CustomerBean bean = null;

		bean = CustomerDao.viewEmailDetails(AccountId);
		return bean;
	}

	public void validateEmail(CustomerBean customerbean) throws BankException {
		// TODO Auto-generated method stub
		List<String> validationError = new ArrayList<String>();
		if (!(isValidEmail(customerbean.getEmail()))) {
			validationError.add("\n Email should be valid \n");
		}
		if (!validationError.isEmpty())
			throw new BankException(validationError + "");
	}
}
