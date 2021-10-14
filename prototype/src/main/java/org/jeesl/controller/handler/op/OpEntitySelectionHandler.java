package org.jeesl.controller.handler.op;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.handler.OpEntitySelection;
import org.jeesl.controller.handler.lazy.XpathLazyModel;
import org.jeesl.interfaces.bean.op.OpEntityBean;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.system.translation.JsonTranslation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpEntitySelectionHandler <T extends EjbWithId> extends AbstractOpSelectionHandler<T> implements OpEntitySelection<T>
{
	final static Logger logger = LoggerFactory.getLogger(OpEntitySelection.class);
	public static final long serialVersionUID=1;
	
	private final List<JsonTranslation> columns; public List<JsonTranslation> getColumns() {return columns;}
	private XpathLazyModel<T> lazy; public XpathLazyModel<T> getLazy() {return lazy;}
	
	public OpEntitySelectionHandler(OpEntityBean bean) {this(bean,new ArrayList<T>());}
    public OpEntitySelectionHandler(OpEntityBean bean, List<T> opEntites)
    {
		super(bean,opEntites);
		columns = new ArrayList<JsonTranslation>();
		
		lazy = new XpathLazyModel<T>();
		lazy.addAll(opEntites);
    }
    
    public OpEntitySelectionHandler(OpEntityBean bean, List<T> opEntites, String xPath)
    {
		super(bean,opEntites);
		showName=true;
		columns = new ArrayList<JsonTranslation>();
    }
    
    public OpEntitySelectionHandler(OpEntityBean bean, List<T> opEntites, Class<?> c)
    {
		super(bean,opEntites);
		showLang=true;
		columns = new ArrayList<JsonTranslation>();
    }
    
    public void addColumn(JsonTranslation translation)
    {
		columns.add(translation);
		lazy.updateFiler(columns);
    }
    
    public void setLazy(List<T> opEntites)
    {
    	lazy.clear();
    	lazy.addAll(opEntites);
    }
    
    public void showOverlay() {}
}