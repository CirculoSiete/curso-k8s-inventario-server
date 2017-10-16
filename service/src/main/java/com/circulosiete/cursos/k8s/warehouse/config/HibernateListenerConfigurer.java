package com.circulosiete.cursos.k8s.warehouse.config;

import com.circulosiete.cursos.k8s.warehouse.model.ModelInterceptor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import static org.hibernate.event.spi.EventType.*;

@Component
public class HibernateListenerConfigurer {
  private final EntityManagerFactory emf;
  private final ModelInterceptor modelInterceptor;

  public HibernateListenerConfigurer(EntityManagerFactory emf, ModelInterceptor modelInterceptor) {
    this.emf = emf;
    this.modelInterceptor = modelInterceptor;
  }

  @PostConstruct
  protected void init() {
    SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);

    registry.getEventListenerGroup(POST_INSERT).appendListener(modelInterceptor);
    registry.getEventListenerGroup(POST_UPDATE).appendListener(modelInterceptor);
    registry.getEventListenerGroup(POST_DELETE).appendListener(modelInterceptor);
  }
}
