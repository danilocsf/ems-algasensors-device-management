package com.algaworks.algasensors.device.management.api.controller;

import com.algaworks.algasensors.device.management.api.client.SensorMonitoringClient;
import com.algaworks.algasensors.device.management.api.mapper.SensorMapper;
import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import com.algaworks.algasensors.device.management.domain.model.SensorId;
import com.algaworks.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;
    private final SensorMapper mapper;
    private final SensorMonitoringClient sensorMonitoringClient;

    @GetMapping
    public Page<SensorOutput> findAll(@PageableDefault Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findAll(pageable);
        return sensors.map(mapper::sensorToSensorOuput);
    }

    @GetMapping("{sensorId}")
    public SensorOutput findById(@PathVariable("sensorId") TSID id) {
        Sensor sensor = sensorRepository.findById(new SensorId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapper.sensorToSensorOuput(sensor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput sensorInput) {
        Sensor sensor = mapper.sensorInputToNewSensor(sensorInput);
        sensor = sensorRepository.saveAndFlush(sensor);
        return mapper.sensorToSensorOuput(sensor);
    }

    @PutMapping("{sensorId}")
    @ResponseStatus(HttpStatus.OK)
    public SensorOutput update(@RequestBody SensorInput sensorInput, @PathVariable("sensorId") TSID id) {
        Sensor sensor = sensorRepository.findById(new SensorId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapper.updateSensorFromSensorInput(sensorInput, sensor);
        sensorRepository.saveAndFlush(sensor);
        return mapper.sensorToSensorOuput(sensor);
    }

    @DeleteMapping("{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("sensorId") TSID id) {
        Sensor sensor = sensorRepository.findById(new SensorId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensorRepository.delete(sensor);
        sensorMonitoringClient.disableMonitoring(id);
    }

    @PutMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable("sensorId") TSID id) {
        Sensor sensor = sensorRepository.findById(new SensorId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensor.setEnabled(true);
        sensorRepository.saveAndFlush(sensor);
        sensorMonitoringClient.enableMonitoring(id);
    }

    @DeleteMapping("{sensorId}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable("sensorId") TSID id) {
        Sensor sensor = sensorRepository.findById(new SensorId(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensor.setEnabled(false);
        sensorRepository.saveAndFlush(sensor);
        sensorMonitoringClient.disableMonitoring(id);
    }
}
