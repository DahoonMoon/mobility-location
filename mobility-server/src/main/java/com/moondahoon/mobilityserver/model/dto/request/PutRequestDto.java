package com.moondahoon.mobilityserver.model.dto.request;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PutRequestDto {

	private String id;
	private BigDecimal latitude;
	private BigDecimal longitude;

}
