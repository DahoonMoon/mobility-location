package com.moondahoon.mobilityserver.interceptor;

import com.moondahoon.Veheiclelocation.GetRequest;
import com.moondahoon.Veheiclelocation.HistoryRequest;
import com.moondahoon.Veheiclelocation.PutRequest;
import com.moondahoon.Veheiclelocation.SearchRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RequestValidator {
    public static final String ID_NOT_NULL_NOT_EMPTY_DESCRIPTION = "ID must Not Null & Not Empty";
    public static final String LATITUDE_RANGE_DESCRIPTION = "Latitude 범위 : -90 < Latitude < 90";
    public static final String LONGITUDE_RANGE_DESCRIPTION = "Longitude 범위 : -180 < Longitude < 180";
    public static final String RADIUS_RANGE_DESCRIPTION = "Radius 범위 : 0 <= Radius";
    public static final String DATETIME_REGEX_DESCRIPTION = "요청 날짜 형식 : yyyy-MM-dd HH:mm:ss";

    public static void validatePutRequest(PutRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new IllegalArgumentException(ID_NOT_NULL_NOT_EMPTY_DESCRIPTION);
        }
        if (request.getLatitude() < -90 || request.getLatitude() > 90) {
            throw new IllegalArgumentException(LATITUDE_RANGE_DESCRIPTION);
        }
        if (request.getLongitude() < -180 || request.getLongitude() > 180) {
            throw new IllegalArgumentException(LONGITUDE_RANGE_DESCRIPTION);
        }
    }

    public static void validateGetRequest(GetRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new IllegalArgumentException(ID_NOT_NULL_NOT_EMPTY_DESCRIPTION);
        }
    }

    public static void validateSearchRequest(SearchRequest request) {
        if (request.getLatitude() < -90 || request.getLatitude() > 90) {
            throw new IllegalArgumentException(LATITUDE_RANGE_DESCRIPTION);
        }
        if (request.getLongitude() < -180 || request.getLongitude() > 180) {
            throw new IllegalArgumentException(LONGITUDE_RANGE_DESCRIPTION);
        }
        if (request.getRadius() < 0) {
            throw new IllegalArgumentException(RADIUS_RANGE_DESCRIPTION);
        }
    }

    public static void validateHistoryRequest(HistoryRequest request) {
        if (request.getId() == null || request.getId().isEmpty()) {
            throw new IllegalArgumentException(ID_NOT_NULL_NOT_EMPTY_DESCRIPTION);
        }
        if (!isValidDatetimeFormat(request.getStartTime())) {
            throw new IllegalArgumentException(DATETIME_REGEX_DESCRIPTION);
        }
        if (!isValidDatetimeFormat(request.getEndTime())) {
            throw new IllegalArgumentException(DATETIME_REGEX_DESCRIPTION);
        }
    }

    private static boolean isValidDatetimeFormat(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            LocalDateTime.parse(dateTime, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    }
