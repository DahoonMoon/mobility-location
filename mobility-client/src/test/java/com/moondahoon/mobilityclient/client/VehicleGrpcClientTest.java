package com.moondahoon.mobilityclient.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.moondahoon.mobilityclient.model.dto.response.GetResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.HistoryResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.HistoryResponseDto.VehicleHistory;
import com.moondahoon.mobilityclient.model.dto.response.PutResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.SearchResponseDto;
import com.moondahoon.mobilityclient.model.dto.response.SearchResponseDto.VehicleInfo;
import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class VehicleGrpcClientTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();
    private ByteArrayOutputStream outContent;
    private ManagedChannel channel;
    private VehicleGrpcClient client;

    @BeforeEach
    void setUp() throws IOException {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
            .forName(serverName).directExecutor().addService(new TestGrpcServer()).build().start());
        channel = grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build());
        client = new VehicleGrpcClient(channel);
    }

    @AfterEach
    void tearOut() {
        channel.shutdown();
        try {
            channel.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @DisplayName("gRPC Client PUT Method 테스트")
    @Test
    public void testPut() {
        // given
        PutResponseDto expectedDto = PutResponseDto.builder()
            .id("1")
            .success(true)
            .message("saved")
            .build();

        // when
        client.put("1", "10.0", "10.0");

        // then
        assertThat(outContent.toString()).isEqualTo(expectedDto.toJson() + "\n");
    }

    @DisplayName("gRPC Client GET Method 테스트")
    @Test
    public void testGet() {
//        given
        GetResponseDto expectedDto = GetResponseDto.builder()
            .id("1")
            .latitude(BigDecimal.valueOf(10.0))
            .longitude(BigDecimal.valueOf(10.0))
            .timestamp(LocalDateTime.of(2023, 7, 10, 22, 0, 0))
            .build();

//        when
        client.get("1");

//        then
        assertThat(outContent.toString()).isEqualTo(expectedDto.toJson() + "\nonCompleted\n");
    }

    @DisplayName("gRPC Client SEARCH Method 테스트")
    @Test
    public void testSearch() {
//        given
        SearchResponseDto expectedDto = SearchResponseDto.builder()
            .vehicles(List.of(
                VehicleInfo.builder()
                    .id("1")
                    .latitude(BigDecimal.valueOf(10.0))
                    .longitude(BigDecimal.valueOf(10.0))
                    .distance(BigDecimal.valueOf(0.0))
                    .timestamp(LocalDateTime.of(2023, 7, 10, 22, 0, 0))
                    .build()))
            .build();

//        when
        client.search("10.0", "10.0", "1000.0");

//        then
        assertThat(outContent.toString()).isEqualTo(expectedDto.toJson() + "\nonCompleted\n");
    }

    @DisplayName("gRPC Client HISTORY Method 테스트")
    @Test
    public void testHistory() {
//         given
        HistoryResponseDto expectedDto = HistoryResponseDto.builder()
            .history(List.of(
                VehicleHistory.builder()
                    .id("1")
                    .latitude(BigDecimal.valueOf(10.0))
                    .longitude(BigDecimal.valueOf(10.0))
                    .timestamp(LocalDateTime.of(2023, 7, 10, 22, 0, 0))
                    .build()))
            .build();

//         when
        client.history("1", "2023-07-10 00:00:00", "2023-07-11 00:00:00");

//         then
        assertThat(outContent.toString()).isEqualTo(expectedDto.toJson() + "\n");
    }

}




