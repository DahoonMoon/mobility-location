package com.moondahoon.mobilityclient.command.sub;

import com.moondahoon.mobilityclient.client.VehicleGrpcClient;
import com.moondahoon.mobilityclient.client.ClientFactory;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
		name = "get",
		description = "Command to Get Location of a Vehicle using gRPC Client(Stream Data)",
		mixinStandardHelpOptions = true,
		requiredOptionMarker = '*',
		optionListHeading = "%nOptions are:%n",
		sortOptions = false,
		sortSynopsis = false
)
public class GetCommand implements Callable<Integer> {

	VehicleGrpcClient client;

	@CommandLine.Option(
			names = {"-i", "--id"},
			required = true,
			description = "vehicle id"
	)
	private String id;


	public GetCommand() {
		this.client = ClientFactory.getClient();
	}

	final Integer SUCCESS = 0;
	final Integer FAILURE = 1;

	@Override
	public Integer call() throws Exception {
		client.get(id);
		client.shutdown();

		return SUCCESS;
	}
}
