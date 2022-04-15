package org.jeesl.util.db.updater;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import net.sf.ahtutils.xml.status.Lang;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.io.revision.data.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.revision.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.system.revision.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JaxbUtil;
import org.jeesl.model.xml.system.revision.Attribute;
import org.jeesl.util.ReflectionUtil;

public class JeeslDbEntityAttributeUpdater <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
											ERD extends JeeslRevisionDiagram<L,D,RC>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDbEntityAttributeUpdater.class);
	
	private final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision;
	private final JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision;
	
	public JeeslDbEntityAttributeUpdater(IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision,
									JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision)
	{
		this.fbRevision=fbRevision;
		this.fRevision=fRevision;
	
	}
	
	public void updateAttributes(RE entity, JeeslLocaleProvider<LOC> lp, Entity xml) throws InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException
	{
		logger.info("Creating/Updating Attributes of "+entity.toString()+ entity.getCode() +" for "+lp.getLocaleCodes());
		JaxbUtil.info(xml);

		// Init the lazy loading of EJB properties
		Class<RE> target = (Class<RE>) entity.getClass();
		entity = fRevision.load(target, entity);
		
		// Preparing Maps for storing the XML and EJB Attributes Objects referenced by their code
		Map<String, Attribute> xmlAttributes				= new HashMap<>();
		Map<String, JeeslRevisionAttribute> ejbAttributes	= new HashMap<>();

		// Load data into the maps
		for (Attribute attribute : xml.getAttribute())
		{
			xmlAttributes.put(attribute.getCode(), attribute);
		}
		for (JeeslRevisionAttribute ra : entity.getAttributes())
		{
			ejbAttributes.put(ra.getCode(), ra);
		}

		// Process all data coming from the XML Attributes for updating or creating properties of EJB object
		for (String xmlCode : xmlAttributes.keySet())
		{
			Attribute xmlAttribute = xmlAttributes.get(xmlCode);

			// See if an Attribute is available in the EJB entity already and load it
			// Otherwise, use a new created instance
			JeeslRevisionAttribute ejbAttribute = entity.getAttributes().get(0).getClass().newInstance();
			if (ejbAttributes.containsKey(xmlCode))
			{
				ejbAttribute = ejbAttributes.get(xmlCode);
			}

			// Iterate through all language codes and update/add the translations
			for (String locale : lp.getLocaleCodes())
			{
				for (Lang lang : xmlAttribute.getLangs().getLang())
				{
					if (lang.getKey().equals(locale))
					{	
						JeeslLang ejbLang = (JeeslLang) ReflectionUtil.getTypeOfMapValues(ejbAttribute, "name").newInstance();
						ejbLang.setLang(lang.getTranslation());
						ejbLang.setLkey(locale);
						ejbAttribute.getName().put(locale, ejbLang);
					}
				}
				
			}

			// Set or Update all other properties of XML representation
			ejbAttribute.setPosition(xmlAttribute.getPosition());
			ejbAttribute.setXpath(xmlAttribute.getXpath());
			ejbAttribute.setShowWeb(xmlAttribute.isWeb());
			ejbAttribute.setShowPrint(xmlAttribute.isPrint());
			ejbAttribute.setShowEnclosure(xmlAttribute.isEnclosure());
			ejbAttribute.setUi(xmlAttribute.isUi());
			ejbAttribute.setBean(xmlAttribute.isBean());
			ejbAttribute.setConstruction(xmlAttribute.isConstruction());
			ejbAttribute.setShowName(xmlAttribute.isName());

			logger.info("EJB Attribute: " +ejbAttribute.toString());
		}

	}

	public String findEnLang(Attribute xmlAttribute)
	{
		for (Lang lang : xmlAttribute.getLangs().getLang())
		{
			if (lang.getKey().equals("en"))
			{
				return lang.getTranslation();
			}
		}
		return "no en translation available";
	}
}