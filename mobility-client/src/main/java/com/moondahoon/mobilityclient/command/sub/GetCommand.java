package com.moondahoon.mobilityclient.command.sub;

import com.moondahoon.mobilityclient.service.VehicleClientService;
import com.moondahoon.mobilityclient.service.VehicleFactory;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

@CommandLine.Command(
	name = "get",
	description = "CLI for gRPC Client Test : Get Vehicle Location Stream Data",
	mixinStandardHelpOptions = true,
	requiredOptionMarker = '*',
	optionListHeading = "%nOptions are:%n"
)
public class GetCommand implements Callable<Integer> {

	@CommandLine.Option(
		names = {"-i", "--id"},
		required = true,
		description = "vehicle id"
	)
	private String id;

	VehicleClientService vehicleClientService;

	public GetCommand(){
		this.vehicleClientService = VehicleFactory.getService();
	}

	final Integer SUCCESS = 0;
	final Integer FAILURE = 1;

	@Override
	public Integer call() throws Exception {
		String response = vehicleClientService.get(id);
		System.out.println(response);
		return SUCCESS;
	}
}
