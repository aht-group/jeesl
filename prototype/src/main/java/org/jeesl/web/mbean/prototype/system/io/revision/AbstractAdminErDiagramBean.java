package org.jeesl.web.mbean.prototype.system.io.revision;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.controller.handler.sb.SbMultiHandler;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.system.io.revision.EjbRevisionDiagramFactory;
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.model.system.io.revision.JeeslRevisionScope;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.system.io.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.io.revision.er.JeeslRevisionDiagram;
import org.jeesl.util.comparator.ejb.system.erdiagram.ErDiagramComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsLockingException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminErDiagramBean <L extends UtilsLang, D extends UtilsDescription, LOC extends UtilsStatus<LOC,L,D>,
										RC extends JeeslRevisionCategory<L,D,RC,?>,
										RV extends JeeslRevisionView<L,D,RVM>,
										RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
										RS extends JeeslRevisionScope<L,D,RC,RA>,
										RST extends UtilsStatus<RST,L,D>,
										RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
										REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
										RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends UtilsStatus<RER,L,D>,
										RAT extends UtilsStatus<RAT,L,D>,
										ERD extends JeeslRevisionDiagram<L,D,RC>>
		extends AbstractAdminRevisionBean<L,D,LOC,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminErDiagramBean.class);

	private EjbRevisionDiagramFactory<L,D,RC,ERD> efErDiagram;

	private final Comparator<ERD> cpDiagram;

	protected List<ERD> diagrams; public List<ERD> getDiagrams() {return diagrams;}

	private ERD diagram; public ERD getDiagram() {return diagram;} public void setDiagram(ERD diagram) {this.diagram = diagram;}
	private String dot; public String getDot() {return dot;} public void setDot(String dot) {this.dot = dot;}

	public AbstractAdminErDiagramBean(final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD> fbRevision)
	{
		super(fbRevision);
		cpDiagram = (new ErDiagramComparator<L,D,RC,ERD>()).factory(ErDiagramComparator.Type.category);
		efErDiagram = fbRevision.ejbDiagram();
	}

	protected void postConstructRevisionDiagram(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD> fRevision)
	{
		String[] langs = bTranslation.getLangKeys().toArray(new String[0]);
		super.initRevisionSuper(langs,bMessage,fRevision);

		sbhCategory.selectAll();
		refreshList();
	}

	private void refreshList()
	{
		diagrams = fRevision.all(fbRevision.getClassDiagram());
		Collections.sort(diagrams,cpDiagram);
	}

	@Override
	public void toggled(Class<?> c) throws UtilsLockingException, UtilsConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}

	public void addErDiagram()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbRevision.getClassDiagram()));}
		diagram = efErDiagram.build(null,null,null);
		diagram.setName(efLang.createEmpty(localeCodes));
		diagram.setDescription(efDescription.createEmpty(localeCodes));
	}

	public void selectDiagram() throws UtilsNotFoundException
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

	public void saveDiagram() throws UtilsNotFoundException, UtilsConstraintViolationException, UtilsLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(diagram));}
		if(diagram.getCategory()!=null){diagram.setCategory(fRevision.find(fbRevision.getClassCategory(),diagram.getCategory()));}
		diagram = fRevision.save(diagram);
		refreshList();
		bMessage.growlSuccessSaved();
		reloadDiagram();
	}


	public void rmDiagram() throws UtilsConstraintViolationException, UtilsLockingException, UtilsNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(diagram));}
		fRevision.rm(diagram);
		diagram=null;
		dot = null;
		refreshList();
		bMessage.growlSuccessRemoved();
	}

	public void cancelDiagram()
	{
		diagram=null;
		dot = null;
	}

}