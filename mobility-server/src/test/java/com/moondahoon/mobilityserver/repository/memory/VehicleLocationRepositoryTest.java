package com.moondahoon.mobilityserver.repository.memory;

import static org.assertj.core.api.Assertions.assertThat;

import com.moondahoon.mobilityserver.model.entity.VehicleLocation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VehicleLocationRepositoryTest {

	private VehicleLocationRepository repository;

	@BeforeEach
	void setUp() {
		repository = new VehicleLocationRepository();

		VehicleLocation vehicleLocation1 = new VehicleLocation("1", BigDecimal.valueOf(10), BigDecimal.valueOf(10), LocalDateTime.now().minusMinutes(30));
		VehicleLocation vehicleLocation2 = new VehicleLocation("1", BigDecimal.valueOf(20), BigDecimal.valueOf(20), LocalDateTime.now().minusMinutes(10));
		VehicleLocation vehicleLocation3 = new VehicleLocation("2", BigDecimal.valueOf(30), BigDecimal.valueOf(30), LocalDateTime.now().minusMinutes(25));

		repository.save(vehicleLocation1);
		repository.save(vehicleLocation2);
		repository.save(vehicleLocation3);
	}

	@DisplayName("Save 테스트")
	@Test
	void testSave() {
//		given
		int expectedSize = 4;
		VehicleLocation vehicleLocation1 = new VehicleLocation("5", BigDecimal.valueOf(10), BigDecimal.valueOf(10), LocalDateTime.now().minusMinutes(30));

//		when
		repository.save(vehicleLocation1);

//		then
		assertThat(repository.getVehicleLocationList()).hasSize(expectedSize);
	}

	@DisplayName("FindAll 테스트")
	@Test
	void testFindAll() {
//		given
		int expectedSize = 3;

//		when
		List<VehicleLocation> vehicleLocations = repository.findAll();

//		when
		assertThat(vehicleLocations).hasSize(expectedSize);
	}

	@DisplayName("FindTopByIdOrderByTimestampDesc 테스트")

	@Test
	void testFindTopByIdOrderByTimestampDesc() {
//		given
		String id = "1";
		BigDecimal expectedLatitude = BigDecimal.valueOf(20);
		BigDecimal expectedLongitude = BigDecimal.valueOf(20);

//		when
		VehicleLocation vehicleLocation = repository.findTopByIdOrderByTimestampDesc(id);

//		when
		assertThat(vehicleLocation.getId()).isEqualTo(id);
		assertThat(vehicleLocation.getLatitude()).isEqualTo(expectedLatitude);
		assertThat(vehicleLocation.getLatitude()).isEqualTo(expectedLongitude);
	}

	@DisplayName("FindLatestForEachId 테스트")
	@Test
	void testFindLatestForEachId() {
//		given
		int expectedSize = 2;

//		when
		List<VehicleLocation> vehicleLocations = repository.findLatestForEachId();

//		then
		assertThat(vehicleLocations).hasSize(expectedSize);
	}

	@DisplayName("FindByIdAndTimestampBetween 테스트")
	@Test
	void testFindByIdAndTimestampBetween() {
//		given
		String id = "1";
		LocalDateTime startTime = LocalDateTime.now().minusMinutes(40);
		LocalDateTime endTime = LocalDateTime.now().minusMinutes(20);

//		when
		List<VehicleLocation> vehicleLocations = repository.findByIdAndTimestampBetween(id, startTime, endTime);

//		then
		assertThat(vehicleLocations.get(0).getId()).isEqualTo(id);
	}
}