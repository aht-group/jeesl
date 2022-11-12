package org.jeesl.controller.handler.lazy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.util.JeeslLazyListHandler;
import org.jeesl.model.json.system.translation.JsonTranslation;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XpathLazyModel <T extends EjbWithId> extends LazyDataModel<T>
{
	final static Logger logger = LoggerFactory.getLogger(XpathLazyModel.class);

	private static final long serialVersionUID = 1L;

	private final JeeslLazyListHandler<T> llh;

	private final List<T> source;
	private final List<T> data;
	private final List<T> tmp;

	private final Map<String,JsonTranslation> mapFilter;
	private boolean debugOnInfo;

	public XpathLazyModel()
	{
        llh = new JeeslLazyListHandler<T>();
        data = new ArrayList<T>();
		source = new ArrayList<T>();
		tmp = new ArrayList<T>();
		mapFilter = new HashMap<String,JsonTranslation>();

		debugOnInfo = false;
	}
	
	@Override public T getRowData(String rowKey){return llh.getRowData(source,rowKey);}
    @Override public Object getRowKey(T account) {return llh.getRowKey(account);}

	public void updateFiler(List<JsonTranslation> columns)
	{
		mapFilter.clear();
		for(int i=0;i<columns.size();i++)
		{
			String key = "column"+i;
			JsonTranslation json = columns.get(i);
			mapFilter.put(key,json);
			if(debugOnInfo) {logger.info("Adding "+i+" "+key+" "+json.getXpath());}
		}
		if(debugOnInfo) {logger.info("Filters updated "+mapFilter.size());}
	}

	public void clear() {source.clear();}
	public void addAll(List<T> items) {source.addAll(items);}
	public void add(T item){source.add(item);}

	@Override public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filters)
	{
		tmp.clear();
		for(T item : source)
		{
			if(this.matches(item,filters)) {tmp.add(item);}
		}

		//sort
		if(sortField != null)
		{

		}

		//rowCount
		this.setRowCount(tmp.size());

		llh.paginator(tmp,first,pageSize,data);
		return data;
	}

    private boolean matches(T item, Map<String,FilterMeta> filters)
	{
    	if(debugOnInfo) {logger.info("Matching ... "+item.toString());}
		boolean match = true;
		if(ObjectUtils.isNotEmpty(filters))
		{
			JXPathContext ctx = JXPathContext.newContext(item);
			for(String key : filters.keySet())
			{
				if(debugOnInfo) {logger.info("Filtering "+key);}
				if(mapFilter.containsKey(key))
				{
					Object value = ctx.getValue(mapFilter.get(key).getXpath());
					if(value==null || value.toString().trim().length()==0)
					{
						match = false;
					}
					else if(Objects.nonNull(filters.get(key).getFilterValue()))
					{
						match = StringUtils.containsIgnoreCase(value.toString(), filters.get(key).getFilterValue().toString());
					}
				}
				else
				{
					logger.warn("The filter "+key+" is not defined");
					logger.warn("We have the following:");
					for(String s : mapFilter.keySet())
					{
						logger.info("\t"+s+" "+mapFilter.get(s).getXpath());
					}
				}
				if(!match){break;}
			}
		}
		return match;
	}
}