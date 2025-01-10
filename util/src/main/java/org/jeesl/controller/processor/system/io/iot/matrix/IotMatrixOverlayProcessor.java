package org.jeesl.controller.processor.system.io.iot.matrix;

import org.jeesl.api.rest.i.iot.JeeslIotMatrixDisplayRestInterface;
import org.jeesl.factory.json.system.io.iot.JsonMatrixDeviceFactory;
import org.jeesl.interfaces.factory.json.JeeslJsonMatrixDeviceFactory;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.pojo.iot.MatrixProcessorCursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixOverlayProcessor
{
	final static Logger logger = LoggerFactory.getLogger(IotMatrixOverlayProcessor.class);
	
	private final JeeslJsonMatrixDeviceFactory jfDevice;
	

	private String deviceCode; public IotMatrixOverlayProcessor device(String code) {this.deviceCode=code; return this;}
	private JsonMatrixDevice remoteDevice;
	private String[][] remoteCells;
	
	public static IotMatrixOverlayProcessor instance(JeeslJsonMatrixDeviceFactory jfDevice) {return new IotMatrixOverlayProcessor(jfDevice);}
	private IotMatrixOverlayProcessor(JeeslJsonMatrixDeviceFactory jfDevice)
	{
		this.jfDevice=jfDevice;
	}
	
	public JsonMatrixDevice download(JeeslIotMatrixDisplayRestInterface remote)
	{
		remoteDevice = remote.deviceJson(deviceCode);
		remoteDevice.setData(JsonMatrixDeviceFactory.transform(remoteDevice));
		remoteCells = JsonMatrixDeviceFactory.toCells(remoteDevice);
		remoteDevice.setData(JsonMatrixDeviceFactory.toData(remoteCells));
		return remoteDevice;
	}
	
	public void build(MatrixProcessorCursor copy, MatrixProcessorCursor paste)
	{
		for(int row=0; row<copy.getRowSpan(); row++)
		{
			for(int col=0; col<copy.getColSpan(); col++)
			{	
				String c = remoteCells[copy.getRow()+row-1][copy.getColumn()+col-1];
				jfDevice.offset(paste,row,col, c);
			}
		}
		paste.move(copy.getRowSpan()-1, copy.getColSpan()-1);
	}
}