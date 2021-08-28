package org.jeesl.controller.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang3.StringUtils;
import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.controller.processor.JobCodeProcessor;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.ReportFactoryBuilder;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnFactory;
import org.jeesl.factory.ejb.io.report.EjbIoReportColumnGroupFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.xls.system.io.report.XlsFactory;
import org.jeesl.interfaces.controller.handler.JeeslProgressHandler;
import org.jeesl.interfaces.controller.report.JeeslComparatorProvider;
import org.jeesl.interfaces.controller.report.JeeslReport;
import org.jeesl.interfaces.factory.txt.JeeslReportAggregationLevelFactory;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.data.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.setting.JeeslReportSetting;
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
import org.jeesl.model.json.JsonFlatFigures;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportCellComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportColumnComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportGroupComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportRowComparator;
import org.jeesl.util.comparator.ejb.system.io.report.IoReportSheetComparator;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.metachart.model.json.pivot.PivotSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.sf.exlp.util.io.JsonUtil;
import net.sf.exlp.util.io.StringUtil;

public abstract class AbstractJeeslReport<L extends JeeslLang,D extends JeeslDescription,
											CATEGORY extends JeeslStatus<L,D,CATEGORY>,
											REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
											IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
											WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
											SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
											GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
											COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
											ROW extends JeeslReportRow<L,D,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
											TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
											CELL extends JeeslReportCell<L,D,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>,
											STYLE extends JeeslReportStyle<L,D>,
											CDT extends JeeslStatus<L,D,CDT>,
											CW extends JeeslStatus<L,D,CW>,
											RT extends JeeslStatus<L,D,RT>,
											RCAT extends JeeslStatus<L,D,RCAT>,
											ENTITY extends EjbWithId,
											ATTRIBUTE extends EjbWithId,
											TL extends JeeslTrafficLight<L,D,TLS>,
											TLS extends JeeslStatus<L,D,TLS>,
											FILLING extends JeeslStatus<L,D,FILLING>,
											TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>>
					implements JeeslReport<REPORT>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslReport.class);
	
	protected final boolean alwaysFalse = false;
	protected boolean debugOnInfo;
	protected boolean developmentMode; public void activateDevelopmenetMode() {developmentMode=true;}

	protected String localeCode;
	protected String jobCode; public String getJobCode() {return jobCode;}
	protected String jobName; public String getJobName() {return jobName;}
	
	protected final List<String> headers; public List<String> getHeaders() {return headers;}
	
	private boolean showHeaderGroup; public boolean isShowHeaderGroup() {return showHeaderGroup;}
	private boolean showHeaderColumn; public boolean isShowHeaderColumn() {return showHeaderColumn;}
	
	private int filterCounter = 0;
	
	protected Map<GROUP,Integer> mapGroupChilds; public Map<GROUP,Integer> getMapGroupChilds() {return mapGroupChilds;}
	protected Map<GROUP,List<COLUMN>> mapGroupColumns; public Map<GROUP,List<COLUMN>> getMapGroupColumns() {return mapGroupColumns;}
	protected Map<GROUP,Boolean> mapGroupVisibilityToggle; public Map<GROUP, Boolean> getMapGroupVisibilityToggle() {return mapGroupVisibilityToggle;}
	protected final Map<Integer,Integer> mapFilter;
	protected List<GROUP> groupsAll; public List<GROUP> getGroupsAll() {return groupsAll;}
	protected List<GROUP> groups; public List<GROUP> getGroups() {return groups;}
	protected List<COLUMN> columns; public List<COLUMN> getColumns() {return columns;}
	
	protected REPORT ioReport; @Override public REPORT getIoReport() {return ioReport;}
	protected SHEET ioSheet; public SHEET getIoSheet() {return ioSheet;}

	protected FILLING reportFilling;
	protected TRANSFORMATION reportSettingTransformation; public TRANSFORMATION getReportSettingTransformation() {return reportSettingTransformation;}

	protected final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport;
	protected final EjbLangFactory<L> efLang;
	protected final EjbIoReportColumnFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efColumn;
	private final EjbIoReportColumnGroupFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> efGroup;
	
	protected XlsFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> xlsFactory;
	
	protected JeeslComparatorProvider<EjbWithId> cProvider;
	
	protected final JobCodeProcessor jobCodeProcessor;
    protected JeeslProgressHandler progressHandler;
	
	protected PivotSettings pivotSettings; public PivotSettings getPivotSettings() {return pivotSettings;}

	protected JsonFlatFigures flats; public JsonFlatFigures getFlats() {return flats;}
	protected final List<Object> jsonDataList; public List<Object> getJsonDataList() {return jsonDataList;}
	protected String jsonStream; public String getJsonStream() {return jsonStream;}
	
	private Comparator<SHEET> comparatorSheet;
	private Comparator<GROUP> comparatorGroup;
	private Comparator<COLUMN> comparatorColumn;
	private Comparator<ROW> comparatorRow;
	private Comparator<CELL> comparatorCell;

	public AbstractJeeslReport(String localeCode, final ReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport)
	{
		this.fbReport=fbReport;
		this.localeCode=localeCode;
		debugOnInfo = false;
		developmentMode = false;
		
		efLang = EjbLangFactory.factory(fbReport.getClassL());
		efGroup = fbReport.group();
		efColumn = fbReport.column();
		
		comparatorSheet = new IoReportSheetComparator<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>().factory(IoReportSheetComparator.Type.position);
		comparatorGroup = new IoReportGroupComparator<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>().factory(IoReportGroupComparator.Type.position);
		comparatorColumn = new IoReportColumnComparator<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>().factory(IoReportColumnComparator.Type.position);
		comparatorRow = new IoReportRowComparator<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>().factory(IoReportRowComparator.Type.position);
		comparatorCell = new IoReportCellComparator<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS>().factory(IoReportCellComparator.Type.position);
		
		mapFilter = new HashMap<>();
		mapGroupVisibilityToggle = new HashMap<>();
		showHeaderGroup = true;
		
		jobCodeProcessor = new JobCodeProcessor();
		jsonDataList = new ArrayList<Object>();
		
		headers = new ArrayList<String>();
	}
	
	protected void initIo(JeeslIoReportFacade<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fReport, Class<?> classReport)
	{
		if(fReport!=null)
		{
			if(reportSettingTransformation==null)
			{
				try {reportSettingTransformation = fReport.fByCode(fbReport.getClassTransformation(), JeeslReportSetting.Transformation.none);}
				catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
			}
			
			try
			{
				ioReport = fReport.fByCode(fbReport.getClassReport(),classReport.getSimpleName());
				ioReport = fReport.load(ioReport,true);
				if(ioReport.getWorkbook()!=null)
				{
					WORKBOOK ioWorkbook = ioReport.getWorkbook();
					if(!ioWorkbook.getSheets().isEmpty())
					{
						Collections.sort(ioWorkbook.getSheets(), comparatorSheet);
						xlsFactory = new XlsFactory<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>(localeCode,fbReport,ioWorkbook);
						
						ioSheet = fReport.load(ioWorkbook.getSheets().get(0), true);
						
						// Only set the toggled visibilities to true on the first time
						if(mapGroupVisibilityToggle.isEmpty()){for(GROUP g : ioSheet.getGroups()){mapGroupVisibilityToggle.put(g,true);}}
						
						calculateSheetSettings();
						
						//Sorting of the report structure (and lazyloading of templates)
						for(SHEET s : ioWorkbook.getSheets())
						{
							Collections.sort(s.getGroups(), comparatorGroup);
							Collections.sort(s.getRows(), comparatorRow);
							for(GROUP g : s.getGroups())
							{
								Collections.sort(g.getColumns(), comparatorColumn);
							}
							for(ROW r : s.getRows())
							{
								if(r.getTemplate()!=null)
								{
									r.setTemplate(fReport.load(r.getTemplate()));
									Collections.sort(r.getTemplate().getCells(),comparatorCell);
								}
							}
						}
					}
				}
			}
			catch (JeeslNotFoundException e) {logger.error(e.getMessage());}
			
			if(debugOnInfo)
			{
				logger.info(StringUtil.stars());
				logger.info("Debugging: "+classReport.getSimpleName());
				logger.info("ShowHeaderGroup: "+showHeaderGroup);
				logger.info("showHeaderColumn: "+showHeaderColumn);
				if(groups!=null)
				{
					for(GROUP g : groups)
					{
						logger.info("\t"+g.getPosition()+". "+g.getClass().getSimpleName()+" (childs:"+mapGroupChilds.get(g)+"): "+g.getName().get(localeCode).getLang());
						for(COLUMN c : columns)
						{
							if(c.getGroup().equals(g))
							{
								logger.info("\t\t"+c.getClass().getSimpleName()+": "+c.getName().get(localeCode).getLang());
							}
						}
					}
				}
			}
		}
		else{logger.warn("Trying to super.initIo(), but "+JeeslIoReportFacade.class.getSimpleName()+" is null");}
	}
	
	protected <A extends JeeslStatus<L,D,A>> void applyTransformation(JeeslReportSetting.Transformation transformation, List<EjbWithId> last, String[] localeCodes, JeeslReportAggregationLevelFactory pf)
	{
		if(transformation.equals(JeeslReportSetting.Transformation.last))
		{
			for(GROUP group : groups)
			{
				if(group.getColumns().size()==1 && !group.getColumns().get(0).getQueryCell().startsWith("g"))
				{
					COLUMN original = group.getColumns().get(0);
					group.getColumns().clear();
					group.setQueryColumns(original.getQueryCell().replaceAll("d","list"));
					int arrayIndex = 0;
					for(EjbWithId ejb : last)
					{
						COLUMN c = efColumn.build(group,group.getColumns());
						c.setId(group.getColumns().size()+1);
						c.setColumWidth(original.getColumWidth());
						c.setDataType(original.getDataType());
						c.setVisible(true);
						c.setShowLabel(true);
						c.setPosition(arrayIndex);
						c.setName(efLang.createEmpty(localeCodes));
						for(String localeCode : localeCodes){c.getName().get(localeCode).setLang(pf.buildTreeLevelName(localeCode, ejb));}
						c.setQueryCell(original.getQueryCell());
						
						group.getColumns().add(c);
						arrayIndex++;
					}
				}
			}
		}
		calculateSheetSettings();
	}
	
	private void calculateSheetSettings()
	{
		groupsAll = EjbIoReportColumnGroupFactory.toListVisibleGroups(ioSheet);
		groups = EjbIoReportColumnGroupFactory.toListVisibleGroups(ioSheet,mapGroupVisibilityToggle);
		columns = efColumn.toListVisibleColumns(ioSheet,mapGroupVisibilityToggle);
		
		mapGroupChilds = efGroup.toMapVisibleGroupSize(ioSheet);
		mapGroupColumns = EjbIoReportColumnGroupFactory.toMapVisibleGroupColumns(ioSheet);

		Collections.sort(groupsAll, comparatorGroup);
		Collections.sort(groups, comparatorGroup);
		Collections.sort(columns, comparatorColumn);
		
		showHeaderGroup=false;
		showHeaderColumn=false;
		for(GROUP g : groups)
		{
			if(g.getShowLabel()){showHeaderGroup=true;}
			for(COLUMN c : g.getColumns())
			{
				if(c.isVisible() && c.getShowLabel())
				{
					{showHeaderColumn=true;}
				}
			}
		}
		
		mapFilter.clear();
		for(COLUMN c : columns)
		{
			if(BooleanComparator.active(c.getFilterBy()))
			{
				int position = mapFilter.size();
				int index = columns.indexOf(c);
				mapFilter.put(position,index);
			}
		}
	}
	
	public void toggleGroupVisibility(GROUP g)
	{
		if(mapGroupVisibilityToggle.containsKey(g))
		{
			mapGroupVisibilityToggle.put(g, !mapGroupVisibilityToggle.get(g));
		}
		else
		{
			mapGroupVisibilityToggle.put(g,true);
		}
	}
	
	public boolean filterBy(Object value, Object filter, Locale locale)
	{
		boolean show=true;
		if(filter!=null)
		{
			String sFilter = (String)filter;
			if(sFilter.trim().length()>0)
			{
				int indexFilter = filterCounter % mapFilter.size();
				int indexColumn = mapFilter.get(indexFilter);
				
//				StringBuffer sb = new StringBuffer();
//				sb.append("Value: ");
//				if(value!=null) {sb.append(value.toString());}else {sb.append("null");}
//				sb.append(" Filter: ").append(filter.toString());
//				sb.append(" FilterIndex:").append(indexFilter);
//				sb.append(" ColumnIndex:").append(indexColumn);
//				logger.info(sb.toString());
				
				JXPathContext context = JXPathContext.newContext(value);
				String sValue = (String)context.getValue(columns.get(indexColumn).getQueryCell());
				show = StringUtils.containsIgnoreCase(sValue,sFilter);
			}
		}
		
		filterCounter++;
		return show;
	}
	
	public void buildJson()
	{
		jsonStream = "";
		try {jsonStream = JsonUtil.toString(jsonDataList);}
		catch (JsonProcessingException e) {e.printStackTrace();}
	}
}