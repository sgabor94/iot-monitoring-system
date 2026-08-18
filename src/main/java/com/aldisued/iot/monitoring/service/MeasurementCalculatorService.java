package com.aldisued.iot.monitoring.service;


import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MeasurementCalculatorService {

  public List<Double> filterByAverageDeviation(List<Double> values, Double deviation) {
    if (deviation == null || deviation < 0.0 || deviation > 1.0) {
      throw new IllegalArgumentException("Deviation must be between 0.0 and 1.0");
    }
    if (values == null || values.isEmpty()) {
      return Collections.emptyList();
    }
    double average = values.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    double deviationAmount = average * deviation;
    double minAcceptable = average - deviationAmount;
    double maxAcceptable = average + deviationAmount;

    return values.stream().filter(val -> val >= minAcceptable && val <= maxAcceptable).toList();
  }

  public List<Double> getMovingAverage(List<Double> data, int windowSize) {
    // TODO: Task 10
    return List.of();
  }

}
