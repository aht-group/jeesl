package org.jeesl.factory.ejb.io.report;

import java.util.UUID;

import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportLayout;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.graphic.Color;
import org.jeesl.model.xml.io.report.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoReportStyleFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
								REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
								IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
								WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
								SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
								GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
								COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,?>,
								ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
								TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
								CELL extends JeeslReportCell<L,D,TEMPLATE>,
								STYLE extends JeeslReportStyle<L,D,?>,
								CDT extends JeeslReportCellType<L,D,CDT,?>,
								CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslReportRowType<L,D,RT,?>>
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