package com.ia.operation.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class AMQPConfiguration {

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange("operation-exchange").durable(true).build();
    }

    @Bean
    public Queue projectionEventQueue() {
        return QueueBuilder.durable("projection-event-queue").build();
    }

    @Bean
    public Binding projectionEventBinding() {
        return BindingBuilder.bind(projectionEventQueue()).to(exchange()).with("event").noargs();
    }
    
    @Bean
    public Queue projectionCmdQueue() {
        return QueueBuilder.durable("projection-cmd-queue").build();
    }

    @Bean
    public Binding projectionCmdBinding() {
        return BindingBuilder.bind(projectionCmdQueue()).to(exchange()).with("cmd").noargs();
    }


    @Autowired
    public void configure(AmqpAdmin admin) {
        admin.declareExchange(exchange());
        admin.declareQueue(projectionEventQueue());
        admin.declareBinding(projectionEventBinding());
        admin.declareQueue(projectionCmdQueue());
        admin.declareBinding(projectionCmdBinding());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory, MessageConverter messageConverter) {
        final RabbitTemplate template = new RabbitTemplate();
        template.setExchange("operation-exchange");
        template.setConnectionFactory(factory);
        template.setMessageConverter(messageConverter);
        return template;
    }
    
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
}
