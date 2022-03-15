package org.jeesl.web.mbean.prototype.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.system.io.log.DebugLoggerHandler;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractSsiMappingBean <L extends JeeslLang,D extends JeeslDescription,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CRED extends JeeslIoSsiCredential<SYSTEM>,
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										DATA extends JeeslIoSsiData<MAPPING,LINK>,
										LINK extends JeeslIoSsiLink<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>,
										JOB extends JeeslJobStatus<L,D,JOB,?>,
										HOST extends JeeslIoSsiHost<L,D,SYSTEM>>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiMappingBean.class);
	
	protected JeeslLogger jogger;
	
	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING> fbSsi;
	private JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,HOST> fSsi;
	
	private final JsonTuple1Handler<MAPPING> thMapping; public JsonTuple1Handler<MAPPING> getThMapping() {return thMapping;}
	private final JsonTuple2Handler<MAPPING,LINK> thLink; public JsonTuple2Handler<MAPPING,LINK> getThLink() {return thLink;}
	
	private final List<ENTITY> entities; public List<ENTITY> getEntities() {return entities;}
	private final List<MAPPING> mappings; public List<MAPPING> getMappings() {return mappings;}
	private final List<LINK> links; public List<LINK> getLinks() {return links;}
	
	private MAPPING mapping; public MAPPING getMapping() {return mapping;} public void setMapping(MAPPING mapping) {this.mapping = mapping;}

	public AbstractSsiMappingBean(final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING> fbSsi)
	{
		this.fbSsi=fbSsi;
		mappings = new ArrayList<>();
		links = new ArrayList<>();
		entities = new ArrayList<>();
		
		thMapping = new JsonTuple1Handler<>(fbSsi.getClassMapping());
		thLink = new JsonTuple2Handler<>(fbSsi.getClassMapping(),fbSsi.getClassLink());
		
		jogger = DebugLoggerHandler.instance(this.getClass());
	}

	public void postConstructSsiMapping(JeeslIoSsiFacade<L,D,SYSTEM,CRED,MAPPING,ATTRIBUTE,DATA,LINK,ENTITY,CLEANING,JOB,HOST> fSsi)
	{	
		this.fSsi=fSsi;
		links.addAll(fSsi.all(fbSsi.getClassLink()));
		jogger.milestone(fbSsi.getClassLink().getSimpleName(),null,links.size());
		
		entities.addAll(fSsi.all(fbSsi.getClassEntity()));
		jogger.milestone(fbSsi.getClassEntity().getSimpleName(),null,entities.size());
		
		reload();
	}

	private void reload()
	{
		mappings.clear();
		mappings.addAll(fSsi.all(fbSsi.getClassMapping()));
		jogger.milestone(fbSsi.getClassMapping().getSimpleName(),null,mappings.size());
		
//		thMapping.init(fSsi.tpMapping());
//		thLink.init(fSsi.tpMappingLink());
//		for(LINK  l : thLink.getListB())
//		{
//			logger.info(l.getCode());
//		}
	}
	
	public void selectMapping()
	{
		logger.info(AbstractLogMessage.selectEntity(mapping));
	}
	
	public void addMapping()
	{
		mapping = fbSsi.ejbMapping().build(null);
	}
	
	public void saveMapping() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fbSsi.ejbMapping().converter(fSsi,mapping);
		mapping = fSsi.save(mapping);
		EjbIdFactory.replace(mappings,mapping);
	}
}