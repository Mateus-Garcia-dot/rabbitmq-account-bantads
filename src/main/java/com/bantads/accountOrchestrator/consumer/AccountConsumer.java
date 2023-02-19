package com.bantads.accountOrchestrator.consumer;

import com.bantads.accountOrchestrator.config.*;
import com.bantads.accountOrchestrator.model.Account;
import com.bantads.accountOrchestrator.model.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class AccountConsumer {

    private RabbitTemplate rabbitTemplate;
    private RestTemplate restTemplate;
    private AccountUrlConfig accountUrlConfig;

    @RabbitListener(queues = AccountOrchestratorConfiguration.createQueueName)
    public void createAccount(Account account) {
        Account newAccount = restTemplate.postForObject(accountUrlConfig.getAccountCUDFullUrl(), account, Account.class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, ManagerConfiguration.sortRequestQueueName,1 );
    }

    @RabbitListener(queues = AccountOrchestratorConfiguration.updateQueueName)
    public void updateAccount(Account account) {
        restTemplate.put("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), account.getUuid()), account);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
    }

    @RabbitListener(queues = AccountOrchestratorConfiguration.deleteQueueName)
    public void deleteAccount(String id) {
        Account account = new Account();
        account.setUuid(id);
        restTemplate.delete("%s/%d".formatted(accountUrlConfig.getAccountCUDFullUrl(), id));
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.createQueueName, account);
    }

    @RabbitListener(queues = AccountOrchestratorConfiguration.patchQueueName)
    public void patchAccount(Account account) {
        System.out.println(account);
        restTemplate.patchForObject("%s/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), account.getCustomer()), account, Account.class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.patchQueueName, account);
    }

    @RabbitListener(queues = ManagerConfiguration.sortResponseQueueName)
    public void sortResponse(@RequestBody Manager[] managers) {
        Account[] accountWithNoManager = restTemplate.getForObject("%s/no-managers".formatted(accountUrlConfig.getAccountRFullUrl()), Account[].class);
        if ((accountWithNoManager != null ? accountWithNoManager.length : 0) == 0) {
            return;
        }
        Account[] accountsWithManagers = restTemplate.getForObject("%s/count-manager".formatted(accountUrlConfig.getAccountRFullUrl()), Account[].class);
        List<Manager> managersNotOnAnyAccount = Arrays.stream(managers)
                .filter(manager -> Arrays.stream(accountsWithManagers)
                        .noneMatch(account -> account.getManager()
                        .equals(manager.getUuid())))
                .toList();
        if (!managersNotOnAnyAccount.isEmpty()) {
            Manager manager = managersNotOnAnyAccount.get(0);
            for (Account account : accountWithNoManager) {
                account.setManager(manager.getUuid());
                this.rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountOrchestratorConfiguration.updateQueueName, account);
            }
            return;
        }
        Account accountWithManagerWithLessAccounts = accountsWithManagers[0];
        for (Account account : accountWithNoManager) {
            account.setManager(accountWithManagerWithLessAccounts.getManager());
            this.rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountOrchestratorConfiguration.updateQueueName, account);
        }
    }

    @RabbitListener(queues = AccountOrchestratorConfiguration.deleteManagerQueueName)
    public void deleteManager(String id) {
        restTemplate.delete("%s/manager/%s".formatted(accountUrlConfig.getAccountCUDFullUrl(), id), Account[].class);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, AccountRConfiguration.deleteManagerQueueName, id);
        rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, ManagerConfiguration.sortRequestQueueName, 1);
    }

}
