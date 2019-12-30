package org.jeesl.web.mbean.prototype.system.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.sb.SbMultiHandler;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.factory.builder.io.IoSsiFactoryBuilder;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.controller.processor.SsiMappingProcessor;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.ssi.data.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.system.io.ssi.data.JeeslIoSsiData;
import org.jeesl.interfaces.model.system.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.system.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.system.io.ssi.data.JeeslIoSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractSsiBean <L extends UtilsLang, D extends UtilsDescription,
										SYSTEM extends JeeslIoSsiSystem,
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK>,
										LINK extends UtilsStatus<LINK,L,D>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>>
						implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiBean.class);
	
	private final IoSsiFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY> fbSsi;
	
	protected JeeslIoSsiFacade<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY> fSsi;
	
	protected final SbMultiHandler<LINK> sbhLink; public SbMultiHandler<LINK> getSbhLink() {return sbhLink;}
	protected final JsonTuple1Handler<LINK> thLink; public JsonTuple1Handler<LINK> getThLink() {return thLink;}
	protected SsiMappingProcessor<MAPPING,DATA> ssiProcessor;
	
	protected final List<DATA> datas; public List<DATA> getDatas() {return datas;}
	protected List<DATA> selection; public List<DATA> getSelection() {return selection;} public void setSelection(List<DATA> selection) {this.selection = selection;}

	
	public AbstractSsiBean(final IoSsiFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY> fbSsi)
	{
		this.fbSsi=fbSsi;
		sbhLink = new SbMultiHandler<LINK>(fbSsi.getClassLink(),this);
		thLink = new JsonTuple1Handler<LINK>(fbSsi.getClassLink());
		datas = new ArrayList<>();
	}

	public void postConstructSsi(JeeslIoSsiFacade<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY> fSsi)
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
	
	protected abstract void reloadData();
	protected abstract boolean constraintEmptySelectionFailed();
	
	public void selectItems()
	{
		logger.info(AbstractLogMessage.selectEntities(fbSsi.getClassData(),selection));
	}
	
	public void evaluateSelection()
	{
		if(constraintEmptySelectionFailed()) {return;}
		ssiProcessor.evaluateData(selection);
		reloadData();
	}
	
	public void ignoreSelection()
	{
		if(constraintEmptySelectionFailed()) {return;}
		ssiProcessor.ignoreData(selection);
		reloadData();
	}
	
	public void importSelection()
	{
		
		if(constraintEmptySelectionFailed()) {return;}
		logger.info("Linking selection");
		ssiProcessor.linkData(selection);
		reloadData();
	}
}