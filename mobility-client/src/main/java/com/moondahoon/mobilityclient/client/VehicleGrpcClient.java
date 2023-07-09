package com.moondahoon.mobilityclient.client;

import com.moondahoon.Veheiclelocation.GetRequest;
import com.moondahoon.Veheiclelocation.GetResponse;
import com.moondahoon.Veheiclelocation.HistoryRequest;
import com.moondahoon.Veheiclelocation.HistoryResponse;
import com.moondahoon.Veheiclelocation.PutRequest;
import com.moondahoon.Veheiclelocation.PutRequest.Builder;
import com.moondahoon.Veheiclelocation.PutResponse;
import com.moondahoon.Veheiclelocation.SearchRequest;
import com.moondahoon.Veheiclelocation.SearchResponse;
import com.moondahoon.VehicleLocationServiceGrpc;
import com.moondahoon.mobilityclient.model.dto.response.GetResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.HistoryResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.HistoryResponseDto.VehicleHistory;
import com.moondahoon.mobilityclient.model.dto.response.PutResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.SearchResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.SearchResponseDto.VehicleInfo;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import picocli.CommandLine;


public class VehicleGrpcClient {

    private final ManagedChannel channel;
    //	sync
    private final VehicleLocationServiceGrpc.VehicleLocationServiceBlockingStub blockingStub;
    //	async
    private final VehicleLocationServiceGrpc.VehicleLocationServiceStub stub;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public VehicleGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build();
        this.blockingStub = VehicleLocationServiceGrpc.newBlockingStub(channel);
        this.stub = VehicleLocationServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(60, TimeUnit.SECONDS);
    }

    public void put(String id, String latitude, String longitude) {
        PutRequest request = PutRequest.newBuilder()
                .setId(id)
                .setLatitude(Double.parseDouble(latitude))
                .setLongitude(Double.parseDouble(longitude))
                .build();
        PutResponse response = blockingStub.put(request);

        PutResponseDto responseDto = PutResponseDto.builder()
            .id(response.getId())
            .success(response.getSuccess())
            .message(response.getMessage())
            .build();
        System.out.println(responseDto.toJson());
    }

    public void get(String id) {
        GetRequest request = GetRequest.newBuilder().setId(id).build();
        StreamObserver<GetResponse> reponseObserver = new StreamObserver<>() {
            @Override
            public void onNext(GetResponse value) {
                GetResponseDto getResponseDto = GetResponseDto.builder()
                    .id(value.getId())
                    .latitude(BigDecimal.valueOf(value.getLatitude()))
                    .longitude(BigDecimal.valueOf(value.getLongitude()))
                    .timestamp(LocalDateTime.parse(value.getTimestamp(), formatter))
                    .build();
                System.out.println(getResponseDto.toJson());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(
                    CommandLine.Help.Ansi.AUTO.string(String.format("@|bold,red ERROR : %s |@", t.getMessage())));
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        };

        stub.get(request, reponseObserver);
    }


    public void history(String id, String startTime, String endTime) {
        HistoryRequest request = HistoryRequest.newBuilder()
            .setId(id)
            .setStartTime(startTime)
            .setEndTime(endTime)
            .build();

        HistoryResponse response = blockingStub.history(request);

        List<VehicleHistory> vehicleHistoryList = new ArrayList<>();
        response.getHistoryList().forEach(history ->
            vehicleHistoryList.add(VehicleHistory.builder()
                .id(history.getId())
                .latitude(BigDecimal.valueOf(history.getLatitude()))
                .longitude(BigDecimal.valueOf(history.getLongitude()))
                .timestamp(LocalDateTime.parse(history.getTimestamp(), formatter))
                .build())
        );

        HistoryResponseDto responseDto = HistoryResponseDto.builder()
            .history(vehicleHistoryList)
            .build();
        System.out.println(responseDto.toJson());
    }

    public void search(String latitude, String longitude, String radius) {
        SearchRequest request = SearchRequest.newBuilder()
            .setLatitude(Double.parseDouble(latitude))
            .setLongitude(Double.parseDouble(longitude))
            .setRadius(Double.parseDouble(radius))
            .build();
        StreamObserver<SearchResponse> reponseObserver = new StreamObserver<>() {
            @Override
            public void onNext(SearchResponse value) {

                List<VehicleInfo> vehicleInfoList = new ArrayList<>();
                value.getVehiclesList().forEach(vehicle -> {
                    vehicleInfoList.add(VehicleInfo.builder()
                        .id(vehicle.getId())
                        .latitude(BigDecimal.valueOf(vehicle.getLatitude()))
                        .longitude(BigDecimal.valueOf(vehicle.getLongitude()))
                        .distance(BigDecimal.valueOf(vehicle.getDistance()))
                        .timestamp(LocalDateTime.parse(vehicle.getTimestamp(), formatter))
                        .build());
                });
                SearchResponseDto searchResponseDto = SearchResponseDto.builder()
                    .vehicles(vehicleInfoList)
                    .build();

                System.out.println(searchResponseDto.toJson());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(
                    CommandLine.Help.Ansi.AUTO.string(String.format("@|bold,red ERROR : %s |@", t.getMessage())));
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        };

        stub.search(request, reponseObserver);
    }


}
