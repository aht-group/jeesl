package org.jeesl.jsf.handler.op;

import java.util.Objects;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.op.OpManySelectionBean;
import org.jeesl.interfaces.bean.op.OpSingleSelectionBean;
import org.jeesl.interfaces.controller.handler.op.OpSelectionHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpSingleSelectionHandler <T extends EjbWithId> implements OpSelectionHandler
{
	final static Logger logger = LoggerFactory.getLogger(OpSingleSelectionHandler.class);

	public static final long serialVersionUID=1;

	private OpSingleSelectionBean<T> bSingle;
	private OpManySelectionBean bMany;
	
	private LazyDataModel<T> lazyModel; public LazyDataModel<T> getLazyModel() {return lazyModel;}
	private T item; public T getItem() {return item;} public void setItem(T item) {this.item = item;}

	public static <T extends EjbWithId> OpSingleSelectionHandler<T> instance(OpSingleSelectionBean<T> bean) {return new OpSingleSelectionHandler<>(bean);}
	public static <T extends EjbWithId> OpSingleSelectionHandler<T> instance(OpManySelectionBean bean) {return new OpSingleSelectionHandler<>(bean);}
	
	private OpSingleSelectionHandler(OpSingleSelectionBean<T> bean)
	{
		this.bSingle = bean;
	}
	private OpSingleSelectionHandler(OpManySelectionBean bean)
	{
		this.bMany = bean;
	}
	
	public OpSingleSelectionHandler<T> lazyModel(LazyDataModel<T> lazyModel) {this.lazyModel=lazyModel; return this;}

	public void initSelect()
	{
		item = null;
	}

	public void selectItem() throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException
	{
		if(Objects.nonNull(bSingle)) {bSingle.callbackOpSelection(this,item);}
		if(Objects.nonNull(bMany)) {bMany.callbackOpSelection(null, item);}
		item = null;
	}
}