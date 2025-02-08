package org.jeesl.web.mbean.prototype.io.revision;

import java.io.Serializable;
import java.util.Collections;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoLabelFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
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
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminRevisionScopeBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											RC extends JeeslRevisionCategory<L,D,RC,?>,
											RV extends JeeslRevisionView<L,D,RVM>,
											RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
											RS extends JeeslRevisionScope<L,D,RC,RA>,
											RST extends JeeslRevisionScopeType<L,D,RST,?>,
											RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
											REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
											RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends JeeslStatus<L,D,RER>,
											RAT extends JeeslStatus<L,D,RAT>,
											ERD extends JeeslRevisionDiagram<L,D,RC>>
					extends AbstractAdminRevisionBean<L,D,LOC,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminRevisionScopeBean.class);

	protected SbMultiHandler<RC> sbhCategory; public SbMultiHandler<RC> getSbhCategory() {return sbhCategory;}
	
	private RS scope; public RS getScope() {return scope;} public void setScope(RS scope) {this.scope = scope;}

	public AbstractAdminRevisionScopeBean(final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision){super(fbRevision);}

	protected void postConstructRevisionScope(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
											JeeslIoLabelFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,?> fRevision)
	{
		super.postConstructRevision(bTranslation,bMessage,fRevision);
		
		sbhCategory = new SbMultiHandler<RC>(fbRevision.getClassCategory(),categories,this);
		
		types = fRevision.allOrderedPositionVisible(fbRevision.getClassAttributeType());
		reloadScopes();
	}

	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		super.toggled(handler,c);
		logger.info(AbstractLogMessage.toggled(c));
		reloadScopes();
		cancel();
	}
	
	@Override public void callbackAfterSbSelection()
	{
		reloadScopes();
	}

	public void reloadScopes()
	{
		scopes = fRevision.findRevisionScopes(sbhCategory.getSelected(), true);
		logger.info(AbstractLogMessage.reloaded(fbRevision.getClassScope(),scopes));
		Collections.sort(scopes, comparatorScope);
	}

	private void reloadScope()
	{
		scope = fRevision.load(fbRevision.getClassScope(), scope);
		attributes = scope.getAttributes();
	}

	public void add() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.createEntity(fbRevision.getClassScope()));
		scope = efScope.build();
		scope.setName(efLang.buildEmpty(lp.getLocales()));
		scope.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}

	public void select() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(scope));
		scope = fRevision.find(fbRevision.getClassScope(),scope);
		scope = efLang.persistMissingLangs(fRevision,lp.getLocales(),scope);
		scope = efDescription.persistMissingLangs(fRevision,lp.getLocales(),scope);
		reloadScope();
		attribute=null;
	}

	public void save() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(scope));
		scope = fRevision.save(scope);
		bMessage.growlSaved(scope);
		reloadScopes();
		reloadScope();
		updatePerformed();
	}

	public void rm() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(scope));
		fRevision.rm(scope);
		bMessage.growlDeleted(scope);
		scope=null;
		reloadScopes();
		updatePerformed();
	}

	public void cancel()
	{
		scope=null;
		attribute=null;
	}

	public void saveAttribute() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(attribute));}
		if(attribute.getType()!=null){attribute.setType(fRevision.find(fbRevision.getClassAttributeType(), attribute.getType()));}
		attribute = fRevision.save(fbRevision.getClassScope(),scope,attribute);
		reloadScope();
		bMessage.growlSaved(attribute);
		updatePerformed();
	}

	public void rmAttribute() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(attribute));}
		fRevision.rm(fbRevision.getClassScope(),scope,attribute);
		bMessage.growlDeleted(attribute);
		attribute=null;
		
		reloadScope();
		updatePerformed();
	}

	protected void reorderScopes() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fRevision, fbRevision.getClassScope(), scopes);Collections.sort(scopes, comparatorScope);}
	protected void reorderAttributes() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fRevision, attributes);}

	protected void updatePerformed(){}
}