package com.moondahoon.mobilityserver.service;

import com.moondahoon.mobilityserver.model.dto.request.GetRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.HistoryRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.PutRequestDto;
import com.moondahoon.mobilityserver.model.dto.request.SearchRequestDto;
import com.moondahoon.mobilityserver.model.dto.response.GetResponseDto;
import com.moondahoon.mobilityserver.model.dto.response.HistoryResponseDto;
import com.moondahoon.mobilityserver.model.dto.response.SearchResponseDto;
import com.moondahoon.mobilityserver.model.entity.VehicleLocation;
import com.moondahoon.mobilityserver.repository.memory.VehicleLocationRepository;
import com.moondahoon.mobilityserver.util.DistanceUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleLocationRepository vehicleLocationRepository;
    ModelMapper modelMapper = new ModelMapper();

    public void putVehicleLocation(PutRequestDto requestDto) {
        VehicleLocation vehicleLocation = modelMapper.map(requestDto, VehicleLocation.class);
        vehicleLocation.setTimestamp(LocalDateTime.now());

        vehicleLocationRepository.save(vehicleLocation);
    }

    public GetResponseDto getVehicleLocation(GetRequestDto requestDto) {
        VehicleLocation vehicleLocation = vehicleLocationRepository.findTopByIdOrderByTimestampDesc(requestDto.getId());

        return modelMapper.map(vehicleLocation, GetResponseDto.class);
    }

    public List<SearchResponseDto> searchVehicleLocation(SearchRequestDto requestDto) {
        List<VehicleLocation> vehicleLocationList = vehicleLocationRepository.findLatestForEachId();

        return vehicleLocationList.stream()
            .filter(vehicleLocation -> {
                BigDecimal distance = DistanceUtils.calculateDistance(requestDto.getLatitude(),
                    requestDto.getLongitude(),
                    vehicleLocation.getLatitude(), vehicleLocation.getLongitude());
                return distance.compareTo(requestDto.getRadius()) < 0;
            })
            .map(vehicleLocation -> SearchResponseDto.builder()
                .id(vehicleLocation.getId())
                .latitude(vehicleLocation.getLatitude())
                .longitude(vehicleLocation.getLongitude())
                .distance(DistanceUtils.calculateDistance(requestDto.getLatitude(), requestDto.getLongitude(),
                        vehicleLocation.getLatitude(), vehicleLocation.getLongitude())
                    .setScale(2, RoundingMode.HALF_UP))
                .timestamp(vehicleLocation.getTimestamp())
                .build())
            .collect(Collectors.toList());
    }

    public List<HistoryResponseDto> historyVehicleLocation(HistoryRequestDto requestDto) {
        List<VehicleLocation> vehicleLocationList = vehicleLocationRepository.findByIdAndTimestampBetween(
            requestDto.getId(),
            requestDto.getStartTime(), requestDto.getEndTime());

        List<HistoryResponseDto> responseDtoList = new ArrayList<>();
        vehicleLocationList.forEach(vehicleLocation ->
            responseDtoList.add(modelMapper.map(vehicleLocation, HistoryResponseDto.class)));

        return responseDtoList;
    }


}
