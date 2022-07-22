package org.jeesl.web.mbean.prototype.io.iot.matrix;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.iot.IotMatrixFactoryBuilder;
import org.jeesl.factory.ejb.io.iot.matrix.EjbMatrixDeviceFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.iot.matrix.JeeslIotMatrixDevice;
import org.jeesl.interfaces.model.io.iot.matrix.JeeslIotMatrixLayout;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractIotMatrixDeviceBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											DEVICE extends JeeslIotMatrixDevice<L,D,LAYOUT>,
											LAYOUT extends JeeslIotMatrixLayout<L,D,LAYOUT,?>
										>
						extends AbstractAdminBean<L,D,LOC>
						implements SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractIotMatrixDeviceBean.class);
	
	private JeeslFacade facade;
	private final IotMatrixFactoryBuilder<L,D,DEVICE,LAYOUT> fbMatrix;
	private final EjbMatrixDeviceFactory<DEVICE> efDevice;
	
	private final List<DEVICE> devices; public List<DEVICE> getDevices() {return devices;}

	private DEVICE device; public DEVICE getDevice() {return device;} public void setDevice(DEVICE device) {this.device = device;}

	protected AbstractIotMatrixDeviceBean(IotMatrixFactoryBuilder<L,D,DEVICE,LAYOUT> fbMatrix)
	{
		super(fbMatrix.getClassL(),fbMatrix.getClassD());
		this.fbMatrix=fbMatrix;
		
		efDevice = fbMatrix.ejbDevice();
		
		devices = new ArrayList<>();
	}
	
	protected void postConstructMatrixDevice(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslFacade facade)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.facade=facade;
		reloadDevices();
	}
	
	private void reloadDevices()
	{
		devices.clear();
		devices.addAll(facade.all(fbMatrix.getClassDevice()));
	}
	
	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		
	}
	
	public void addDevice()
	{
		logger.info(AbstractLogMessage.addEntity(fbMatrix.getClassDevice()));
		device = efDevice.build();
	}
	
	public void selectDevice()
	{
		logger.info(AbstractLogMessage.selectEntity(device));
	}
	
	public void saveDevice() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(device));
		efDevice.converter(facade,device);
		device = facade.save(device);
		this.reloadDevices();
	}
	
//	public void reorderStorages() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fFr, storages);}
}