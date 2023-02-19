package com.bantads.accountOrchestrator.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RabbitMqConfig.class)
public class AccountOrchestratorConfiguration {

    public static final String createQueueName = "account.create";
    public static final String updateQueueName = "account.update";
    public static final String deleteQueueName = "account.delete";
    public static final String patchQueueName = "account.patch.consumer";
    public static final String deleteManagerQueueName = "account.delete.manager";

    @Bean
    public Queue AccountOrchestratorCreateQueueCreate() {
        return new Queue(createQueueName, true);
    }

    @Bean
    public Queue AccountOrchestratorUpdateQueueUpdate() {
        return new Queue(updateQueueName, true);
    }

    @Bean
    public Queue AccountOrchestratorDeleteQueueDelete() {
        return new Queue(deleteQueueName, true);
    }

    @Bean
    public Queue AccountOrchestratorPatchQueuePatch() {
        return new Queue(patchQueueName, true);
    }

    @Bean
    public Queue AccountOrchestratorDeleteManagerQueueDelete() {
        return new Queue(deleteManagerQueueName, true);
    }

    @Bean
    Binding AccountOrchestratorCreateBinding(Queue AccountOrchestratorCreateQueueCreate, DirectExchange exchange) {
        return BindingBuilder.bind(AccountOrchestratorCreateQueueCreate).to(exchange).with(createQueueName);
    }

    @Bean
    Binding AccountOrchestratorUpdateBinding(Queue AccountOrchestratorUpdateQueueUpdate, DirectExchange exchange) {
        return BindingBuilder.bind(AccountOrchestratorUpdateQueueUpdate).to(exchange).with(updateQueueName);
    }

    @Bean
    Binding AccountOrchestratorDeleteBinding(Queue AccountOrchestratorDeleteQueueDelete, DirectExchange exchange) {
        return BindingBuilder.bind(AccountOrchestratorDeleteQueueDelete).to(exchange).with(deleteQueueName);
    }

    @Bean
    Binding AccountOrchestratorPatchBinding(Queue AccountOrchestratorPatchQueuePatch, DirectExchange exchange) {
        return BindingBuilder.bind(AccountOrchestratorPatchQueuePatch).to(exchange).with(patchQueueName);
    }

    @Bean
    Binding AccountOrchestratorDeleteManagerBinding(Queue AccountOrchestratorDeleteManagerQueueDelete, DirectExchange exchange) {
        return BindingBuilder.bind(AccountOrchestratorDeleteManagerQueueDelete).to(exchange).with(deleteManagerQueueName);
    }
}
