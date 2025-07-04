package com.algaworks.algasensors.device.management.api.mapper;

import com.algaworks.algasensors.device.management.api.model.SensorInput;
import com.algaworks.algasensors.device.management.api.model.SensorOutput;
import com.algaworks.algasensors.device.management.domain.model.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper()
public interface SensorMapper {

    @Mapping(target = "id", expression = "java(new com.algaworks.algasensors.device.management.domain.model.SensorId(com.algaworks.algasensors.device.management.common.IdGenerator.generateTSID()))")
    @Mapping(target = "enabled", constant = "true")
    Sensor sensorInputToNewSensor(SensorInput sensorInput);

    @Mapping(target = "id", source = "sensor.id.value")
    SensorOutput sensorToSensorOuput(Sensor sensor);
}
