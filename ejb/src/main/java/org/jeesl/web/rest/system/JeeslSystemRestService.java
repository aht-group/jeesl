package org.jeesl.web.rest.system;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.api.facade.io.JeeslIoLabelFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.xml.jeesl.XmlContainerFactory;
import org.jeesl.factory.xml.system.io.revision.XmlEntityFactory;
import org.jeesl.factory.xml.system.symbol.XmlGraphicFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslAbstractStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslMcsStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.query.xml.SymbolQuery;
import org.jeesl.util.query.xml.system.io.XmlRevisionQuery;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class JeeslSystemRestService <L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,G>,
								S extends EjbWithId,
								G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
								GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>,
								RC extends JeeslRevisionCategory<L,D,RC,?>,
								REM extends JeeslRevisionEntityMapping<?,?,?>,
								RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
								RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
								RER extends JeeslStatus<L,D,RER>,
								RAT extends JeeslStatus<L,D,RAT>,
								ERD extends JeeslRevisionDiagram<L,D,RC>>
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslSystemRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(JeeslSystemRestService.class);

	protected final IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,RE,?,RA,RER,RAT,ERD,?> fbRevision;

	private final JeeslIoGraphicFacade<S,G,GT,GC,GS> fGraphic;
	private final JeeslIoLabelFacade<L,D,RC,?,?,?,?,RE,?,RA,ERD,?> fRevision;

	private final XmlGraphicFactory<L,D,G,GT,GC,GS> xfGraphic;
	private final XmlEntityFactory<L,D,RC,REM,RE,RA,RER,RAT,ERD> xfEntity;

	private JeeslSystemRestService(IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,RE,?,RA,RER,RAT,ERD,?> fbRevision,
							JeeslIoGraphicFacade<S,G,GT,GC,GS> fGraphic,
							JeeslIoLabelFacade<L,D,RC,?,?,?,?,RE,?,RA,ERD,?> fRevision)
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
						G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<L,D,GT,G>,
						GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<L,D,GS,G>,
						RC extends JeeslRevisionCategory<L,D,RC,?>,
						REM extends JeeslRevisionEntityMapping<?,?,?>,
						RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
						RER extends JeeslStatus<L,D,RER>,
						RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
						RAT extends JeeslStatus<L,D,RAT>,
						ERD extends JeeslRevisionDiagram<L,D,RC>>
	JeeslSystemRestService<L,D,R,S,G,GT,GC,GS,RC,REM,RE,RA,RER,RAT,ERD>
		factory(IoRevisionFactoryBuilder<L,D,RC,?,?,?,?,RE,?,RA,RER,RAT,ERD,?> fbRevision,
				JeeslIoGraphicFacade<S,G,GT,GC,GS> fGraphic,
				JeeslIoLabelFacade<L,D,RC,?,?,?,?,RE,?,RA,ERD,?> fRevision)
	{
		return new JeeslSystemRestService<L,D,R,S,G,GT,GC,GS,RC,REM,RE,RA,RER,RAT,ERD>(fbRevision,fGraphic,fRevision);
	}


	
	@Override public org.jeesl.model.xml.xsd.Container exportStatus(String code) throws UtilsConfigurationException
	{
		return gExportStatus(code);
	}
	@SuppressWarnings("unchecked")
	public <X extends JeeslStatus<L,D,X>> org.jeesl.model.xml.xsd.Container gExportStatus(String code) throws UtilsConfigurationException
	{
		try
		{
			Class<X> x = (Class<X>)Class.forName(code).asSubclass(JeeslStatus.class);
			logger.info(x.getName());
			logger.info("FG: "+(fGraphic!=null));
			org.jeesl.model.xml.xsd.Container xml = xfContainer.build(fGraphic.allOrderedPosition(x));

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
	
	@Override
	public Container updateTranslation(String code, Container xml) throws UtilsConfigurationException
	{
		logger.info("updateTranslation");
		return exportStatus(code);
	}

	@SuppressWarnings("unchecked")
	public <Y extends JeeslMcsStatus<L,D,R,Y,G>, X extends JeeslStatus<L,D,X>, RREF extends EjbWithId> org.jeesl.model.xml.xsd.Container exportMcsStatus(R realm, RREF rref, String code) throws UtilsConfigurationException
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

			org.jeesl.model.xml.xsd.Container xContainer = XmlContainerFactory.build();
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
	
	
	public static Class<?> classForInterface(String code, List<String> packages) throws UtilsConfigurationException
	{
		if(code.equals(JeeslAbstractStatus.class.getName()))
		{
			String cios = "org.jeesl.model.ejb.io.locale.IoStatus";
			try
			{
				Class<?> c = Class.forName(cios);
				return c;
			}
			catch (ClassNotFoundException e) {throw new UtilsConfigurationException("Special Handling for  "+cios+" failed");}
		}
		else
		{
			List<String> searchInPackages = new ArrayList<>();
			searchInPackages.addAll(packages);
			searchInPackages.add("org.jeesl.jsf.jx.util");
			
			
			logger.info("Searching class for provided interface: "+code);
			try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages(searchInPackages.toArray(new String[0])).scan())
			{
				ClassInfoList list = scanResult.getClassesImplementing(code);
				
				if(list.getNames().isEmpty()) {throw new UtilsConfigurationException("No implementing class found for "+code+" using "+ClassGraph.class.getSimpleName());}
				else
				{
					Class<?> c = null;
					if(list.getNames().size()>1)
					{
						logger.info("Multiple Results:");
						for(String s : list.getNames())
						{
							logger.info("\t"+list.getNames().indexOf(s)+" "+s.toString());
						}
					}
					
					for(String fqcn : list.getNames())
					{
						try
						{
							if(list.getNames().size()>1) {logger.info("Trying "+list.getNames().indexOf(fqcn)+": "+fqcn);}
							c = Class.forName(fqcn);
							return c;
						}
						catch (ClassNotFoundException e) {}
					}
					throw new UtilsConfigurationException("Even for multiple candidats no class with "+code+" found using "+ClassGraph.class.getSimpleName());
				}
			}
		}
		
	}
}