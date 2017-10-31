package com.circulosiete.cursos.k8s.warehouse;

import brave.Tracing;
import brave.opentracing.BraveTracer;
import brave.sampler.Sampler;
import io.grpc.ServerInterceptor;
import io.opentracing.Tracer;
import io.opentracing.contrib.ServerTracingInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.okhttp3.OkHttpSender;

import static io.opentracing.contrib.ServerTracingInterceptor.ServerRequestAttribute.*;
import static java.lang.String.format;
import static zipkin.reporter.Encoding.JSON;

@SpringBootApplication
public class ValidacionServer {
  @Value("${zipkin.hostname:localhost}")
  String zipkinHostname;
  @Value("${zipkin.port:9411}")
  Integer zipkinPort;
  @Value("${zipkin.path:api/v1/spans}")
  String zipkinPath;
  @Value("${spring.application.name}")

    //dd
  String applicationName;

  @Bean
  public Tracer zipkinTracer() {
    OkHttpSender okHttpSender = OkHttpSender.builder()
      .encoding(JSON)
      .endpoint(format("http://%s:%d/%s", zipkinHostname, zipkinPort, zipkinPath))
      .build();
    AsyncReporter<Span> reporter = AsyncReporter.builder(okHttpSender).build();
    Tracing braveTracer = Tracing.newBuilder()
      .localServiceName(applicationName)
      .reporter(reporter)
      .traceId128Bit(true)
      .sampler(Sampler.ALWAYS_SAMPLE)
      .build();
    return BraveTracer.create(braveTracer);
  }

  @Bean
  @GRpcGlobalInterceptor
  public ServerInterceptor globalInterceptor(Tracer tracer) {
    return new ServerTracingInterceptor
      .Builder(tracer)
      .withStreaming()
      .withVerbosity()
      .withTracedAttributes(HEADERS, METHOD_TYPE, METHOD_NAME)
      .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(ValidacionServer.class, args);
  }
}
