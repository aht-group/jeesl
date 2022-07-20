package org.jeesl.controller.processor.system.io.iot.matrix;

import java.time.LocalDate;

import org.jeesl.model.json.io.iot.matrix.MatrixDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixWeekdayProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixWeekdayProcessor.class);
	
	public final static String colorDay = "010010010";
	public final static String colorNow = "020010010";
	
	public IotMatrixWeekdayProcessor()
	{
		
	}
	
	public void build(MatrixDevice device, LocalDate start, LocalDate now, int size)
	{
		for(int i=0;i<size;i++)
		{
			boolean isNow = start.plusDays(i).isEqual(now);
			if(isNow) {device.getData().add(colorNow);}
			else{device.getData().add(colorDay);}
		}
	}
}