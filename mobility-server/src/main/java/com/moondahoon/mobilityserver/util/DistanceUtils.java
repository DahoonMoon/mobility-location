package com.moondahoon.mobilityserver.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.extern.slf4j.Slf4j;

public class DistanceUtils {

    private DistanceUtils() {
        throw new IllegalStateException("Utility class");
    }

    //	지구 반지름 미터
    private static final BigDecimal EARTH_RADIUS = new BigDecimal("6371000");

    public static BigDecimal calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        // 위도와 경도를 라디안 단위로 변환
        BigDecimal radLat1 = toRadians(lat1);
        BigDecimal radLon1 = toRadians(lon1);
        BigDecimal radLat2 = toRadians(lat2);
        BigDecimal radLon2 = toRadians(lon2);

        BigDecimal deltaLat = radLat2.subtract(radLat1);
        BigDecimal deltaLon = radLon2.subtract(radLon1);

        // 대원거리 공식
        BigDecimal a = BigDecimal.valueOf(
                Math.sin(deltaLat.divide(new BigDecimal("2"), RoundingMode.HALF_UP).doubleValue()))
            .multiply(
                BigDecimal.valueOf(Math.sin(deltaLat.divide(new BigDecimal("2"), RoundingMode.HALF_UP).doubleValue())))
            .add(BigDecimal.valueOf(Math.cos(radLat1.doubleValue()))
                .multiply(BigDecimal.valueOf(Math.cos(radLat2.doubleValue())))
                .multiply(
                    BigDecimal.valueOf(
                        Math.sin(deltaLon.divide(new BigDecimal("2"), RoundingMode.HALF_UP).doubleValue())))
                .multiply(BigDecimal.valueOf(
                    Math.sin(deltaLon.divide(new BigDecimal("2"), RoundingMode.HALF_UP).doubleValue()))));

        BigDecimal c = new BigDecimal("2").multiply(
            BigDecimal.valueOf(Math.atan2(Math.sqrt(a.doubleValue()), Math.sqrt(1 - a.doubleValue()))));

        return EARTH_RADIUS.multiply(c);
    }

    private static BigDecimal toRadians(BigDecimal degree) {
        BigDecimal radiansPerDegree = new BigDecimal(Math.PI).divide(new BigDecimal("180"), RoundingMode.HALF_UP);
        return degree.multiply(radiansPerDegree);
    }

}
