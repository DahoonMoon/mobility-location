package com.moondahoon.mobilityserver.util;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class DistanceUtilsTest {

	@Test
	void getDistanceTest(){

		BigDecimal x1 = BigDecimal.valueOf(100);
		BigDecimal y1 = BigDecimal.valueOf(200);
		BigDecimal x2 = BigDecimal.valueOf(100);
		BigDecimal y2 = BigDecimal.valueOf(300);

		BigDecimal distance = DistanceUtils.getEuclideanDistance(x1, y1, x2, y2);

		System.out.println(distance);

	}

}