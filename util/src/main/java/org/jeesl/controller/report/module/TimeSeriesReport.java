package org.jeesl.controller.report.module;

import java.util.Date;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoReportFacade;
import org.jeesl.api.facade.module.JeeslTsFacade;
import org.jeesl.controller.report.AbstractJeeslReport;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoReportFactoryBuilder;
import org.jeesl.factory.builder.module.TsFactoryBuilder;
import org.jeesl.factory.xml.module.ts.XmlDataFactory;
import org.jeesl.factory.xml.module.ts.XmlTsFactory;
import org.jeesl.factory.xml.system.io.report.XmlReportFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.interfaces.model.io.report.JeeslIoReportCategory;
import org.jeesl.interfaces.model.io.report.col.JeeslReportCellType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRow;
import org.jeesl.interfaces.model.io.report.row.JeeslReportRowType;
import org.jeesl.interfaces.model.io.report.row.JeeslReportTemplate;
import org.jeesl.interfaces.model.io.report.style.JeeslReportAlignment;
import org.jeesl.interfaces.model.io.report.style.JeeslReportColumnWidth;
import org.jeesl.interfaces.model.io.report.style.JeeslReportStyle;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportCell;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumn;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportColumnGroup;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportSheet;
import org.jeesl.interfaces.model.io.report.xlsx.JeeslReportWorkbook;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsCategory;
import org.jeesl.interfaces.model.module.ts.config.JeeslTsInterval;
import org.jeesl.interfaces.model.module.ts.core.JeeslTimeSeries;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsEntityClass;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsMultiPoint;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScopeType;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsBridge;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsData;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsDataPoint;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsSample;
import org.jeesl.interfaces.model.module.ts.data.JeeslTsTransaction;
import org.jeesl.interfaces.model.module.ts.stat.JeeslTsStatistic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLangDescription;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.module.ts.Ts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeSeriesReport <L extends JeeslLang,D extends JeeslDescription,
						CATEGORY extends JeeslIoReportCategory<L,D,CATEGORY,?>,
						REPORT extends JeeslIoReport<L,D,CATEGORY,WORKBOOK>,
						IMPLEMENTATION extends JeeslStatus<L,D,IMPLEMENTATION>,
						WORKBOOK extends JeeslReportWorkbook<REPORT,SHEET>,
						SHEET extends JeeslReportSheet<L,D,IMPLEMENTATION,WORKBOOK,GROUP,ROW>,
						GROUP extends JeeslReportColumnGroup<L,D,SHEET,COLUMN,STYLE>,
						COLUMN extends JeeslReportColumn<L,D,GROUP,STYLE,CDT,CW,TLS>,
						ROW extends JeeslReportRow<L,D,SHEET,TEMPLATE,CDT,RT>,
						TEMPLATE extends JeeslReportTemplate<L,D,CELL>,
						CELL extends JeeslReportCell<L,D,TEMPLATE>,
						STYLE extends JeeslReportStyle<L,D,ALIGNMENT>,
						ALIGNMENT extends JeeslReportAlignment<L,D,ALIGNMENT,?>,
						CDT extends JeeslReportCellType<L,D,CDT,?>,
						CW extends JeeslReportColumnWidth<L,D,CW,?>,
						RT extends JeeslReportRowType<L,D,RT,?>,
						RCAT extends JeeslStatus<L,D,RCAT>,
						ENTITY extends EjbWithId,
						ATTRIBUTE extends EjbWithId,
						TL extends JeeslTrafficLight<L,D,TLS>,
						TLS extends JeeslTrafficLightScope<L,D,TLS,?>,
						FILLING extends JeeslStatus<L,D,FILLING>,
						TRANSFORMATION extends JeeslStatus<L,D,TRANSFORMATION>,
						
						CAT extends JeeslTsCategory<L,D,CAT,?>,
						SCOPE extends JeeslTsScope<L,D,CAT,ST,UNIT,EC,INT>,
						ST extends JeeslTsScopeType<L,D,ST,?>,
						UNIT extends JeeslStatus<L,D,UNIT>,
						MP extends JeeslTsMultiPoint<L,D,SCOPE,UNIT,?>,
						TS extends JeeslTimeSeries<SCOPE,TS,BRIDGE,INT,STAT>,
						TRANSACTION extends JeeslTsTransaction<SOURCE,DATA,USER,?>,
						SOURCE extends EjbWithLangDescription<L,D>, 
						BRIDGE extends JeeslTsBridge<EC>,
						EC extends JeeslTsEntityClass<L,D,CAT,E2>,
						E2 extends JeeslRevisionEntity<L,D,?,?,?,?>,
						INT extends JeeslTsInterval<L,D,INT,?>,
						STAT extends JeeslTsStatistic<L,D,STAT,?>,
						DATA extends JeeslTsData<TS,TRANSACTION,SAMPLE,POINT,WS>,
						POINT extends JeeslTsDataPoint<DATA,MP>,
						SAMPLE extends JeeslTsSample,
						USER extends JeeslSecurityUser, 
						WS extends JeeslStatus<L,D,WS>,
						QAF extends JeeslStatus<L,D,QAF>
						>
					extends AbstractJeeslReport<L,D,USER,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION>
//implements JeeslReportHeader//,JeeslFlatReport,JeeslXlsReport
{
	final static Logger logger = LoggerFactory.getLogger(TimeSeriesReport.class);

	private final JeeslTsFacade<CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,E2,INT,STAT,DATA,POINT,SAMPLE,USER,WS,?> fTs;
	
	private final TsFactoryBuilder<L,D,?,CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,E2,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,?> fbTs;
	
	public TimeSeriesReport(String localeCode,
			final JeeslIoReportFacade<REPORT,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL> fReport,
			final JeeslTsFacade<CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,E2,INT,STAT,DATA,POINT,SAMPLE,USER,WS,?> fTs,
			final IoReportFactoryBuilder<L,D,CATEGORY,REPORT,IMPLEMENTATION,WORKBOOK,SHEET,GROUP,COLUMN,ROW,TEMPLATE,CELL,STYLE,ALIGNMENT,CDT,CW,RT,RCAT,ENTITY,ATTRIBUTE,TL,TLS,FILLING,TRANSFORMATION> fbReport,
			final TsFactoryBuilder<L,D,?,CAT,SCOPE,ST,UNIT,MP,TS,TRANSACTION,SOURCE,BRIDGE,EC,E2,INT,STAT,DATA,POINT,SAMPLE,USER,WS,QAF,?> fbTs)
	{
		super(localeCode,fbReport);
		super.initIo(fReport,this.getClass());
		this.fTs=fTs;
		this.fbTs=fbTs;
	}
	
	public Report build(WS workspace, SCOPE scope, INT interval, BRIDGE bridge, Date from, Date to) throws JeeslNotFoundException
	{
		STAT statistic = fTs.fByEnum(fbTs.getClassStat(), JeeslTsStatistic.Code.raw);
		TS ts = fTs.fTimeSeries(scope,interval,statistic,bridge);
		List<DATA> tsData = fTs.fData(workspace,ts,JeeslTsData.QueryInterval.closedOpen,from,to);
		logger.info("Records: "+tsData.size());
		Report xml = XmlReportFactory.build();
		
		Ts xTs = XmlTsFactory.build();
		
		for(DATA data : tsData)
		{
			xTs.getData().add(XmlDataFactory.build(data.getRecord(),data.getValue()));
		}		
		return xml;
	}
	
}
/*
	public Report build()
	{
		r = new Report();
		rTitle = "Project Data Entry Report";
		rFooter = rTitle;
				
		r.setInfo(getInfo());
		XmlLabelsFactory.aggregationGroups(localeCode,r.getInfo().getLabels(),aggregations);
		XmlLabelsFactory.aggregationHeader(localeCode,r.getInfo().getLabels(),mapAggregationLabels);
		r.setMeis(new Meis());
		
		return r;
	
	@Override public void buildFlat()
	{
		flats = JsonFlatFiguresFactory.build();
		for(MeisProject project : fProject.all(MeisProject.class))
		{
			JsonFlatFigure json = new JsonFlatFigure();
			
			project = fProject.load(project);
			MeisProgram program = null;
			if(!project.getPrograms().isEmpty())
			{
				program = project.getPrograms().get(0);
				program = fProgram.load(program);
			}
			
			if(program!=null)
			{
				json.setG1(program.getCode());
				json.setG2(program.getName());
			}
			json.setG3(project.getCode());
			json.setG4(project.getName());
			json.setG5(project.getDistrict().getName());
			if(project.getCoverage()!=null){json.setG6(TxtSectorFactory.build(project.getCoverage().getSectors()));}
			
			json.setG7(project.getTypeMain().getName().get(localeCode).getLang());
			json.setG8(project.getType().getName().get(localeCode).getLang());
			if(project.getTypeSecond()!=null){json.setG9(project.getTypeSecond().getName().get(localeCode).getLang());}
			
			if(program!=null && !program.getBudgets().isEmpty())
			{
				json.setG10(TxtContributionFactory.fundingSourceCode(program.getBudgets()));
			}
			
			flats.getFigures().add(json);
		}
	}
*/