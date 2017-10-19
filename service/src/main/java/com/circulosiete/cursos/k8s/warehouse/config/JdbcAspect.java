package com.circulosiete.cursos.k8s.warehouse.config;

import io.opentracing.Tracer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class JdbcAspect {
  @Autowired
  Tracer tracer;

  public JdbcAspect() {
    System.out.println("JdbcAspect");
    System.out.println("==============================================");
    System.out.println("==============================================");
    System.out.println("==============================================");
    System.out.println("JdbcAspect el bueno, bueno");
  }

  @Around("@annotation(Trace)")
  public Object around(final ProceedingJoinPoint pjp) throws Throwable {
    /*
    private Span getSpanFromHeaders(Map<String, String> headers, String operationName) {
        Span span;
        try {
            SpanContext parentSpanCtx = tracer.extract(Format.Builtin.HTTP_HEADERS,
                new TextMapExtractAdapter(headers));
            if (parentSpanCtx == null) {
                span = tracer.buildSpan(operationName).start();
            } else {
                span = tracer.buildSpan(operationName).asChildOf(parentSpanCtx).start();
            }
        } catch (IllegalArgumentException iae){
            span = tracer.buildSpan(operationName)
                .withTag("Error", "Extract failed and an IllegalArgumentException was thrown")
                .start();
        }
        return span;
    }
     */
    System.out.println();
    System.out.println("==============================================");
    System.out.println("==============================================");
    System.out.println("==============================================");
    /*
    Connection conn = (Connection) pjp.proceed();
    String url = conn.getMetaData().getURL();
    String user = conn.getMetaData().getUserName();
    String dbType;
    try {
      dbType = url.split(":")[1];
    } catch (Throwable t) {
      throw new IllegalArgumentException(
        "Invalid JDBC URL. Expected to find the database type after the first ':'. URL: " + url);
    }
    return new TracingConnection(conn, dbType, user, false);
    */
    return pjp.proceed();
  }
}
