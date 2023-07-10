package com.moondahoon.mobilityclient.command.sub;

import com.moondahoon.mobilityclient.client.VehicleGrpcClient;
import com.moondahoon.mobilityclient.client.ClientFactory;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
		name = "put",
		description = "Command to Save the Location of a Vehicle using gRPC Client(Unary Data)",
		mixinStandardHelpOptions = true,
		requiredOptionMarker = '*',
		optionListHeading = "%nOptions are:%n",
		sortOptions = false,
		sortSynopsis = false
)
public class PutCommand implements Callable<Integer> {

	VehicleGrpcClient client;

	@CommandLine.Option(
			names = {"-i", "--id"},
			required = true,
			description = "vehicle id",
			order = 1
	)
	public String id;

	@CommandLine.Option(
			names = {"-x", "--latitude"},
			required = true,
			description = "latitude",
			order = 2
	)
	public String latitude;

	@CommandLine.Option(
			names = {"-y", "--longitude"},
			required = true,
			description = "longitude",
			order = 3
	)
	public String longitude;


	public PutCommand() {
		this.client = ClientFactory.getClient();
	}

	final Integer SUCCESS = 0;
	final Integer FAILURE = 1;

	@Override
	public Integer call() throws Exception {
		client.put(id, latitude, longitude);
		client.shutdown();
		return SUCCESS;
	}
}
