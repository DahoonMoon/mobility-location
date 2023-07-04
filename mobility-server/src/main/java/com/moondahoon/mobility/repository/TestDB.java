package com.moondahoon.mobility.repository;

import com.moondahoon.Veheicle.PutResponse;
import java.util.ArrayList;
import java.util.List;

public class TestDB {

	public static List<PutResponse>	 getPutResponse() {
		return new ArrayList<PutResponse>() {
			{
				add(PutResponse.newBuilder().setId("1").setMessage("saved").build());
				add(PutResponse.newBuilder().setId("2").setMessage("saved").build());
				add(PutResponse.newBuilder().setId("3").setMessage("saved").build());
				add(PutResponse.newBuilder().setId("4").setMessage("saved").build());
			}
		};
	}

}
