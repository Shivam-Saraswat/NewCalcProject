package com.capgemini.obs.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.capgemini.obs.bean.AccountMasterBean;
import com.capgemini.obs.bean.CustomerBean;
import com.capgemini.obs.bean.FundTransferBean;
import com.capgemini.obs.bean.PayeeBean;
import com.capgemini.obs.bean.ServiceTrackerBean;
import com.capgemini.obs.bean.StatementBean;
import com.capgemini.obs.bean.TransactionsBean;
import com.capgemini.obs.bean.UserBean;
import com.capgemini.obs.exceptions.BankException;
import com.capgemini.obs.service.AccountMasterService;
import com.capgemini.obs.service.CustomerService;
import com.capgemini.obs.service.FundTransferService;
import com.capgemini.obs.service.IAccountMasterService;
import com.capgemini.obs.service.ICustomerService;
import com.capgemini.obs.service.IUserService;
import com.capgemini.obs.service.ServiceTracker;
import com.capgemini.obs.service.StatementServiceImpl;
import com.capgemini.obs.service.TransactionService;
import com.capgemini.obs.service.UserService;

public final class Client {

	static Scanner sc = new Scanner(System.in);

	static Logger logger = Logger.getRootLogger();

	static TransactionsBean transactionsBean = null;
	static CustomerBean customerBean = null;
	static ServiceTrackerBean serviceTrackerBean = null;
	static FundTransferBean fundBean = null;
	static FundTransferBean fundTransferBean = null;
	static UserBean userBean = null;
	static UserBean userForgotPass = null;
	static UserBean loginBean = null;
	static AccountMasterBean accountMasterBean = null;
	static IUserService userService = null;
	static UserService userServiceImpl = null;
	static FundTransferService fundTransferService = null;
	static ICustomerService CustomerService = null;
	static ServiceTracker trackRequestService = null;
	static ServiceTracker reqChequeService = null;
	static ICustomerService updateAddress = null;
	static CustomerService CustomerServiceImpl = null;
	static IAccountMasterService accountMasterService = null;
	static PayeeBean payee = new PayeeBean();
	static PayeeBean addPayee = new PayeeBean();
	static FundTransferBean fundBean2 = null;
	static AccountMasterBean accountDetails = new AccountMasterBean();
	static TransactionsBean transactionbean = null;
	static TransactionService transactionService = null;

	public static void main(String[] args) {
		PropertyConfigurator.configure("resource//log4j.properties");

		long accId = 0;
		String serviceId = null;
		int customerId = 0;
		int choice = 0;
		int choose = 0;
		char opt = 0;
		char option;
		int flag = 0;
		int fund_option = 0;
		UserBean auth = null;

		loop1: do {
			// show menu

			System.out.println();
			System.out.println();
			System.out
					.println("*********** Welcome To Bank Application ************\n");
			System.out
					.println("                   LOGIN                          \n");

			System.out.println("1. Customer ");
			System.out.println("2. Bank Admin");

			choice = sc.nextInt();

			switch (choice) {
			case 1:
				// method for new Page
				loginBean = null;

				loop2: while (flag < 3) {
					flag++;
					while (loginBean == null) {
						loginBean = enterUser();
					}
					try {
						userService = new UserService();

						auth = userService.authenticUser(loginBean);

						if (auth == null) {
							// if (flag < 3) {
							System.out
									.println("---------------------------------");
							System.out
									.println("Please Enter Valid Credentials!!");
							System.out
									.println("---------------------------------");

							System.out.println("Forgot Password?y/n");
							char ans = sc.next().charAt(0);

							if (ans == 'y' || ans == 'Y') {
								loginBean = forgotPassword();
								userForgotPass = userService
										.forgotPassword(loginBean);
								logger.info("Forgot Password option chosen.");
								try {

									if (loginBean.getSecretQuestion().equals(
											userForgotPass.getSecretQuestion())
											&& loginBean.getSecretAnswer()
													.equals(userForgotPass
															.getSecretAnswer())) {

										System.out
												.println("\nSet your Login Password with specifications given below.\n");
										System.out
												.println("** A digit must occur at least once");
										System.out
												.println("** An alphabet must occur at least once");
										System.out
												.println("** A special character must occur at least once");
										System.out
												.println("** At least 6 characters\n");
										System.out
												.println("Enter your new Password: ");
										userForgotPass.setLoginPassword(sc
												.next());

										try {
											if (!userService
													.validateUserCredentials(userForgotPass)) {
												System.out
														.println("Please set your password with above specifications!");
											}
										} catch (BankException bankexception) {
											logger.error("exception occured",
													bankexception);
											System.err.println("Invalid data:");
											System.err.println(bankexception
													.getMessage()
													+ "\nTry again..");
										}

										// sending to user service layer
										userService.setNewLoginPassword(
												userForgotPass, userForgotPass
														.getLoginPassword());
										System.out
												.println("\nYour Login Password is set successfully. Please login again.");
										logger.info("New Login Password is set successfully.");
										System.out
												.println("-----------------------------------------------------\n");
										userForgotPass = null;
										flag = 0;
										loginBean = null;
										continue loop2;

									} else {
										System.out
												.println("Wrong Verification Details!");
									}
								} catch (BankException bankException) {
									bankException.getMessage();
								}
							} else {

								if (flag >= 3) {
									System.out
											.println("\nYou have exceeded your 3 attempts. Please try logging in after 24 hours.");
									loginBean.setLockStatus("L");
									try {
										userService.setLock(loginBean,
												loginBean.getLockStatus());
										logger.info("User Id-"
												+ loginBean.getUserId()
												+ ": Account is Locked.");

									} catch (BankException e) {
										e.printStackTrace();
									}
									System.exit(0);
								} else {
									loginBean = null;
									continue loop2;
								}
							}

							loginBean = null;
							continue loop2;
							// }
						} else if (auth.getUserId() == loginBean.getUserId()
								&& auth.getLoginPassword().equals(
										loginBean.getLoginPassword())) {
							logger.info("Logged in successfully");
							break loop2;
						}
					} catch (BankException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				logger.info("User Id-" + auth.getUserId()
						+ ": User entered into the system Account.");

				System.out.println("\n**************Welcome "
						+ auth.getUserId() + "!****************\n");
				System.out.println("Account ID: " + auth.getAccountId() + "\n");
				System.out.println("a. View  Mini statement ");
				System.out.println("b. View Detailed statement ");
				System.out
						.println("c. Request for change in communication address for bank account");
				System.out
						.println("d. Request for change in email address for bank account");
				System.out.println("e. Request for cheque book ");
				System.out.println("f. Track  service request ");
				System.out.println("g. Fund Transfer ");
				System.out.println("h. Change password ");
				System.out.println("i. Logout ");

				loop3: while (true) {
					System.out
							.println("Please enter your choice from the menu: ");

					option = sc.next().charAt(0);

					switch (option) {
					case 'a':

						// Method for Mini statement

						StatementServiceImpl statementService = new StatementServiceImpl();
						StatementBean stmt = new StatementBean();

						stmt.setAccountId(auth.getAccountId());
						try {
							List<TransactionsBean> miniStatementList = new ArrayList<TransactionsBean>();
							miniStatementList = statementService
									.viewMiniStatement(stmt);
							TransactionsBean transaction = null;

							if (miniStatementList != null) {
								Iterator<TransactionsBean> itr = miniStatementList
										.iterator();

								System.out
										.println("------------------------------------------------------------------------------------------");
								System.out
										.println("Transaction Id    Transaction Description    Date of transaction  transaction type amount ");
								while (itr.hasNext()) {
									transaction = itr.next();
									System.out.println(transaction.toString());
								}
								System.out
										.println("------------------------------------------------------------------------------------------");
								// System.out.println(i +
								// " records are retrieved");
							} else {
								System.out
										.println("No Transactions are available");
							}
						} catch (BankException e) {
							System.err.println("Error  :" + e.getMessage());
						}

						continue loop3;

					case 'b':
						// Method for Detailed statement
						boolean valid= true;
			     		 LocalDate startDate=null;
			     		 LocalDate endDate=null;
			     		    do{
				        	 System.out.print("Enter start date [dd/MM/yyyy] :");
				    	      String sDate = sc.next();
					         System.out.println("Enter end date [dd/MM/yyyy] :");
					          String eDate = sc.next();
					          
					          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
					    		try{
					    			 startDate = LocalDate.parse(sDate, formatter);
						    		 endDate = LocalDate.parse(eDate, formatter);
						    		 valid=true;
					    		}catch(DateTimeParseException e)
					    		{
					    			System.out.println("Please enter the dates in dd/MM/yyyy");
					    			valid=false;
					    		}
					         
			     		    }while(!valid);
						StatementServiceImpl statementService1 = new StatementServiceImpl();
						StatementBean stmt1 = new StatementBean();
						stmt1.setAccountId(auth.getAccountId());
						stmt1.setStartDate(startDate);
						stmt1.setEndDate(endDate);
						try {
							List<TransactionsBean> detailStatementList = new ArrayList<TransactionsBean>();
							detailStatementList = statementService1
									.viewDetailedStatement(stmt1);
							TransactionsBean transaction = null;
							TransactionsBean reference = new TransactionsBean();
							reference.setAccountId(120);
							if (detailStatementList != null) {
								Iterator<TransactionsBean> itr = detailStatementList
										.iterator();
								// int i=0;
								while (itr.hasNext()) {
									transaction = itr.next();
									if (reference.getAccountId() != transaction
											.getAccountId()) {
										System.out
												.println("------------------------------------------------------------------------------------------");
										reference.setAccountId(transaction
												.getAccountId());
										System.out
												.println("Transactions for Account "
														+ reference
																.getAccountId()
														+ "\n");
										System.out
												.println("Transaction Id    Transaction Description    Date of transaction  transaction type amount ");
									}
									System.out.println(transaction.toString());
								}
								System.out
										.println("----------------------------------------------------------------------------------------------");
								// System.out.println(i +
								// " records are retrieved");
							} else {
								System.out
										.println("No Transactions are available for given period");
							}
						}

						catch (BankException e) {
							System.err.println("Error  :" + e.getMessage());
						}
						continue loop3;

					case 'c':
						// Method for Request for change in communication
						// address
						customerBean = new CustomerBean();

						System.out.println("Your current address is:");
						customerBean = getAddressDetails(auth.getAccountId());

						if (customerBean != null) {
							System.out.println(customerBean.getAddress());
							System.out
									.println("please provide your new address details:");

						} else {
							System.err
									.println("There are no Address details associated with Account id "
											+ auth.getAccountId());
						}

						try {
							customerBean = UpdateAddress();
							CustomerService = new CustomerService();

							updateAddress = new CustomerService();
							customerBean.setAccountId(auth.getAccountId());
							customerId = updateAddress
									.updateCustomerAddress(customerBean);
							if (customerId == 1) {
								System.out
										.println("Your address has been successfully updated");
							}
						} catch (BankException bankexception) {
							logger.error("exception occured", bankexception);
							System.out.println("ERROR : "
									+ bankexception.getMessage());
						} finally {
							customerId = 0;
							customerBean = null;

						}

						continue loop3;

					case 'd':
						// Method for Request for change in email

						customerBean = new CustomerBean();

						customerBean.setAccountId(auth.getAccountId());
						System.out.println("Your current email is:");
						customerBean = getEmailDetails(auth.getAccountId());

						if (customerBean != null) {
							System.out.println(customerBean.getEmail());
							System.out
									.println("please provide your new email details:");

						} else {
							System.err
									.println("There are no email details associated with Account id "
											+ auth.getAccountId());
						}

						try {

							customerBean = UpdateEmail();
							CustomerService = new CustomerService();

							updateAddress = new CustomerService();
							customerBean.setAccountId(auth.getAccountId());
							customerId = updateAddress
									.updateEmail(customerBean);
							if (customerId == 1) {
								System.out
										.println("Your email has been successfully updated");
							}
						} catch (BankException bankexception) {
							logger.error("exception occured", bankexception);
							System.out.println("ERROR : "
									+ bankexception.getMessage());
						} finally {
							customerId = 0;
							customerBean = null;

						}

						continue loop3;

					case 'e':
						// Method for Request for cheque book
						String c = sc.nextLine();
						ServiceTracker reqChequeService = new ServiceTracker();
						int requestId = 0;
						ServiceTrackerBean chequeRequest = new ServiceTrackerBean();

						System.out
								.println("Enter Description( Service description should contain atleast 3 letters):");
						String serviceDescription = sc.nextLine();
						//System.out.println(serviceDescription);
						chequeRequest.setServiceDescription(serviceDescription);
						chequeRequest.setAccountId(auth.getAccountId());
						chequeRequest.setServiceStatus("Open");
						try {
							requestId = reqChequeService
									.reqChequeBook(chequeRequest);
							System.out
									.println("Your request has been successful. Your Service Request id is "
											+ requestId);

						} catch (BankException e) {

							System.err.println("Error :" + e.getMessage());
						}
						continue loop3;

					case 'f':
						// Method for Track service request
						System.out.println("Select one choice");
						System.out
								.println("1. Track Request on basis of Service Request Number");
						System.out
								.println("2. Track Request on basis of Account Number");
						
						int choice3 = 0;
						boolean validChoice3=true;
						do{
						try{
						sc=new Scanner(System.in);	
						choice3 = sc.nextInt();
						validChoice3=true;
						}
						catch(InputMismatchException e)
						{
							System.out.println("Enter only number");
							validChoice3=false;
						}}while(!validChoice3);
						ServiceTracker trackRequestService = new ServiceTracker();
						ServiceTrackerBean serviceTracker = new ServiceTrackerBean();
						serviceTracker.setAccountId(auth.getAccountId());
						switch (choice3) {
						case 1:
							System.out.println("Enter Service Request Number");
							int serviceRequestNumber = 0;
							boolean validrequest= true;
							do{
				    	    	 try{
				    	    		 sc = new Scanner(System.in);
				    	    		 serviceRequestNumber=sc.nextInt();
				    	    		 validrequest=true;
				    	    	 }
				    	    	 catch(InputMismatchException e)
				    			  {
				    				  System.out.println("Enter only number");
				    				  validrequest=false;
				    			  }
				    	     }while(!validrequest);
							serviceTracker.setServiceId(serviceRequestNumber);
							ServiceTrackerBean request = null;
							try {
								request = trackRequestService
										.trackServiceRequest(serviceTracker);
								if (request != null) {
									System.out
											.println("--------------------------------------------------------------");
									System.out
											.println("ServiceId       Description      Service Raised Date   Status");
									System.out.println(request);
									System.out
											.println("--------------------------------------------------------------");
								} else {
									System.out
											.println("No record found with provided id for your account");
								}
							} catch (BankException e) {

								System.err.println("Error  :" + e.getMessage());
							}

							continue loop3;

						case 2:

							List<ServiceTrackerBean> list = new ArrayList<ServiceTrackerBean>();
							try {

								list = trackRequestService
										.trackAllRequest(serviceTracker);
								Iterator<ServiceTrackerBean> itr = list
										.iterator();
								if (!list.isEmpty()) {
									System.out
											.println("--------------------------------------------------------------");
									System.out
											.println("ServiceId        Description      Service Raised Date   Status");
									while (itr.hasNext()) {
										System.out.println(itr.next());
									}
									System.out
											.println("--------------------------------------------------------------");
								} else {
									System.out
											.println("No service requests are found for the given account number");
								}
							} catch (BankException e) {

								System.err.println("Error  :" + e.getMessage());
							}
							continue loop3;
							
							default: System.out.println("Enter correct option");
						}

						continue loop3;

					case 'g':
						// Method for Fund Transfer

						System.out
								.println("1. Transfer to your own bank account across India");
						System.out
								.println("2. Transfer to account of same bank account across India");

						fund_option = sc.nextInt();
						switch (fund_option) {

						case 1:
							// populate details

							while (fundBean == null) {
								System.out.println("Details populating");
								fundBean = populateDetails(fundBean);

							}
							System.out.println("Enter your nickname");
							String nickname = sc.next();
							System.out
									.println("Enter your Transaction Password");
							String tranPassword = sc.next();

							fundTransferBean = new FundTransferBean(
									fundBean.getAccountId(),
									fundBean.getPayeeAccountId(),
									fundBean.getTransferAmount());

							PayeeBean payeeSameAccount = new PayeeBean(
									fundBean.getAccountId(),
									fundBean.getPayeeAccountId(), nickname);

							try {
								fundTransferService = new FundTransferService();

								// checkpayee() returns 1 if payee is not
								// registered

								if (fundTransferService
										.checkPayee(payeeSameAccount) == 1) {

									// if payee is not registered
									System.out
											.println("Registering your account..");
									TimeUnit.SECONDS.sleep(2);

									// adding payee to payeetable
									addPayee = fundTransferService
											.addPayee(payeeSameAccount);

									// update in fund_transfer atble and
									// transactions table
									doTransactionOwnAccount(
											fundBean.getAccountId(),
											tranPassword,
											fundBean.getPayeeAccountId(),
											fundBean.getTransferAmount());

								} else {

									do {
										if (fundTransferService
												.checkPayee(payeeSameAccount) == 0) {
											System.out
													.println("Press 1 to pay");
											int pay_option = sc.nextInt();

											do {
												if (pay_option == 1) {
													doTransactionOwnAccount(
															fundBean.getAccountId(),
															tranPassword,
															fundBean.getPayeeAccountId(),
															fundBean.getTransferAmount());
													break;
												} else {
													System.err
															.println("Sorry!!You need to press 1 to pay");
													pay_option = sc.nextInt();
												}
											} while (true);
											break;
										}

										else {
											System.out
													.println("The Nickname doesn't match with the Payee");
											System.out
													.println("Enter nickname again");
											nickname = sc.next();
											payeeSameAccount
													.setNickName(nickname);
										}
									} while (true);

								}

							} catch (BankException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {

								e.printStackTrace();
							}
							continue loop3;

						case 2:
						 
							while (fundBean2 == null) {
								System.out.println("details populating");
								fundBean2 = populateDetails(fundBean2);

							}
							System.out.println("Enter Nickname");
							String NickName = sc.next();
 
							char continue_URN = 0;
							PayeeBean payeeBean = new PayeeBean(
							fundBean2.getAccountId(),
									fundBean2.getPayeeAccountId(), NickName);

							try {
								fundTransferService = new FundTransferService();

								System.out.println(fundTransferService
										.checkPayee(payeeBean));
								if (fundTransferService.checkPayee(payeeBean) == 1) {

									System.out
											.println("Payee is not registered..Payee Registration is in pending status");
									String Dummy_URN = "abc345";
									System.out.println("Your URN is "
											+ Dummy_URN);
									System.out
											.println("To confirm payee registrartion enter your dummy URN");
									String user_URN = sc.next();

									do {
										if (Dummy_URN.equals(user_URN)) {

											System.out
													.println("Payee Registration in Progress..");

											payee = fundTransferService
													.addPayee(payeeBean);

											System.out
													.println("Payee Registration Confirmed");
											System.out
													.println("Enter transaction Password");
											String tranPwd = sc.next();

											doTransaction(
													fundBean2.getAccountId(),
													tranPwd,
													fundBean2
															.getPayeeAccountId(),
													fundBean2
															.getTransferAmount());
											break;
										} else {
											logger.error("Invalid URN");
											System.out
													.println("Please enter valid URN");
											System.out
													.println("Do you want to continue?Press Yes or No");
											if (continue_URN == 'n'
													|| continue_URN == 'N')
												break;
											System.out
													.println("Enter your URN again");
											user_URN = sc.next();
										}
									} while (true);

								} else {
									do {
										if (fundTransferService
												.checkPayee(payeeBean) == 0) {

											System.out
													.println("1. Select Pay Option");
											int pay_option = sc.nextInt();
										
											System.out
													.println("Enter Transaction Password");
											String tranPwd = sc.next();

											do {
												if (pay_option == 1) {

													doTransaction(
															fundBean2
																	.getAccountId(),
															tranPwd,
															fundBean2
																	.getPayeeAccountId(),
															fundBean2
																	.getTransferAmount());
													break;
												} else {
													System.err
															.println("Sorry!!You need to press 1 to pay");
													pay_option = sc.nextInt();
												}
											} while (true);

											break;
										} else {
											System.out
													.println("The Nickname doesn't match with the Payee");
											System.out
													.println("Enter nickname again");
											NickName = sc.next();
											payeeBean.setNickName(NickName);
										}
									} while (true);

								}
							} catch (BankException e) {

								e.printStackTrace();
							}
							continue loop3;
						default:
							System.out.println("Please Choose a Valid Option!");
							break;
						}// end of fund_option

						continue loop3;

					case 'h':
						// Method for Change password
						loginBean = new UserBean();

						userService = new UserService();

						loginBean = changePassword();
						try {

							if (loginBean.getSecretQuestion().equals(
									auth.getSecretQuestion())
									&& loginBean.getSecretAnswer().equals(
											auth.getSecretAnswer())) {

								System.out.println("Enter your old Password: ");
								loginBean.setLoginPassword(sc.next());

								if (loginBean.getLoginPassword().equals(
										auth.getLoginPassword())) {
									System.out
											.println("\nSet your Login Password with specifications given below.\n");
									System.out
											.println("** A digit must occur at least once");
									System.out
											.println("** An alphabet must occur at least once");
									System.out
											.println("** A special character must occur at least once");
									System.out
											.println("** At least 6 characters\n");
									System.out
											.println("Enter your new Password: ");
									auth.setLoginPassword(sc.next());

									try {
										if (!userService
												.validateUserCredentials(auth)) {
											System.out
													.println("Please set your password with above specifications!");
										}
									} catch (BankException bankexception) {
										logger.error("exception occured",
												bankexception);
										System.err.println("Invalid data:");
										System.err
												.println(bankexception
														.getMessage()
														+ "\nTry again..");
									}

									// sending to user service layer
									userService.setNewLoginPassword(auth,
											auth.getLoginPassword());
									System.out
											.println("Your Password is set successfully.");
									logger.info("User Id-"
											+ auth.getUserId()
											+ ": New Login Password is set successfully.");
								} else {
									System.out.println("Wrong Password!");
									logger.info("User Id-" + auth.getUserId()
											+ ": Wrong Password!");
								}
							} else {
								System.out
										.println("Wrong Verification Details!");
								logger.info("User Id-" + auth.getUserId()
										+ ": Wrong Verification Details!");
							}
						} catch (BankException bankException) {
							bankException.getMessage();
						}

						continue loop3;
					case 'i':

						System.out.println("Logging Out..");
						try {
							TimeUnit.SECONDS.sleep(2);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						continue loop1;

					default:
						System.out.println("Invalid Option!");
						logger.info("Invalid Option chosen!");

						break;
					}
					continue loop3;
				}
				// -----------------------------------------------------------------------

			case 2:
				// Bank Admin
				
				System.out.println("------------------------------------------------------");
				
				do {
					System.out.println("Enter User Name: ");
					String username = sc.next();
				System.out.println("Enter Password: ");
				String password = sc.next();

				if ("admin".equals(username) && "admin".equals(password)) {
					System.out.println("------------------------------------------------------");
					System.out.println("\n Welcome Admin!");
					System.out.println("\n 1. Create a new Account");
					System.out
							.println("\n 2. View Transactions of all users");
					System.out.println("\n 3. Log Out");
                    boolean validChoice=true;
					do{
						try{
							sc = new Scanner(System.in);
					      choose = sc.nextInt();
					      validChoice=true;
						}catch(InputMismatchException e)
						{
							System.out.println("Please enter number");
							validChoice=false;
						}
					}while(!validChoice);
					switch (choose) {
					case 1:

						while (customerBean == null
								&& accountMasterBean == null
								&& userBean == null) {
							customerBean = populateCustomerdetails();
							accountMasterBean = populateAccountdetails();
								userBean = populateUserdetails();
								// Now Write methods in UI
							}
							try {
								accountMasterService = new AccountMasterService();
								CustomerService = new CustomerService();

								accId = accountMasterService
										.addAccountDetails(accountMasterBean);
								customerBean = CustomerService
										.addCustomerDetails(customerBean, accId);
								userBean = userService.UserDetails(userBean,
										accId);

								System.out.println("In Customer Email Id: \n");
								System.out
										.println("Congratulations! Your personal details have been successfully registered. ");
								System.out
										.println("Please use the following Login details for login and change your password.\n");

								System.out.println("Account ID : "
										+ userBean.getAccountId());
								System.out.println("User ID : "
										+ userBean.getUserId());
								System.out.println("Password : "
										+ userBean.getLoginPassword());
								logger.info("User Id-"
										+ userBean.getUserId()
										+ ": New Account created by bank admin.");

							} catch (BankException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								accId = 0;
								customerBean = null;
							}

							break;

						case 2:
							// Write two functionalities here
							System.out.println("\ta.View daily Transactions");
							System.out
									.println("\n\tb.View monthly or yearly Transactions");

							opt = sc.next().charAt(0);

							switch (opt) {

							case 'a':
								System.out
										.print("Enter the date for which you want to view the transactions in [dd/MM/yyyy] format :");

								boolean validDate=true;
								LocalDate daily=null;
								DateTimeFormatter formatter = DateTimeFormatter
										.ofPattern("dd/MM/yyyy");
								do{
								String sDate = sc.next();
								try{
							    daily = LocalDate.parse(sDate,formatter);
							    validDate=true;
								}
								catch(DateTimeParseException e)
								{
									System.out.println("Please enter date in [dd/MM/yyyy] format");
									validDate=false;
								}
								}while(!validDate);
								
								List<TransactionsBean> transactionlist = new ArrayList<>();
								transactionbean = new TransactionsBean();
								transactionbean.setDateOfTransaction(daily);
								LocalDate dateOfTransaction = transactionbean
										.getDateOfTransaction();
								transactionlist = getMonthlyTransactions(dateOfTransaction);
								TransactionsBean temp = new TransactionsBean();
								Iterator<TransactionsBean> iterator = transactionlist
										.iterator();

								if (!transactionlist.isEmpty()) {

									System.out.println("");
									System.out.println("");

									System.out
											.println("The transaction details for the given date "
													+ dateOfTransaction
													+ " are shown below");
									System.out.println("");
									System.out.println("");

									System.out
											.format("+-----------+------------+---------------------+-----------------+-----------------+----------+%n");
									System.out
											.format("|transId    |tranDesc    |transDate            |transType        |transAmount      |accountId |%n");
									System.out
											.format("+-----------+------------+---------------------+-----------------+-----------------+----------+%n");

									while (iterator.hasNext()) {

										temp = iterator.next();
										System.out.println(temp);

									}
									System.out
											.format("+-----------+-------------+----------------------+--------------------+------------------------+%n");

								}
								break;
							case 'b':
								boolean validDates=true;
								LocalDate stdate=null;
								LocalDate endate=null;
								System.out
										.print("Enter the start date and end date for which you want to view the transactions in [dd/MM/yyyy] format :");
								System.out.println("");
								do{
								System.out.println("Enter starting date");
								String stDate = sc.next();
								System.out.println("");
								System.out.println("Enter ending date");
								String enDate = sc.next();
								DateTimeFormatter formatter1 = DateTimeFormatter
										.ofPattern("dd/MM/yyyy");
								try{
								stdate = LocalDate.parse(stDate,
										formatter1);
								endate = LocalDate.parse(enDate,
										formatter1);
								validDates=true;
								}
								catch(DateTimeParseException e)
								{
									System.out.println("Please enter dates in valid format [dd/MM/yyyy]");
									validDates=false;
								}
								}while(!validDates);
								List<TransactionsBean> mytransactionlist = new ArrayList<>();
								transactionbean = new TransactionsBean();
								transactionlist = getMyTransactions(stdate,
										endate);
								TransactionsBean data = new TransactionsBean();
								Iterator<TransactionsBean> dataiterator = transactionlist
										.iterator();

								if (!transactionlist.isEmpty()) {

									System.out.println("");
									System.out.println("");

									System.out
											.println("The transaction details between the dates "
													+ stdate
													+ " and "
													+ endate
													+ "are shown below");
									System.out.println("");
									System.out.println("");

									System.out
											.format("+-----------+------------+---------------------+-----------------+-----------------+----------+%n");
									System.out
											.format("|transId    |tranDesc    |transDate            |transType        |transAmount      |accountId |%n");
									System.out
											.format("+-----------+------------+---------------------+-----------------+-----------------+----------+%n");

									while (dataiterator.hasNext()) {

										data = dataiterator.next();
										System.out.println(data);

									}
									System.out
											.format("+-----------+-------------+----------------------+--------------------+------------------------+%n");

								}
								break;
							
							}

							break;						 
			// -----------------------------------------------------------------------		
							//Logout from Admin account
					case 3:
							
							System.out.println("Logging Out..");
							try {
								TimeUnit.SECONDS.sleep(2);
								logger.info("Logged out of admin account.");
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							continue loop1;
					

					default:
						System.out.println("Please choose valid option!");
						logger.info("Invalid Option chosen!");

						break;							
					 
						}   //End of (choose from 3) switch statement	
					break;
										
					} else {
						System.out
								.println("Invalid Username/Password! \nPlease enter the valid credentials.\n");
						logger.info("Invalid Username/Password.");
					}	
					
				} while (true);
				 break;
			}

		} while (true);

	}

	private static UserBean forgotPassword() {

		UserBean userBean = new UserBean();

		userBean.setUserId(0);

		do {
			try {
				System.out.println("\nPlease Enter User Id: ");
				userBean.setUserId(sc.nextInt());

			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("Please Enter numeric value for UserId!");
			}

		} while (userBean.getUserId() <= 0);

		System.out.println("Select your Secret Question: \n");

		System.out.println("1. What is your favourite destination? ");
		System.out.println("2. What is your pet's name? ");
		System.out.println("3. What is your birth place? ");

		int ques = sc.nextInt();

		String setQues = null;
		while (true) {
			if (ques == 1) {
				setQues = "What is your favourite destination? ";
				break;
			} else if (ques == 2) {
				setQues = "What is your pet's name? ";
				break;
			} else if (ques == 3) {
				setQues = "What is your birth place? ";
				break;
			} else {
				System.out.println("Invalid Option");
				logger.info("Invalid Account Type Option.");
			}
		}
		userBean.setSecretQuestion(setQues);

		System.out.println("Enter your Answer:");
		userBean.setSecretAnswer(sc.next());

		return userBean;
	}

	private static UserBean populateUserdetails() {
		UserBean userBean = new UserBean();

		System.out.println("\nEnter Details for Security Credentials");

		System.out.println("Select your Secret Question");

		System.out.println("1. What is your favourite destination? ");
		System.out.println("2. What is your pet's name? ");
		System.out.println("3. What is your birth place? ");

		int ques = sc.nextInt();

		String setQues = null;
		while (true) {
			if (ques == 1) {
				setQues = "What is your favourite destination? ";
				break;
			} else if (ques == 2) {
				setQues = "What is your pet's name? ";
				break;
			} else if (ques == 3) {
				setQues = "What is your birth place? ";
				break;
			} else {
				System.out.println("Invalid Option");
				logger.info("Invalid Account Type Option.");
			}
		}
		userBean.setSecretQuestion(setQues);

		System.out.println("Enter your Answer: ");
		userBean.setSecretAnswer(sc.next());

		System.out.println("Set your Transaction Password \n");
		System.out.println("** A digit must occur at least once");
		System.out.println("** An alphabet must occur at least once");
		System.out.println("** A special character must occur at least once");
		System.out.println("** At least 6 characters");
		userBean.setTransactionPassword(sc.next());

		userService = new UserService();

		try {
			if (userService.validateUserCredentials(userBean)) {
				return userBean;
			} else {
				return null;
			}
		} catch (BankException bankexception) {
			logger.error("exception occured", bankexception);
			System.err.println("Invalid data:");
			System.err.println(bankexception.getMessage() + "\nTry again..");
			System.exit(0);

		}
		return null;
	}

	private static AccountMasterBean populateAccountdetails() {
		AccountMasterBean accountMasterBean = new AccountMasterBean();

		System.out.println("\nEnter Details Related to Account Opening");

		System.out.println("Account Type: ");
		System.out.println("1. Savings Account");
		System.out.println("2. Current Account");
		System.out.println("Enter the Account Type you prefer: ");
		int AccTypeOpt = sc.nextInt();

		String type = null;
		while (true) {
			if (AccTypeOpt == 1) {
				type = "Savings";
				break;
			} else if (AccTypeOpt == 2) {
				type = "Current";
				break;
			} else {
				System.out.println("Invalid Option");
				logger.info("Invalid Account Type Option.");
			}
		}
		accountMasterBean.setAccountType(type);
        boolean validBalance=true;
		System.out.println("Enter Opening Balance Amount: ");
		int balance=0;
		do{
			try{
				Scanner sc = new Scanner(System.in);
				balance=sc.nextInt();
				validBalance=true;
			}catch(InputMismatchException e)
			{
				System.out.println("Please Enter Balance in number");
				validBalance=false;
			}
		}while(!validBalance);
		accountMasterBean.setAccountBalance(balance);

		accountMasterService = new AccountMasterService();

		try {
			if (accountMasterService.validateAccount(accountMasterBean)) {
				return accountMasterBean;
			} else {
				return null;
			}
		} catch (BankException bankexception) {
			logger.error("exception occured", bankexception);
			System.err.println("Invalid data:");
			System.err.println(bankexception.getMessage() + " \n Try again..");
			System.exit(0);

		}
		return null;

	}

	private static CustomerBean getAddressDetails(long AccountId) {

		CustomerBean customerbean = new CustomerBean();
		CustomerService = new CustomerService();

		try {
			customerbean = CustomerService.viewAddressDetails(AccountId);
		} catch (BankException e) {
			logger.error("exception occured", e);
			System.err.println("Invalid data:");
			System.err.println(e.getMessage() + " \n Try again..");
			System.exit(0);
		}

		CustomerService = null;
		return customerbean;
	}

	private static CustomerBean UpdateAddress() {

		CustomerBean customerbean = new CustomerBean();
		sc.nextLine();
		customerbean.setAddress(sc.nextLine());

		CustomerServiceImpl = new CustomerService();

		try {
			CustomerServiceImpl.validateAddress(customerbean);
			return customerbean;
		} catch (BankException bankexception) {
			logger.error("exception occured", bankexception);
			System.err.println("Invalid data:");
			System.err.println(bankexception.getMessage() + " \n Try again..");
			System.exit(0);

		}
		return null;

	}

	private static CustomerBean getEmailDetails(long AccountId) {

		CustomerBean customerbean = new CustomerBean();
		CustomerService = new CustomerService();

		try {
			customerbean = CustomerService.viewEmailDetails(AccountId);
		} catch (BankException e) {
			// TODO Auto-generated catch block
			logger.error("exception occured", e);
			System.err.println("Invalid data:");
			System.err.println(e.getMessage() + " \n Try again..");
			System.exit(0);
		}

		CustomerService = null;
		return customerbean;
	}

	private static CustomerBean UpdateEmail() {

		CustomerBean customerbean = new CustomerBean();
		customerbean.setEmail(sc.next());

		CustomerServiceImpl = new CustomerService();

		try {
			CustomerServiceImpl.validateEmail(customerbean);
			return customerbean;
		} catch (BankException bankexception) {
			logger.error("exception occured", bankexception);
			System.err.println("Invalid data:");
			System.err.println(bankexception.getMessage() + " \n Try again..");
			System.exit(0);

		}
		return null;

	}

	private static UserBean changePassword() {

		UserBean userBean = new UserBean();

		System.out.println("Select your Secret Question: \n");

		System.out.println("1. What is your favourite destination? ");
		System.out.println("2. What is your pet's name? ");
		System.out.println("3. What is your birth place? ");

		int ques = sc.nextInt();

		String setQues = null;
		while (true) {
			if (ques == 1) {
				setQues = "What is your favourite destination? ";
				break;
			} else if (ques == 2) {
				setQues = "What is your pet's name? ";
				break;
			} else if (ques == 3) {
				setQues = "What is your birth place? ";
				break;
			} else {
				System.out.println("Invalid Option");
				logger.info("Invalid Account Type Option.");
			}
		}
		userBean.setSecretQuestion(setQues);

		System.out.println("Enter your Answer:");
		userBean.setSecretAnswer(sc.next());

		return userBean;
	}

	private static UserBean enterUser() {

		UserBean loginBean = new UserBean();
		loginBean.setUserId(0);
		loginBean.setLoginPassword(null);

		do {
			try {
				System.out.println("\nPlease Enter User Id: ");
				loginBean.setUserId(sc.nextInt());

			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out.println("Please Enter numeric value for UserId!");
			}

		} while (loginBean.getUserId() <= 0);

		System.out.println("Please Enter Password: ");
		loginBean.setLoginPassword(sc.next());

		return loginBean;

	}

	private static CustomerBean populateCustomerdetails() {

		CustomerBean customerbean = new CustomerBean();

		System.out.println("\nEnter Details");

		System.out.println("Enter Customer name: ");
		customerbean.setCustomerName(sc.next());

		System.out.println("Enter Customer Email Address: ");
		customerbean.setEmail(sc.next());

		System.out.println("Enter Customer address: ");
		customerbean.setAddress(sc.next());

		System.out.println("Enter Customer pan card number: ");
		customerbean.setPanCard(sc.next());

		CustomerServiceImpl = new CustomerService();

		try {
			if (CustomerServiceImpl.validateCustomer(customerbean)) {
				return customerbean;
			} else {
				return null;
			}
		} catch (BankException bankexception) {
			logger.error("exception occured", bankexception);
			System.err.println("Invalid data:");
			System.err.println(bankexception.getMessage() + " \n Try again..");
			System.exit(0);

		}
		return null;

	}

	private static void doTransaction(long l, String tranPwd, long m,
			int other_bank_amount) throws BankException {
		char choose = 0;
		do {
			if (fundTransferService.checkTransactionPwd(l, tranPwd)) {

				System.out.println("Enter Transaction Description");
				String tran_desc = sc.next();

				System.out.println("Checking for account balance..");

				do {

					if (fundTransferService.checkAccountBalance(l,
							other_bank_amount)) {

						System.out.println("Checking for max transactions..");

						if (fundTransferService.checkMaxFunds(l,
								other_bank_amount)) {

							FundTransferBean fundTransfer = new FundTransferBean(
									l, m, other_bank_amount);
							System.out
									.println("Your Transfer is in progress..");
							fundTransferService.payPayee(fundTransfer);
							System.out.println("Your Transfer Completed");

							// Update in transaction Table
							fundTransferService.updateTransaction(l, m,
									tran_desc, other_bank_amount);

							break;
						} else {
							logger.error("Maximum limit reached");
							System.err
									.println("Sorry!You cannot transfer beacuse you reached the limit of 10Lakh for today");
							break;
						}

					} else {
						logger.error("Insufficient Balance");
						System.out
								.println("Sorry!No Sufficient Account Balance");
						System.out
								.println("Do you want to continue?If Yes..Press Y or else N");
						choose = sc.next().charAt(0);
						if (choose == 'N' || choose == 'n')
							break;
						System.out
								.println("Enter the amount to be transferred");
						other_bank_amount = sc.nextInt();

					}

				} while (true);

				break;

			} else {
				logger.error("Invalid Transaction Password");
				System.out
						.println("Sorry!!Transaction cannot be done because of Invalid transaction Password");

				System.out.println("Enter the transaction Password again");
				tranPwd = sc.next();
			}
		} while (true);
	}// end of dotransaction

	private static void doTransactionOwnAccount(long l, String tranPassword,
			long m, int transferAmount) throws BankException {
		char choose = 0;

		do {
			if (fundTransferService.checkTransactionPwd(l, tranPassword)) {

				System.out.println("Enter Transaction Description");
				String tran_desc = sc.next();

				System.out.println("Checking for account balance..");

				do {
					if (fundTransferService.checkAccountBalance(l,
							transferAmount)) {

						FundTransferBean fundTransfer = new FundTransferBean(l,
								m, transferAmount);
						System.out.println("Your Transfer is in progress..");

						fundTransferService.payPayee(fundTransfer);
						System.out.println("Your Transfer Completed");
						fundTransferService.updateTransaction(l, m, tran_desc,
								transferAmount);

						break;
					} else {
						System.out.println("No sufficient account balance");
						System.out.println("Do you want to continue?");
						choose = sc.next().charAt(0);
						if (choose == 'N' || choose == 'n')
							break;
						System.out
								.println("Enter the amount to be transferred");
						transferAmount = sc.nextInt();
					}
				} while (true);

				break;
			} else {
				logger.error("Invalid Transaction Password");
				System.err
						.println("Sorry!!Transaction cannot be done because of Invalid transaction Password");
				if (choose == 'N' || choose == 'n')
					break;
				System.out.println("Enter the transaction Password again");
				tranPassword = sc.next();
			}
		} while (true);
	}// end of doTransactionOwnAccount

	private static FundTransferBean populateDetails(
			FundTransferBean fundTransferBean) {

		// Reading and setting the values for the donorBean

		fundTransferBean = new FundTransferBean();
		// fundTransferBean.setAccountId(0);

		System.out.println("\n Enter Details");

		do {
			System.out.println("Enter your account number: ");

			try {
				fundTransferBean.setAccountId(sc.nextInt());
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out
						.println("Please enter numeric values for account Id");

			}
		} while (fundTransferBean.getAccountId() <= 0);

		do {
			System.out.println("Enter Payee account number ");
			try {
				fundTransferBean.setPayeeAccountId(sc.nextLong());
			} catch (InputMismatchException e) {
				sc.nextLine();
				System.out
						.println("Please enter numeric values for payee account Id");
			}
		} while (fundTransferBean.getPayeeAccountId() <= 0);

		System.out.println("Enter amount to transferred:");

		try {
			fundTransferBean.setTransferAmount(sc.nextInt());
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.err
					.println("Please enter a numeric value for donation amount, try again");
		}

		FundTransferService fundServiceImpl = new FundTransferService();

		try {
			fundServiceImpl.validateDetails(fundTransferBean);
			return fundTransferBean;
		} catch (BankException bankException) {
			logger.error("exception occured");
			System.err.println("Invalid data:");
			System.err.println(bankException.getMessage() + " \n Try again..");
			System.exit(0);

		}
		return null;

	}

	private static List<TransactionsBean> getMonthlyTransactions(
			LocalDate dateOfTransaction) {
		List<TransactionsBean> transactionsBean = new ArrayList<TransactionsBean>();
		transactionService = new TransactionService();

		try {
			transactionsBean = transactionService
					.viewDailyTransactionDetails(dateOfTransaction);
		} catch (BankException e) {

			logger.error("exception occured", e);
			System.err.println("Invalid data:");
			System.err.println(e.getMessage() + " \n Try again..");
			System.exit(0);
		}

		CustomerService = null;
		return transactionsBean;
	}

	private static List<TransactionsBean> getMyTransactions(LocalDate stdate,
			LocalDate endate) {
		List<TransactionsBean> transactionsBean = new ArrayList<TransactionsBean>();
		transactionService = new TransactionService();

		try {
			transactionsBean = transactionService.viewMyTransactionDetails(
					stdate, endate);
		} catch (BankException e) {

			logger.error("exception occured", e);
			System.err.println("Invalid data:");
			System.err.println(e.getMessage() + " \n Try again..");
			System.exit(0);
		}

		transactionService = null;
		return transactionsBean;
	}

}
