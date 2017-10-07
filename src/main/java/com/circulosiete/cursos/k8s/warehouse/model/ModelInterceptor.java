package com.circulosiete.cursos.k8s.warehouse.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.event.spi.EventType.*;


@Slf4j
@Component
public class ModelInterceptor implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {

  private final RabbitMessagingTemplate rabbitMessagingTemplate;
  private final ObjectMapper objectMapper;

  public ModelInterceptor(RabbitMessagingTemplate rabbitMessagingTemplate, ObjectMapper objectMapper) {
    this.rabbitMessagingTemplate = rabbitMessagingTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onPostInsert(PostInsertEvent event) {
    notify(event.getEntity(), POST_INSERT.eventName());
  }

  @Override
  public void onPostDelete(PostDeleteEvent event) {
    notify(event.getEntity(), POST_DELETE.eventName());
  }

  @Override
  public void onPostUpdate(PostUpdateEvent event) {
    notify(event.getEntity(), POST_UPDATE.eventName());
  }

  @Override
  public boolean requiresPostCommitHanding(EntityPersister persister) {
    return false;
  }

  private void notify(Object entity, String event) {
    String entityName = entity.getClass().getName();
    log.info("{} de {}", event, entityName);

    Map props = objectMapper.convertValue(entity, Map.class);

    Map payload = Collections.unmodifiableMap(
      Stream.of(
        entry("entity", (Object) entityName),
        entry("event", (Object) event),
        entry("data", (Object) props))
        .collect(entriesToMap())
    );

    String json;
    try {
      json = objectMapper.writeValueAsString(payload);
      log.info("Se enviara: {}", json);
      rabbitMessagingTemplate.convertAndSend("exchange", "index.queue", json);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }

  }

  //TODO: mover esto a otra clase de utileria
  public static <K, V> Map.Entry<K, V> entry(K key, V value) {
    return new AbstractMap.SimpleEntry<>(key, value);
  }

  //TODO: mover esto a otra clase de utileria
  public static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> entriesToMap() {
    return Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue());
  }
}
