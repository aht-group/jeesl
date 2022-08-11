package org.jeesl.web.rest.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.api.rest.system.io.revision.JeeslRevisionRestExport;
import org.jeesl.api.rest.system.io.revision.JeeslRevisionRestImport;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.io.label.EjbLabelAttributeFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelEntityFactory;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.factory.xml.jeesl.XmlContainerFactory;
import org.jeesl.factory.xml.system.io.revision.XmlDiagramFactory;
import org.jeesl.factory.xml.system.io.revision.XmlDiagramsFactory;
import org.jeesl.factory.xml.system.io.revision.XmlEntityFactory;
import org.jeesl.factory.xml.system.io.sync.XmlDataUpdateFactory;
import org.jeesl.factory.xml.system.io.sync.XmlResultFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.system.revision.Attribute;
import org.jeesl.model.xml.system.revision.Diagrams;
import org.jeesl.model.xml.system.revision.Entities;
import org.jeesl.model.xml.system.revision.Entity;
import org.jeesl.util.comparator.ejb.PositionParentComparator;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.jeesl.util.query.xml.XmlStatusQuery;
import org.jeesl.util.query.xml.system.io.XmlRevisionQuery;
import org.metachart.factory.xml.graph.XmlDotFactory;
import org.metachart.factory.xml.graph.XmlGraphFactory;
import org.metachart.xml.graph.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.sync.DataUpdate;

public class RevisionRestService <L extends JeeslLang,D extends JeeslDescription,
								RC extends JeeslRevisionCategory<L,D,RC,?>,
								RV extends JeeslRevisionView<L,D,RVM>,
								RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
								RS extends JeeslRevisionScope<L,D,RC,RA>,
								RST extends JeeslStatus<L,D,RST>,
								RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
								REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
								RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
								RER extends JeeslStatus<L,D,RER>,
								RAT extends JeeslStatus<L,D,RAT>,
								ERD extends JeeslRevisionDiagram<L,D,RC>
>
					implements JeeslRevisionRestExport,JeeslRevisionRestImport
{
	final static Logger logger = LoggerFactory.getLogger(RevisionRestService.class);

	private final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision;
	private JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision;

	private final XmlContainerFactory xfContainer;
	private final XmlEntityFactory<L,D,RC,REM,RE,RA,RER,RAT,ERD> xfEntity;
	private final XmlDiagramFactory<L,D,RC,ERD> xfDiagram;

	private EjbLangFactory<L> efLang;
	private EjbDescriptionFactory<D> efDescription;
	private EjbLabelEntityFactory<L,D,RC,RV,RVM,RE,REM,RA,RER,RAT,ERD> efEntity;
	private EjbLabelAttributeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> efAttribute;

	public RevisionRestService(IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision,
								JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision)
	{
		this.fbRevision=fbRevision;
		this.fRevision=fRevision;

		xfContainer = new XmlContainerFactory(XmlStatusQuery.get(XmlStatusQuery.Key.StatusExport).getStatus());
		xfEntity = new XmlEntityFactory<>(XmlRevisionQuery.get(XmlRevisionQuery.Key.xEntity));

		efLang = EjbLangFactory.instance(fbRevision.getClassL());
		efDescription = EjbDescriptionFactory.factory(fbRevision.getClassD());
		efEntity = fbRevision.ejbEntity();
		efAttribute = EjbLabelAttributeFactory.factory(fbRevision.getClassAttribute());
		xfDiagram = fbRevision.xmlDiagram(XmlRevisionQuery.get(XmlRevisionQuery.Key.xDiagram));
	}

	@Override public Container exportSystemIoRevisionAttributeTypes() {return xfContainer.build(fRevision.allOrderedPosition(fbRevision.getClassAttributeType()));}
	@Override public Container exportSystemIoRevisionScopeTypes() {return xfContainer.build(fRevision.allOrderedPosition(fbRevision.getClassScopeType()));}
	@Override public Container exportSystemRevisionCategories(){return xfContainer.build(fRevision.allOrderedPosition(fbRevision.getClassCategory()));}
	@Override public Container exportSystemRevisionRelationType() {return xfContainer.build(fRevision.allOrderedPosition(fbRevision.getClassRelation()));}

	@Override public Entities exportSystemRevisionEntities()
	{
		Entities xml = new Entities();

		List<RE> list = fRevision.all(fbRevision.getClassEntity());
		Collections.sort(list, new PositionParentComparator<RE>(fbRevision.getClassEntity()));

		for(RE re : list)
		{
			re = fRevision.load(fbRevision.getClassEntity(), re);
			xml.getEntity().add(xfEntity.build(re));
		}

		return xml;
	}

	@Override public Diagrams exportSystemRevisionDiagrams()
	{
		Diagrams xml = XmlDiagramsFactory.build();
		List<ERD> list = fRevision.all(fbRevision.getClassDiagram());
		Collections.sort(list, new PositionParentComparator<ERD>(fbRevision.getClassDiagram()));
		for(ERD diagram : list)
		{
			xml.getDiagram().add(xfDiagram.build(diagram));
		}
		return xml;
	}

	@Override public Graph exportSystemRevisionGraph(String code)
	{
		Graph g = XmlGraphFactory.build(code);
		try
		{
			ERD diagram = fRevision.fByCode(fbRevision.getClassDiagram(),code);
			g.setDot(XmlDotFactory.build(diagram.getDotGraph()));
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}

		return g;
	}

	@Override public DataUpdate importSystemIoRevisionAttributeTypes(Container categories){return importStatus(fbRevision.getClassAttributeType(),fbRevision.getClassL(),fbRevision.getClassD(),categories,null);}
	@Override public DataUpdate importSystemIoRevisionScopeTypes(Container categories){return importStatus(fbRevision.getClassScopeType(),fbRevision.getClassL(),fbRevision.getClassD(),categories,null);}
	@Override public DataUpdate importSystemRevisionCategories(org.jeesl.model.xml.jeesl.Container categories){return importStatus(fbRevision.getClassCategory(),fbRevision.getClassL(),fbRevision.getClassD(),categories,null);}

	@Override public DataUpdate importSystemRevisionEntities(Entities entities)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(fbRevision.getClassEntity().getName(),"DB Import"));

		Set<RE> inDbRevisionEntity = new HashSet<RE>(fRevision.all(fbRevision.getClassEntity()));
		List<L> dbDeleteL = new ArrayList<L>();
		List<D> dbDeleteD = new ArrayList<D>();

		if(logger.isInfoEnabled())
		{
			logger.info("Importing Revision Entites");
			logger.info("\tAlread in DB: "+fbRevision.getClassEntity().getSimpleName()+" "+inDbRevisionEntity.size());
			logger.info("\tUpdatinf from XML: "+Entity.class.getSimpleName()+" "+entities.getEntity().size());
		}

		for(Entity xml : entities.getEntity())
		{
			try
			{
				iuRevisionEntity(inDbRevisionEntity,xml,dbDeleteL,dbDeleteD);
				dut.success();
			}
			catch (JeeslNotFoundException e) {dut.fail(e, true);}
			catch (JeeslConstraintViolationException e) {dut.fail(e, true);}
			catch (JeeslLockingException e) {dut.fail(e, true);}
		}

		if(logger.isDebugEnabled())
		{
			logger.debug("Will delete in DB");
			logger.debug("\t"+fbRevision.getClassEntity().getSimpleName()+" "+inDbRevisionEntity.size());
			logger.debug("\t"+fbRevision.getClassL().getSimpleName()+" "+dbDeleteL.size());
			logger.debug("\t"+fbRevision.getClassD().getSimpleName()+" "+dbDeleteD.size());
		}
		try
		{
			fRevision.rm(dbDeleteL);
			fRevision.rm(dbDeleteD);
		}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		return dut.toDataUpdate();
	}

	private void iuRevisionEntity(Set<RE> inDbRevisionEntity, Entity xml, List<L> dbDeleteL, List<D> dbDeleteD) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		RE re;
		try
		{
			re = fRevision.fByCode(fbRevision.getClassEntity(), xml.getCode());
			inDbRevisionEntity.remove(re);
		}
		catch (JeeslNotFoundException e)
		{
			RC category = fRevision.fByCode(fbRevision.getClassCategory(), xml.getCategory().getCode());
			re = efEntity.build(category,xml);
			re = fRevision.persist(re);
		}
		re = fRevision.load(fbRevision.getClassEntity(), re);

		dbDeleteL.addAll(re.getName().values());
		dbDeleteD.addAll(re.getDescription().values());
		re.getName().clear();
		re.getDescription().clear();

		re.setName(efLang.getLangMap(xml.getLangs()));
		re.setDescription(efDescription.create(xml.getDescriptions()));

		efEntity.applyValues(re, xml);

		Set<RA> set = new HashSet<RA>(re.getAttributes());
		for(Attribute xmlAttribute : xml.getAttribute())
		{
			RA  ra = iuRevisionAttribute(re,xmlAttribute,dbDeleteL,dbDeleteD);
			if(set.contains(ra)){set.remove(ra);}
		}
		for(RA ra : new ArrayList<RA>(set))
		{
			fRevision.rm(fbRevision.getClassEntity(),re,ra);
		}
	}

	private RA iuRevisionAttribute(RE ejbRevisionEntity, Attribute xml, List<L> dbDeleteL, List<D> dbDeleteD) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		RA ejbAttribute = null;

		for(RA ra : ejbRevisionEntity.getAttributes())
		{
			logger.debug("****");
			logger.debug("ra.code "+ra.getCode()+" "+ejbRevisionEntity.getCode());
			logger.debug("xml.code "+xml.getCode());

			if(ra.getCode().equals(xml.getCode()))
			{
				ejbAttribute=fRevision.find(fbRevision.getClassAttribute(), ra);
				dbDeleteL.addAll(ejbAttribute.getName().values());
				dbDeleteD.addAll(ejbAttribute.getDescription().values());

				ejbAttribute.getName().clear();
				ejbAttribute.getDescription().clear();
			}
		}

		if(ejbAttribute==null)
		{
			RAT type = fRevision.fByCode(fbRevision.getClassAttributeType(), xml.getType().getCode());
			ejbAttribute = efAttribute.build(type,xml);
			ejbAttribute = fRevision.save(fbRevision.getClassEntity(),ejbRevisionEntity,ejbAttribute);
		}
		ejbAttribute.setName(efLang.getLangMap(xml.getLangs()));
		ejbAttribute.setDescription(efDescription.create(xml.getDescriptions()));
		efAttribute.applyValues(ejbAttribute, xml);

		ejbAttribute = fRevision.save(fbRevision.getClassEntity(),ejbRevisionEntity,ejbAttribute);
		return ejbAttribute;
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <S extends JeeslStatus<L,D,S>, P extends JeeslStatus<L,D,P>> DataUpdate importStatus(Class<S> clStatus, Class<L> clLang, Class<D> clDescription, Aht container, Class<P> clParent)
    {
    	for(Status xml : container.getStatus()){xml.setGroup(clStatus.getSimpleName());}
		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
        asdi.setStatusEjbFactory(EjbStatusFactory.createFactory(clStatus, clLang, clDescription));
        asdi.setFacade(fRevision);
        DataUpdate dataUpdate = asdi.iuStatus(container.getStatus(), clStatus, clLang, clParent);
        asdi.deleteUnusedStatus(clStatus, clLang, clDescription);
        return dataUpdate;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <S extends JeeslStatus<L,D,S>, P extends JeeslStatus<L,D,P>> DataUpdate importStatus(Class<S> clStatus, Class<L> clLang, Class<D> clDescription, Container container, Class<P> clParent)
    {
    	for(Status xml : container.getStatus()){xml.setGroup(clStatus.getSimpleName());}
		JeeslDbStatusUpdater asdi = new JeeslDbStatusUpdater();
        asdi.setStatusEjbFactory(EjbStatusFactory.createFactory(clStatus, clLang, clDescription));
        asdi.setFacade(fRevision);
        DataUpdate dataUpdate = asdi.iuStatus(container.getStatus(), clStatus, clLang, clParent);
        asdi.deleteUnusedStatus(clStatus, clLang, clDescription);
        return dataUpdate;
    }

	@Override
	public DataUpdate importSystemRevisionDiagram(Graph graph)
	{
		try
		{
			ERD diagram = fRevision.fByCode(fbRevision.getClassDiagram(),graph.getCode());
			if(!diagram.isDotManual())
			{

				try
				{
					diagram.setDotGraph(graph.getDot().getValue());
					diagram = fRevision.save(diagram);
					return XmlDataUpdateFactory.build(XmlResultFactory.buildOk());
				}
				catch (JeeslConstraintViolationException | JeeslLockingException e)
				{
					return XmlDataUpdateFactory.build(XmlResultFactory.buildFail());
				}
			}
			else {return XmlDataUpdateFactory.build(XmlResultFactory.buildFail());}
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				RC category = fRevision.all(fbRevision.getClassCategory(),1).get(0);
				ERD diagram = fbRevision.ejbDiagram().build(category,graph.getCode(),graph.getDot().getValue());
				diagram = fRevision.save(diagram);
				return XmlDataUpdateFactory.build(XmlResultFactory.buildOk());
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e1)
			{
				return XmlDataUpdateFactory.build(XmlResultFactory.buildOk());
			}
		}
	}
}