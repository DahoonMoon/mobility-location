package com.moondahoon.mobilityserver.model.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchRequestDto {

	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal radius;

}
