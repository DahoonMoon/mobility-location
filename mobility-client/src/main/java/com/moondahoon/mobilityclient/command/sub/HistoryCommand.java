package com.moondahoon.mobilityclient.command.sub;

import com.moondahoon.mobilityclient.client.VehicleGrpcClient;
import com.moondahoon.mobilityclient.client.ClientFactory;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
		name = "history",
		description = "Command to Get Vehicle Location Stream Data using gRPC Client(Unary Data)",
		mixinStandardHelpOptions = true,
		requiredOptionMarker = '*',
		optionListHeading = "%nOptions are:%n",
		sortOptions = false,
		sortSynopsis = false
)
public class HistoryCommand implements Callable<Integer> {

	VehicleGrpcClient client;

	@CommandLine.Option(
			names = {"-i", "--id"},
			required = true,
			description = "vehicle id",
			order = 1
	)
	private String id;

	@CommandLine.Option(
			names = {"-s", "--start"},
			required = true,
			description = "start time",
			order = 2
	)
	private String startTime;

	@CommandLine.Option(
			names = {"-e", "--end"},
			required = true,
			description = "end time",
			order = 3
	)
	private String endTime;


	public HistoryCommand() {
		this.client = ClientFactory.getClient();
	}

	final Integer SUCCESS = 0;
	final Integer FAILURE = 1;

	@Override
	public Integer call() throws Exception {
		client.history(id, startTime, endTime);
		client.shutdown();
		return SUCCESS;
	}
}
