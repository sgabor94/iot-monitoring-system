package com.aldisued.iot.monitoring.service;


import java.util.ArrayList;
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
    if (data == null || data.isEmpty()) {
      throw new IllegalArgumentException("Data list must not be null or empty");
    }
    if (windowSize <= 0 || windowSize > data.size()) {
      throw new IllegalArgumentException("Window size must be positive and cannot exceed data size");
    }
    List<Double> movingAverages = new ArrayList<>();
    for (int i = 0; i <= data.size() - windowSize; i++) {
      double sum = 0.0;
      for (int j = i; j < i + windowSize; j++) {
        sum += data.get(j);
      }
      movingAverages.add(sum / windowSize);
    }
    return movingAverages;
  }

}
