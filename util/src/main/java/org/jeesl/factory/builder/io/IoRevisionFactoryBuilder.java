package org.jeesl.factory.builder.io;

import java.util.Comparator;

import org.jeesl.controller.util.comparator.ejb.io.label.LabelEntityComparator;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.io.label.EjbLabelAttributeFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelDiagramFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelEntityFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelMappingEntityFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelMappingViewFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelScopeFactory;
import org.jeesl.factory.ejb.io.label.EjbLabelViewFactory;
import org.jeesl.factory.xml.system.io.revision.XmlDiagramFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionMissingLabel;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.db.query.QueryRevision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoRevisionFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
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
									ERD extends JeeslRevisionDiagram<L,D,RC>,
									RML extends JeeslRevisionMissingLabel
>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoRevisionFactoryBuilder.class);

	private final Class<RC> cCategory; public Class<RC> getClassCategory(){return cCategory;}
	private final Class<RV> cView; public Class<RV> getClassView(){return cView;}
	private final Class<RVM> cViewMapping; public Class<RVM> getClassViewMapping(){return cViewMapping;}
	private final Class<RS> cScope; public Class<RS> getClassScope(){return cScope;}
	private final Class<RST> cScopeType; public Class<RST> getClassScopeType(){return cScopeType;}
	private final Class<RE> cEntity; public Class<RE> getClassEntity(){return cEntity;}
	private final Class<REM> cMappingEntity; public Class<REM> getClassEntityMapping(){return cMappingEntity;}
	private final Class<RA> cAttribute; public Class<RA> getClassAttribute(){return cAttribute;}
	private final Class<RER> cRelation; public Class<RER> getClassRelation(){return cRelation;}
	private final Class<RAT> cRat; public Class<RAT> getClassAttributeType(){return cRat;}
	private final Class<ERD> cErd; public Class<ERD> getClassDiagram(){return cErd;}
	private final Class<RML> cMr; public Class<RML> getClassMissingRevision(){return cMr;}

	public IoRevisionFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<RC> cCategory,
									final Class<RV> cView,
									final Class<RVM> cViewMapping,
									final Class<RS> cScope,
									final Class<RST> cScopeType,
									final Class<RE> cEntity,
									final Class<REM> cMappingEntity,
									final Class<RA> cAttribute,
									final Class<RER> cRelation,
									final Class<RAT> cRat,
									final Class<ERD> cErd,
									final Class<RML> cMr)
	{
		super(cL,cD);
		this.cCategory=cCategory;
		this.cView=cView;
		this.cViewMapping=cViewMapping;
		this.cScope=cScope;
		this.cScopeType=cScopeType;
		this.cEntity=cEntity;
		this.cMappingEntity=cMappingEntity;
		this.cAttribute=cAttribute;
		this.cRelation=cRelation;
		this.cRat=cRat;
		this.cErd=cErd;
		this.cMr = cMr;
	}

	public EjbLabelViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> ejbView()
	{
		return new EjbLabelViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>(cView);
	}

	public EjbLabelMappingViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> ejbMappingView()
	{
		return new EjbLabelMappingViewFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>(cViewMapping);
	}

	public EjbLabelScopeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> ejbScope()
	{
		return new EjbLabelScopeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>(cScope);
	}

	public EjbLabelEntityFactory<L,D,RC,RV,RVM,RE,REM,RA,RER,RAT,ERD> ejbEntity() {return new EjbLabelEntityFactory<>(cL,cD,cEntity);}

	public EjbLabelMappingEntityFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> ejbMappingEntity()
	{
		return new EjbLabelMappingEntityFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>(cMappingEntity);
	}

	public EjbLabelAttributeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> ejbAttribute()
	{
		return new EjbLabelAttributeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>(cAttribute);
	}

	public EjbLabelDiagramFactory<L,D,RC,ERD> ejbDiagram(){return new EjbLabelDiagramFactory<>(cErd);}

	public XmlDiagramFactory<L,D,RC,ERD> xmlDiagram(QueryRevision q){return new XmlDiagramFactory<>(q);}

	public Comparator<RE> cpEjbEntity(LabelEntityComparator.Type type) {return (new LabelEntityComparator<RC,RE>()).factory(type);}
}