package com.moondahoon.mobility.service;

import com.moondahoon.Veheicle.GetRequest;
import com.moondahoon.Veheicle.GetResponse;
import com.moondahoon.Veheicle.HistoryRequest;
import com.moondahoon.Veheicle.HistoryResponse;
import com.moondahoon.Veheicle.PutRequest;
import com.moondahoon.Veheicle.PutResponse;
import com.moondahoon.Veheicle.SearchRequest;
import com.moondahoon.Veheicle.SearchResponse;
import com.moondahoon.VehicleLocationServiceGrpc;
import com.moondahoon.mobility.repository.TestDB;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class VehicleLocationService extends VehicleLocationServiceGrpc.VehicleLocationServiceImplBase {

	@Override
	public void put(PutRequest request, StreamObserver<PutResponse> responseObserver) {

		TestDB.getPutResponse()
				.stream()
				.filter(car -> car.getId().equals(request.getId()))
				.findFirst().ifPresent(responseObserver::onNext);
		responseObserver.onCompleted();
	}

	@Override
	public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
		super.get(request, responseObserver);
	}

	@Override
	public void search(SearchRequest request, StreamObserver<SearchResponse> responseObserver) {
		super.search(request, responseObserver);
	}

	@Override
	public void history(HistoryRequest request, StreamObserver<HistoryResponse> responseObserver) {
		super.history(request, responseObserver);
	}
}
