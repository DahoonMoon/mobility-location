package com.moondahoon.mobilityserver.util;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DistanceUtilsTest {

	@Test
	void getDistanceTest(){

		BigDecimal x1 = BigDecimal.valueOf(0);
		BigDecimal y1 = BigDecimal.valueOf(0);
		BigDecimal x2 = BigDecimal.valueOf(1);
		BigDecimal y2 = BigDecimal.valueOf(10);

		BigDecimal distance = DistanceUtils.calculateDistance(x1, y1, x2, y2);

		System.out.println(distance);

	}

}