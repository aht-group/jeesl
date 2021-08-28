package org.jeesl.factory.ejb.io.report;

import java.util.UUID;

import org.jeesl.controller.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Template;

public class EjbIoReportTemplateFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslStatus<L,D,RT>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslStatus<L,D,TLS>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
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