package org.jeesl.controller.handler.sb;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.bean.sb.SbSearchBean;
import org.jeesl.interfaces.controller.handler.JeeslAutoCompleteHandler;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SbSearchHandler <T extends EjbWithId> extends SbSingleHandler<T>
{
	final static Logger logger = LoggerFactory.getLogger(SbMultiHandler.class);
	private static final long serialVersionUID = 1L;

	private final SbSearchBean bean;
	private final JeeslAutoCompleteHandler<T> acHandler;
	
	private final List<T> list; public List<T> getList() {return list;} public void setList(List<T> list) {this.list.clear();this.list.addAll(list);}

	private String searchText; public String getSearchText() {return this.searchText;} public void setSearchText(String searchText) {this.searchText = searchText;}
	private T selection; public T getSelection() {return selection;} public void setSelection(T selected) {this.selection = selected;}
	
	public SbSearchHandler(Class<T> c, SbSearchBean bean, JeeslAutoCompleteHandler<T> acHandler)
	{
		super(c,bean);
		this.bean=bean;
		this.acHandler=acHandler;
		
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
		if(searchText != "")
		{
			list.addAll(acHandler.autoCompleteListByQuery(c,searchText));
			bean.triggerSbSearch();
		}
	}
	
	public void selectSbSearch(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info("selectSbSearch");
		bean.selectSbSingle(item);
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