package net.sf.ahtutils.interfaces.model.sensor;

import org.jeesl.model.xml.module.monitoring.Data;
import org.jeesl.model.xml.module.monitoring.Indicator;
import org.jeesl.model.xml.module.monitoring.Observer;

public interface Sensor extends SensorRead
{	
	public double getRaw();

	public Data getData();
	
	public Indicator getIndicator();
	public Observer getObserver();
}
