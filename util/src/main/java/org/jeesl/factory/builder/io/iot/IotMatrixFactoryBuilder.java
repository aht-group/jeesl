package org.jeesl.factory.builder.io.iot;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.iot.matrix.EjbMatrixDeviceFactory;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixDevice;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixLayout;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotMatrixFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
										DEVICE extends JeeslIotMatrixDevice<L,D,LAYOUT>,
										LAYOUT extends JeeslIotMatrixLayout<L,D,LAYOUT,?>
										>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IotMatrixFactoryBuilder.class);

	private final Class<DEVICE> cDevice; public Class<DEVICE> getClassDevice() {return cDevice;}

	public IotMatrixFactoryBuilder(final Class<L> cL, final Class<D> cD,
			final Class<DEVICE> cDevice)
	{
		super(cL,cD);
		this.cDevice=cDevice;
	}
	
	public EjbMatrixDeviceFactory<DEVICE> ejbDevice() {return new EjbMatrixDeviceFactory<>(cDevice);}
}