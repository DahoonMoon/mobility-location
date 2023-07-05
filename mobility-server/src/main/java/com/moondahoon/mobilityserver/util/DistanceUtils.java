package com.moondahoon.mobilityserver.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;

public class DistanceUtils {

	private DistanceUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static BigDecimal getEuclideanDistance(BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
//		피타고라스 정리
		BigDecimal deltaX = x2.subtract(x1);
		BigDecimal deltaY = y2.subtract(y1);
		BigDecimal distanceSquared = deltaX.pow(2).add(deltaY.pow(2));
//		소수점 10자리, 반올림하여 거리 계산
		return distanceSquared.sqrt(new MathContext(10, RoundingMode.HALF_UP));
	}

}
