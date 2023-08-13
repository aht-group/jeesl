package org.jeesl.util.db.updater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
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
import org.jeesl.model.xml.system.revision.Attribute;
import org.jeesl.model.xml.system.revision.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Lang;
import net.sf.exlp.util.xml.JaxbUtil;

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
	
	private final EjbLangFactory<L> efLang;
	private final JeeslDbLangUpdater<RA,L> dbuLang;

	
	public JeeslDbEntityAttributeUpdater(IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision,
									JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,?> fRevision)
	{
		this.fbRevision=fbRevision;
		this.fRevision=fRevision;
		
		efLang = EjbLangFactory.instance(fbRevision.getClassL());
		dbuLang = JeeslDbLangUpdater.factory(fbRevision.getClassAttribute(),fbRevision.getClassL());
	}
	
	public void updateAttributes2(RE entity, List<LOC> locales, Entity xml) throws JeeslConstraintViolationException
	{
		updateAttributes(entity,EjbCodeFactory.toListCode(locales),xml);
	}
	public void updateAttributes(RE entity, List<String> localeCodes, Entity xml) throws JeeslConstraintViolationException
	{
		logger.info("Creating/Updating Attributes of "+entity.toString()+ entity.getCode() +" for "+localeCodes.toString());
		JaxbUtil.trace(xml);

		// Init the lazy loading of EJB properties
		entity = fRevision.load(fbRevision.getClassEntity(), entity);
		
		// Preparing Maps for storing the XML and EJB Attributes Objects referenced by their code
		Map<String,Attribute> xmlAttributes				= new HashMap<>();
		Map<String,RA> ejbAttributes	= new HashMap<>();


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

			RAT type = fRevision.all(fbRevision.getClassAttributeType(),1).get(0);
			//Needs to be handled, check if type is part of the XML repsonse
			
			// See if an Attribute is available in the EJB entity already and load it
			// Otherwise, use a new created instance
			RA ejbAttribute = fbRevision.ejbAttribute().build(type,xmlAttribute);
			if (ejbAttributes.containsKey(xmlCode))
			{
				ejbAttribute = ejbAttributes.get(xmlCode);
			}

			// Iterate through all language codes and update/add the translations
			for (String locale : localeCodes)
			{
				for (Lang lang : xmlAttribute.getLangs().getLang())
				{
					if (lang.getKey().equals(locale))
					{	
						if (ejbAttribute.getName()==null) 
						{
							logger.info("No Name Matrix present for " +ejbAttribute.getCode() +". Creating a new one");
							ejbAttribute.setName(new HashMap<>());
						}
						if (ejbAttribute.getName().containsKey(locale))
						{
							ejbAttribute.getName().get(locale).setLang(lang.getTranslation());
						} else
						{
							ejbAttribute.getName().put(locale,efLang.createLang(lang));
						}
					}
				}
			}
			try
			{
				ejbAttribute.setEntity(entity);
				efLang.persistMissingLangsForCode(fRevision, localeCodes, ejbAttribute);
				dbuLang.handle(fRevision, ejbAttribute, localeCodes);
				fRevision.save(fbRevision.getClassEntity(), entity, ejbAttribute); 
				fRevision.save(entity);
				
				logger.info("EJB Attribute: " +ejbAttribute.toString() +" has been updated in database");
			} catch(Exception e)
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