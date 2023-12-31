package com.moondahoon.mobilityclient.model.dto.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moondahoon.mobilityclient.util.LocalDateTimeAdapter;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PutResponseDto {

	private String id;
	private Boolean success;
	private String message;

	public String toJson(){
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
		return gson.toJson(this);
	}

}
