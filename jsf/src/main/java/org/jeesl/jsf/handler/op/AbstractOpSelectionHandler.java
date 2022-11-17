package org.jeesl.jsf.handler.op;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.handler.OpEntitySelection;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.bean.op.OpEntityBean;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOpSelectionHandler <T extends EjbWithId> implements OpEntitySelection<T>
{
	final static Logger logger = LoggerFactory.getLogger(OpEntitySelection.class);
	public static final long serialVersionUID=1;
	
    protected T op; @Override public T getOp() {return op;} @Override public void setOp(T opEntity) {this.op = opEntity;}
    protected T tb; @Override public T getTb() {return tb;} @Override public void setTb(T tbEntity) {this.tb = tbEntity;}

    protected List<T> opList; @Override public List<T> getOpList() {return opList;} @Override public void setOpList(List<T> opList) {this.opList.clear(); this.opList.addAll(opList);}
    protected final List<T> tbList; @Override public List<T> getTbList() {return tbList;} @Override public void setTbList(List<T> list) {tbList.clear(); tbList.addAll(list);}
    
    protected OpEntityBean bean;

    protected boolean showName; public boolean isShowName() {return showName;}
	protected boolean showLang; public boolean isShowLang() {return showLang;}
    
    public AbstractOpSelectionHandler(OpEntityBean bean, List<T> opList)
    {
    	this.bean=bean;
    	this.opList=opList;
    	showName=false;
    	showLang=false;
    	if(opList==null){opList = new ArrayList<T>();}
    	tbList = new ArrayList<T>(); 
    }
    
    public void reset() {reset(true,true,true);}
    protected void reset(boolean rTbList, boolean rTb, boolean rOp)
    {
    	if(rTbList) {tbList.clear();}
        if(rTb){tb=null;}
        if(rOp){op=null;}
    }
    
    @Override public void clearTable()
    {
    	tbList.clear();
    	tb = null;
    }
    
	@Override public void selectTb() {}
	public void prepareAdd() {}
	
	@Override public void addEntity(T item) throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException
    {
    	op = item;
    	addEntity();
	}
	
	@SuppressWarnings({"unchecked" })
	public <C extends EjbWithCode, E extends Enum<E>> void addEntity(JeeslFacade fUtils, Class<C> c, E code)
    {
		try
		{
			addEntity((T)fUtils.fByCode(c, code));
		}
		catch (JeeslLockingException e) {e.printStackTrace();}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
	}

    @Override public void addEntity() throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException
    {
        if(op!=null && !tbList.contains(op))
        {
        	tbList.add(op);
        	bean.addOpEntity(op);
        }
        reset(false,true,true);
    }

    @Override public void removeEntity() throws JeeslLockingException, JeeslConstraintViolationException, JeeslNotFoundException
    {
        if(tbList.contains(tb))
        {
        	tbList.remove(tb);
        	bean.rmOpEntity(tb);
        }
        reset(false,true,true);
    }
    
    public void prepareSelection() {}
}