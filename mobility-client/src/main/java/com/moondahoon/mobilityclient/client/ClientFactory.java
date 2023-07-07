package com.moondahoon.mobilityclient.client;

public class ClientFactory {

    public static VehicleGrpcClient getClient(){
        return new VehicleGrpcClient("localhost", 8081);
    }

}
