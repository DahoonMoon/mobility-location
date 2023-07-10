package com.moondahoon.mobilityserver.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.moondahoon.mobilityserver.model.dto.request.GetRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.HistoryRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.PutRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.SearchRequestDto;
import com.moondahoon.mobilityserver.model.dto.response.GetResponseDto;
import com.moondahoon.mobilityserver.model.dto.response.HistoryResponseDto;
import com.moondahoon.mobilityserver.model.dto.response.SearchResponseDto;
import com.moondahoon.mobilityserver.model.entity.VehicleLocation;
import com.moondahoon.mobilityserver.repository.memory.VehicleLocationRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

	@InjectMocks
	private VehicleService vehicleService;

	@Mock
	private VehicleLocationRepository vehicleLocationRepository;


	@DisplayName("Put Service 테스트")
	@Test
	void putVehicleLocation() {
//		given
		PutRequestDto requestDto = PutRequestDto.builder()
				.id("1")
				.latitude(BigDecimal.valueOf(10))
				.longitude(BigDecimal.valueOf(10))
				.build();

//		when
		vehicleService.putVehicleLocation(requestDto);

//		then
		Mockito.verify(vehicleLocationRepository, Mockito.times(1))
				.save(Mockito.any(VehicleLocation.class));
	}

	@DisplayName("Get Service 테스트")
	@Test
	void getVehicleLocation() {
//		given
		GetRequestDto requestDto = new GetRequestDto("1");

		String expectedId = "1";
		BigDecimal expectedLatitude = BigDecimal.valueOf(10);
		BigDecimal expectedLongitude = BigDecimal.valueOf(10);
		LocalDateTime expectedLocalDateTime = LocalDateTime.of(2023, 7, 10, 10, 0, 0);

		VehicleLocation mockVehicleLocation = new VehicleLocation("1", BigDecimal.valueOf(10), BigDecimal.valueOf(10), expectedLocalDateTime);
		Mockito.when(vehicleLocationRepository.findTopByIdOrderByTimestampDesc(Mockito.any())).thenReturn(mockVehicleLocation);

//		when
		GetResponseDto responseDto = vehicleService.getVehicleLocation(requestDto);

//		then
		assertThat(responseDto.getId()).isEqualTo(expectedId);
		assertThat(responseDto.getLatitude()).isEqualTo(expectedLatitude);
		assertThat(responseDto.getLongitude()).isEqualTo(expectedLongitude);
		assertThat(responseDto.getTimestamp()).isEqualTo(expectedLocalDateTime);
	}

	@DisplayName("Search Service 테스트")
	@Test
	void searchVehicleLocation() {
//		 given
		SearchRequestDto requestDto = SearchRequestDto.builder()
				.latitude(BigDecimal.valueOf(10))
				.longitude(BigDecimal.valueOf(10))
				.radius(BigDecimal.valueOf(1000))
				.build();

		String expectedId = "1";
		BigDecimal expectedLatitude = BigDecimal.valueOf(10);
		BigDecimal expectedLongitude = BigDecimal.valueOf(10);
		LocalDateTime expectedLocalDateTime = LocalDateTime.of(2023, 7, 10, 10, 0, 0);

		List<VehicleLocation> vehicleLocationList = List.of(
				new VehicleLocation("1", BigDecimal.valueOf(10), BigDecimal.valueOf(10), expectedLocalDateTime));

		Mockito.when(vehicleLocationRepository.findLatestForEachId()).thenReturn(vehicleLocationList);

//		when
		List<SearchResponseDto> responseDtoList = vehicleService.searchVehicleLocation(requestDto);

//		then
		assertThat(responseDtoList).hasSize(1);
		assertThat(responseDtoList.get(0).getId()).isEqualTo(expectedId);
		assertThat(responseDtoList.get(0).getLatitude()).isEqualTo(expectedLatitude);
		assertThat(responseDtoList.get(0).getLongitude()).isEqualTo(expectedLongitude);
		assertThat(responseDtoList.get(0).getTimestamp()).isEqualTo(expectedLocalDateTime);
	}

	@DisplayName("History Service 테스트")
	@Test
	void historyVehicleLocation() {
//		given
		HistoryRequestDto requestDto = HistoryRequestDto.builder()
				.id("1")
				.startTime(LocalDateTime.of(2023, 7, 10, 9, 0, 0))
				.endTime(LocalDateTime.of(2023, 7, 10, 11, 0, 0))
				.build();

		String expectedId = "1";
		BigDecimal expectedLatitude = BigDecimal.valueOf(10);
		BigDecimal expectedLongitude = BigDecimal.valueOf(10);
		LocalDateTime expectedLocalDateTime = LocalDateTime.of(2023, 7, 10, 10, 0, 0);

		List<VehicleLocation> vehicleLocationList = List.of(
				new VehicleLocation("1", BigDecimal.valueOf(10), BigDecimal.valueOf(10), expectedLocalDateTime));

		Mockito.when(vehicleLocationRepository.findByIdAndTimestampBetween(requestDto.getId(),
				requestDto.getStartTime(), requestDto.getEndTime())).thenReturn(vehicleLocationList);

//		when
		List<HistoryResponseDto> responseDtoList = vehicleService.historyVehicleLocation(requestDto);

//		then
		assertThat(responseDtoList).hasSize(1);
		assertThat(responseDtoList.get(0).getId()).isEqualTo(expectedId);
		assertThat(responseDtoList.get(0).getLatitude()).isEqualTo(expectedLatitude);
		assertThat(responseDtoList.get(0).getLongitude()).isEqualTo(expectedLongitude);
		assertThat(responseDtoList.get(0).getTimestamp()).isEqualTo(expectedLocalDateTime);
	}
}