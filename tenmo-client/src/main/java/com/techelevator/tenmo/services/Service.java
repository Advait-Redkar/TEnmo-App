package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Service {
    private static String AUTH_TOKEN;
    private static String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public Service(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public void setAuthToken(String authToken) {
        AUTH_TOKEN = authToken;
    }

    public BigDecimal getCurrentUserBalance(String currentUserToken) {
        ResponseEntity<BigDecimal> balance = null;
        //user logs in app
        //gettoken--current user token
        //use restTemplate.exchange instead of getForObject
        try {
            balance = restTemplate.exchange(BASE_URL + "/account/balance", HttpMethod.GET, makeAuthEntity(currentUserToken), BigDecimal.class);
        } catch (RestClientResponseException ex) {
            System.out.println("not found");
        } catch (Exception e) {
            System.out.println("not found");
        }
        return balance.getBody();
    }

    public List<Transfer>getAllTransfers(String currentUserToken) throws Exception {
        List transfers=new ArrayList();
        try{
            Transfer[]temps=restTemplate.exchange(BASE_URL+"/account/transfer",HttpMethod.GET,makeAuthEntity(currentUserToken),Transfer[].class).getBody();
            transfers=Arrays.asList(temps);
        }catch (RestClientResponseException ex) {
            throw new Exception(ex.getRawStatusCode() + ex.getResponseBodyAsString());
        }
        return transfers;
    }

    public List<User> findAll(String currentUserToken) throws Exception {
        List users = new ArrayList();
        try {
            User[] temp = restTemplate.exchange(BASE_URL + "/account/users", HttpMethod.GET, makeAuthEntity(currentUserToken), User[].class).getBody();

            users = Arrays.asList(temp);
        } catch (RestClientResponseException ex) {
            throw new Exception(ex.getRawStatusCode() + ex.getResponseBodyAsString());
        }
        return users;
    }

    public int getAccountIdForUserId(int userId, String currentUserToken) throws Exception {
        Integer accountId;
        try{
            accountId=restTemplate.exchange(BASE_URL+"/account/"+userId,HttpMethod.GET,makeAuthEntity(currentUserToken),Integer.class).getBody();
        }catch (RestClientResponseException ex) {
            throw new Exception(ex.getRawStatusCode() + ex.getResponseBodyAsString());
        }
        return accountId;
    }
    public Transfer getTransferByTransferId(int transferId, String currentUserToken) throws Exception {
        Transfer transfer = null;
        try {
            transfer = restTemplate.exchange(BASE_URL+"/account/transfer/"+transferId, HttpMethod.GET, makeAuthEntity(currentUserToken), Transfer.class).getBody();
    }  catch (RestClientResponseException ex) {
            throw new Exception(ex.getRawStatusCode() + ex.getResponseBodyAsString());
        } return transfer;
    }


    public void createTransfer(int userIdTo, BigDecimal amount, String currentUserToken) throws Exception {
        Transfer transfer = new Transfer(userIdTo, amount);

        try {
            restTemplate.exchange(BASE_URL + "/account/transfer", HttpMethod.POST,
                    makeTransferEntity(transfer,currentUserToken), Transfer.class);
        } catch (RestClientResponseException ex) {
            throw new Exception(ex.getRawStatusCode() + ex.getResponseBodyAsString());
        }
        //pass in values transfer needs
    }
    public void createRequestTransfer(int userIdFrom, BigDecimal amount, String currentUserToken) throws Exception {
        Transfer transfer = new Transfer(userIdFrom, amount);

        try {
            restTemplate.exchange(BASE_URL + "/account/requesttransfer", HttpMethod.POST,  makeTransferEntity(transfer,currentUserToken), Transfer.class);
        } catch (RestClientResponseException ex) {
            throw new Exception(ex.getRawStatusCode() + ex.getResponseBodyAsString());
        }
        //pass in values transfer needs
    }

    private HttpEntity makeTransferEntity(Transfer transfer, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }


    private HttpEntity makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
