package org.jeesl.model.json.ssi.pvgis.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MountingSystem
{
    @JsonProperty("fixed")
    private Fixed fixed;
    public Fixed getFixed() { return fixed; }
    public void setFixed(Fixed fixed) { this.fixed = fixed; }
}