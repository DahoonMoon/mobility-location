package com.moondahoon.mobilityserver.config;

import com.moondahoon.mobilityserver.service.VehicleGrpcService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GrpcServerConfig {

	private final VehicleGrpcService vehicleGrpcService;

//	@Value("${grpc.port}")
	Integer grpcPort = 8080;

	@Bean
	public Server grpcServer() {
		return ServerBuilder.forPort(8888)
				.addService(vehicleGrpcService)
				.build();
	}



}
