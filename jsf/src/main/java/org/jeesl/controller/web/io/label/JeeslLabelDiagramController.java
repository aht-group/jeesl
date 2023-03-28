package org.jeesl.controller.web.io.label;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.io.label.EjbLabelDiagramFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.util.comparator.ejb.io.revision.RevisionDiagramComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslLabelDiagramController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										RC extends JeeslRevisionCategory<L,D,RC,?>,
										ERD extends JeeslRevisionDiagram<L,D,RC>>
		extends AbstractJeeslWebController<L,D,LOC>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslLabelDiagramController.class);

	private JeeslIoRevisionFacade<L,D,RC,?,?,?,?,?,?,?,?,?,ERD,?> fRevision;
	private final IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,?,?,?,?,?,ERD,?> fbRevision;
	
	protected SbMultiHandler<RC> sbhCategory; public SbMultiHandler<RC> getSbhCategory() {return sbhCategory;}
	
	private EjbLabelDiagramFactory<L,D,RC,ERD> efErDiagram;

	private final Comparator<ERD> cpDiagram;

	protected List<ERD> diagrams; public List<ERD> getDiagrams() {return diagrams;}

	private ERD diagram; public ERD getDiagram() {return diagram;} public void setDiagram(ERD diagram) {this.diagram = diagram;}
	private String dot; public String getDot() {return dot;} public void setDot(String dot) {this.dot = dot;}

	public JeeslLabelDiagramController(final IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,?,?,?,?,?,ERD,?> fbRevision)
	{
		super(fbRevision.getClassL(),fbRevision.getClassD());
		this.fbRevision=fbRevision;
		sbhCategory = new SbMultiHandler<RC>(fbRevision.getClassCategory(),this);
		
		cpDiagram = (new RevisionDiagramComparator<RC,ERD>()).factory(RevisionDiagramComparator.Type.category);
		efErDiagram = fbRevision.ejbDiagram();
	}

	public void postConstructRevisionDiagram(JeeslLocaleProvider<LOC> lp,
											JeeslFacesMessageBean bMessage,
											JeeslIoRevisionFacade<L,D,RC,?,?,?,?,?,?,?,?,?,ERD,?> fRevision)
	{
		super.postConstructWebController(lp,bMessage);
		this.fRevision=fRevision;
		
		sbhCategory.setList(fRevision.allOrderedPositionVisible(fbRevision.getClassCategory()));
		sbhCategory.selectAll();
		reloadDiagrams();
	}

	private void reloadDiagrams()
	{
		diagrams = fRevision.allForParents(fbRevision.getClassDiagram(), sbhCategory.getSelected());
//		diagrams = fRevision.all(fbRevision.getClassDiagram());
		Collections.sort(diagrams,cpDiagram);
	}

	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
		reloadDiagrams();
	}

	public void addErDiagram()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbRevision.getClassDiagram()));}
		diagram = efErDiagram.build(null,null,null);
		diagram.setName(efLang.buildEmpty(lp.getLocales()));
		diagram.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}

	public void selectDiagram() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(diagram));}
		diagram = efLang.persistMissingLangs(fRevision,lp.getLocales(),diagram);
		diagram = efDescription.persistMissingLangs(fRevision,lp.getLocales(),diagram);
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