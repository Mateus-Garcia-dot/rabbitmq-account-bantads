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
public class AccountCUDConfiguration {

    public static final String createQueueName = "accountCUD.create";
    public static final String updateQueueName = "accountCUD.update";
    public static final String deleteQueueName = "accountCUD.delete";
    public static final String patchQueueName = "accountCUD.patch";

    @Bean
    public Queue AccountCudCreateQueueCreate() {
        return new Queue(createQueueName, true);
    }

    @Bean
    public Queue AccountCudUpdateQueueUpdate() {
        return new Queue(updateQueueName, true);
    }

    @Bean
    public Queue AccountCudDeleteQueueDelete() {
        return new Queue(deleteQueueName, true);
    }

    @Bean
    public Queue AccountCudPatchQueuePatch() {
        return new Queue(patchQueueName, true);
    }

    @Bean
    Binding AccountCudCreateBinding(Queue AccountCudCreateQueueCreate, DirectExchange exchange) {
        return BindingBuilder.bind(AccountCudCreateQueueCreate).to(exchange).with(createQueueName);
    }

    @Bean
    Binding AccountCudUpdateBinding(Queue AccountCudUpdateQueueUpdate, DirectExchange exchange) {
        return BindingBuilder.bind(AccountCudUpdateQueueUpdate).to(exchange).with(updateQueueName);
    }

    @Bean
    Binding AccountCudDeleteBinding(Queue AccountCudDeleteQueueDelete, DirectExchange exchange) {
        return BindingBuilder.bind(AccountCudDeleteQueueDelete).to(exchange).with(deleteQueueName);
    }

    @Bean
    Binding AccountCudPatchBinding(Queue AccountCudPatchQueuePatch, DirectExchange exchange) {
        return BindingBuilder.bind(AccountCudPatchQueuePatch).to(exchange).with(patchQueueName);
    }

}
