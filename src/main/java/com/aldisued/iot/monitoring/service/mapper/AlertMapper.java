package com.aldisued.iot.monitoring.service.mapper;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    AlertDto toDTO(Alert alert);

    Alert toEntity(AlertDto alertDto);
}
