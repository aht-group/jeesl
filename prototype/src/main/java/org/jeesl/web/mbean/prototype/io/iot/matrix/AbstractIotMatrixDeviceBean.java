package org.jeesl.web.mbean.prototype.io.iot.matrix;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.handler.system.io.fr.JeeslFileTypeHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.iot.IotMatrixFactoryBuilder;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.fr.JeeslFileType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractIotMatrixDeviceBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>
									>
						extends AbstractAdminBean<L,D,LOC>
						implements SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractIotMatrixDeviceBean.class);
	
	private JeeslFacade facade;
	private final IotMatrixFactoryBuilder<L,D> fbMatrix;


	protected AbstractIotMatrixDeviceBean(IotMatrixFactoryBuilder<L,D> fbMatrix)
	{
		super(fbMatrix.getClassL(),fbMatrix.getClassD());
		this.fbMatrix=fbMatrix;
	}
	
	protected void postConstructMatrixDevice(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslFacade facade)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.facade=facade;
		
	}
	
	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		
	}
	
	
//	public void reorderStorages() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fFr, storages);}
}