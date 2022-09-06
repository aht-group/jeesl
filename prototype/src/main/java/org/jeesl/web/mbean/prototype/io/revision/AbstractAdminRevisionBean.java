package org.jeesl.web.mbean.prototype.io.revision;

import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.io.label.EjbLabelAttributeFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelMappingEntityFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelMappingViewFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelScopeFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelViewFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
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
import org.jeesl.util.comparator.ejb.io.revision.RevisionDiagramComparator;
import org.jeesl.util.comparator.ejb.io.revision.RevisionEntityComparator;
import org.jeesl.util.comparator.ejb.io.revision.RevisionScopeComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractAdminRevisionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
					extends AbstractAdminBean<L,D,LOC>
					implements SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminRevisionBean.class);

	protected JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision;
	protected final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision;

//	protected final SbMultiHandler<ERD> sbhDiagram; public SbMultiHandler<ERD> getSbhDiagram() {return sbhDiagram;}

	protected final Comparator<RS> comparatorScope;
	protected final Comparator<RE> cpEntity;
	protected final Comparator<ERD> cpDiagram;

	protected final EjbLabelViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> efView;
	protected final EjbLabelMappingViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> efMappingView;
	protected final EjbLabelScopeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> efScope;
	
	protected final EjbLabelMappingEntityFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> efMappingEntity;
	protected final EjbLabelAttributeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> efAttribute;

	protected List<RA> attributes; public List<RA> getAttributes() {return attributes;}
	protected List<RC> categories; public List<RC> getCategories() {return categories;}
	protected List<RS> scopes; public List<RS> getScopes() {return scopes;}
	protected List<RST> scopeTypes; public List<RST> getScopeTypes() {return scopeTypes;}
	
	protected List<REM> entityMappings; public List<REM> getEntityMappings() {return entityMappings;}
	protected List<RAT> types; public List<RAT> getTypes() {return types;}
	protected List<RER> relations; public List<RER> getrelations() {return relations;}

	protected RA attribute; public RA getAttribute() {return attribute;}public void setAttribute(RA attribute) {this.attribute = attribute;}

	public AbstractAdminRevisionBean(final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision)
	{
		super(fbRevision.getClassL(),fbRevision.getClassD());
		this.fbRevision=fbRevision;
		
//		sbhDiagram = new SbMultiHandler<>(fbRevision.getClassDiagram(),this);

		cpEntity = fbRevision.cpEjbEntity(RevisionEntityComparator.Type.position);
		comparatorScope = (new RevisionScopeComparator<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>()).factory(RevisionScopeComparator.Type.position);
		cpDiagram = (new RevisionDiagramComparator<RC,ERD>()).factory(RevisionDiagramComparator.Type.category);

		efView = fbRevision.ejbView();
		efMappingView = fbRevision.ejbMappingView();
		efScope = fbRevision.ejbScope();
		efMappingEntity = fbRevision.ejbMappingEntity();
		efAttribute = fbRevision.ejbAttribute();
	}

	protected void postConstructRevision(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fRevision=fRevision;

		if(fRevision==null) {logger.warn(JeeslIoRevisionFacade.class.getSimpleName()+" is NULL");}
		if(fbRevision==null) {logger.warn(IoRevisionFactoryBuilder.class.getSimpleName()+" is NULL");}
		if(fbRevision.getClassCategory()==null) {logger.warn(IoRevisionFactoryBuilder.class.getSimpleName()+".getClassCategory() is NULL");}

		categories = fRevision.allOrderedPositionVisible(fbRevision.getClassCategory());
	}

	@Override public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{

	}
	
	
	
	public abstract void callbackAfterSbSelection();

	public void addAttribute() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbRevision.getClassAttribute()));}
		attribute = efAttribute.build(null);
		attribute.setName(efLang.createEmpty(langs));
		attribute.setDescription(efDescription.createEmpty(langs));
	}

	public void selectAttribute() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(attribute));}
		attribute = fRevision.find(fbRevision.getClassAttribute(), attribute);
		attribute = efLang.persistMissingLangs(fRevision,langs,attribute);
		attribute = efDescription.persistMissingLangs(fRevision,langs,attribute);
	}

	public void cancelAttribute()
	{
		attribute=null;
	}
}