package com.bantads.accountOrchestrator.controller;

import com.bantads.accountOrchestrator.dto.Account;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private RabbitTemplate rabbitTemplate;
    @Autowired private RestTemplate restTemplate;

    private static final String accountCUD = "http://localhost:3000/account";
    private static final String accountR = "http://localhost:3001/account";

    @GetMapping()
    public Account[] getAccount() {
        return restTemplate.getForObject(accountR,  Account[].class );
    }

    @PostMapping()
    public Account createAccount(@RequestParam Account account) {
        return restTemplate.postForObject(accountCUD, account, Account.class);
    }

    @PutMapping()
    public Account updateAccount(@RequestParam Account account) {
        restTemplate.put(accountCUD, account);
        return account;
    }

    @DeleteMapping()
    public Account deleteAccount(@RequestParam Account account) {
        restTemplate.delete(accountCUD, account);
        return account;
    }

}
