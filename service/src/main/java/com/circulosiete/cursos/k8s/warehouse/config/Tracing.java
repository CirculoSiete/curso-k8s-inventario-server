package com.circulosiete.cursos.k8s.warehouse.config;

import brave.opentracing.BraveTracer;
import brave.sampler.Sampler;
import io.opentracing.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.okhttp3.OkHttpSender;

import static java.lang.String.format;
import static zipkin.reporter.Encoding.JSON;

@Configuration
public class Tracing {
  @Value("${zipkin-svc.hostname:localhost}")
  String zipkinHostname;
  @Value("${zipkin-svc.port:9411}")
  Integer zipkinPort;
  @Value("${zipkin-svc.path:api/v1/spans}")
  String zipkinPath;
  @Value("${spring.application.name}")
  String applicationName;

  @Bean
  public Tracer zipkinTracer() {
    OkHttpSender okHttpSender = OkHttpSender.builder()
      .encoding(JSON)
      .endpoint(format("http://%s:%d/%s", zipkinHostname, zipkinPort, zipkinPath))
      .build();
    AsyncReporter<Span> reporter = AsyncReporter.builder(okHttpSender).build();
    brave.Tracing braveTracer = brave.Tracing.newBuilder()
      .localServiceName(applicationName)
      .reporter(reporter)
      .traceId128Bit(true)
      .sampler(Sampler.ALWAYS_SAMPLE)
      .build();
    return BraveTracer.create(braveTracer);
  }
}
