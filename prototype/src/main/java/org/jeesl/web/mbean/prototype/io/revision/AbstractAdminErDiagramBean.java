package org.jeesl.web.mbean.prototype.io.revision;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.io.label.EjbLabelDiagramFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
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
import org.jeesl.util.comparator.ejb.io.revision.RevisionDiagramComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

@Deprecated
public class AbstractAdminErDiagramBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminErDiagramBean.class);

	protected SbMultiHandler<RC> sbhCategory; public SbMultiHandler<RC> getSbhCategory() {return sbhCategory;}
	
	private EjbLabelDiagramFactory<L,D,RC,ERD> efErDiagram;

	private final Comparator<ERD> cpDiagram;

	protected List<ERD> diagrams; public List<ERD> getDiagrams() {return diagrams;}

	private ERD diagram; public ERD getDiagram() {return diagram;} public void setDiagram(ERD diagram) {this.diagram = diagram;}
	private String dot; public String getDot() {return dot;} public void setDot(String dot) {this.dot = dot;}

	public AbstractAdminErDiagramBean(final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision)
	{
		super(fbRevision);
		
		sbhCategory = new SbMultiHandler<RC>(fbRevision.getClassCategory(),this);
		
		cpDiagram = (new RevisionDiagramComparator<RC,ERD>()).factory(RevisionDiagramComparator.Type.category);
		efErDiagram = fbRevision.ejbDiagram();
	}

	protected void postConstructRevisionDiagram(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision)
	{
		super.postConstructRevision(bTranslation,bMessage,fRevision);
		
		sbhCategory.setList(fRevision.allOrderedPositionVisible(fbRevision.getClassCategory()));
		sbhCategory.selectAll();
		reloadDiagrams();
	}
	
	@Override public void callbackAfterSbSelection()
	{
		reloadDiagrams();
	}

	private void reloadDiagrams()
	{
		diagrams = fRevision.allForParents(fbRevision.getClassDiagram(), sbhCategory.getSelected());
//		diagrams = fRevision.all(fbRevision.getClassDiagram());
		Collections.sort(diagrams,cpDiagram);
	}

	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}

	public void addErDiagram()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbRevision.getClassDiagram()));}
		diagram = efErDiagram.build(null,null,null);
		diagram.setName(efLang.createEmpty(localeCodes));
		diagram.setDescription(efDescription.createEmpty(localeCodes));
	}

	public void selectDiagram() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(diagram));}
		diagram = efLang.persistMissingLangs(fRevision,localeCodes,diagram);
		diagram = efDescription.persistMissingLangs(fRevision,localeCodes,diagram);
		reloadDiagram();
	}
	
	

	private void reloadDiagram()
	{
		diagram = fRevision.find(fbRevision.getClassDiagram(), diagram);
		dot = diagram.getDotGraph();
	}

	public void saveDiagram() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(diagram));}
		if(diagram.getCategory()!=null){diagram.setCategory(fRevision.find(fbRevision.getClassCategory(),diagram.getCategory()));}
		diagram = fRevision.save(diagram);
		reloadDiagrams();
		bMessage.growlSuccessSaved();
		reloadDiagram();
	}


	public void rmDiagram() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(diagram));}
		fRevision.rm(diagram);
		diagram=null;
		dot = null;
		reloadDiagrams();
		bMessage.growlSuccessRemoved();
	}

	public void cancelDiagram()
	{
		diagram=null;
		dot = null;
	}

	public void reorderDiagrams() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.reorder(fbRevision.getClassDiagram(), diagrams));
		PositionListReorderer.reorder(fRevision,fbRevision.getClassDiagram(),diagrams);
		reloadDiagrams();
//	Collections.sort(diagrams,cpDiagram);
	}
	
}