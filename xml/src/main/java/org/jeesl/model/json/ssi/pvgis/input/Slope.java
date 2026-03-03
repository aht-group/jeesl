package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Slope
{
    @JsonProperty("value")
    private Double value;
    public Double getValue() {return value;}
    public void setValue(Double value) {this.value = value;} 

    @JsonProperty("optimal")
    private Boolean optimal;
    public Boolean isOptimal() {return optimal;}
    public void setOptimal(Boolean optimal) {this.optimal = optimal;}
}