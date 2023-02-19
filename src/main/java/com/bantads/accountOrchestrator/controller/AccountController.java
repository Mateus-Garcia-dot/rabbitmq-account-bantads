package com.bantads.accountOrchestrator.controller;

import com.bantads.accountOrchestrator.config.AccountRConfiguration;
import com.bantads.accountOrchestrator.config.AccountUrlConfig;
import com.bantads.accountOrchestrator.config.ManagerConfiguration;
import com.bantads.accountOrchestrator.config.RabbitMqConfig;
import com.bantads.accountOrchestrator.model.Account;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Account[]> getAccount() {
          return  ResponseEntity.ok(restTemplate.getForObject(accountUrlConfig.getAccountRFullUrl(), Account[].class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(restTemplate.getForObject("%s/%s".formatted(accountUrlConfig.getAccountRFullUrl(), id), Account.class));
    }

    @PostMapping()
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account newAccount = restTemplate.postForObject(accountUrlConfig.getAccountCUDFullUrl(), account, Account.class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
        return ResponseEntity.ok(newAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable String id, @RequestBody Account account) {
        account.setUuid(id);
        restTemplate.put("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), account);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.updateQueueName, account);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable String id) {
        Account account = new Account();
        account.setUuid(id);
        restTemplate.delete("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id));
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.deleteQueueName, account);
        return ResponseEntity.ok("Account deleted");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Account> patchAccount(@PathVariable String id, @RequestBody Account account) {
        account.setUuid(id);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.patchQueueName, account);
        Account patchedAccount = restTemplate.patchForObject("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), account, Account.class);
        return ResponseEntity.ok(patchedAccount);
    }

    @DeleteMapping("/manager/{id}")
    public void deleteManager(@PathVariable String id) {
        restTemplate.delete("%s/manager/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), Account[].class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.deleteManagerQueueName, id);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, ManagerConfiguration.sortRequestQueueName, 1);
    }
}
