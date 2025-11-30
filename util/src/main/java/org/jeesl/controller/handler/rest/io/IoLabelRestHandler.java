package org.jeesl.controller.handler.rest.io;

import java.util.Collections;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoLabelFacade;
import org.jeesl.api.rest.i.io.JeeslIoLabelRestInterface;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.xml.system.io.revision.XmlDiagramFactory;
import org.jeesl.factory.xml.system.io.revision.XmlEntityFactory;
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
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.util.comparator.ejb.PositionParentComparator;
import org.jeesl.util.query.xml.system.io.XmlRevisionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoLabelRestHandler <L extends JeeslLang, D extends JeeslDescription,
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
								ERD extends JeeslRevisionDiagram<L,D,RC>
>
					implements JeeslIoLabelRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(IoLabelRestHandler.class);

	private final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision;
	private JeeslIoLabelFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,?> fLabel;

	private final XmlEntityFactory<L,D,RC,REM,RE,RA,RER,RAT,ERD> xfEntity;
	private final XmlDiagramFactory<L,D,RC,ERD> xfDiagram;

	public IoLabelRestHandler(IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision,
								JeeslIoLabelFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,?> fRevision)
	{
		this.fbRevision=fbRevision;
		this.fLabel=fRevision;

		xfEntity = new XmlEntityFactory<>(XmlRevisionQuery.get(XmlRevisionQuery.Key.xEntity));
		xfDiagram = fbRevision.xmlDiagram(XmlRevisionQuery.get(XmlRevisionQuery.Key.xDiagram));
	}

	@Override public Entities exportSystemRevisionEntities()
	{
		Entities xml = new Entities();

		List<RE> list = fLabel.all(fbRevision.getClassEntity());
		Collections.sort(list, new PositionParentComparator<RE>(fbRevision.getClassEntity()));

		for(RE re : list)
		{
			re = fLabel.load(fbRevision.getClassEntity(), re);
			xml.getEntity().add(xfEntity.build(re));
		}

		return xml;
	}
}