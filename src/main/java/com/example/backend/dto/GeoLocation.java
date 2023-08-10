package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoLocation {
    private String country;
    private String code;
    private String r1;
    private String r2;
    private String r3;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("long")
    private double longitude;

    private String net;
}
