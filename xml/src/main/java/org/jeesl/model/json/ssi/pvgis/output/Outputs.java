package org.jeesl.model.json.ssi.pvgis.output;

import java.util.List;

public class Outputs
{
    private List<HourlyEntry> hourly;
    public List<HourlyEntry> getHourly() { return hourly; }
    public void setHourly(List<HourlyEntry> hourly) { this.hourly = hourly; }
}