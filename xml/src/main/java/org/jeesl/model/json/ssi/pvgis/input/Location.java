package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location
{
    @JsonProperty("latitude")
    private Double latitude;
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    @JsonProperty("longitude")
    private Double longitude;
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    @JsonProperty("elevation")
    private Double elevation;
    public Double getElevation() { return elevation; }
    public void setElevation(Double elevation) { this.elevation = elevation; }
}