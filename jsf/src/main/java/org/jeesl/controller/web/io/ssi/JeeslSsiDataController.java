package org.jeesl.controller.web.io.ssi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.ssi.JeeslIoSsiDataCallback;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.util.query.io.EjbIoSsiQuery;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.JsonUtil;

public class JeeslSsiDataController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										CTX extends JeeslIoSsiContext<?,?>,
										DATA extends JeeslIoSsiData<CTX,STATUS,?>,
										STATUS extends JeeslIoSsiLink<L,D,STATUS,?>,
										JSON extends Object>
//									extends AbstractJeeslWebController<L,D,LOC>
									extends LazyDataModel<DATA>
									implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiDataController.class);
	
	private JeeslIoSsiFacade<L,D,?,?,CTX,?,DATA,STATUS,?,?,?,?> fSsi;
	private final JeeslIoSsiDataCallback callback;
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	
	final Map<DATA,JSON> mapJson; public Map<DATA,JSON> getMapJson() {return mapJson;}

	private final List<DATA> datas; public List<DATA> getDatas() {return datas;}
	private List<DATA> selection; public List<DATA> getSelection() {return selection;} public void setSelection(List<DATA> selection) {this.selection = selection;}
	
	private CTX context; public CTX getContext() {return context;} public void setContext(CTX context) {this.context = context;}
	private Class<JSON> cJson; public void setClassJson(Class<JSON> cJson) {this.cJson = cJson;}

	private String json; public String getJson() {return json;} public void setJson(String json) {this.json = json;}

	public JeeslSsiDataController(final JeeslIoSsiDataCallback callback, IoSsiDataFactoryBuilder<L,D,?,?,?,DATA,STATUS,?,?,?> fbSsiData)
	{
//		super(fbSsiData.getClassL(),fbSsiData.getClassD());
		this.callback=callback;
		
		sbhStatus = new SbMultiHandler<>(fbSsiData.getClassLink(),this);
		
		mapJson = new HashMap<>();
		datas = new ArrayList<>();
	}

	public void postConstructSsiData(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
										JeeslIoSsiFacade<L,D,?,?,CTX,?,DATA,STATUS,?,?,?,?> fSsi)
	{
		this.fSsi=fSsi;
//		super.postConstructWebController(lp,bMessage);
	}
	
	public void addDatas(List<DATA> list)
	{
		datas.clear();
		datas.addAll(list);
	}

	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<DATA> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filters)
	{
		datas.clear(); mapJson.clear();
		EjbIoSsiQuery<CTX,STATUS> query = new EjbIoSsiQuery<>();
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		if(Objects.nonNull(context)) {query.add(context);}
		

		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
		super.setRowCount(fSsi.cSsiData(query).intValue());
		datas.addAll(fSsi.fSsiData(query));
		logger.info("PageSize:"+pageSize+" page:"+first+" results:"+datas.size() +" rows:" +getRowCount()+" time:"+ptt.toTotalTime());

		if(Objects.nonNull(cJson))
		{
			for(DATA d : datas)
			{
				try {mapJson.put(d,JsonUtil.read(cJson,d.getJson()));}
				catch (IOException e) {e.printStackTrace();}
			}
		}
		
		return datas;
	}

	@Override public DATA getRowData(String rowKey)
	{
		Long id = new Long(rowKey);
//		for (MeisProgram h : data)
//		{
//			if(h.getId()==id){return h;}
//		}
//		try {return fProgram.find(MeisProgram.class,id);}
//		catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
		return null;
	}
}