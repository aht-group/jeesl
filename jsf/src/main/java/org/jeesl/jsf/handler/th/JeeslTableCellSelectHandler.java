package org.jeesl.jsf.handler.th;

import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTableCellSelectHandler <A extends EjbWithId, B extends EjbWithId> implements Serializable
{
	final static Logger logger = LoggerFactory.getLogger(JeeslTableCellSelectHandler.class);
	private static final long serialVersionUID = 1L;

	private JeeslFacade facade;
	
	private final Class<A> cA;
	private final Class<B> cB;
	
	private A row; public A getRow() {return row;}
	private B column; public B getColumn() {return column;}
	
	public static <A extends EjbWithId, B extends EjbWithId> JeeslTableCellSelectHandler<A,B> instance(Class<A> cA, Class<B> cB) {return new JeeslTableCellSelectHandler<>(cA,cB);}
	public JeeslTableCellSelectHandler(Class<A> cA, Class<B> cB)
	{
		this.cA=cA;
		this.cB=cB;
	}

	public JeeslTableCellSelectHandler<A,B> facade(JeeslFacade facade)
	{
		this.facade=facade;
		return this;
	}
	
	public void selectionTriggered()
	{
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String rowCode = params.get("rowCode");
		String columnCode = params.get("columnCode");
		String tableId = params.get("tableId");
		
		StringBuilder sb = new StringBuilder();
		sb.append("selectionTriggered ");
		sb.append(" tableId:").append(tableId);
		sb.append(" rowCode:").append(rowCode);
		sb.append(" columnCode:").append(columnCode);
		logger.info(sb.toString());
		
		try
		{
			row = facade.find(cA,Long.valueOf(rowCode));
			column = facade.find(cB,Long.valueOf(columnCode));
		}
		catch (NumberFormatException | JeeslNotFoundException e) {e.printStackTrace();}
	}
}