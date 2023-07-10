package com.moondahoon.mobilityclient.client;

import com.moondahoon.Veheiclelocation.GetRequest;
import com.moondahoon.Veheiclelocation.GetResponse;
import com.moondahoon.Veheiclelocation.HistoryRequest;
import com.moondahoon.Veheiclelocation.HistoryResponse;
import com.moondahoon.Veheiclelocation.HistoryResponse.VehicleHistory;
import com.moondahoon.Veheiclelocation.PutRequest;
import com.moondahoon.Veheiclelocation.PutResponse;
import com.moondahoon.Veheiclelocation.SearchRequest;
import com.moondahoon.Veheiclelocation.SearchResponse;
import com.moondahoon.Veheiclelocation.SearchResponse.VehicleInfo;
import com.moondahoon.VehicleLocationServiceGrpc.VehicleLocationServiceImplBase;
import io.grpc.stub.StreamObserver;


public class TestGrpcServer extends VehicleLocationServiceImplBase {


    @Override
    public void put(PutRequest request, StreamObserver<PutResponse> responseObserver) {
        PutResponse response = PutResponse.newBuilder()
            .setId("1")
            .setSuccess(true)
            .setMessage("saved")
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        GetResponse response = GetResponse.newBuilder()
            .setId(request.getId())
            .setLatitude(10)
            .setLongitude(10)
            .setTimestamp("2023-07-10 22:00:00")
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void search(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
            SearchResponse.Builder searchResponseBuilder = SearchResponse.newBuilder();
            searchResponseBuilder.addVehicles(VehicleInfo.newBuilder()
                .setId("1")
                .setLatitude(10.0)
                .setLongitude(10.0)
                .setDistance(0.0)
                .setTimestamp("2023-07-10 22:00:00")
                .build());
            SearchResponse response = searchResponseBuilder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void history(HistoryRequest request, StreamObserver<HistoryResponse> responseObserver) {
        HistoryResponse.Builder historyResponseBuilder = HistoryResponse.newBuilder();
        historyResponseBuilder.addHistory(VehicleHistory.newBuilder()
            .setId("1")
            .setLatitude(10.0)
            .setLongitude(10.0)
            .setTimestamp("2023-07-10 22:00:00")
            .build());
        HistoryResponse response = historyResponseBuilder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
