package org.jeesl.jsf.handler.sb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.sb.SbSearchBean;
import org.jeesl.interfaces.controller.handler.JeeslAutoCompleteHandler;
import org.jeesl.interfaces.controller.handler.JeeslHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbSearchHandler <T extends EjbWithId> implements JeeslHandler
{
	final static Logger logger = LoggerFactory.getLogger(SbSearchHandler.class);
	private static final long serialVersionUID = 1L;

	private final SbSearchBean bean;
	private JeeslAutoCompleteHandler<T> acHandler; public SbSearchHandler<T> acHandler(JeeslAutoCompleteHandler<T> acHandler) {this.acHandler=acHandler; return this;}
	
	private final Class<T> c;
	
	private final List<T> list; public List<T> getList() {return list;} public void setList(List<T> list) {this.list.clear();this.list.addAll(list);}

	private String searchText; public String getSearchText() {return this.searchText;} public void setSearchText(String searchText) {this.searchText = searchText;}
	private T selection; public T getSelection() {return selection;} public void setSelection(T selected) {this.selection = selected;}
	
	public static <T extends EjbWithId> SbSearchHandler<T> instance(Class<T> c, SbSearchBean bean) {return new SbSearchHandler<>(c,bean);}
	private SbSearchHandler(Class<T> c, SbSearchBean bean)
	{
		this.c=c;
		this.bean=bean;
		
		list = new ArrayList<>();
		
		logger.trace("creating handler: " + SbSearchHandler.class.getSimpleName());
		this.searchText ="";
	}
	
	private void reset()
	{
		list.clear();
	}

	public void handleKeyEvent()
	{
		logger.trace("handle search event: " + SbSearchHandler.class.getSimpleName());
		this.reset();
		if(Objects.nonNull(searchText))
		{
			if(Objects.nonNull(acHandler)) {list.addAll(acHandler.autoCompleteListByQuery(c,searchText));}
			else {logger.warn(JeeslAutoCompleteHandler.class.getSimpleName()+" is NULL");}
		}
	}
	
	public void selectSbSearch(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException
	{
		logger.info("selectSbSearch");
		bean.selectSbSearch(this,item);
	}
	
	public void applySbSearch()
	{
		logger.info("applySbSearch");
		bean.applySbSearch(this);
	}

	public void cancelEvent()
	{
		logger.trace("cancel search event: " + SbSearchHandler.class.getSimpleName());
//		this.clear();
		this.searchText ="";
//		this.update(new ArrayList<T>());
	}

	@Override public String toString()
	{
		return "handler name: " + this.getClass().getSimpleName();
	}
}