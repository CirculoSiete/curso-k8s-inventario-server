package com.circulosiete.cursos.k8s.warehouse.grpc;

import io.grpc.*;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.stereotype.Component;

//@Component
//@GRpcGlobalInterceptor
public class TracingInterceptor implements ServerInterceptor {
  public  TracingInterceptor() {
    System.out.println("Creando int");
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    System.out.println("Interceptando llamada");
    Context ctxWithSpan = Context.current();
    ServerCall.Listener<ReqT> listenerWithContext = Contexts
      .interceptCall(ctxWithSpan, call, headers, next);

    ServerCall.Listener<ReqT> tracingListenerWithContext =
      new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(listenerWithContext) {

        @Override
        public void onMessage(ReqT message) {
          delegate().onMessage(message);
        }

        @Override
        public void onHalfClose() {
          delegate().onHalfClose();
        }

        @Override
        public void onCancel() {
          delegate().onCancel();
        }

        @Override
        public void onComplete() {
          delegate().onComplete();
        }
      };

    return tracingListenerWithContext;
  }
}
