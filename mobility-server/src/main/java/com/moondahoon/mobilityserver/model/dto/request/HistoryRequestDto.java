package com.moondahoon.mobilityserver.model.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HistoryRequestDto {

	private String id;
	private LocalDateTime startTime;
	private LocalDateTime endTime;

}
