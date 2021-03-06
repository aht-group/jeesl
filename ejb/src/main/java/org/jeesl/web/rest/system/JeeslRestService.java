package org.jeesl.web.rest.system;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.xml.jeesl.XmlContainerFactory;
import org.jeesl.factory.xml.system.io.revision.XmlEntityFactory;
import org.jeesl.factory.xml.system.symbol.XmlGraphicFactory;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.revision.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicFigure;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslMcsStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.rest.JeeslExportRest;
import org.jeesl.model.xml.system.revision.Entity;
import org.jeesl.util.query.xml.SymbolQuery;
import org.jeesl.util.query.xml.system.io.XmlRevisionQuery;
import org.jeesl.web.rest.AbstractJeeslRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Status;

public class JeeslRestService <L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,G>,
								S extends EjbWithId,
								G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
								F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>,
								RC extends JeeslRevisionCategory<L,D,RC,?>,
								REM extends JeeslRevisionEntityMapping<?,?,?>,
								RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
								RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
								RER extends JeeslStatus<L,D,RER>,
								RAT extends JeeslStatus<L,D,RAT>,
								ERD extends JeeslRevisionDiagram<L,D,RC>>
					extends AbstractJeeslRestService<L,D>
					implements JeeslExportRest<L,D,R,G>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslRestService.class);

	protected final IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,RE,?,RA,RER,RAT,ERD,?> fbRevision;

	private final JeeslGraphicFacade<L,D,S,G,GT,F,FS> fGraphic;
	private final JeeslIoRevisionFacade<L,D,RC,?,?,?,?,RE,?,RA,?,RAT,ERD,?> fRevision;

	private final XmlGraphicFactory<L,D,G,GT,F,FS> xfGraphic;
	private final XmlEntityFactory<L,D,RC,REM,RE,RA,RER,RAT,ERD> xfEntity;

	private JeeslRestService(IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,RE,?,RA,RER,RAT,ERD,?> fbRevision,
							JeeslGraphicFacade<L,D,S,G,GT,F,FS> fGraphic,
							JeeslIoRevisionFacade<L,D,RC,?,?,?,?,RE,?,RA,?,RAT,ERD,?> fRevision)
	{
		super(fGraphic,fbRevision.getClassL(),fbRevision.getClassD());
		this.fbRevision=fbRevision;
		this.fRevision=fRevision;
		this.fGraphic=fGraphic;
		xfGraphic = new XmlGraphicFactory<>(SymbolQuery.get(SymbolQuery.Key.GraphicExport));
		xfEntity = new XmlEntityFactory<>(XmlRevisionQuery.get(XmlRevisionQuery.Key.xEntity));
	}

	public static <L extends JeeslLang,D extends JeeslDescription,
						R extends JeeslTenantRealm<L,D,R,G>,
						S extends EjbWithId,
						G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
						F extends JeeslGraphicFigure<L,D,G,GT,F,FS>, FS extends JeeslStatus<L,D,FS>,
						RC extends JeeslRevisionCategory<L,D,RC,?>,
						REM extends JeeslRevisionEntityMapping<?,?,?>,
						RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
						RER extends JeeslStatus<L,D,RER>,
						RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
						RAT extends JeeslStatus<L,D,RAT>,
						ERD extends JeeslRevisionDiagram<L,D,RC>>
	JeeslRestService<L,D,R,S,G,GT,F,FS,RC,REM,RE,RA,RER,RAT,ERD>
		factory(IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,RE,?,RA,RER,RAT,ERD,?> fbRevision,
				JeeslGraphicFacade<L,D,S,G,GT,F,FS> fGraphic,
				JeeslIoRevisionFacade<L,D,RC,?,?,?,?,RE,?,RA,?,RAT,ERD,?> fRevision)
	{
		return new JeeslRestService<L,D,R,S,G,GT,F,FS,RC,REM,RE,RA,RER,RAT,ERD>(fbRevision,fGraphic,fRevision);
	}


	@SuppressWarnings("unchecked")
	@Override public <X extends JeeslStatus<L,D,X>> org.jeesl.model.xml.jeesl.Container exportStatus(String code) throws UtilsConfigurationException
	{
		try
		{
			Class<X> x = (Class<X>)Class.forName(code).asSubclass(JeeslStatus.class);
			org.jeesl.model.xml.jeesl.Container xml = xfContainer.build(fGraphic.allOrderedPosition(x));

			if(EjbWithGraphic.class.isAssignableFrom(x))
			{
				for(Status xStatus : xml.getStatus())
				{
					X eStatus = fGraphic.fByCode(x,xStatus.getCode());
					try
					{
						G eGraphic = fGraphic.fGraphicForStatus(eStatus.getId());
						xStatus.setGraphic(xfGraphic.build(eGraphic));
					}
					catch (JeeslNotFoundException e) {}
				}
			}
			return xml;
		}
		catch (ClassNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (JeeslNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
	}

	@SuppressWarnings("unchecked")
	public <Y extends JeeslMcsStatus<L,D,R,Y,G>, X extends JeeslStatus<L,D,X>, RREF extends EjbWithId> org.jeesl.model.xml.jeesl.Container exportMcsStatus(R realm, RREF rref, String code) throws UtilsConfigurationException
	{
		try
		{
			Class<Y> cMcs = (Class<Y>)Class.forName(code).asSubclass(JeeslMcsStatus.class);
//			Class<X> cStatus = (Class<X>)Class.forName(code).asSubclass(JeeslStatus.class);
			List<Y> list = fGraphic.all(cMcs,realm,rref);
			List<X> list2 = new ArrayList<X>();
			for(Y y : list)
			{
				list2.add((X)y);
			}

			org.jeesl.model.xml.jeesl.Container xContainer = XmlContainerFactory.build();
			xContainer = xfContainer.build(list2);

			if(EjbWithGraphic.class.isAssignableFrom(cMcs))
			{
				for(Status xml : xContainer.getStatus())
				{
					for(X ejb : list2)
					{
						if(xml.getCode().equals(ejb.getCode())) {
							try
							{
								G eGraphic = fGraphic.fGraphic(cMcs,ejb.getId());
								xml.setGraphic(xfGraphic.build(eGraphic));
							}
							catch (JeeslNotFoundException e) {e.printStackTrace();}
						}
					}
				}
			}
			return xContainer;
		}
		catch (ClassNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
	}

	@Override public Entity exportRevisionEntity(String code) throws UtilsConfigurationException
	{
		try
		{
			RE entity = fGraphic.fByCode(fbRevision.getClassEntity(), code);
			entity = fRevision.load(fbRevision.getClassEntity(), entity);
			return xfEntity.build(entity);
		}
		catch (JeeslNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
}