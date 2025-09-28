package org.jeesl.controller.web.io.ssi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.controller.handler.io.log.DebugJeeslLogger;
import org.jeesl.controller.handler.tuple.JsonTuple1Handler;
import org.jeesl.controller.handler.tuple.JsonTuple2Handler;
import org.jeesl.controller.util.comparator.ejb.EjbIdComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.factory.ejb.io.ssi.data.EjbIoSsiContextFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.cache.io.ssi.JeeslIoSsiContextCache;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSsiContextController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										SYSTEM extends JeeslIoSsiSystem<L,D>,
										CONTEXT extends JeeslIoSsiContext<SYSTEM,ENTITY>,
										STATUS extends JeeslIoSsiStatus<L,D,STATUS,?>,
										ERROR extends JeeslIoSsiError<L,D,CONTEXT,?>,
										ENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>>
						extends AbstractJeeslLocaleWebController<L,D,LOC>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSsiContextController.class);

	protected JeeslLogger jogger;

	private final IoSsiDataFactoryBuilder<L,D,SYSTEM,CONTEXT,?,?,STATUS,ERROR,ENTITY,?,?> fbSsi;
	private JeeslIoSsiFacade<SYSTEM,?,CONTEXT,?,?,STATUS,ERROR,ENTITY,?,?,?> fSsi;

	private JeeslIoSsiContextCache<CONTEXT,STATUS> cache;
	private final Comparator<CONTEXT> cpContext;

	private final EjbIoSsiContextFactory<SYSTEM,CONTEXT,ENTITY> efContext;

	private final JsonTuple1Handler<CONTEXT> thContext; public JsonTuple1Handler<CONTEXT> getThContext() {return thContext;}
	private final JsonTuple1Handler<ERROR> thError; public JsonTuple1Handler<ERROR> getThError() {return thError;}
	private final JsonTuple2Handler<CONTEXT,STATUS> thStatus; public JsonTuple2Handler<CONTEXT,STATUS> getThStatus() {return thStatus;}

	private final List<SYSTEM> systems; public List<SYSTEM> getSystems() {return systems;}
	private final List<ENTITY> entities; public List<ENTITY> getEntities() {return entities;}
	private final List<CONTEXT> contexts; public List<CONTEXT> getContexts() {return contexts;}
	private final List<STATUS> links; public List<STATUS> getLinks() {return links;}
	private final List<ERROR> errors; public List<ERROR> getErrors() {return errors;}

	private CONTEXT context; public CONTEXT getContext() {return context;} public void setContext(CONTEXT context) {this.context = context;}
	private ERROR error; public ERROR getError() {return error;} public void setError(ERROR error) {this.error = error;}

	public JeeslSsiContextController(final IoSsiDataFactoryBuilder<L,D,SYSTEM,CONTEXT,?,?,STATUS,ERROR,ENTITY,?,?> fbSsi)
	{
		super(fbSsi.getClassL(),fbSsi.getClassD());
		this.fbSsi=fbSsi;

		efContext = fbSsi.ejbContext();

		systems = new ArrayList<>();
		contexts = new ArrayList<>();
		entities = new ArrayList<>();
		links = new ArrayList<>();
		errors = new ArrayList<>();

		cpContext = new EjbIdComparator<CONTEXT>().factory(EjbIdComparator.Type.dsc);

		thContext = new JsonTuple1Handler<>(fbSsi.getClassMapping());
		thError = new JsonTuple1Handler<>(fbSsi.getClassError());
		thStatus = new JsonTuple2Handler<>(fbSsi.getClassMapping(),fbSsi.getClassStatus());

		jogger = DebugJeeslLogger.instance(this.getClass());
	}

	public void postConstructSsiMapping(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
										JeeslIoSsiFacade<SYSTEM,?,CONTEXT,?,?,STATUS,ERROR,ENTITY,?,?,?> fSsi,
										JeeslIoSsiContextCache<CONTEXT,STATUS> cache)
	{
		super.postConstructLocaleWebController(lp, bMessage);
		this.fSsi=fSsi;
		this.cache=cache;

		systems.addAll(fSsi.all(fbSsi.getClassSystem()));
		links.addAll(fSsi.allOrderedPosition(fbSsi.getClassStatus()));
		if(Objects.nonNull(jogger)) {jogger.milestone(fbSsi.getClassStatus().getSimpleName(),null,links.size());}

		entities.addAll(fSsi.all(fbSsi.getClassEntity()));
		jogger.milestone(fbSsi.getClassEntity().getSimpleName(),null,entities.size());

		this.reload();

		if(Objects.nonNull(cache))
		{
			thStatus.clear();
			for(CONTEXT c : contexts)
			{
				JsonTuples2<CONTEXT,STATUS> t = cache.cacheGetTuples2(this.getClass(),c);
				logger.info(c.toString()+" null:"+Objects.isNull(t));
				thStatus.append(t);
			}
		}
	}

	public void reset(boolean rErrors, boolean rError)
	{
		if(rErrors) {errors.clear();}
		if(rError) {error=null;}
	}

	private void reload()
	{
		contexts.clear();
		contexts.addAll(fSsi.all(fbSsi.getClassMapping()));
		Collections.sort(contexts,cpContext);
		jogger.milestone(fbSsi.getClassMapping().getSimpleName(),null,contexts.size());
	}

	public void selectContext()
	{
		logger.info(AbstractLogMessage.selectEntity(context));
		this.reloadErrors();
	}

	public void countNumbers()
	{
		efContext.converter(fSsi, context);
		thStatus.clearFrom(context);
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
			thStatus.load(tuples);
		}
	}

	public void addContext()
	{
		logger.info(AbstractLogMessage.createEntity(fbSsi.getClassMapping()));
		context = fbSsi.ejbContext().build(null);
	}

	public void saveContext() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("Save");
		efContext.converter(fSsi,context);
		context = fSsi.save(context);
		EjbIdFactory.replace(contexts,context);
		logger.info("Saved");
	}

	public void deleteMapping() throws JeeslConstraintViolationException, JeeslLockingException
	{
		fSsi.rm(context);
		EjbIdFactory.remove(contexts, context);
	}

	private void reloadErrors()
	{
		this.reset(true,false);
		errors.addAll(fSsi.allForParent(fbSsi.getClassError(),context));
	}
	public void reorderErrors() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSsi,errors); this.reloadErrors();}
	public void addError()
	{
		logger.info(AbstractLogMessage.createEntity(fbSsi.getClassError()));
		error = fbSsi.efError().build(context, errors);
		error.setName(efLang.buildEmpty(lp.getLocales()));
		error.setDescription(efDescription.buildEmpty(lp.getLocales()));
		thError.clear();
	}
	public void selectError()
	{
		logger.info(AbstractLogMessage.selectEntity(error));
		thError.clear();
	}
	public void saveError() throws JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(error));
		try
		{
			error = fSsi.save(error);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {bMessage.constraintDuplicate(null);}
		this.reloadErrors();
	}
	public void deleteError() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.deleteEntity(error));
		fSsi.rm(error);
		this.reset(false, true);
		this.reloadErrors();
	}

	public void countErrors()
	{
		thError.clear();
		thError.init(fSsi.tpcIoSsiErrorContext(context, null, null));
	}
}