package com.moondahoon.mobilityserver.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleLocation {

	@Id
	private String id;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private LocalDateTime timestamp;

}
