package org.jeesl.controller.web.io.ssi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.exlp.util.io.JsonUtil;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.web.io.ssi.JeeslIoSsiDataCallback;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.EjbIoSsiQuery;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSsiDataController <CTX extends JeeslIoSsiContext<?,?>,
										DATA extends JeeslIoSsiData<CTX,STATUS,?,JOB>,
										STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
										JOB extends JeeslJobStatus<?,?,JOB,?>,
										JSON extends Object>
//									extends AbstractJeeslWebController<L,D,LOC>
									extends LazyDataModel<DATA>
									implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiDataController.class);
	
	private JeeslIoSsiFacade<?,?,CTX,?,DATA,STATUS,?,?,?,JOB,?> fSsi;
	private final JeeslIoSsiDataCallback callback;
	
	protected final SbMultiHandler<STATUS> sbhStatus; public SbMultiHandler<STATUS> getSbhStatus() {return sbhStatus;}
	
	protected JsonTuple1Handler<STATUS> thStatus; public JsonTuple1Handler<STATUS> getThStatus() {return thStatus;}
	protected JsonTuple2Handler<STATUS,JOB> thJob; public JsonTuple2Handler<STATUS,JOB> getThJob() {return thJob;}
	
	final Map<DATA,JSON> mapJson; public Map<DATA,JSON> getMapJson() {return mapJson;}

	private final List<DATA> datas; public List<DATA> getDatas() {return datas;}
	private List<DATA> selection; public List<DATA> getSelection() {return selection;} public void setSelection(List<DATA> selection) {this.selection = selection;}
	
	private CTX context; public CTX getContext() {return context;} public void setContext(CTX context) {this.context = context;}
	private Class<JSON> cJson; public void setClassJson(Class<JSON> cJson) {this.cJson = cJson;}

	private String json; public String getJson() {return json;} public void setJson(String json) {this.json = json;}

	private Long refA; public <A extends EjbWithId> void setRefA(A a) {this.refA = a.getId();}
	private Long refB; public <B extends EjbWithId> void setRefB(B b) {this.refB = b.getId();}

	public JeeslSsiDataController(final JeeslIoSsiDataCallback callback,
						IoSsiDataFactoryBuilder<?,?,?,?,?,DATA,STATUS,?,?,?,JOB> fbSsiData)
	{
		this.callback=callback;
		
		sbhStatus = new SbMultiHandler<>(fbSsiData.getClassStatus(),this);
		
		thStatus = JsonTuple1Handler.instance(fbSsiData.getClassStatus());
		thJob = JsonTuple2Handler.instance(fbSsiData.getClassStatus(),fbSsiData.getClassJob());
		
		mapJson = new HashMap<>();
		datas = new ArrayList<>();
	}

	public void postConstructSsiData(JeeslIoSsiFacade<?,?,CTX,?,DATA,STATUS,?,?,?,JOB,?> fSsi)
	{
		this.fSsi=fSsi;
		logger.trace(callback.getClass().getName());
	}
	
	public void reloadStatistics()
	{
		thStatus.clear();
		JsonTuples1<STATUS> tStatus = fSsi.tpcIoSsiStatusForContext(context,EjbIdFactory.toIdEjb(refA),EjbIdFactory.toIdEjb(refB));
		JsonUtil.info(tStatus);
		thStatus.init(tStatus);
		thStatus.initListA(fSsi);
		
		JsonTuples2<STATUS,JOB> tJob = fSsi.tpcIoSsiStatusJobForContext(context,EjbIdFactory.toIdEjb(refA),EjbIdFactory.toIdEjb(refB));
		JsonUtil.info(tJob);
		thJob.init(tJob);
		thJob.initListB(fSsi);
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
	}
	
	@Override
	public List<DATA> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,FilterMeta> filters)
	{
		datas.clear(); mapJson.clear();
		EjbIoSsiQuery<CTX,STATUS> query = new EjbIoSsiQuery<>();
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		query.id2(refB);
		if(Objects.nonNull(context)) {query.add(context);}
		if(sbhStatus.hasSelected()) {query.addStatus(sbhStatus.getSelected());}
		
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
		Long id = new Long(rowKey);

		return null;
	}
}