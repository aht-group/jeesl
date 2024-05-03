package org.jeesl.controller.processor.system.io.iot.matrix;

import java.time.LocalTime;

import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixHourProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixHourProcessor.class);
	
	private final JeeslJsonMatrixDeviceFactory jfDevice;
	public final static String colorHour = "254254254";
	public final static String colorNow = "255255000";
	
	public static IotMatrixHourProcessor instance(JeeslJsonMatrixDeviceFactory jfDevice) {return new IotMatrixHourProcessor(jfDevice);}
	private IotMatrixHourProcessor(JeeslJsonMatrixDeviceFactory jfDevice)
	{
		this.jfDevice=jfDevice;
	}
	
	public void build(MatrixProcessorCursor cursor, LocalTime start, LocalTime now, int size)
	{
		cursor.previous();
		for(int i=0;i<size;i++)
		{
			cursor.next();
			jfDevice.apply(cursor, start.plusHours(i).getHour()==now.getHour() ? colorNow : colorHour);
		}
	}
}