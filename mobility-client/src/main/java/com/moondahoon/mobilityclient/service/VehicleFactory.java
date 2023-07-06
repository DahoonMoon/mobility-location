package com.moondahoon.mobilityclient.service;

public class VehicleFactory {
    public static VehicleClientService getService(){
        return new VehicleClientService();
    }

}
