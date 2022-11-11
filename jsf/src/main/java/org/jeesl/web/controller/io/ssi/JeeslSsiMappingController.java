package org.jeesl.web.controller.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.system.io.log.DebugLoggerHandler;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.web.controller.AbstractJeeslWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslSsiMappingController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										
										MAPPING extends JeeslIoSsiMapping<SYSTEM,ENTITY>,
										LINK extends JeeslIoSsiLink<L,D,LINK,?>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>>
						extends AbstractJeeslWebController<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiMappingController.class);
	
	protected JeeslLogger jogger;
	
	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,?,?,LINK,ENTITY,?,?> fbSsi;
	private JeeslIoSsiFacade<L,D,SYSTEM,?,MAPPING,?,?,LINK,ENTITY,?,?,?> fSsi;
	
	private final JsonTuple1Handler<MAPPING> thMapping; public JsonTuple1Handler<MAPPING> getThMapping() {return thMapping;}
	private final JsonTuple2Handler<MAPPING,LINK> thLink; public JsonTuple2Handler<MAPPING,LINK> getThLink() {return thLink;}
	
	private final Map<MAPPING,Boolean> mapTuples; public Map<MAPPING, Boolean> getMapTuples() {return mapTuples;}
	
	private final List<SYSTEM> systems; public List<SYSTEM> getSystems() {return systems;}
	private final List<ENTITY> entities; public List<ENTITY> getEntities() {return entities;}
	private final List<MAPPING> mappings; public List<MAPPING> getMappings() {return mappings;}
	private final List<LINK> links; public List<LINK> getLinks() {return links;}
	
	private MAPPING mapping; public MAPPING getMapping() {return mapping;} public void setMapping(MAPPING mapping) {this.mapping = mapping;}

	public JeeslSsiMappingController(final IoSsiDataFactoryBuilder<L,D,SYSTEM,MAPPING,?,?,LINK,ENTITY,?,?> fbSsi)
	{
		super(fbSsi.getClassL(),fbSsi.getClassD());
		this.fbSsi=fbSsi;
		mapTuples = new HashMap<>();
		
		systems = new ArrayList<>();
		mappings = new ArrayList<>();
		links = new ArrayList<>();
		entities = new ArrayList<>();
		
		thMapping = new JsonTuple1Handler<>(fbSsi.getClassMapping());
		thLink = new JsonTuple2Handler<>(fbSsi.getClassMapping(),fbSsi.getClassLink());
		
		jogger = DebugLoggerHandler.instance(this.getClass());
	}

	public void postConstructSsiMapping(JeeslIoSsiFacade<L,D,SYSTEM,?,MAPPING,?,?,LINK,ENTITY,?,?,?> fSsi)
	{	
		this.fSsi=fSsi;
		
		systems.addAll(fSsi.all(fbSsi.getClassSystem()));
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
	}
	
	public void considerTuple(MAPPING m)
	{
		if(!mapTuples.containsKey(m)) {mapTuples.put(m,false);}
		mapTuples.put(m,!mapTuples.get(m));
	}
	
	public void reloadNumbers()
	{
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
		List<MAPPING> tupleMappings = new ArrayList<>();
		for(MAPPING m : mappings)
		{
			if(mapTuples.containsKey(m) && mapTuples.get(m)) {tupleMappings.add(m);}
		}
		thLink.init(fSsi.tpMappingLink(tupleMappings));
		logger.info(AbstractLogMessage.reloaded(fbSsi.getClassMapping(),ptt,tupleMappings));
	}
	
	public void selectMapping()
	{
		logger.info(AbstractLogMessage.selectEntity(mapping));
		
	}
	
	public void addMapping()
	{
		logger.info(AbstractLogMessage.createEntity(fbSsi.getClassMapping()));
		mapping = fbSsi.ejbMapping().build(null);
	}
	
	public void saveMapping() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fbSsi.ejbMapping().converter(fSsi,mapping);
		mapping = fSsi.save(mapping);
		EjbIdFactory.replace(mappings,mapping);
	}
}