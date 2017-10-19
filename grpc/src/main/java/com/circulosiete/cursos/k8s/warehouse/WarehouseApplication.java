package com.circulosiete.cursos.k8s.warehouse;

import io.grpc.ServerInterceptor;
import io.opentracing.Tracer;
import io.opentracing.contrib.ServerTracingInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static io.opentracing.contrib.ServerTracingInterceptor.ServerRequestAttribute.*;

@SpringBootApplication
@EnableJpaAuditing
public class WarehouseApplication {

  @Bean
  @GRpcGlobalInterceptor
  public ServerInterceptor globalInterceptor(Tracer tracer) {
    return new ServerTracingInterceptor
      .Builder(tracer)
      .withStreaming()
      .withVerbosity()
      .withTracedAttributes(
        HEADERS,
        METHOD_TYPE,
        METHOD_NAME,
        CALL_ATTRIBUTES)
      .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(WarehouseApplication.class, args);
  }

}
