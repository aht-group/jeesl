package org.jeesl.web.mbean.prototype.system.constraint;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.controller.util.comparator.ejb.system.constraint.ContraintScopeComparator;
import org.jeesl.controller.util.comparator.ejb.system.constraint.SystemConstraintAlgorithmComparator;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.ConstraintFactoryBuilder;
import org.jeesl.factory.ejb.system.constraint.EjbConstraintFactory;
import org.jeesl.factory.ejb.system.constraint.EjbConstraintScopeFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithmGroup;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintCategory;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.ui.UiTwiceClickHelper;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated //Use JeeslSystemConstraintDefinitionWebController
public class AbstractSystemConstraintDefinitionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										GROUP extends JeeslConstraintAlgorithmGroup<L,D,GROUP,?>,
										ALGORITHM extends JeeslConstraintAlgorithm<L,D,GROUP>,
										SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
										CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,LEVEL,TYPE>,
										CATEGORY extends JeeslConstraintCategory<L,D,CATEGORY,?>,
										LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
										TYPE extends JeeslConstraintType<L,D,TYPE,?>,
										RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSystemConstraintDefinitionBean.class);
	
	protected JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CATEGORY,LEVEL,TYPE,RESOLUTION> fConstraint;
	protected final ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint;

	protected final Comparator<ALGORITHM> cpAlgorithm;
	
	private JeeslConstraintsBean<CONSTRAINT> bConstraint;
	
	private List<SCOPE> scopes; public List<SCOPE> getScopes() {return scopes;}
	private List<CONSTRAINT> constraints; public List<CONSTRAINT> getConstraints() {return constraints;}
	private List<TYPE> types; public List<TYPE> getTypes() {return types;}
	private List<LEVEL> levels; public List<LEVEL> getLevels() {return levels;}
	
	private SCOPE scope; public SCOPE getScope() {return scope;} public void setScope(SCOPE scope) {this.scope = scope;}
	private CONSTRAINT constraint; public CONSTRAINT getConstraint() {return constraint;} public void setConstraint(CONSTRAINT constraint) {this.constraint = constraint;}
	
	private final EjbConstraintScopeFactory<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> efScope;
	private final EjbConstraintFactory<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> efConstraint;
	
	protected SbMultiHandler<CATEGORY> sbhCategory; public SbMultiHandler<CATEGORY> getSbhCategory() {return sbhCategory;}
	private final UiTwiceClickHelper ui2; public UiTwiceClickHelper getUi2() {return ui2;}
	private final Comparator<SCOPE> cpScope;
	
	public AbstractSystemConstraintDefinitionBean(ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint)
	{
		super(fbConstraint.getClassL(),fbConstraint.getClassD());
		this.fbConstraint=fbConstraint;
		
		cpAlgorithm = (new SystemConstraintAlgorithmComparator<ALGORITHM,GROUP>()).factory(SystemConstraintAlgorithmComparator.Type.position);
		
		cpScope = new ContraintScopeComparator<SCOPE,CATEGORY>().factory(ContraintScopeComparator.Type.position);
		ui2 = new UiTwiceClickHelper();
		efScope = new EjbConstraintScopeFactory<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>(fbConstraint.getClassL(),fbConstraint.getClassD(),fbConstraint.getClassScope(),fbConstraint.getClassConstraintCategory());
		efConstraint = new EjbConstraintFactory<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>(fbConstraint.getClassL(),fbConstraint.getClassD(),fbConstraint.getClassConstraint(),fbConstraint.getClassConstraintType());
	}
	
	protected void postConstructConstraintDefinition(JeeslConstraintsBean<CONSTRAINT> bConstraint, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
													JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CATEGORY,LEVEL,TYPE,RESOLUTION> fConstraint)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fConstraint=fConstraint;
		this.bConstraint=bConstraint;
		
		types = fConstraint.allOrderedPosition(fbConstraint.getClassConstraintType());
		levels = fConstraint.allOrderedPosition(fbConstraint.getClassConstraintLevel());
		
		sbhCategory = new SbMultiHandler<CATEGORY>(fbConstraint.getClassConstraintCategory(),fConstraint.allOrderedPosition(fbConstraint.getClassConstraintCategory()),this);
		if(sbhCategory.getHasSome()) {sbhCategory.toggle(sbhCategory.getList().get(0));}
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reloadScopes();
	}
	
	private void reset(boolean rScope, boolean rConstraint)
	{
		if(rScope){scope=null;ui2.checkA(scope);}
		if(rConstraint){constraint=null;ui2.checkB(constraint);}
	}

	private void reloadScopes()
	{
		scopes = fConstraint.all(fbConstraint.getClassScope());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbConstraint.getClassScope(),scopes));}
	}
	
	public void addScope() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbConstraint.getClassScope()));}
		scope = efScope.build(null);
		scope.setName(efLang.createEmpty(localeCodes));
		scope.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,true);
		ui2.checkA(scope);
	}
	
	public void selectScope() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(scope));}
		scope = fConstraint.find(fbConstraint.getClassScope(),scope);
		scope = efScope.updateLD(fConstraint, scope, localeCodes);
		reloadConstraints();
		ui2.checkA(scope);
	}
	
	public void saveScope() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(scope));}
		scope.setCategory(fConstraint.find(fbConstraint.getClassConstraintCategory(),scope.getCategory()));
		scope = fConstraint.save(scope);
		reloadScopes();
		reloadConstraints();
	}
	
//	protected void reorderEntites() throws UtilsConstraintViolationException, UtilsLockingException {PositionListReorderer.reorder(fRevision, cEntity, entities);Collections.sort(entities, comparatorEntity);}
	
	public void reloadConstraints()
	{
		constraints = fConstraint.allForParent(fbConstraint.getClassConstraint(),scope);
	}
	
	public void addConstraint() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbConstraint.getClassConstraint()));}
		constraint = efConstraint.build(scope,null);
		constraint.setName(efLang.createEmpty(localeCodes));
		constraint.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,false);
		ui2.checkB(constraint);
	}
	
	public void saveConstraint() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(constraint));}
		constraint.setType(fConstraint.find(fbConstraint.getClassConstraintType(),constraint.getType()));
		if(constraint.getLevel()!=null) {constraint.setLevel(fConstraint.find(fbConstraint.getClassConstraintLevel(),constraint.getLevel()));}
		constraint = fConstraint.save(constraint);
		reloadConstraints();
		
		bConstraint.update(constraint);
	}
	
	public void selectConstraint() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(constraint));}
		constraint = fConstraint.find(fbConstraint.getClassConstraint(),constraint);
		efConstraint.updateLD(fConstraint, constraint, localeCodes);
		ui2.checkB(constraint);
	}
	
	protected void reorderScopes() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fConstraint, fbConstraint.getClassScope(), scopes);Collections.sort(scopes, cpScope);}
}