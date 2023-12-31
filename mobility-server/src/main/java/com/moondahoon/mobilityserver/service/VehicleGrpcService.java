package com.moondahoon.mobilityserver.service;

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
import com.moondahoon.VehicleLocationServiceGrpc;
import com.moondahoon.mobilityserver.model.dto.request.GetRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.HistoryRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.PutRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.SearchRequestDto;
import com.moondahoon.mobilityserver.model.dto.response.GetResponseDto;
import com.moondahoon.mobilityserver.model.dto.response.HistoryResponseDto;
import com.moondahoon.mobilityserver.model.dto.response.SearchResponseDto;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Setter
@GrpcService
@RequiredArgsConstructor
public class VehicleGrpcService extends VehicleLocationServiceGrpc.VehicleLocationServiceImplBase {

    private final VehicleService vehicleService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Value("${grpc.message.max-size}")
    int maxMessages;

    @Override
    public void put(PutRequest request, StreamObserver<PutResponse> responseObserver) {

        PutRequestDto requestDto = PutRequestDto.builder()
            .id(request.getId())
            .latitude(BigDecimal.valueOf(request.getLatitude()))
            .longitude(BigDecimal.valueOf(request.getLongitude()))
            .build();

        vehicleService.putVehicleLocation(requestDto);

        PutResponse response = PutResponse.newBuilder()
            .setSuccess(true)
            .setId(requestDto.getId())
            .setMessage("saved")
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {

        GetRequestDto requestDto = new GetRequestDto(request.getId());

        int sentMessages = 0;

        while (sentMessages < maxMessages) {
            GetResponseDto response = vehicleService.getVehicleLocation(requestDto);

            responseObserver.onNext(GetResponse.newBuilder()
                .setId(response.getId())
                .setLatitude(response.getLatitude().doubleValue())
                .setLongitude(response.getLongitude().doubleValue())
                .setTimestamp(response.getTimestamp().format(formatter))
                .build());

            sentMessages++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                responseObserver.onError(Status.INTERNAL
                    .withDescription("ERROR : interrupted during sleeping.")
                    .asRuntimeException());
                break;
            }
        }
        responseObserver.onCompleted();
    }


    @Override
    public void search(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {

        SearchRequestDto requestDto = SearchRequestDto.builder()
            .latitude(BigDecimal.valueOf(request.getLatitude()))
            .longitude(BigDecimal.valueOf(request.getLongitude()))
            .radius(BigDecimal.valueOf(request.getRadius()))
            .build();

        int sentMessages = 0;

        while (sentMessages < maxMessages) {
            List<SearchResponseDto> responseDtoList = vehicleService.searchVehicleLocation(requestDto);

            SearchResponse.Builder searchResponseBuilder = SearchResponse.newBuilder();
            responseDtoList.forEach(res -> searchResponseBuilder.addVehicles(VehicleInfo.newBuilder()
                .setId(res.getId())
                .setLatitude(res.getLatitude().doubleValue())
                .setLongitude(res.getLongitude().doubleValue())
                .setDistance(res.getDistance().doubleValue())
                .setTimestamp(res.getTimestamp().format(formatter))
                .build())
            );
            SearchResponse searchResponse = searchResponseBuilder.build();

            responseObserver.onNext(searchResponse);

            sentMessages++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                responseObserver.onError(Status.INTERNAL
                    .withDescription("ERROR : interrupted during sleeping.")
                    .asRuntimeException());
                break;
            }
        }
        responseObserver.onCompleted();
    }


    @Override
    public void history(HistoryRequest request, StreamObserver<HistoryResponse> responseObserver) {
        HistoryRequestDto requestDto = HistoryRequestDto.builder()
            .id(request.getId())
            .startTime(LocalDateTime.parse(request.getStartTime(), formatter))
            .endTime(LocalDateTime.parse(request.getEndTime(), formatter))
            .build();

        List<HistoryResponseDto> responseDtoList = vehicleService.historyVehicleLocation(requestDto);

        HistoryResponse.Builder historyResponseBuilder = HistoryResponse.newBuilder();
        responseDtoList.forEach(res -> historyResponseBuilder.addHistory(VehicleHistory.newBuilder()
            .setId(res.getId())
            .setLatitude(res.getLatitude().doubleValue())
            .setLongitude(res.getLongitude().doubleValue())
            .setTimestamp(res.getTimestamp().format(formatter))
            .build())
        );
        HistoryResponse historyResponse = historyResponseBuilder.build();

        responseObserver.onNext(historyResponse);
        responseObserver.onCompleted();
    }


}
