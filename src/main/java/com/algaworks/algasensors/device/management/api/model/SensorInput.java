package com.algaworks.algasensors.device.management.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorInput {
    private String name;
    private String location;
    private String ip;
    private String protocol;
    private String model;
}
