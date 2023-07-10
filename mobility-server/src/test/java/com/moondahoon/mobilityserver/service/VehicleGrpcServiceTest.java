package com.moondahoon.mobilityserver.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.moondahoon.Veheiclelocation.GetRequest;
import com.moondahoon.Veheiclelocation.GetResponse;
import com.moondahoon.Veheiclelocation.HistoryRequest;
import com.moondahoon.Veheiclelocation.HistoryResponse;
import com.moondahoon.Veheiclelocation.PutRequest;
import com.moondahoon.Veheiclelocation.PutResponse;
import com.moondahoon.Veheiclelocation.SearchRequest;
import com.moondahoon.Veheiclelocation.SearchResponse;
import com.moondahoon.VehicleLocationServiceGrpc;
import com.moondahoon.mobilityserver.model.entity.VehicleLocation;
import com.moondahoon.mobilityserver.repository.memory.VehicleLocationRepository;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VehicleGrpcServiceTest {
	@Rule
	public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
	private VehicleLocationRepository repository;
	private VehicleService vehicleService;
	private VehicleGrpcService grpcService;
	private Server server;
	private ManagedChannel channel;
	private final int maxMessages = 3;

	@BeforeEach
	void setUp() throws IOException {
		repository = new VehicleLocationRepository();

		repository.save(new VehicleLocation("1", BigDecimal.valueOf(10), BigDecimal.valueOf(10), LocalDateTime.now().minusMinutes(30)));
		repository.save(new VehicleLocation("2", BigDecimal.valueOf(20), BigDecimal.valueOf(20), LocalDateTime.now().minusMinutes(10)));
		repository.save(new VehicleLocation("3", BigDecimal.valueOf(30), BigDecimal.valueOf(30), LocalDateTime.now().minusMinutes(25)));

		vehicleService = new VehicleService(repository);
		grpcService = new VehicleGrpcService(vehicleService);
//		메시지 최대 3개로 제한
		grpcService.setMaxMessages(maxMessages);

		server = grpcCleanup.register(InProcessServerBuilder.forName("test-server").directExecutor().addService(grpcService).build()).start();
		channel = grpcCleanup.register(InProcessChannelBuilder.forName("test-server").directExecutor().build());
	}

	@AfterEach
	void shutdown() throws InterruptedException {
		channel.shutdown();
		boolean isTerminated = channel.awaitTermination(5, TimeUnit.SECONDS);
		try {
			server.shutdown().awaitTermination();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@DisplayName("gRPC PUT Method 테스트")
	@Test
	void putTest() {
//		given
//		request 정의
		PutRequest request = PutRequest.newBuilder()
				.setId("1")
				.setLatitude(10.0)
				.setLongitude(10.0)
				.build();
//		stub 세팅
		VehicleLocationServiceGrpc.VehicleLocationServiceBlockingStub blockingStub = VehicleLocationServiceGrpc.newBlockingStub(channel);

//		when
		PutResponse response = blockingStub.put(request);

//		then
		assertThat(response.getSuccess()).isTrue();
		assertThat(response.getId()).isEqualTo("1");
		assertThat(response.getMessage()).isEqualTo("saved");
	}

	@DisplayName("gRPC GET Method 테스트")
	@Test
	void getTest() throws InterruptedException {
//		given
//		stub 정의
		VehicleLocationServiceGrpc.VehicleLocationServiceStub stub = VehicleLocationServiceGrpc.newStub(channel);
//		request 정의
		GetRequest request = GetRequest.newBuilder().setId("1").build();

//		when
//		응답 받을 배열 설정
		List<GetResponse> responses = new ArrayList<>();
//		비동기 처리를 위해 countdown 설정
		CountDownLatch latch = new CountDownLatch(3);
//		responseObserver 세팅
		StreamObserver<GetResponse> responseObserver = new StreamObserver<>() {
			@Override
			public void onNext(GetResponse response) {
				responses.add(response);
				latch.countDown();
			}

			@Override
			public void onError(Throwable t) {
				latch.countDown();
			}

			@Override
			public void onCompleted() {
				latch.countDown();
			}
		};

//		실행
		stub.get(request, responseObserver);
		latch.await();

//		then
		assertThat(responses).hasSize(maxMessages);
		assertThat(responses.get(0).getId()).isEqualTo("1");
		assertThat(responses.get(0).getLatitude()).isEqualTo(Double.parseDouble("10"));
		assertThat(responses.get(0).getLongitude()).isEqualTo(Double.parseDouble("10"));

	}

	@DisplayName("gRPC SEARCH Method 테스트")
	@Test
	void searchTest() throws InterruptedException {
//		given
//		stub 정의
		VehicleLocationServiceGrpc.VehicleLocationServiceStub stub = VehicleLocationServiceGrpc.newStub(channel);
//		request 정의
		SearchRequest request = SearchRequest.newBuilder()
				.setLatitude(10)
				.setLongitude(10)
				.setRadius(10000)
				.build();

//		when
//		응답 받을 배열 설정
		List<SearchResponse> responses = new ArrayList<>();
//		비동기 처리를 위해 countdown 설정
		CountDownLatch latch = new CountDownLatch(3);
//		responseObserver 세팅
		StreamObserver<SearchResponse> responseObserver = new StreamObserver<>() {
			@Override
			public void onNext(SearchResponse response) {
				responses.add(response);
				latch.countDown();
			}

			@Override
			public void onError(Throwable t) {
				latch.countDown();
			}

			@Override
			public void onCompleted() {
				latch.countDown();
			}
		};

//		실행
		stub.search(request, responseObserver);
		latch.await();

//		then
		assertThat(responses).hasSize(maxMessages);
		assertThat(responses.get(0).getVehicles(0).getId()).isEqualTo("1");
		assertThat(responses.get(0).getVehicles(0).getLatitude()).isEqualTo(Double.parseDouble("10"));
		assertThat(responses.get(0).getVehicles(0).getLongitude()).isEqualTo(Double.parseDouble("10"));
		assertThat(responses.get(0).getVehicles(0).getDistance()).isEqualTo(Double.parseDouble("0"));

	}


	@DisplayName("gRPC HISTORY Method 테스트")
	@Test
	void historyTest() {
//		given
//		사전 세팅
		HistoryRequest request = HistoryRequest.newBuilder()
				.setId("1")
				.setStartTime("2023-01-01 10:00:00")
				.setEndTime("2023-12-10 19:00:00")
				.build();

		VehicleLocationServiceGrpc.VehicleLocationServiceBlockingStub blockingStub = VehicleLocationServiceGrpc.newBlockingStub(channel);

//		when
		HistoryResponse response = blockingStub.history(request);

//		then
		assertThat(response.getHistory(0).getId()).isEqualTo("1");
		assertThat(response.getHistory(0).getLatitude()).isEqualTo(Double.parseDouble("10"));
		assertThat(response.getHistory(0).getLongitude()).isEqualTo(Double.parseDouble("10"));
	}


}