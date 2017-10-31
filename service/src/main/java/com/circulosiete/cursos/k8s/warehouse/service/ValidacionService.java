package com.circulosiete.cursos.k8s.warehouse.service;

import com.circulosiete.cursos.k8s.CreateRequest;
import com.circulosiete.cursos.k8s.ValidacionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.opentracing.Tracer;
import io.opentracing.contrib.ClientTracingInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static io.opentracing.contrib.ClientTracingInterceptor.ClientRequestAttribute.*;

@Slf4j
@Service
public class ValidacionService {

  private ManagedChannel validationChannel;
  private ValidacionServiceGrpc.ValidacionServiceBlockingStub validacionServiceStub;

  @Autowired
  public ValidacionService(Tracer tracer,
                           @Value("${validation.hostname:localhost}") String hostname,
                           @Value("${validation.port:7000}") int port) {
    validationChannel = ManagedChannelBuilder.forAddress(hostname, port)
      .usePlaintext(true)
      .build();

    ClientTracingInterceptor tracingInterceptor = new ClientTracingInterceptor
      .Builder(tracer)
      .withStreaming()
      .withVerbosity()
      .withTracedAttributes(
        METHOD_TYPE,
        METHOD_NAME,
        DEADLINE,
        COMPRESSOR,
        AFFINITY,
        AUTHORITY,
        ALL_CALL_OPTIONS,
        HEADERS)
      .build();
    validacionServiceStub = ValidacionServiceGrpc.newBlockingStub(tracingInterceptor.intercept(validationChannel));
  }

  public boolean createValidacion(String nombre) {

    log.info("Validando: {}", nombre);

    return validacionServiceStub.validacionCreate(CreateRequest.newBuilder()
      .setNombre(nombre)
      .build())
      .getValido();
  }

}
