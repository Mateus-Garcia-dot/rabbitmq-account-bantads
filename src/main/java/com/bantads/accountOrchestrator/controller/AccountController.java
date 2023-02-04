package com.bantads.accountOrchestrator.controller;

import com.bantads.accountOrchestrator.config.AccountUrlConfig;
import com.bantads.accountOrchestrator.config.MessagingConfig;
import com.bantads.accountOrchestrator.dto.Account;
import com.bantads.accountOrchestrator.dto.AccountStatus;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired private RabbitTemplate rabbitTemplate;
    @Autowired private RestTemplate restTemplate;
    @Autowired private AccountUrlConfig accountUrlConfig;


    @GetMapping()
    public Account[] getAccount() {
          return restTemplate.getForObject(accountUrlConfig.getAccountRFullUrl(), Account[].class);
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return restTemplate.getForObject("%s/%d".formatted(accountUrlConfig.getAccountRFullUrl(), id), Account.class);
    }

    @PostMapping()
    public Account createAccount(@RequestBody Account account) {
        AccountStatus accountStatus = new AccountStatus(account, "CREATE", "Added to queue");
        Account newAccount = restTemplate.postForObject(accountUrlConfig.getAccountCUDFullUrl(), account, Account.class);
        rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, accountStatus);
        return newAccount;
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable long id, @RequestBody Account account) {
        account.setId(id);
        restTemplate.put("%s/%d".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), account);
        AccountStatus accountStatus = new AccountStatus(account, "UPDATE", "Added to queue");
        rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, accountStatus);
        return account;
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        Account account = new Account();
        account.setId(id);
        restTemplate.delete("%s/%d".formatted(accountUrlConfig.getAccountCUDFullUrl(), id));
        AccountStatus accountStatus = new AccountStatus(account, "DELETE", "Added to the queue");
        rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, accountStatus);
    }

}
