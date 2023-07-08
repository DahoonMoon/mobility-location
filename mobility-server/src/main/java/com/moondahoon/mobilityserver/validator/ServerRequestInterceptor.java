package com.moondahoon.mobilityserver.validator;

import com.moondahoon.Veheiclelocation.GetRequest;
import com.moondahoon.Veheiclelocation.HistoryRequest;
import com.moondahoon.Veheiclelocation.PutRequest;
import com.moondahoon.Veheiclelocation.SearchRequest;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class ServerRequestInterceptor implements ServerInterceptor {

    private final Map<String, Consumer<Object>> validatorMap = new HashMap<>();
    private static final String PUT_METHOD_NAME = "com.moondahoon.VehicleLocationService/Put";
    private static final String GET_METHOD_NAME = "com.moondahoon.VehicleLocationService/Get";
    private static final String SEARCH_METHOD_NAME = "com.moondahoon.VehicleLocationService/Search";
    private static final String HISTORY_METHOD_NAME = "com.moondahoon.VehicleLocationService/History";

    public ServerRequestInterceptor() {
        validatorMap.put(PUT_METHOD_NAME,
            request -> RequestValidator.validatePutRequest((PutRequest) request));
        validatorMap.put(GET_METHOD_NAME,
            request -> RequestValidator.validateGetRequest((GetRequest) request));
        validatorMap.put(SEARCH_METHOD_NAME,
            request -> RequestValidator.validateSearchRequest((SearchRequest) request));
        validatorMap.put(HISTORY_METHOD_NAME,
            request -> RequestValidator.validateHistoryRequest((HistoryRequest) request));
    }

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
        ServerCallHandler<ReqT, RespT> next) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(
            next.startCall(call, headers)
        ) {
            @Override
            public void onMessage(ReqT message) {
                String methodName = call.getMethodDescriptor().getFullMethodName();
                if (validatorMap.containsKey(methodName)) {
                    try {
                        validatorMap.get(methodName).accept(message);
                    } catch (RuntimeException e) {
                        call.close(Status.fromThrowable(e), new Metadata());
                    }
                }
                super.onMessage(message);
            }
        };
    }

}
