package com.bantads.accountOrchestrator.consumer;

import com.bantads.accountOrchestrator.config.AccountOrchestratorConfiguration;
import com.bantads.accountOrchestrator.config.AccountRConfiguration;
import com.bantads.accountOrchestrator.config.AccountUrlConfig;
import com.bantads.accountOrchestrator.config.RabbitMqConfig;
import com.bantads.accountOrchestrator.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Service
@Data
@AllArgsConstructor
public class AccountConsumer {

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;
    private final AccountUrlConfig accountUrlConfig;

    @RabbitListener(queues = AccountOrchestratorConfiguration.createQueueName)
    public void createAccount(@RequestBody Account account) {
        Account newAccount = restTemplate.postForObject(accountUrlConfig.getAccountCUDFullUrl(), account, Account.class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
    }

    @RabbitListener(queues = AccountOrchestratorConfiguration.updateQueueName)
    public void updateAccount(@PathVariable long id, @RequestBody Account account) {
        account.setId(id);
        restTemplate.put("%s/%d".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), account);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
    }

    @RabbitListener(queues = AccountOrchestratorConfiguration.deleteQueueName)
    public void deleteAccount(@PathVariable Long id) {
        Account account = new Account();
        account.setId(id);
        restTemplate.delete("%s/%d".formatted(accountUrlConfig.getAccountCUDFullUrl(), id));
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
    }
}
