package org.jeesl.controller.processor.system.io.iot.matrix;

import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixOnProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixOnProcessor.class);
	
	public final String colorOn = "255255255";
	
	public IotMatrixOnProcessor()
	{
		
	}
	
	
	public void build(JsonMatrixDevice device, int size)
	{
		for(int i=1;i<=size;i++)
		{
			device.getData().add(colorOn);
		}
	}
	
	public void build(JeeslJsonMatrixDeviceFactory factory, MatrixProcessorCursor cursorStart, int sizeRow, int sizeCol)
	{
		MatrixProcessorCursor cursor = MatrixProcessorCursor.clone(cursorStart);
		for(int row=0;row<sizeRow;row++)
		{
			for(int col=0;col<sizeCol;col++)
			{
				cursor.move(cursorStart, row, col);
				String color = colorOn;
				logger.info(cursor.toString()+" "+color);
				factory.getCells()[cursor.getRow()-1][cursor.getColumn()-1] = color;
			}
		}
		cursorStart.apply(cursor);
	}
}