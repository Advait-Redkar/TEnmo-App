package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.Service;
import com.techelevator.view.ConsoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private Service service;
    private Account account;
    private User user;

    public static void main(String[] args) throws Exception {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new Service(API_BASE_URL));
        app.run();
        Scanner scan = new Scanner(System.in);
    }

    public App(ConsoleService console, AuthenticationService authenticationService, Service service) {
        this.console = console;
        this.authenticationService = authenticationService;
        this.service = service;
    }

    public void run() throws Exception {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() throws Exception {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance() {
        String token = currentUser.getToken();
        // TODO Auto-generated method stub
        BigDecimal balance = service.getCurrentUserBalance(token);
        System.out.println("Your current balance is: $" + balance);
    }

    private void viewTransferHistory() throws Exception {
        // TODO Auto-generated method stub
        String token = currentUser.getToken();
        List<Transfer> transfers = service.getAllTransfers(token);
        //int currentUserAcctId=service.getAccountIdForUserId(currentUser.getUser().getId(),token);
        System.out.println("-------------------------------------------\n" +
                "Transfers\n" +
                "ID          From/To                 Amount\n" +
                "-------------------------------------------");
        //assign service call result to a variable
        String transferType;
        String transferStatus = null;

        for (Transfer transfer : transfers) {

                if (transfer.getAccountTo() == service.getAccountIdForUserId(currentUser.getUser().getId(), token)) {
                    transferType = "To";
                    System.out.println(transfer.getTransferId() + "       " + transferType + ":  " + transfer.getUsernameTo() + "              $ " + transfer.getAmount());
                } else if (transfer.getAccountFrom() == service.getAccountIdForUserId(currentUser.getUser().getId(), token)) {
                    transferType = "From";
                    System.out.println(transfer.getTransferId() + "       " + transferType + ":  " + transfer.getUsernameTo() + "              $ " + transfer.getAmount());
                }
            }
            

                System.out.println("---------");
                System.out.print("Please enter transfer ID to view details (0 to cancel): ");
                Scanner scanner = new Scanner(System.in);
                String transferIdStr = scanner.next();
                int transferId = Integer.parseInt(transferIdStr);
                if (transferId == 0) {
                    mainMenu();
                } else {
                    Transfer transfer = new Transfer();
                    transfer = service.getTransferByTransferId(transferId, token);

                    System.out.println("_________________________________");
                    System.out.println("Transfer Details");
                    System.out.println("_________________________________");
                    System.out.println("Id: " + transfer.getTransferId());
                    System.out.println("From: " + transfer.getUsernameFrom());
                    System.out.println("To: " + transfer.getUsernameTo());
                    if (transfer.getAccountTo() == service.getAccountIdForUserId(currentUser.getUser().getId(), token)) {
                        transferType = "Send";
                    } else if (transfer.getAccountFrom() == service.getAccountIdForUserId(currentUser.getUser().getId(), token)) {
                        transferType = "Receive";
                        System.out.println("Type: " + transferType);
                        if (transfer.getTransferStatusId() == 2) {
                            transferStatus = "Approved";
                        } else if (transfer.getTransferStatusId() == 1) ;
                        transferStatus = "Pending";
                    } else transferStatus = "Rejected";
                    System.out.println("Status: " + transferStatus);
                    System.out.println("Amount: $" + transfer.getAmount());
                    System.out.println("_________________________________");


                }
            }


            private void viewPendingRequests () {
                // TODO Auto-generated method stub

            }
            @ResponseStatus
            private void sendBucks () throws Exception {
                String token = currentUser.getToken();

                System.out.println("*********************************************" +
                        "\nUsers                                      " +
                        "\nID              Name                   ");


                List<User> users = service.findAll(token);
                for (User user : users) {
                    //Added line of code (108) below to make sure only other accounts are available for sendBucks()
                    if (!user.getId().equals(currentUser.getUser().getId()))
                        System.out.println(user.getId() + "            " + user.getUsername());
                }

                System.out.print("**************************************************" +
                        "\nEnter ID of user you are sending to (0 to cancel): ");

                Scanner scanner = new Scanner(System.in);
                String inputIdStr = scanner.next();
                int inputId = Integer.parseInt(inputIdStr);
                if (inputId == 0) {
                    mainMenu();
                }
                System.out.print("Enter amount: ");
                String inputAmtStr = scanner.next();
                BigDecimal amtBD = new BigDecimal(inputAmtStr);
                service.createTransfer(inputId, amtBD, token);

                // TODO Auto-generated method stub

            }

            private void requestBucks () throws Exception {
                String token = currentUser.getToken();

                System.out.println("*********************************************" +
                        "\nUsers                                      " +
                        "\nID              Name                   ");


                List<User> users = service.findAll(token);
                for (User user : users) {
                    //Added line of code (108) below to make sure only other accounts are available for sendBucks()
                    if (!user.getId().equals(currentUser.getUser().getId()))
                        System.out.println(user.getId() + "            " + user.getUsername());
                }

                System.out.print("**************************************************" +
                        "\nEnter ID of user you are requesting from (0 to cancel): ");

                Scanner scanner = new Scanner(System.in);
                String inputIdStr = scanner.next();
                int inputId = Integer.parseInt(inputIdStr);
                if (inputId == 0) {
                    mainMenu();
                }
                //try-catch for reading input
                System.out.print("Enter amount: ");
                String inputAmtStr = scanner.next();
                BigDecimal amtBD = new BigDecimal(inputAmtStr);
                service.createRequestTransfer(inputId, amtBD, token);

                // TODO Auto-generated method stub

            }


            private void exitProgram () {
                System.exit(0);
            }

            private void registerAndLogin () {
                while (!isAuthenticated()) {
                    String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
                    if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                        login();
                    } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                        register();
                    } else {
                        // the only other option on the login menu is to exit
                        exitProgram();
                    }
                }
            }

            private boolean isAuthenticated () {
                return currentUser != null;
            }

            private void register () {
                System.out.println("Please register a new user account");
                boolean isRegistered = false;
                while (!isRegistered) //will keep looping until user is registered
                {
                    UserCredentials credentials = collectUserCredentials();
                    try {
                        authenticationService.register(credentials);
                        isRegistered = true;
                        System.out.println("Registration successful. You can now login.");
                    } catch (AuthenticationServiceException e) {
                        System.out.println("REGISTRATION ERROR: " + e.getMessage());
                        System.out.println("Please attempt to register again.");
                    }
                }
            }

            private void login () {
                System.out.println("Please log in");
                currentUser = null;
                while (currentUser == null) //will keep looping until user is logged in
                {
                    UserCredentials credentials = collectUserCredentials();
                    try {
                        currentUser = authenticationService.login(credentials);
                    } catch (AuthenticationServiceException e) {
                        System.out.println("LOGIN ERROR: " + e.getMessage());
                        System.out.println("Please attempt to login again.");
                    }
                }
            }

            private UserCredentials collectUserCredentials () {
                String username = console.getUserInput("Username");
                String password = console.getUserInput("Password");
                return new UserCredentials(username, password);
            }
        }
