package com.moondahoon.mobilityserver.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DistanceUtilsTest {

	@DisplayName("두 좌표 사이 거리 계산 테스트")
	@Test
	void getDistanceTest(){
//		given
		BigDecimal latitudeSeoul = BigDecimal.valueOf(37.541);
		BigDecimal longitudeSeoul = BigDecimal.valueOf(126.986);
		BigDecimal latitudeBusan = BigDecimal.valueOf(35.180);
		BigDecimal longitudeBusan = BigDecimal.valueOf(129.076);

//		when
		BigDecimal distance = DistanceUtils.calculateDistance(latitudeSeoul, longitudeSeoul, latitudeBusan, longitudeBusan);

//		then
		assertThat(distance).isCloseTo(BigDecimal.valueOf(320000), Percentage.withPercentage(1));
	}
}