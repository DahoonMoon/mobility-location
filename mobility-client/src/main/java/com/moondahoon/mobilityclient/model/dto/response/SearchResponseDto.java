package com.moondahoon.mobilityclient.model.dto.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moondahoon.mobilityclient.util.LocalDateTimeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
public class SearchResponseDto {

	private List<VehicleInfo> vehicles;

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class VehicleInfo {
		private String id;
		private BigDecimal latitude;
		private BigDecimal longitude;
		private BigDecimal distance;
		private LocalDateTime timestamp;
	}

	public String toJson() {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
				.create();
		return gson.toJson(this);
	}

}
