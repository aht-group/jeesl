package org.jeesl.controller.processor.system.io.iot.matrix;

import org.jeesl.model.json.io.iot.matrix.MatrixDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixOffProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixOffProcessor.class);
	
	public final static String colorOff = "000000000";
	
	public void build(MatrixDevice device, int size)
	{
		for(int i=1;i<=size;i++)
		{
			device.getData().add(colorOff);
		}
	}
	
	public void padding(MatrixDevice device)
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
}