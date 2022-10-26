package org.jeesl.controller.processor.system.io.iot.matrix;

import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixOnProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixOnProcessor.class);
	
	public final String color = "200000000";
	
	public IotMatrixOnProcessor()
	{
		
	}
	
	
	public void build(JsonMatrixDevice device, int size)
	{
		for(int i=1;i<=size;i++)
		{
			device.getData().add(color);
		}
	}
}