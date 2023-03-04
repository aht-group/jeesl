package org.jeesl.web.mbean.prototype.io.ssi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.processor.SsiMappingProcessor;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiHost;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.system.job.JeeslJobStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;
import net.sf.exlp.util.io.JsonUtil;

public abstract class AbstractSsiBean <L extends JeeslLang, D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK,JOB>,
										LINK extends JeeslIoSsiLink<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										HOST extends JeeslIoSsiHost<L,D,SYSTEM>,
										JSON extends Object
										>
						implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiBean.class);
	
	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi;
	
	protected JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,HOST> fSsi;
	
	protected final SbMultiHandler<LINK> sbhLink; public SbMultiHandler<LINK> getSbhLink() {return sbhLink;}
	protected final JsonTuple1Handler<LINK> thLink; public JsonTuple1Handler<LINK> getThLink() {return thLink;}
	protected SsiMappingProcessor<MAPPING,DATA,JSON> ssiProcessor;
	
	protected final Map<DATA,JSON> mapData; public Map<DATA,JSON> getMapData() {return mapData;}
	
	protected final List<DATA> datas; public List<DATA> getDatas() {return datas;}
	protected List<DATA> selection; public List<DATA> getSelection() {return selection;} public void setSelection(List<DATA> selection) {this.selection = selection;}

	private final Class<JSON> cJson;
	protected Long refA;
	
	public AbstractSsiBean(final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB> fbSsi, final Class<JSON> cJson)
	{
		this.fbSsi=fbSsi;
		this.cJson=cJson;
		sbhLink = new SbMultiHandler<LINK>(fbSsi.getClassLink(),this);
		thLink = new JsonTuple1Handler<LINK>(fbSsi.getClassLink());
		mapData = new HashMap<>();
		datas = new ArrayList<>();
	}

	public void postConstructSsi(JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,HOST> fSsi)
	{
		this.fSsi=fSsi;
		sbhLink.setList(fSsi.allOrderedPositionVisible(fbSsi.getClassLink()));
		sbhLink.preSelect(JeeslIoSsiLink.Code.possible,JeeslIoSsiLink.Code.unlinked);
	}
	
	@Override public void toggled(Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(fbSsi.getClassLink().isAssignableFrom(c)){reloadData();}
	}
	
	protected abstract boolean constraintEmptySelectionFailed();
	
	public void selectItems()
	{
		logger.info(AbstractLogMessage.selectEntities(fbSsi.getClassData(),selection));
	}
	
	public void evaluateSelection()
	{
		boolean hasConstraintsFailed = constraintEmptySelectionFailed();
		logger.info("evaluateSelection: "+selection.size()+" hasConstraintsFailed:"+hasConstraintsFailed);
		if(hasConstraintsFailed) {return;}
		ssiProcessor.evaluateData(selection);
		if(ObjectUtils.isNotEmpty(selection)) {selection.clear();}
		reloadData();
	}
	
	public void ignoreSelection()
	{
		if(constraintEmptySelectionFailed()) {return;}
		ssiProcessor.ignoreData(selection);
		if(ObjectUtils.isNotEmpty(selection)) {selection.clear();}
		reloadData();
	}
	
	public void importSelection()
	{
		if(constraintEmptySelectionFailed()) {return;}
		logger.info("Linking selection");
		ssiProcessor.linkData(selection);
		if(ObjectUtils.isNotEmpty(selection)) {selection.clear();}
		reloadData();
	}
	
//	@Override
	protected void reloadData()
	{
		datas.clear();
		thLink.clear();
		
		if(Objects.nonNull(refA))
		{
			ATTRIBUTE a = fbSsi.ejbAttribute().build(ssiProcessor.getMapping()); a.setId(refA);
			datas.addAll(fSsi.fIoSsiData(ssiProcessor.getMapping(),sbhLink.getSelected(),a));
			thLink.init(fSsi.tpIoSsiLinkForMapping(ssiProcessor.getMapping(),a));
		}
		else
		{
			datas.addAll(fSsi.fIoSsiData(ssiProcessor.getMapping(),sbhLink.getSelected()));
			thLink.init(fSsi.tpIoSsiLinkForMapping(ssiProcessor.getMapping()));
		}
		
//		logger.info("List: "+AbstractLogMessage.reloaded(cJson, datas));
		for(DATA d : datas)
		{	
			try {mapData.put(d,JsonUtil.read(cJson,d.getJson()));}
			catch (IOException e) {e.printStackTrace();}
		}
		
//		logger.info("Map "+AbstractLogMessage.reloaded(IoSsiData.class, mapData.keySet()));	
	}
}