package com.bantads.accountOrchestrator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUrlConfig {

    @Value("${ACCOUNT_CUD_URL}") private String accountCUDUrl ;
    @Value("${ACCOUNT_CUD_PORT}") private String accountCUDPort;
    @Value("${ACCOUNT_R_URL}") private String accountRUrl;
    @Value("${ACCOUNT_R_PORT}") private String accountRPort;

    public String getAccountCUDFullUrl() {
        return "http://%s:%s/account".formatted(accountCUDUrl, accountCUDPort);
    }

    public String getAccountRFullUrl() {
        return "http://%s:%s/account".formatted(accountRUrl, accountRPort);
    }
}
