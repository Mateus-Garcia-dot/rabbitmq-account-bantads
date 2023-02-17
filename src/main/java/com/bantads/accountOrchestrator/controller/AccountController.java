package com.bantads.accountOrchestrator.controller;

import com.bantads.accountOrchestrator.config.AccountRConfiguration;
import com.bantads.accountOrchestrator.config.AccountUrlConfig;
import com.bantads.accountOrchestrator.config.RabbitMqConfig;
import com.bantads.accountOrchestrator.model.Account;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Data
@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private RabbitTemplate rabbitTemplate;
    private RestTemplate restTemplate;
    private AccountUrlConfig accountUrlConfig;


    @GetMapping()
    public Account[] getAccount() {
          return restTemplate.getForObject(accountUrlConfig.getAccountRFullUrl(), Account[].class);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return restTemplate.getForObject("%s/%s".formatted(accountUrlConfig.getAccountRFullUrl(), id), Account.class);
    }

    @PostMapping()
    public Account createAccount(@RequestBody Account account) {
        Account newAccount = restTemplate.postForObject(accountUrlConfig.getAccountCUDFullUrl(), account, Account.class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
        return newAccount;
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable String id, @RequestBody Account account) {
        account.setUuid(id);
        restTemplate.put("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), account);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
        return account;
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable String id) {
        Account account = new Account();
        account.setUuid(id);
        restTemplate.delete("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id));
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
    }

    @PatchMapping("/{id}")
    public void patchAccount(@PathVariable String id, @RequestBody Account account) {
        account.setUuid(id);
        restTemplate.patchForObject("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), account, Account.class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
    }

}
