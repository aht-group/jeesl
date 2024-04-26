package org.jeesl.controller.processor.system.io.iot.matrix;

import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixOnProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixOnProcessor.class);
	
	private final JeeslJsonMatrixDeviceFactory jfDevice;
	
	public String colorOn = "255255255"; public IotMatrixOnProcessor color(String color) {this.colorOn=color; return this;}
	
	public static IotMatrixOnProcessor instance(JeeslJsonMatrixDeviceFactory jfDevice) {return new IotMatrixOnProcessor(jfDevice);}
	private IotMatrixOnProcessor(JeeslJsonMatrixDeviceFactory jfDevice)
	{
		this.jfDevice=jfDevice;
	}
	
	@Deprecated
	public void build(JsonMatrixDevice device, int size)
	{
		for(int i=1;i<=size;i++)
		{
			device.getData().add(colorOn);
		}
	}
	
	@Deprecated
	public void build(JeeslJsonMatrixDeviceFactory factory, MatrixProcessorCursor cursorStart, int sizeRow, int sizeCol)
	{
		MatrixProcessorCursor cursor = MatrixProcessorCursor.clone(cursorStart);
		for(int row=0;row<sizeRow;row++)
		{
			for(int col=0;col<sizeCol;col++)
			{
				cursor.move(cursorStart, row, col);
				String c = colorOn;
				logger.info(cursor.toString()+" "+c);
				factory.getCells()[cursor.getRow()-1][cursor.getColumn()-1] = c;
			}
		}
		cursorStart.apply(cursor);
	}
	
	public void build(MatrixProcessorCursor cursor, int rowSpan, int colSpan)
	{
		
		for(int row=0;row<rowSpan;row++)
		{
			for(int col=0;col<colSpan;col++)
			{
				String c = colorOn;
				jfDevice.offset(cursor,row,col, c);
			}
		}
		cursor.move(rowSpan-1, colSpan-1);
	}
	
	public void build(MatrixProcessorCursor cursor)
	{
		
		for(int row=0;row<cursor.getRowSpan();row++)
		{
			for(int col=0;col<cursor.getColSpan();col++)
			{
				String c = colorOn;
				jfDevice.offset(cursor,row,col, c);
			}
		}
		cursor.move(cursor.getRowSpan()-1, cursor.getColSpan()-1);
	}
}