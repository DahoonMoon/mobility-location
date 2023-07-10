package com.moondahoon.mobilityserver.interceptor;

import static com.moondahoon.mobilityserver.interceptor.RequestValidator.DATETIME_REGEX_DESCRIPTION;
import static com.moondahoon.mobilityserver.interceptor.RequestValidator.ID_NOT_NULL_NOT_EMPTY_DESCRIPTION;
import static com.moondahoon.mobilityserver.interceptor.RequestValidator.LATITUDE_RANGE_DESCRIPTION;
import static com.moondahoon.mobilityserver.interceptor.RequestValidator.LONGITUDE_RANGE_DESCRIPTION;
import static com.moondahoon.mobilityserver.interceptor.RequestValidator.RADIUS_RANGE_DESCRIPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.moondahoon.Veheiclelocation.GetRequest;
import com.moondahoon.Veheiclelocation.HistoryRequest;
import com.moondahoon.Veheiclelocation.PutRequest;
import com.moondahoon.Veheiclelocation.SearchRequest;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RequestValidatorTest {

	@DisplayName("Validator for PutRequest 테스트")
	@ParameterizedTest
	@MethodSource("parametersForValidatePutRequest")
	void validatePutRequest(String id, Double latitude, Double longitude, String expectedMessage) {
//		given
		PutRequest request = PutRequest.newBuilder()
				.setId(id)
				.setLatitude(latitude)
				.setLongitude(longitude)
				.build();

//		when
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> RequestValidator.validatePutRequest(request)
		);

//		then
		assertThat(exception.getMessage()).isEqualTo(expectedMessage);
	}

	private static Stream<Arguments> parametersForValidatePutRequest() {
		return Stream.of(
				Arguments.arguments("", 45.0, 90.0, ID_NOT_NULL_NOT_EMPTY_DESCRIPTION),
				Arguments.of("1", 100.0, 90.0, LATITUDE_RANGE_DESCRIPTION),
				Arguments.of("1", 45.0, 200.0, LONGITUDE_RANGE_DESCRIPTION)
		);
	}


	@DisplayName("Validator for GetRequest 테스트")
	@ParameterizedTest
	@MethodSource("parametersForValidateGetRequest")
	void validateGetRequest(String id, String expectedMessage) {
		// given
		GetRequest request = GetRequest.newBuilder()
				.setId(id)
				.build();

		// when
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> RequestValidator.validateGetRequest(request)
		);

		// then
		assertThat(exception.getMessage()).isEqualTo(expectedMessage);
	}

	private static Stream<Arguments> parametersForValidateGetRequest() {
		return Stream.of(
				Arguments.of("", ID_NOT_NULL_NOT_EMPTY_DESCRIPTION)
		);
	}

	@DisplayName("Validator for SearchRequest 테스트")
	@ParameterizedTest
	@MethodSource("parametersForValidateSearchRequest")
	void validateSearchRequest(Double latitude, Double longitude, Double radius, String expectedMessage) {
		// given
		SearchRequest request = SearchRequest.newBuilder()
				.setLatitude(latitude)
				.setLongitude(longitude)
				.setRadius(radius)
				.build();

		// when
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> RequestValidator.validateSearchRequest(request)
		);

		// then
		assertThat(exception.getMessage()).isEqualTo(expectedMessage);
	}

	private static Stream<Arguments> parametersForValidateSearchRequest() {
		return Stream.of(
				Arguments.of(100.0, 0.0, 10.0, LATITUDE_RANGE_DESCRIPTION),
				Arguments.of(0.0, 200.0, 10.0, LONGITUDE_RANGE_DESCRIPTION),
				Arguments.of(0.0, 0.0, -10.0, RADIUS_RANGE_DESCRIPTION)
		);
	}

	@DisplayName("Validator for HistoryRequest 테스트")
	@ParameterizedTest
	@MethodSource("parametersForValidateHistoryRequest")
	void validateHistoryRequest(String id, String startTime, String endTime, String expectedMessage) {
		// given
		HistoryRequest request = HistoryRequest.newBuilder()
				.setId(id)
				.setStartTime(startTime)
				.setEndTime(endTime)
				.build();

		// when
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> RequestValidator.validateHistoryRequest(request)
		);

		// then
		assertThat(exception.getMessage()).isEqualTo(expectedMessage);
	}

	private static Stream<Arguments> parametersForValidateHistoryRequest() {
		return Stream.of(
				Arguments.of("", "2023-07-10 10:00:00", "2023-07-10 12:00:00", ID_NOT_NULL_NOT_EMPTY_DESCRIPTION),
				Arguments.of("1", "invalid", "2023-07-10 12:00:00", DATETIME_REGEX_DESCRIPTION),
				Arguments.of("1", "2023-07-10 10:00:00", "invalid", DATETIME_REGEX_DESCRIPTION)
		);
	}
}