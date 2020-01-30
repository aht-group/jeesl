package net.sf.ahtutils.jsf.handler.crud;

import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.crud.EjbCrud;
import net.sf.ahtutils.interfaces.web.crud.CrudHandlerBean;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

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
		logger.info(AbstractLogMessage.addEntity(cT));
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