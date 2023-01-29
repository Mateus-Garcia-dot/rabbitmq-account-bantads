package com.bantads.accountOrchestrator.consumer;

import com.bantads.accountOrchestrator.config.MessagingConfig;
import com.bantads.accountOrchestrator.dto.AccountStatus;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class AccountConsumer {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(AccountStatus accountStatus) {
        System.out.println("Message received from queue: " + accountStatus);
    }
}
