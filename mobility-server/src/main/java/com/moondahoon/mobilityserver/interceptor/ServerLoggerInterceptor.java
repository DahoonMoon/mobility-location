package com.moondahoon.mobilityserver.interceptor;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@Slf4j
@GrpcGlobalServerInterceptor
public class ServerLoggerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {
        log.info("[Request] Service : {}, Method : {}", call.getMethodDescriptor().getServiceName(), call.getMethodDescriptor().getBareMethodName());
        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
            @Override
            public void onMessage(ReqT message) {
                String json;
                try {
                    json = JsonFormat.printer().print((Message) message);
                } catch (InvalidProtocolBufferException e) {
                    json = "Failed to convert protobuf message to JSON";
                }
                log.info("[Request] Message : \n{}", json);
                super.onMessage(message);
            }

            @Override
            public void onComplete() {
                log.info("[Request] Call completed.");
                super.onComplete();
            }
        };
    }
}
