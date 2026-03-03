package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Fixed
{
    @JsonProperty("slope")
    private Slope slope;
    public Slope getSlope() { return slope; }
    public void setSlope(Slope slope) { this.slope = slope; }

    @JsonProperty("azimuth")
    private Azimuth azimuth;
    public Azimuth getAzimuth() { return azimuth; }
    public void setAzimuth(Azimuth azimuth) { this.azimuth = azimuth; }

    @JsonProperty("type")
    private String type;
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}