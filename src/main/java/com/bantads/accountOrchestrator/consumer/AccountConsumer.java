package com.bantads.accountOrchestrator.consumer;

import com.bantads.accountOrchestrator.config.AccountUrlConfig;
import com.bantads.accountOrchestrator.config.MessagingConfig;
import com.bantads.accountOrchestrator.dto.AccountStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccountConsumer {

    @Autowired private AccountUrlConfig accountUrlConfig;
    @Autowired private RestTemplate restTemplate;

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(AccountStatus accountStatus) {
        try {
            switch (accountStatus.getStatus()) {
                case "CREATE" -> restTemplate.postForObject(accountUrlConfig.getAccountRFullUrl(), accountStatus.getAccount(), AccountStatus.class);
                case "UPDATE" -> restTemplate.put(accountUrlConfig.getAccountRFullUrl(), accountStatus.getAccount());
                case "DELETE" -> restTemplate.delete("%s/%d".formatted(accountUrlConfig.getAccountRFullUrl(), accountStatus.getAccount().getId()));
                default -> System.out.println("Account creation failed");
            }
        } catch(Exception e) {
            System.out.println("Account " + accountStatus.getStatus() + " failed: " + e.getMessage());
        }
    }
}
