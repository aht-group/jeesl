package org.jeesl.jsf.handler;

import java.util.List;

import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.bean.CrudHandlerBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.marker.crud.EjbCrud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsCrudHandlerSimple <T extends EjbCrud>
{	
	final static Logger logger = LoggerFactory.getLogger(UtilsCrudHandlerSimple.class);
	
	private JeeslFacade fUtils;
	private CrudHandlerBean<T> bean;
	
	private final Class<T> cT;
	
	private List<T> list; public List<T> getList() {return list;}
	
	public UtilsCrudHandlerSimple(CrudHandlerBean<T> bean, JeeslFacade fUtils, Class<T> cT)
	{
		this(fUtils,cT);
		this.bean=bean;
	}
	
	private UtilsCrudHandlerSimple(JeeslFacade fUtils, Class<T> cT)
	{
		this.fUtils=fUtils;
		this.cT=cT;
	}

	public void reloadList()
	{
		list = fUtils.all(cT);
	}
	
	public void add()
	{
		logger.info(AbstractLogMessage.createEntity(cT));
		if(bean!=null){entity = bean.crudBuild(cT);}
		else {logger.warn("No Bean available!!");}
	}
	
	public void select()
	{
		logger.info(AbstractLogMessage.selectEntity(entity));
		if(bean!=null){bean.crudNotifySelect(entity);}
		else {logger.warn("No Bean available!!");}
	}
	
	public void save() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(bean!=null){entity = bean.crudPreSave(entity);}
		entity = fUtils.save(entity);
		reloadList();
	}
	
	public void rm()
	{
		try
		{
			fUtils.rm(entity);
			entity=null;
			reloadList();
		}
		catch (JeeslConstraintViolationException e)
		{
			bean.crudRmConstraintViolation(cT);
		}
	}
	
	private T entity;
	public T getEntity() {return entity;}
	public void setEntity(T entity) {this.entity = entity;}
}