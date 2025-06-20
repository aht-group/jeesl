package org.jeesl.factory.ejb.io.report;

import java.util.UUID;

import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.report.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoReportTemplateFactory<L extends JeeslLang,D extends JeeslDescription,
								
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D,?>,
								CDT extends JeeslStatus<L,D,CDT>,
								CW extends JeeslStatus<L,D,CW>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportTemplateFactory.class);
	
	final Class<TEMPLATE> cTemplate;
	
	private JeeslDbLangUpdater<TEMPLATE,L> dbuLang;
	private JeeslDbDescriptionUpdater<TEMPLATE,D> dbuDescription;
    
	public EjbIoReportTemplateFactory(final Class<L> cL,final Class<D> cD,final Class<TEMPLATE> cTemplate)
	{       
        this.cTemplate = cTemplate;
        
		dbuLang = JeeslDbLangUpdater.factory(cTemplate,cL);
		dbuDescription = JeeslDbDescriptionUpdater.factory(cTemplate,cD);
	}
	    
	public TEMPLATE build()
	{
		TEMPLATE ejb = null;
		try
		{
			ejb = cTemplate.newInstance();
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public TEMPLATE build(Template xTemplate)
	{
		TEMPLATE ejb = null;
		try
		{
			ejb = cTemplate.newInstance();
			ejb.setCode(xTemplate.getCode());
			ejb = update(ejb,xTemplate);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public TEMPLATE update (TEMPLATE eTemplate, Template xTemplate)
	{
		eTemplate.setPosition(xTemplate.getPosition());
		eTemplate.setVisible(xTemplate.isVisible());
		return eTemplate;
	}
	
	public TEMPLATE updateLD(JeeslFacade fUtils, TEMPLATE eTemplate, Template xTemplate) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eTemplate=dbuLang.handle(fUtils, eTemplate, xTemplate.getLangs());
		eTemplate = fUtils.save(eTemplate);
		
		eTemplate=dbuDescription.handle(fUtils, eTemplate, xTemplate.getDescriptions());
		eTemplate = fUtils.save(eTemplate);
		
		return eTemplate;
	}
}