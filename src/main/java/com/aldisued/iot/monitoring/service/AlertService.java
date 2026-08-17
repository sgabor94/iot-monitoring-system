package com.aldisued.iot.monitoring.service;

import com.aldisued.iot.monitoring.dto.AlertDto;
import com.aldisued.iot.monitoring.entity.Alert;
import com.aldisued.iot.monitoring.entity.Sensor;
import com.aldisued.iot.monitoring.repository.AlertRepository;
import com.aldisued.iot.monitoring.repository.SensorRepository;
import com.aldisued.iot.monitoring.service.mapper.AlertMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AlertService {

  private final AlertRepository alertRepository;
  private final AlertMapper alertMapper;
  private final SensorRepository sensorRepository;
  private final KafkaTemplate<String, AlertDto> kafkaTemplate;

  public AlertService(AlertRepository alertRepository, AlertMapper alertMapper,
                      SensorRepository sensorRepository,
                      KafkaTemplate<String, AlertDto> kafkaTemplate) {
    this.alertRepository = alertRepository;
    this.alertMapper = alertMapper;
    this.sensorRepository = sensorRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  public Alert saveAlert(AlertDto alertDto) {
    Sensor sensor = sensorRepository.findById(alertDto.sensorId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found with id: " + alertDto.sensorId()));
    Alert alertEntity = alertMapper.toEntity(alertDto);
    alertEntity.setSensor(sensor);
    Alert savedAlert = alertRepository.save(alertEntity);
    kafkaTemplate.send("alerts", alertDto);
    return savedAlert;
  }

  public AlertDto findLastAlertBySensorId(UUID sensorId) {
    Alert alert = alertRepository.findFirstBySensorIdOrderByTimestampDesc(sensorId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No alert found for sensor id: " + sensorId
            ));

    return alertMapper.toDTO(alert);
  }
}
