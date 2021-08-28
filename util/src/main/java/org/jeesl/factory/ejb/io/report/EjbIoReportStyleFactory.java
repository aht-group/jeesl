package org.jeesl.factory.ejb.io.report;

import java.util.UUID;

import org.jeesl.controller.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
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

import net.sf.ahtutils.xml.report.Style;
import net.sf.ahtutils.xml.symbol.Color;

public class EjbIoReportStyleFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
								ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
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
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportStyleFactory.class);
	
	final Class<STYLE> cStyle;
	
	private JeeslDbLangUpdater<STYLE,L> dbuLang;
	private JeeslDbDescriptionUpdater<STYLE,D> dbuDescription;
    
	public EjbIoReportStyleFactory(final Class<L> cL,final Class<D> cD,final Class<STYLE> cStyle)
	{       
        this.cStyle = cStyle;
        
        dbuLang = JeeslDbLangUpdater.factory(cStyle,cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cStyle,cD);
	}
	    
	public STYLE build()
	{
		STYLE ejb = null;
		try
		{
			ejb = cStyle.newInstance();
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public STYLE build(Style xStyle)
	{
		STYLE ejb = null;
		try
		{
			ejb = cStyle.newInstance();
			ejb.setCode(xStyle.getCode());
			ejb = update(ejb,xStyle);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public STYLE update(STYLE eStyle, Style xStyle)
	{
		eStyle.setPosition(xStyle.getPosition());
		eStyle.setVisible(xStyle.isVisible());
		
		reset(eStyle, true);
		for(Color color : xStyle.getLayout().getColor())
		{
			switch(JeeslReportLayout.Color.valueOf(color.getGroup()))
			{
				case background: eStyle.setColorBackground(color.getValue());break;
				default: break;
			}
		}
		
		return eStyle;
	}
	
	private void reset(STYLE eStyle, boolean colors)
	{
		eStyle.setColorBackground(null);
		eStyle.setColorFont(null);
		eStyle.setColorBorderTop(null);
		eStyle.setColorBorderLeft(null);
		eStyle.setColorBorderRight(null);
		eStyle.setColorBorderBottom(null);
		
	}
	
	public STYLE updateLD(JeeslFacade fUtils, STYLE eStyle, Style xStyle) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eStyle=dbuLang.handle(fUtils, eStyle, xStyle.getLangs());
		eStyle = fUtils.save(eStyle);
		
		eStyle=dbuDescription.handle(fUtils, eStyle, xStyle.getDescriptions());
		eStyle = fUtils.save(eStyle);
		
		return eStyle;
	}
}