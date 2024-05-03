package org.jeesl.controller.processor.system.io.iot.matrix;

import org.jeesl.interfaces.controller.iot.IotMatrixProcessor;
import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixOffProcessor implements IotMatrixProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixOffProcessor.class);
	
	private final JeeslJsonMatrixDeviceFactory jfDevice;
	
	public final static String colorOff = "000000000";
	
	public static IotMatrixOffProcessor instance(JeeslJsonMatrixDeviceFactory jfDevice) {return new IotMatrixOffProcessor(jfDevice);}
	private IotMatrixOffProcessor(JeeslJsonMatrixDeviceFactory jfDevice)
	{
		this.jfDevice=jfDevice;
	}
	
	@Deprecated public void build(JeeslJsonMatrixDeviceFactory factory, MatrixProcessorCursor cursorStart, int sizeRow, int sizeCol)
	{
		MatrixProcessorCursor cursor = MatrixProcessorCursor.clone(cursorStart);
		for(int row=0;row<sizeRow;row++)
		{
			for(int col=0;col<sizeCol;col++)
			{
				cursor.move(cursorStart, row, col);
				String color = colorOff;
				logger.info(cursor.toString()+" "+color);
				factory.getCells()[cursor.getRow()-1][cursor.getColumn()-1] = color;
			}
		}
		cursorStart.apply(cursor);
	}
	
	public void build(MatrixProcessorCursor cursor, int rowSpan, int colSpan)
	{
		for(int row=0; row<rowSpan; row++)
		{
			for(int col=0; col<colSpan; col++)
			{
				jfDevice.offset(cursor,row,col, colorOff);
			}
		}
		cursor.move(rowSpan-1, colSpan-1);
	}
	
	public void padding(JsonMatrixDevice device)
	{
		int size = device.getData().size();
		int columns = device.getColumns();
		int mod = size%columns;
		
		logger.info("size:"+size+" columns:"+columns+" mod"+mod);
		
		if(mod>0)
		{
			this.build(device,columns-mod);
		}
	}
	@Deprecated private void build(JsonMatrixDevice device, int size)
	{
		for(int i=1;i<=size;i++)
		{
			device.getData().add(colorOff);
		}
	}
}