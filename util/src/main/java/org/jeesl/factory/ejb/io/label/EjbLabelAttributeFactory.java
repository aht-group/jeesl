package org.jeesl.factory.ejb.io.label;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.system.revision.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class EjbLabelAttributeFactory<L extends JeeslLang,D extends JeeslDescription,
									RC extends JeeslRevisionCategory<L,D,RC,?>,
									RV extends JeeslRevisionView<L,D,RVM>,
									RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
									RS extends JeeslRevisionScope<L,D,RC,RA>,
									RST extends JeeslStatus<L,D,RST>,
									RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
									REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
									RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends JeeslStatus<L,D,RER>,
									RAT extends JeeslStatus<L,D,RAT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbLabelAttributeFactory.class);
	
	final Class<RA> cAttribute;
    
	public EjbLabelAttributeFactory(final Class<RA> cAttribute)
	{       
        this.cAttribute = cAttribute;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					RC extends JeeslRevisionCategory<L,D,RC,?>,
					RV extends JeeslRevisionView<L,D,RVM>,
					RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
					RS extends JeeslRevisionScope<L,D,RC,RA>,
					RST extends JeeslStatus<L,D,RST>,
					RE extends JeeslRevisionEntity<L,D,RC,REM,RA,?>,
					REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
					RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends JeeslStatus<L,D,RER>,
					RAT extends JeeslStatus<L,D,RAT>>
	EjbLabelAttributeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT> factory(final Class<RA> cAttribute)
	{
		return new EjbLabelAttributeFactory<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT>(cAttribute);
	}
    
	public RA build(RAT type, Attribute xml)
	{
		RA ejb = build(type);
		ejb.setCode(xml.getCode());
		applyValues(ejb,xml);
		
		return ejb;
	}
	
	public void applyValues(RA ejb, Attribute xml)
	{
		if(xml.isSetRemark()){ejb.setDeveloperInfo(xml.getRemark().getValue());}
		else{ejb.setDeveloperInfo(null);}
		
		ejb.setPosition(xml.getPosition());
		ejb.setXpath(xml.getXpath());
		
		ejb.setShowWeb(xml.isWeb());
		ejb.setShowPrint(xml.isPrint());
		ejb.setShowName(xml.isName());
		ejb.setShowEnclosure(xml.isEnclosure());
		ejb.setUi(xml.isUi());
		ejb.setBean(xml.isBean());
		ejb.setConstruction(xml.isSetConstruction() && xml.isConstruction());
	}
	
	public RA build(RAT type)
	{
		RA ejb = null;
		try
		{
			ejb = cAttribute.newInstance();
			ejb.setPosition(1);
			ejb.setType(type);
			
			ejb.setShowWeb(false);
			ejb.setShowPrint(false);
			ejb.setShowEnclosure(false);
			ejb.setShowName(false);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void toReverseAttribute(RE entity, RA attribute)
	{
		logger.info(StringUtil.stars());
		logger.info("Gettings Reverse from "+entity.getCode()+"--"+attribute.getCode());
	}
}