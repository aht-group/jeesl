package org.jeesl.controller.processor.system.io.iot.matrix;

import java.time.LocalTime;

import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixHourProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixHourProcessor.class);
	
	public final static String colorHour = "254254254";
	public final static String colorNow = "255255000";
	
	public IotMatrixHourProcessor()
	{
		
	}
	
	public void build(JsonMatrixDevice device, LocalTime start, LocalTime now, int size)
	{
		for(int i=0;i<size;i++)
		{
			boolean isNow = start.plusHours(i).getHour()==now.getHour();
			if(isNow) {device.getData().add(colorNow);}
			else{device.getData().add(colorHour);}
		}
	}
}