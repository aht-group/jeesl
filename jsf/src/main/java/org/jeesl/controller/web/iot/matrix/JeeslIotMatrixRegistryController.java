package org.jeesl.controller.web.iot.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.iot.IotMatrixFactoryBuilder;
import org.jeesl.factory.ejb.io.iot.matrix.EjbMatrixDeviceFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.iot.JeeslIotMatrixCallback;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixDevice;
import org.jeesl.interfaces.model.iot.matrix.JeeslIotMatrixLayout;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIotMatrixRegistryController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											DEVICE extends JeeslIotMatrixDevice<L,D,LAYOUT>,
											LAYOUT extends JeeslIotMatrixLayout<L,D,LAYOUT,?>
										>
			extends AbstractJeeslWebController<L,D,LOC>
			implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIotMatrixRegistryController.class);
	
	private JeeslFacade facade;
	
	private final JeeslIotMatrixCallback callback;
	
	private final IotMatrixFactoryBuilder<L,D,DEVICE,LAYOUT> fbMatrix;
	private final EjbMatrixDeviceFactory<DEVICE> efDevice;
	
	private final List<DEVICE> devices; public List<DEVICE> getDevices() {return devices;}

	private DEVICE device; public DEVICE getDevice() {return device;} public void setDevice(DEVICE device) {this.device = device;}

	public JeeslIotMatrixRegistryController(final JeeslIotMatrixCallback callback, IotMatrixFactoryBuilder<L,D,DEVICE,LAYOUT> fbMatrix)
	{
		super(fbMatrix.getClassL(),fbMatrix.getClassD());
		this.callback=callback;
		this.fbMatrix=fbMatrix;
		
		efDevice = fbMatrix.ejbDevice();
		
		devices = new ArrayList<>();
	}
	
	public void postConstructMatrixDevice(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslFacade facade)
	{
		super.postConstructWebController(lp,bMessage);
		this.facade=facade;
		reloadDevices();
	}
	
	private void reloadDevices()
	{
		devices.clear();
		devices.addAll(facade.all(fbMatrix.getClassDevice()));
	}
	
	public void addDevice()
	{
		logger.info(AbstractLogMessage.createEntity(fbMatrix.getClassDevice()));
		device = efDevice.build();
	}
	
	public void selectDevice()
	{
		logger.info(AbstractLogMessage.selectEntity(device));
		callback.callbackDeviceSelected();
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