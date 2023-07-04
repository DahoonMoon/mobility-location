package com.moondahoon.mobility.config;

import com.moondahoon.mobility.service.VehicleLocationService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {

	private final VehicleLocationService vehicleLocationService;

//	@Value("${grpc.port}")
	Integer grpcPort = 8080;

	@Bean
	public Server grpcServer() {
		return ServerBuilder.forPort(grpcPort)
				.addService(vehicleLocationService)
				.build();
	}



}
