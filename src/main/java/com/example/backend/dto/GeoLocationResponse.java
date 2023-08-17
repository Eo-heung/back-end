package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeoLocationResponse {
    private int returnCode;
    private String requestId;
    private GeoLocation geoLocation;

}
