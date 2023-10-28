package org.jeesl.controller.web.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.system.io.log.DebugJeeslLogger;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.cache.io.ssi.JeeslIoSsiContextCache;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslSsiContextController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CONTEXT extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										STATUS extends JeeslIoSsiStatus<L,D,STATUS,?>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>>
						extends AbstractJeeslLocaleWebController<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiContextController.class);
	
	protected JeeslLogger jogger;
	
	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,CONTEXT,?,?,STATUS,ENTITY,?,?> fbSsi;
	private JeeslIoSsiFacade<SYSTEM,?,CONTEXT,?,?,STATUS,ENTITY,?,?,?> fSsi;
	
	private JeeslIoSsiContextCache<CONTEXT,STATUS> cache; 
	
	private final JsonTuple1Handler<CONTEXT> thContext; public JsonTuple1Handler<CONTEXT> getThContext() {return thContext;}
	private final JsonTuple2Handler<CONTEXT,STATUS> thStatus; public JsonTuple2Handler<CONTEXT,STATUS> getThStatus() {return thStatus;}

	private final List<SYSTEM> systems; public List<SYSTEM> getSystems() {return systems;}
	private final List<ENTITY> entities; public List<ENTITY> getEntities() {return entities;}
	private final List<CONTEXT> mappings; public List<CONTEXT> getMappings() {return mappings;}
	private final List<STATUS> links; public List<STATUS> getLinks() {return links;}
	
	private CONTEXT context; public CONTEXT getContext() {return context;} public void setContext(CONTEXT context) {this.context = context;}

	public JeeslSsiContextController(final IoSsiDataFactoryBuilder<L,D,SYSTEM,CONTEXT,?,?,STATUS,ENTITY,?,?> fbSsi)
	{
		super(fbSsi.getClassL(),fbSsi.getClassD());
		this.fbSsi=fbSsi;
		
		systems = new ArrayList<>();
		mappings = new ArrayList<>();
		links = new ArrayList<>();
		entities = new ArrayList<>();
		
		thContext = new JsonTuple1Handler<>(fbSsi.getClassMapping());
		thStatus = new JsonTuple2Handler<>(fbSsi.getClassMapping(),fbSsi.getClassLink());
		
		jogger = DebugJeeslLogger.instance(this.getClass());
	}

	public void postConstructSsiMapping(JeeslIoSsiFacade<SYSTEM,?,CONTEXT,?,?,STATUS,ENTITY,?,?,?> fSsi,
										JeeslIoSsiContextCache<CONTEXT,STATUS> cache)
	{	
		this.fSsi=fSsi;
		this.cache=cache;
		
		systems.addAll(fSsi.all(fbSsi.getClassSystem()));
		links.addAll(fSsi.allOrderedPosition(fbSsi.getClassLink()));
		if(Objects.nonNull(jogger)) {jogger.milestone(fbSsi.getClassLink().getSimpleName(),null,links.size());}
		
		entities.addAll(fSsi.all(fbSsi.getClassEntity()));
		jogger.milestone(fbSsi.getClassEntity().getSimpleName(),null,entities.size());
		
		this.reload();
		
		if(Objects.nonNull(cache))
		{
			thStatus.clear();
			for(CONTEXT c : mappings)
			{
				JsonTuples2<CONTEXT,STATUS> t = cache.cacheGetTuples2(this.getClass(),c);
				logger.info(c.toString()+" null:"+Objects.isNull(t));
				thStatus.append(t);
			}
		}
	}

	private void reload()
	{
		mappings.clear();
		mappings.addAll(fSsi.all(fbSsi.getClassMapping()));
		jogger.milestone(fbSsi.getClassMapping().getSimpleName(),null,mappings.size());
	}
	
	public void selectContext()
	{
		logger.info(AbstractLogMessage.selectEntity(context));
		
	}
	
	public void countNumbers()
	{
		JsonTuples2<CONTEXT,STATUS> tuples = fSsi.tpcContextStatus(Arrays.asList(context));
		if(Objects.nonNull(cache))
		{
			thStatus.append(tuples);
			cache.cachePutTuples2(this.getClass(),context,tuples);
			
			JsonTuples2<CONTEXT,STATUS> t = cache.cacheGetTuples2(this.getClass(),context);
			logger.warn("t from cache is null ? "+Objects.isNull(t));
		}
		else
		{
			thStatus.init(tuples);
		}
	}
	
	public void addContext()
	{
		logger.info(AbstractLogMessage.createEntity(fbSsi.getClassMapping()));
		context = fbSsi.ejbMapping().build(null);
	}
	
	public void saveMapping() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fbSsi.ejbMapping().converter(fSsi,context);
		context = fSsi.save(context);
		EjbIdFactory.replace(mappings,context);
	}
}