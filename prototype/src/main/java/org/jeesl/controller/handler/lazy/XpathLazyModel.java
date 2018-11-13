package org.jeesl.controller.handler.lazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.jeesl.jsf.util.JeeslLazyListHandler;
import org.jeesl.model.json.system.translation.JsonTranslation;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class XpathLazyModel <T extends EjbWithId> extends LazyDataModel<T>
{
	final static Logger logger = LoggerFactory.getLogger(XpathLazyModel.class);

	private static final long serialVersionUID = 1L;

	private final JeeslLazyListHandler<T> llh;
	
	private final List<T> source;
	private final List<T> data;
	private final List<T> tmp;
	
	private final Map<String,JsonTranslation> mapFilter;
	
	public XpathLazyModel()
	{
        llh = new JeeslLazyListHandler<T>();
        data = new ArrayList<T>();
		source = new ArrayList<T>();
		tmp = new ArrayList<T>();
		mapFilter = new HashMap<String,JsonTranslation>();
	}
	
	public void updateFiler(List<JsonTranslation> columns)
	{
		mapFilter.clear();
		for(int i=0;i<columns.size();i++)
		{
			mapFilter.put("column"+i, columns.get(i));
		}
	}
	
	public void clear()
	{
		source.clear();
	}
	
	public void addAll(List<T> items)
	{
		source.addAll(items);
//		set.addAll(EjbVupBeneficiaryListItemFactory.toEligibilities(items));
	}
	
	public void add(T item)
	{
		source.add(item);
//		set.addAll(EjbVupBeneficiaryListItemFactory.toEligibilities(items));
	}

	@Override public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters)
	{
		tmp.clear();
		for(T item : source)
		{
			if(matches(item,filters)) {tmp.add(item);}	
		}
 
		//sort
		if(sortField != null)
		{
          
		}

		//rowCount
		this.setRowCount(tmp.size());
 
		
		llh.paginator(data,tmp,first,pageSize);
		return data;
	}
	
	@Override public T getRowData(String rowKey){return llh.getRowData(source,rowKey);}
    @Override public Object getRowKey(T account) {return llh.getRowKey(account);}
    
    private boolean matches(T item, Map<String,Object> filters)
	{
    	JXPathContext ctx = JXPathContext.newContext(item);
		boolean match = true;
		if (filters != null)
		{
			for(String key : filters.keySet())
			{
				if(mapFilter.containsKey(key))
				{
					Object value = ctx.getValue(mapFilter.get(key).getXpath());
					if(value==null || value.toString().trim().length()==0)
					{
						match = false;
					}
					else
					{
						match = StringUtils.containsIgnoreCase(value.toString(), filters.get(key).toString());
					}
				}
				if(!match){break;}
			}
		}
		return match;
	}
}