package com.bantads.accountOrchestrator.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(RabbitMqConfig.class)
@Configuration
public class AccountRConfiguration {

    public static final String createQueueName = "accountR.create";
    public static final String updateQueueName = "accountR.update";
    public static final String deleteQueueName = "accountR.delete";
    public static final String patchQueueName = "accountR.patch.consumer";
    public static final String deleteManagerQueueName = "accountR.delete.manager";

    @Bean
    public Queue createQueueCreate() {
        return new Queue(createQueueName, true);
    }

    @Bean
    public Queue updateQueueUpdate() {
        return new Queue(updateQueueName, true);
    }

    @Bean
    public Queue deleteQueueDelete() {
        return new Queue(deleteQueueName, true);
    }

    @Bean
    public Queue patchQueuePatch() {
        return new Queue(patchQueueName, true);
    }

    @Bean
    public Queue deleteManagerQueueDelete() {
        return new Queue(deleteManagerQueueName, true);
    }

    @Bean
    Binding createBinding(Queue createQueueCreate, DirectExchange exchange) {
        return BindingBuilder.bind(createQueueCreate).to(exchange).with(createQueueName);
    }

    @Bean
    Binding updateBinding(Queue updateQueueUpdate, DirectExchange exchange) {
        return BindingBuilder.bind(updateQueueUpdate).to(exchange).with(updateQueueName);
    }

    @Bean
    Binding deleteBinding(Queue deleteQueueDelete, DirectExchange exchange) {
        return BindingBuilder.bind(deleteQueueDelete).to(exchange).with(deleteQueueName);
    }

    @Bean
    Binding patchBinding(Queue patchQueuePatch, DirectExchange exchange) {
        return BindingBuilder.bind(patchQueuePatch).to(exchange).with(patchQueueName);
    }

    @Bean
    Binding deleteManagerBinding(Queue deleteManagerQueueDelete, DirectExchange exchange) {
        return BindingBuilder.bind(deleteManagerQueueDelete).to(exchange).with(deleteManagerQueueName);
    }
}
