package com.circulosiete.cursos.k8s.server.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

  @Bean
  Queue queueMessage() {
    return new Queue("index.queue", false);
  }

  @Bean
  Queue queueMessages() {
    return new Queue("index.queue", false);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange("exchange");
  }

  @Bean
  Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
    return BindingBuilder.bind(queueMessage).to(exchange).with("index.queue");
  }

  @Bean
  Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
    return BindingBuilder.bind(queueMessages).to(exchange).with("index.queue");
  }

}