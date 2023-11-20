package org.jeesl.controller.processor.system.io.iot.matrix;

import java.time.LocalDate;

import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
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
	
	public void build(JeeslJsonMatrixDeviceFactory factory, MatrixProcessorCursor cursorStart, int size)
	{
		MatrixProcessorCursor cursor = MatrixProcessorCursor.clone(cursorStart);
		for(int i=0;i<size;i++)
		{
			cursor.moveCol(cursorStart, i);
			factory.getCells()[cursor.getRow()-1][cursor.getColumn()-1] = colorDay;
		}
		cursorStart.apply(cursor);
	}
	public void build(JeeslJsonMatrixDeviceFactory factory, MatrixProcessorCursor cursorStart, LocalDate start, LocalDate now, int size)
	{
		MatrixProcessorCursor cursor = MatrixProcessorCursor.clone(cursorStart);
		for(int i=0;i<size;i++)
		{
			cursor.moveCol(cursorStart, i);
			String color = colorDay;
			if(start.plusDays(i).isEqual(now)) {color = colorNow;}
			logger.info(cursor.toString()+" "+color);
			factory.getCells()[cursor.getRow()-1][cursor.getColumn()-1] = color;
		}
		cursorStart.apply(cursor);
	}
	
	public void build(JsonMatrixDevice device, LocalDate start, LocalDate now, int size)
	{
		for(int i=0;i<size;i++)
		{
			boolean isNow = start.plusDays(i).isEqual(now);
			if(isNow) {device.getData().add(colorNow);}
			else{device.getData().add(colorDay);}
		}
	}
}