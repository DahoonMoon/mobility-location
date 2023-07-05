package com.moondahoon.mobilityserver.model.dto.response;

import java.math.BigDecimal;
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
public class HistoryResponseDto {

	private String id;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private LocalDateTime timestamp;

}
