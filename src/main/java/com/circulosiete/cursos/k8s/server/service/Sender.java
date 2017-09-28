package com.circulosiete.cursos.k8s.server.service;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Sender {

  @Autowired
  private RabbitMessagingTemplate rabbitMessagingTemplate;

  public void sendToRabbitmq(String msg){
    this.rabbitMessagingTemplate.convertAndSend("exchange", "index.queue", msg);
  }

}