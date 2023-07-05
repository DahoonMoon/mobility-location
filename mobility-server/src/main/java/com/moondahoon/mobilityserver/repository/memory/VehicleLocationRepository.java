package com.moondahoon.mobilityserver.repository.memory;

import com.moondahoon.mobilityserver.model.entity.VehicleLocation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class VehicleLocationRepository {

	//	메모리 데이터 저장소 용도의 ArrayList 생성
	private List<VehicleLocation> vehicleLocationList = new ArrayList<>();

	public void save(VehicleLocation vehicleLocation) {
		vehicleLocationList.add(vehicleLocation);
	}

	public List<VehicleLocation> findAll() {
		return vehicleLocationList;
	}

	public VehicleLocation findTopByIdOrderByTimestampDesc(String id) {
		return vehicleLocationList.stream()
				.filter(vehicleLocation -> vehicleLocation.getId().equals(id))
				.max(Comparator.comparing(VehicleLocation::getTimestamp))
				.orElse(null);
	}

//	JPA에서는 query 직접 작성해줘야함
	public List<VehicleLocation> findLatestForEachId() {
		Map<String, VehicleLocation> latestLocationsMap = vehicleLocationList.stream()
				.collect(Collectors.toMap(VehicleLocation::getId, Function.identity(),
						(entity1, entity2) -> {
							if (entity1.getTimestamp().isAfter(entity2.getTimestamp())) {
								return entity1;
							} else {
								return entity2;
							}
						})
				);
		return new ArrayList<>(latestLocationsMap.values());
	}

	public List<VehicleLocation> findByIdAndTimestampBetween(String id, LocalDateTime startTime, LocalDateTime endTime){
		log.info("{} {}", vehicleLocationList.get(0).getId(), vehicleLocationList.get(0).getTimestamp());

		return vehicleLocationList.stream()
				.filter(vehicleLocation -> vehicleLocation.getId().equals(id)
						&& vehicleLocation.getTimestamp().isAfter(startTime)
						&& (vehicleLocation.getTimestamp().isEqual(endTime) || vehicleLocation.getTimestamp().isBefore(endTime)))
				.collect(Collectors.toList());
	}

}
