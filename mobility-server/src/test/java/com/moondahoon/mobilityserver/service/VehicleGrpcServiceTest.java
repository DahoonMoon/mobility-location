package com.moondahoon.mobilityserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.moondahoon.Veheiclelocation.GetRequest;
import com.moondahoon.Veheiclelocation.GetResponse;
import com.moondahoon.Veheiclelocation.HistoryRequest;
import com.moondahoon.Veheiclelocation.HistoryResponse;
import com.moondahoon.Veheiclelocation.PutRequest;
import com.moondahoon.Veheiclelocation.PutResponse;
import com.moondahoon.Veheiclelocation.SearchRequest;
import com.moondahoon.Veheiclelocation.SearchResponse;
import com.moondahoon.VehicleLocationServiceGrpc;
import com.moondahoon.mobilityserver.model.dto.request.PutRequestDto;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleGrpcServiceTest {

	@Rule
	public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

	@Mock
	private VehicleService mockVehicleService;

	@DisplayName("gRPC PUT Method 테스트")
	@Test
	void putTest() throws IOException, InterruptedException {
//		given
		VehicleGrpcService grpcService = new VehicleGrpcService(mockVehicleService);
		Server server = grpcCleanup.register(InProcessServerBuilder.forName("test-server1").directExecutor().addService(grpcService).build()).start();

		ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder.forName("test-server1").directExecutor().build());

		PutRequest request = PutRequest.newBuilder()
				.setId("1")
				.setLatitude(10.0)
				.setLongitude(10.0)
				.build();

		VehicleLocationServiceGrpc.VehicleLocationServiceBlockingStub blockingStub = VehicleLocationServiceGrpc.newBlockingStub(channel);

//		when
		PutResponse response = blockingStub.put(request);

//		then
		assertThat(response.getSuccess()).isEqualTo(true);
		assertThat(response.getId()).isEqualTo("1");
		assertThat(response.getMessage()).isEqualTo("saved");
		Mockito.verify(mockVehicleService, Mockito.times(1)).putVehicleLocation(any(PutRequestDto.class));

		channel.shutdown();
		boolean isTerminated = channel.awaitTermination(5, TimeUnit.SECONDS);
		server.shutdown();
	}

	@DisplayName("gRPC GET Method 테스트")
	@Test
	void getTest() throws IOException, InterruptedException {
//		given
//		사전 세팅
		VehicleLocationRepository vehicleLocationRepository = new VehicleLocationRepository();
		vehicleLocationRepository.setVehicleLocationList(
				List.of(VehicleLocation.builder().id("1").latitude(BigDecimal.valueOf(10)).longitude(BigDecimal.valueOf(10)).timestamp(
						LocalDateTime.now()).build()));
		VehicleService vehicleService = new VehicleService(vehicleLocationRepository);
		VehicleGrpcService grpcService = new VehicleGrpcService(vehicleService);
//		메시지 최대 3개로 제한
		int maxMessages = 3;
		grpcService.setMaxMessages(maxMessages);
//		서버 세팅
		Server server = grpcCleanup.register(InProcessServerBuilder.forName("test-server2").directExecutor().addService(grpcService).build()).start();
		ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder.forName("test-server2").directExecutor().build());
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

		channel.shutdown();
		boolean isTerminated = channel.awaitTermination(5, TimeUnit.SECONDS);
		server.shutdown().awaitTermination();
	}

	@DisplayName("gRPC SEARCH Method 테스트")
	@Test
	void searchTest() throws IOException, InterruptedException {
//		given
//		사전 세팅
		VehicleLocationRepository vehicleLocationRepository = new VehicleLocationRepository();
		vehicleLocationRepository.setVehicleLocationList(
				List.of(VehicleLocation.builder().id("1").latitude(BigDecimal.valueOf(10)).longitude(BigDecimal.valueOf(10)).timestamp(
						LocalDateTime.now()).build()));
		VehicleService vehicleService = new VehicleService(vehicleLocationRepository);
		VehicleGrpcService grpcService = new VehicleGrpcService(vehicleService);
//		메시지 최대 3개로 제한
		int maxMessages = 3;
		grpcService.setMaxMessages(maxMessages);
//		서버 세팅
		Server server = grpcCleanup.register(InProcessServerBuilder.forName("test-server3").directExecutor().addService(grpcService).build()).start();
		ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder.forName("test-server3").directExecutor().build());
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


		channel.shutdown();
		boolean isTerminated = channel.awaitTermination(5, TimeUnit.SECONDS);
		server.shutdown().awaitTermination();
	}


	@DisplayName("gRPC HISTORY Method 테스트")
	@Test
	void historyTest() throws IOException, InterruptedException {
//		given
//		사전 세팅
		VehicleLocationRepository vehicleLocationRepository = new VehicleLocationRepository();
		vehicleLocationRepository.setVehicleLocationList(
				List.of(VehicleLocation.builder().id("1").latitude(BigDecimal.valueOf(10)).longitude(BigDecimal.valueOf(10)).timestamp(
						LocalDateTime.now()).build()));
		VehicleService vehicleService = new VehicleService(vehicleLocationRepository);
		VehicleGrpcService grpcService = new VehicleGrpcService(vehicleService);

		Server server = grpcCleanup.register(InProcessServerBuilder.forName("test-server4").directExecutor().addService(grpcService).build()).start();

		ManagedChannel channel = grpcCleanup.register(InProcessChannelBuilder.forName("test-server4").directExecutor().build());

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

		channel.shutdown();
		boolean isTerminated = channel.awaitTermination(5, TimeUnit.SECONDS);
		server.shutdown();
	}


}