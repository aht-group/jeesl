package org.jeesl.controller.web.io.ssi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.JsonUtil;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.web.io.ssi.JeeslIoSsiDataCallback;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.system.job.core.JeeslJobStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.util.query.cq.CqLong;
import org.jeesl.util.query.ejb.io.EjbIoSsiQuery;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSsiDataController <SYSTEM extends JeeslIoSsiSystem<?,?>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										CTX extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										DATA extends JeeslIoSsiData<CTX,STATUS,?,JOB>,
										STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
										ERROR extends JeeslIoSsiError<?,?,CTX,?>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
										JOB extends JeeslJobStatus<?,?,JOB,?>,
										JSON extends Object>
									extends LazyDataModel<DATA>
									implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiDataController.class);
	
	private JeeslIoSsiFacade<SYSTEM,CRED,CTX,?,DATA,STATUS,ERROR,ENTITY,?,JOB,?> fSsi;
	private final IoSsiDataFactoryBuilder<?,?,?,?,?,DATA,STATUS,ERROR,?,?,JOB> fbSsiData;
	private final JeeslIoSsiDataCallback callback;
	
	private final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	
	private JsonTuple1Handler<STATUS> thStatus; public JsonTuple1Handler<STATUS> getThStatus() {return thStatus;}
	private JsonTuple2Handler<STATUS,JOB> thJob; public JsonTuple2Handler<STATUS,JOB> getThJob() {return thJob;}
	private JsonTuple2Handler<STATUS,ERROR> thError; public JsonTuple2Handler<STATUS,ERROR> getThError() {return thError;}
	
	final Map<DATA,JSON> mapJson; public Map<DATA,JSON> getMapJson() {return mapJson;}

	private final List<DATA> datas; public List<DATA> getDatas() {return datas;}
	private List<DATA> selection; public List<DATA> getSelection() {return selection;} public void setSelection(List<DATA> selection) {this.selection = selection;}
	
	private CTX context; public CTX getContext() {return context;} public void setContext(CTX context) {this.context = context;}
	private Class<JSON> cJson; public JeeslSsiDataController<SYSTEM,CRED,CTX,DATA,STATUS,ERROR,ENTITY,JOB,JSON> json(Class<JSON> cJson) {this.cJson = cJson; return this;}

	private String json; public String getJson() {return json;} public void setJson(String json) {this.json = json;}

	private Long refA; public <A extends EjbWithId> void setRefA(A a) {this.refA = a.getId();}
	private Long refB; public <B extends EjbWithId> void setRefB(B b) {this.refB = b.getId();}
	private Long refC; public <C extends EjbWithId> void setRefC(C c) {this.refC = c.getId();}

	public static <SYSTEM extends JeeslIoSsiSystem<?,?>,
					CRED extends JeeslIoSsiCredential<SYSTEM>,
					CTX extends JeeslIoSsiContext<SYSTEM,ENTITY>,
					DATA extends JeeslIoSsiData<CTX,STATUS,?,JOB>,
					STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
					ERROR extends JeeslIoSsiError<?,?,CTX,?>,
					ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
					JOB extends JeeslJobStatus<?,?,JOB,?>,
					JSON extends Object>
				JeeslSsiDataController<SYSTEM,CRED,CTX,DATA,STATUS,ERROR,ENTITY,JOB,JSON> instance(final JeeslIoSsiDataCallback callback, IoSsiDataFactoryBuilder<?,?,?,?,?,DATA,STATUS,ERROR,?,?,JOB> fbSsi)
	{
		return new JeeslSsiDataController<>(callback,fbSsi);
	}
	public JeeslSsiDataController(final JeeslIoSsiDataCallback callback, IoSsiDataFactoryBuilder<?,?,?,?,?,DATA,STATUS,ERROR,?,?,JOB> fbSsiData)
	{
		this.callback=callback;
		this.fbSsiData=fbSsiData;
		
		sbhStatus = new SbMultiHandler<>(fbSsiData.getClassStatus(),this);
		
		thStatus = JsonTuple1Handler.instance(fbSsiData.getClassStatus());
		thJob = JsonTuple2Handler.instance(fbSsiData.getClassStatus(),fbSsiData.getClassJob());
		thError = JsonTuple2Handler.instance(fbSsiData.getClassStatus(),fbSsiData.getClassError());
		
		mapJson = new HashMap<>();
		datas = new ArrayList<>();
	}

	public void postConstructSsiData(JeeslIoSsiFacade<SYSTEM,CRED,CTX,?,DATA,STATUS,ERROR,ENTITY,?,JOB,?> fSsi)
	{
		this.fSsi=fSsi;
		logger.trace(callback.getClass().getName());
	}
	
	public void reloadStatistics()
	{
		thStatus.clear();
		
		EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> query = new EjbIoSsiQuery<>();
		if(Objects.nonNull(refA)) {query.add(CqLong.isValue(refA,CqLong.path(JeeslIoSsiData.Attributes.refA)));}
		if(Objects.nonNull(refB)) {query.add(CqLong.isValue(refB,CqLong.path(JeeslIoSsiData.Attributes.refB)));}
		if(Objects.nonNull(refC)) {query.add(CqLong.isValue(refC,CqLong.path(JeeslIoSsiData.Attributes.refC)));}
		if(Objects.nonNull(context)) {query.add(context);}
		
		thStatus.init(fSsi.tpIoSsiDataByStatus(query));
		thJob.load(fSsi.tpcIoSsiStatusJobForContext(context,EjbIdFactory.toIdEjb(refA),EjbIdFactory.toIdEjb(refB)));
		thJob.initListB(fSsi);
		
		thError.load(fSsi.tpIoSsiDataByStatusError(query)).initListB(fSsi);
	}
	
	public void addDatas(List<DATA> list)
	{
		datas.clear();
		datas.addAll(list);
	}

	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		callback.ssiDataFilterChanged();
		this.reloadStatistics();
	}
	
	@Override
	public List<DATA> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filters)
	{
		datas.clear(); mapJson.clear();
		EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> query = new EjbIoSsiQuery<>();
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		if(Objects.nonNull(refA)) {query.add(CqLong.isValue(refA,CqLong.path(JeeslIoSsiData.Attributes.refA)));}
		if(Objects.nonNull(refB)) {query.add(CqLong.isValue(refB,CqLong.path(JeeslIoSsiData.Attributes.refB)));}
		if(Objects.nonNull(refC)) {query.add(CqLong.isValue(refC,CqLong.path(JeeslIoSsiData.Attributes.refC)));}
		if(Objects.nonNull(context)) {query.add(context);}
		if(sbhStatus.hasSelected()) {query.addStatus(sbhStatus.getSelected());}
		query.debug(true);
		
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
		
		callback.ssiDataLoaded();
		return datas;
	}

	@Override public DATA getRowData(String rowKey)
	{
		Long id = Long.valueOf(rowKey);
		for (DATA d : datas) {if(d.getId()==id){return d;}}
		try {return fSsi.find(fbSsiData.getClassData(),id);} catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
		return null;
	}
	
	public void selectItems()
	{
		this.setJson(null);
		if(Objects.nonNull(cJson) && ObjectUtils.isNotEmpty(selection))
		{
			if(selection.size()==1)
			{
				DATA data = selection.get(0);
				try
				{
					JSON json = JsonUtil.read(cJson,data.getJson());
					this.setJson(JsonUtil.toPrettyString(json));
				}
				catch (IOException e) {e.printStackTrace();}
			}
			else
			{
				logger.info("Selection size is "+this.getSelection().size());
			}
		}
		else {logger.warn("Selection is NULL");}
	}
}