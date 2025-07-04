package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.mapper.SensorMapper;
import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;
    private final SensorMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput sensorInput) {
        Sensor sensor = mapper.sensorInputToNewSensor(sensorInput);
        sensor = sensorRepository.saveAndFlush(sensor);
        return mapper.sensorToSensorOuput(sensor);
    }
}
