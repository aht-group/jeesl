package org.jeesl.factory.ejb.io.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeesl.controller.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
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
import org.jeesl.util.query.xpath.ReportXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.ColumnGroup;
import net.sf.exlp.exception.ExlpXpathNotFoundException;

public class EjbIoReportColumnGroupFactory<L extends JeeslLang,D extends JeeslDescription,
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
								STYLE extends JeeslReportStyle<L,D>,CDT extends JeeslStatus<L,D,CDT>,CW extends JeeslStatus<L,D,CW>,
								RT extends JeeslStatus<L,D,RT>,
								ENTITY extends EjbWithId,
								ATTRIBUTE extends EjbWithId,
								TL extends JeeslTrafficLight<L,D,TLS>,
								TLS extends JeeslStatus<L,D,TLS>,
								FILLING extends JeeslStatus<L,D,FILLING>,
								TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoReportColumnGroupFactory.class);
	
	final Class<GROUP> cGroup;
	final Class<STYLE> cStyle;
	
	private JeeslDbLangUpdater<GROUP,L> dbuLang;
	private JeeslDbDescriptionUpdater<GROUP,D> dbuDescription;
    
	public EjbIoReportColumnGroupFactory(final Class<L> cL,final Class<D> cD,final Class<GROUP> cGroup, final Class<STYLE> cStyle)
	{       
        this.cGroup = cGroup;
        this.cStyle = cStyle;
        dbuLang = JeeslDbLangUpdater.factory(cGroup, cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cGroup, cD);
	}
	    
	public GROUP build(SHEET sheet, List<GROUP> list)
	{
		GROUP ejb = null;
		try
		{
			ejb = cGroup.newInstance();
			ejb.setCode(UUID.randomUUID().toString());
			ejb.setSheet(sheet);
			ejb.setVisible(false);
			if(list==null) {ejb.setPosition(1);}
			else {ejb.setPosition(list.size()+1);}
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public GROUP build(JeeslFacade fReport, SHEET sheet, ColumnGroup group) throws JeeslNotFoundException
	{
		GROUP ejb = null;
		try
		{
			ejb = cGroup.newInstance();
			ejb.setCode(group.getCode());
			ejb.setSheet(sheet);
			ejb = update(fReport,ejb,group);

		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public GROUP update(JeeslFacade fReport, GROUP eGroup, ColumnGroup xGroup) throws JeeslNotFoundException
	{
		eGroup.setPosition(xGroup.getPosition());
		eGroup.setVisible(xGroup.isVisible());
		eGroup.setQueryColumns(xGroup.getQuery());
		
		eGroup.setShowLabel(xGroup.isShowLabel());
		eGroup.setShowWeb(xGroup.isSetShowWeb() && xGroup.isShowWeb());
		
		if(xGroup.isSetLayout())
		{
			if(xGroup.getLayout().isSetStyles())
			{
				try {eGroup.setStyleHeader(fReport.fByCode(cStyle, ReportXpath.getStyle(JeeslReportLayout.Style.header, xGroup.getLayout().getStyles()).getCode()));}
				catch (ExlpXpathNotFoundException e) {}
			}
		}
		
		return eGroup;
	}
	
	public GROUP updateLD(JeeslFacade fReport, GROUP eGroup, ColumnGroup xGroup) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eGroup=dbuLang.handle(fReport, eGroup, xGroup.getLangs());
		eGroup = fReport.save(eGroup);
		eGroup=dbuDescription.handle(fReport, eGroup, xGroup.getDescriptions());
		eGroup = fReport.save(eGroup);
		return eGroup;
	}
	
	public Map<GROUP,Integer> toMapVisibleGroupSize(SHEET sheet)
	{
		Map<GROUP,Integer> map = new HashMap<GROUP,Integer>();
		for(GROUP g : sheet.getGroups())
		{
			int size=0;
			for(COLUMN c : g.getColumns())
			{
				if(c.isVisible()){size++;}
			}
			map.put(g,size);
		}
		return map;
	}
	
	public static <SHEET extends JeeslReportSheet<?,?,?,?,GROUP,?>,
					GROUP extends JeeslReportColumnGroup<?,?,SHEET,COLUMN,?>,
					COLUMN extends JeeslReportColumn<?,?,GROUP,?,?,?,?>>
		Map<GROUP,List<COLUMN>> toMapVisibleGroupColumns(SHEET sheet)
	{
		Map<GROUP,List<COLUMN>> map = new HashMap<GROUP,List<COLUMN>>();
		for(GROUP g : sheet.getGroups())
		{
			if(g.isVisible())
			{
				List<COLUMN> list = new ArrayList<COLUMN>();
				for(COLUMN c : g.getColumns())
				{
					if(c.isVisible()){list.add(c);}
				}
				map.put(g,list);
			}
			
		}
		return map;
	}
	
	public static <SHEET extends JeeslReportSheet<?,?,?,?,GROUP,?>,
				GROUP extends JeeslReportColumnGroup<?,?,SHEET,?,?>>
		List<GROUP> toListVisibleGroups(SHEET sheet) {return toListVisibleGroups(sheet,null);}
	
	public static <
				SHEET extends JeeslReportSheet<?,?,?,?,GROUP,?>,
				GROUP extends JeeslReportColumnGroup<?,?,SHEET,?,?>>
		List<GROUP> toListVisibleGroups(SHEET sheet,Map<GROUP,Boolean> mapGroupVisibilityToggle)
	{
		List<GROUP> list = new ArrayList<GROUP>();
		for(GROUP g : sheet.getGroups())
		{
			if(EjbIoReportColumnGroupFactory.visible(g,mapGroupVisibilityToggle))
			{
				list.add(g);
			}
		}
		return list;
	}
	
	public static <GROUP extends JeeslReportColumnGroup<?,?,?,?,?>>
			boolean visible(GROUP g, Map<GROUP,Boolean> mapGroupVisibilityToggle)
	{
		boolean toggle = true;
		if(mapGroupVisibilityToggle!=null)
		{
			if(mapGroupVisibilityToggle.containsKey(g)){toggle = mapGroupVisibilityToggle.get(g);}
			else{toggle = false;}
		}
		
		return g.isVisible() && toggle;
	}
}