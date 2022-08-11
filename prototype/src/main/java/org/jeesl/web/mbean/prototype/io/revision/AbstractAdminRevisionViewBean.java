package org.jeesl.web.mbean.prototype.io.revision;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminRevisionViewBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											RC extends JeeslRevisionCategory<L,D,RC,?>,
											RV extends JeeslRevisionView<L,D,RVM>,
											RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
											RS extends JeeslRevisionScope<L,D,RC,RA>,
											RST extends JeeslRevisionScopeType<L,D,RST,?>,
											RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
											REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
											RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
											RER extends JeeslStatus<L,D,RER>,
											RAT extends JeeslStatus<L,D,RAT>,
											ERD extends JeeslRevisionDiagram<L,D,RC>>
					extends AbstractAdminRevisionBean<L,D,LOC,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminRevisionViewBean.class);

	protected final List<RE> entities; public List<RE> getEntities() {return entities;}
	private List<RV> views; public List<RV> getViews() {return views;}
	private List<RVM> viewMappings; public List<RVM> getViewMappings() {return viewMappings;}

	private RV rv; public RV getRv() {return rv;} public void setRv(RV rv) {this.rv = rv;}
	private RVM mapping; public RVM getMapping() {return mapping;}public void setMapping(RVM mapping) {this.mapping = mapping;}

	public AbstractAdminRevisionViewBean(final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision)
	{
		super(fbRevision);
		
		entities = new ArrayList<>();
	}

	protected void postConstructRevisionView(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
							JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision)
	{
		super.postConstructRevision(bTranslation,bMessage,fRevision);
		entities.addAll(fRevision.all(fbRevision.getClassEntity()));
		reloadViews();
	}
	
	@Override public void callbackAfterSbSelection()
	{
		reloadViews();
	}

	public void reloadViews()
	{
		views = fRevision.allOrderedPosition(fbRevision.getClassView());
		logger.info(AbstractLogMessage.reloaded(fbRevision.getClassView(),views));
//		if(showInvisibleCategories){categories = fUtils.allOrderedPosition(cCategory);}
//		else{categories = fUtils.allOrderedPositionVisible(cCategory);}
	}

	public void add() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.addEntity(fbRevision.getClassView()));
		rv = efView.build();
		rv.setName(efLang.createEmpty(langs));
		rv.setDescription(efDescription.createEmpty(langs));
	}

	public void select() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(rv));
		rv = fRevision.find(fbRevision.getClassView(), rv);
		rv = efLang.persistMissingLangs(fRevision,langs,rv);
		rv = efDescription.persistMissingLangs(fRevision,langs,rv);
		mapping=null;
		reloadView();
	}

	private void reloadView()
	{
		rv = fRevision.load(fbRevision.getClassView(), rv);
		viewMappings = rv.getMaps();
	}

	public void save() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(rv));
		rv = fRevision.save(rv);
		bMessage.growlSuccessSaved();
		reloadViews();
		reloadView();
		updatePerformed();
	}

	public void rm() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(rv));
		fRevision.rm(rv);
		bMessage.growlSuccessRemoved();
		rv=null;
		reloadViews();
		updatePerformed();
	}

	public void cancel()
	{
		rv = null;
		mapping=null;
	}

	//*************************************************************************************

	public void changeEntity()
	{
		if(mapping.getEntity()!=null)
		{
			mapping.setEntity(fRevision.find(fbRevision.getClassEntity(),mapping.getEntity()));
			logger.info(AbstractLogMessage.selectOneMenuChange(mapping.getEntity()));
			reloadEntityMappings();
			if(entityMappings.isEmpty()){mapping.setEntityMapping(null);}
			else{mapping.setEntityMapping(entityMappings.get(0));}
		}
	}

	private void reloadEntityMappings()
	{
		RE e = fRevision.load(fbRevision.getClassEntity(), mapping.getEntity());
		entityMappings = e.getMaps();
	}

	public void addMapping() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.addEntity(fbRevision.getClassViewMapping())+" entites:"+entities.size()+" empty:"+entities.isEmpty());
		RE re = null;
		if(!entities.isEmpty())
		{
			re = entities.get(0);
			mapping = efMappingView.build(rv,re,null);
			reloadEntityMappings();
			if(!entityMappings.isEmpty()){mapping.setEntityMapping(entityMappings.get(0));}
		}
		else
		{
			mapping = efMappingView.build(rv,re,null);
			entityMappings.clear();
		}
	}

	public void selectMapping() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(mapping));
		mapping = fRevision.find(fbRevision.getClassViewMapping(), mapping);
		reloadEntityMappings();
	}

	public void saveMapping() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(mapping));
		mapping.setEntityMapping(fRevision.find(fbRevision.getClassEntityMapping(),mapping.getEntityMapping()));
		mapping.setEntity(fRevision.find(fbRevision.getClassEntity(),mapping.getEntity()));
		mapping = fRevision.save(mapping);
		reloadView();
		bMessage.growlSuccessSaved();
		updatePerformed();
	}

	public void rmMapping() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(mapping));
		fRevision.rm(fbRevision.getClassViewMapping(),mapping);
		mapping=null;
		bMessage.growlSuccessRemoved();
		reloadView();
		updatePerformed();
	}

	public void cancelMapping()
	{
		mapping=null;
	}

	protected void reorderViews() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fRevision, views);}
	protected void reorderMappings() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fRevision, viewMappings);}
	protected void updatePerformed(){}

	@SuppressWarnings("rawtypes")
	@Override protected void updateSecurity2(JeeslJsfSecurityHandler jsfSecurityHandler, String actionDeveloper)
	{
		uiAllowSave = jsfSecurityHandler.allow(actionDeveloper);

		if(logger.isTraceEnabled())
		{
			logger.info(uiAllowSave+" allowSave ("+actionDeveloper+")");
		}
	}
}