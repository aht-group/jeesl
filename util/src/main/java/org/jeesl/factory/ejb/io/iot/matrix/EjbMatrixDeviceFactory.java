package org.jeesl.factory.ejb.io.iot.matrix;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.iot.matrix.JeeslIotMatrixDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMatrixDeviceFactory <DEVICE extends JeeslIotMatrixDevice<?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbMatrixDeviceFactory.class);
	
	final Class<DEVICE> cDevice;
	
    public EjbMatrixDeviceFactory(final Class<DEVICE> cDevice)
    {
        this.cDevice = cDevice;
    } 
        
	public DEVICE build()
	{
		DEVICE ejb = null;
        try
        {
			ejb=cDevice.newInstance();
		}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        
        return ejb;
    }
	
	public void converter(JeeslFacade facade, DEVICE ejb)
	{
		
    }
}