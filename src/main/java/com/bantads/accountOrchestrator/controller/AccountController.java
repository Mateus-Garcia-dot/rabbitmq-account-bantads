package com.bantads.accountOrchestrator.controller;

import com.bantads.accountOrchestrator.dto.Account;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
