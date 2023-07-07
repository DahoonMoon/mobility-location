package com.moondahoon.mobilityclient.command.sub;

import com.moondahoon.mobilityclient.client.VehicleGrpcClient;
import com.moondahoon.mobilityclient.client.ClientFactory;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
		name = "search",
		description = "Command to Search for Vehicles in a Range using gRPC Client(Stream Data)",
		mixinStandardHelpOptions = true,
		requiredOptionMarker = '*',
		optionListHeading = "%nOptions are:%n",
		sortOptions = false,
		sortSynopsis = false
)
public class SearchCommand implements Callable<Integer> {

	VehicleGrpcClient client;

	@CommandLine.Option(
			names = {"-x", "--latitude"},
			required = true,
			description = "latitude",
			order = 1
	)
	private String latitude;

	@CommandLine.Option(
			names = {"-y", "--longitude"},
			required = true,
			description = "longitude",
			order = 2
	)
	private String longitude;

	@CommandLine.Option(
			names = {"-r", "--radius"},
			required = true,
			description = "radius",
			order = 3
	)
	private String radius;


	public SearchCommand() {
		this.client = ClientFactory.getClient();
	}

	final Integer SUCCESS = 0;
	final Integer FAILURE = 1;

	@Override
	public Integer call() throws Exception {
		client.search(latitude, longitude, radius);
		client.shutdown();
		return SUCCESS;
	}
}
