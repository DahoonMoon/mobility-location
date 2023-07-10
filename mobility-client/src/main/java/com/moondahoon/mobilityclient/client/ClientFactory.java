package com.moondahoon.mobilityclient.client;

public class ClientFactory {

    public final static String host = "localhost";
    public final static int port = 8081;

    public static VehicleGrpcClient getClient() {
        return new VehicleGrpcClient(host, port);
    }

}
