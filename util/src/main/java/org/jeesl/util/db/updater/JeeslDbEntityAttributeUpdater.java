package org.jeesl.util.db.updater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttributeType;
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
import org.jeesl.model.xml.io.label.Attribute;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JeeslDbEntityAttributeUpdater <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbEntityAttributeUpdater.class);
	
	private final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision;
	private final JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,?> fRevision;
	
	private final EjbCodeCache<RAT> cacheType;
	
	private final EjbLangFactory<L> efLang;
	private final EjbDescriptionFactory<D> efDescription;
	private final JeeslDbLangUpdater<RA,L> dbuLang;
	
	

	
	public JeeslDbEntityAttributeUpdater(IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision,
									JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,?> fRevision)
	{
		this.fbRevision=fbRevision;
		this.fRevision=fRevision;
		
		cacheType = EjbCodeCache.instance(fbRevision.getClassAttributeType()).facade(fRevision);
		
		efLang = EjbLangFactory.instance(fbRevision.getClassL());
		efDescription  = EjbDescriptionFactory.instance(fbRevision.getClassD());
		dbuLang = JeeslDbLangUpdater.factory(fbRevision.getClassAttribute(),fbRevision.getClassL());
	}
	
	public void updateAttributes2(RE entity, List<LOC> locales, Entity xml) throws JeeslConstraintViolationException
	{
		updateAttributes(entity,EjbCodeFactory.toListCode(locales),xml);
	}
	public void updateAttributes(RE entity, List<String> eLocales, Entity xml) throws JeeslConstraintViolationException
	{
		logger.info("Creating/Updating Attributes of "+entity.toString()+ entity.getCode() +" for "+eLocales.toString());

		// Init the lazy loading of EJB properties
		entity = fRevision.load(fbRevision.getClassEntity(), entity);
		
		// Preparing Maps for storing the XML and EJB Attributes Objects referenced by their code
		Map<String,Attribute> xmlAttributes = new HashMap<>();
		Map<String,RA> ejbAttributes = new HashMap<>();

		// Load data into the maps
		for (Attribute attribute : xml.getAttribute())
		{
			xmlAttributes.put(attribute.getCode(), attribute);
		}
		for (RA ra : entity.getAttributes())
		{
			ejbAttributes.put(ra.getCode(), ra);
		}

		
		// Process all data coming from the XML Attributes for updating or creating properties of EJB object
		for (String xmlCode : xmlAttributes.keySet())
		{
			Attribute xmlAttribute = xmlAttributes.get(xmlCode);

			RAT type = cacheType.ejb(JeeslRevisionAttributeType.Code.text); 
			
			// See if an Attribute is available in the EJB entity already and load it
			// Otherwise, use a new created instance
			RA ejbAttribute = null;
			if (ejbAttributes.containsKey(xmlCode))
			{
				ejbAttribute = ejbAttributes.get(xmlCode);
			}
			else
			{
				ejbAttribute = fbRevision.ejbAttribute().build(type,xmlAttribute);
			}
			
			ejbAttribute.setEntity(entity);
			ejbAttribute.setType(cacheType.ejb(xmlAttribute.getType().getCode()));
			
			if(Objects.isNull(fRevision)) {logger.warn("NULL: fRevision");}
			if(Objects.isNull(eLocales)) {logger.warn("NULL: eLocales");}
			if(Objects.isNull(ejbAttribute)) {logger.warn("NULL: ejbAttribute");}
			
			efLang.persistMissingLangsForCode(fRevision, eLocales, ejbAttribute);
			efLang.update(ejbAttribute,xmlAttribute.getLangs());
			
			efDescription.persistMissingLangsForCode(fRevision, eLocales, ejbAttribute);
			efDescription.update(ejbAttribute,xmlAttribute.getDescriptions());
			
			try
			{
				
				efLang.persistMissingLangsForCode(fRevision, eLocales, ejbAttribute);
				dbuLang.handle(fRevision, ejbAttribute, eLocales);
				fRevision.save(fbRevision.getClassEntity(), entity, ejbAttribute); 
				fRevision.save(entity);
				
				logger.info("EJB Attribute: " +ejbAttribute.toString() +" has been updated in database");
			}
			catch(Exception e)
			{
				logger.error("EJB Attribute: " +ejbAttribute.toString() +" has not been updated in database. Reason: " +e.getMessage());
			}
		}

	}

	//Seems not required here ...
//	public String findEnLang(Attribute xmlAttribute)
//	{
//		for (Lang lang : xmlAttribute.getLangs().getLang())
//		{
//			if (lang.getKey().equals("en"))
//			{
//				return lang.getTranslation();
//			}
//		}
//		return "no en translation available";
//	}
}