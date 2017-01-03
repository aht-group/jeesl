package net.sf.ahtutils.xml.xpath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.report.ColumnGroup;
import net.sf.ahtutils.xml.report.DataAssociation;
import net.sf.ahtutils.xml.report.DataHandler;
import net.sf.ahtutils.xml.report.ImportStructure;
import net.sf.ahtutils.xml.report.Info;
import net.sf.ahtutils.xml.report.Jr;
import net.sf.ahtutils.xml.report.Layout;
import net.sf.ahtutils.xml.report.Media;
import net.sf.ahtutils.xml.report.Queries;
import net.sf.ahtutils.xml.report.Query;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.Rows;
import net.sf.ahtutils.xml.report.Size;
import net.sf.ahtutils.xml.report.Style;
import net.sf.ahtutils.xml.report.Styles;
import net.sf.ahtutils.xml.report.Template;
import net.sf.ahtutils.xml.report.Templates;
import net.sf.ahtutils.xml.report.XlsColumn;
import net.sf.ahtutils.xml.report.XlsDefinition;
import net.sf.ahtutils.xml.report.XlsSheet;
import net.sf.ahtutils.xml.report.XlsWorkbook;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Implementation;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JDomUtil;

public class ReportXpath
{
	final static Logger logger = LoggerFactory.getLogger(ReportXpath.class);
	
	public static synchronized Media getMedia(List<Media> list, String type) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Report report = new Report();
		report.getMedia().addAll(list);
		JXPathContext context = JXPathContext.newContext(report);
		
		@SuppressWarnings("unchecked")
		List<Media> listResult = (List<Media>)context.selectNodes("media[@type='"+type+"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Media.class.getSimpleName()+" for type="+type);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Media.class.getSimpleName()+" for type="+type);}
		return listResult.get(0);
	}
	
	public static synchronized Report getReport(Reports reports, String id) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(reports);
		
		@SuppressWarnings("unchecked")
		List<Report> listResult = (List<Report>)context.selectNodes("report[@id='"+id+"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Report.class.getSimpleName()+" for id="+id);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Report.class.getSimpleName()+" for id="+id);}
		return listResult.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized ArrayList<Jr> getSubreports(Reports reports, String id, String format)
	{
		JXPathContext context = JXPathContext.newContext(reports);
		ArrayList<Jr> subReports = new ArrayList<Jr>();
		subReports.addAll((ArrayList<Jr>) context.selectNodes("//jr[parent::media/parent::report/@id='" +id +"' and parent::media/@type='" +format +"' and @type='sr']"));
		return subReports;
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Jr getMr(Reports reports, String id, String format) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("//jr[@type='mr' ");
		sb.append("and parent::media/parent::report/@id='").append(id).append("' ");
		sb.append("and parent::media/@type='").append(format).append("']");
		
		JXPathContext context = JXPathContext.newContext(reports);
		List<Jr> listResult = (List<Jr>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Report.class.getSimpleName()+" for id="+id+" and format="+format);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Report.class.getSimpleName()+" for id="+id+" and format="+format);}
		return listResult.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Jr getSr(Reports reports, String id, String subreport, String format) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		StringBuffer sb = new StringBuffer();
		sb.append("//jr[@type='sr' ");
		sb.append("and parent::media/parent::report/@id='").append(id).append("' ");
		sb.append("and @name='").append(subreport).append("' ");
		sb.append("and parent::media/@type='").append(format).append("']");
		
		JXPathContext context = JXPathContext.newContext(reports);
		List<Jr> listResult = (List<Jr>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Report.class.getSimpleName()+" for id="+id);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Report.class.getSimpleName()+" for id="+id);}
		return listResult.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Template getTemplate(Templates templates, String id) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(templates);
		List<Template> listResult = (List<Template>)context.selectNodes("//template[@id='" +id +"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Template.class.getSimpleName()+" for id="+id);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Template.class.getSimpleName()+" for id="+id);}
		return listResult.get(0);
	}
	
	public static Info getReportInfo(Document jdomDocument)
	{
		XPathExpression<Element> xpath = XPathFactory.instance().compile("/report/info", Filters.element());
		Element eInfo = xpath.evaluateFirst(jdomDocument);
		
		eInfo = JDomUtil.setNameSpaceRecursive(eInfo, JeeslNsPrefixMapper.nsReport);
		Info info = JDomUtil.toJaxb(eInfo, Info.class);
		return info;
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized XlsWorkbook getWorkbook(XlsDefinition definition, String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(definition);
		List<XlsWorkbook> listResult = (List<XlsWorkbook>)context.selectNodes("//xlsWorkbook[@code='" +code +"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+XlsWorkbook.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+XlsWorkbook.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized DataHandler getDataHandler(XlsSheet definition, String clazz) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(definition);
		List<DataHandler> listResult = (List<DataHandler>)context.selectNodes("//dataHandler[@class='" +clazz +"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+DataHandler.class.getSimpleName()+" for class="+clazz);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+DataHandler.class.getSimpleName()+" for code="+clazz);}
		return listResult.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized DataAssociation getDataAssociation(XlsSheet definition, String property) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(definition);
		List<DataAssociation> listResult = (List<DataAssociation>)context.selectNodes("//dataAssociation[@property='" +property +"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+DataAssociation.class.getSimpleName()+" for property="+property);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+DataAssociation.class.getSimpleName()+" for code="+property);}
		return listResult.get(0);
	}
	
	public static synchronized Langs getFirstLangs(List<Serializable> contentList)
	{
		for(Object o : contentList)
		{
			if (o instanceof Langs) {return (Langs) o;}
		}
		return null;
	}
	
	public static synchronized ImportStructure getImportStructure(List<Serializable> contentList) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		for(Object o  : contentList)
		{
			if (o instanceof ImportStructure) {return (ImportStructure) o;}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized List<Lang> getLang(Langs definition) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(definition);
		List<Lang> listResult = (List<Lang>)context.selectNodes("//lang");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Lang.class.getSimpleName()+" are set");}
		return listResult;
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized List<XlsColumn> getColumns(XlsSheet definition) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(definition);
		List<XlsColumn> listResult = (List<XlsColumn>)context.selectNodes("//xlsColumn");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+XlsColumn.class.getSimpleName()+" are set");}
		return listResult;
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized ImportStructure getImportStructure(XlsSheet definition) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(definition);
		List<ImportStructure> listResult = (List<ImportStructure>)context.selectNodes("//importStructure");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+ImportStructure.class.getSimpleName()+" is set");}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+ImportStructure.class.getSimpleName()+" are set");}
		return listResult.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized String getPrimaryKey(XlsSheet definition) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(definition);
		List<String> listResult = (List<String>)context.selectNodes("@primaryKey");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+ImportStructure.class.getSimpleName()+" is set");}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+ImportStructure.class.getSimpleName()+" are set");}
		return listResult.get(0);
	}
	
	public static Langs getLangs(XlsSheet sheet) throws ExlpXpathNotFoundException
	{
		for(Serializable s : sheet.getContent())
		{
			if(s instanceof Langs){return (Langs)s;}
		}
		throw new ExlpXpathNotFoundException("No "+Langs.class.getSimpleName()+" available");
	}
	
	public static Descriptions getDescriptions(XlsSheet sheet) throws ExlpXpathNotFoundException
	{
		for(Serializable s : sheet.getContent())
		{
			if(s instanceof Descriptions){return (Descriptions)s;}
		}
		throw new ExlpXpathNotFoundException("No "+Descriptions.class.getSimpleName()+" available");
	}
	
	public static Implementation getImplementation(XlsSheet sheet) throws ExlpXpathNotFoundException
	{
		for(Serializable s : sheet.getContent())
		{
			if(s instanceof Implementation){return (Implementation)s;}
		}
		throw new ExlpXpathNotFoundException("No "+Implementation.class.getSimpleName()+" available");
	}
	
	public static Queries getQueries(XlsSheet q) throws ExlpXpathNotFoundException
	{
		for(Serializable s : q.getContent())
		{
			if(s instanceof Queries){return (Queries)s;}
		}
		throw new ExlpXpathNotFoundException("No "+Queries.class.getSimpleName()+" available");
	}
	
	public static ColumnGroup getColumnGroup(XlsSheet q) throws ExlpXpathNotFoundException
	{
		for(Serializable s : q.getContent())
		{
			if(s instanceof ColumnGroup){return (ColumnGroup)s;}
		}
		throw new ExlpXpathNotFoundException("No "+ColumnGroup.class.getSimpleName()+" available");
	}
	
	public static Rows getRows(XlsSheet q) throws ExlpXpathNotFoundException
	{
		for(Serializable s : q.getContent())
		{
			if(s instanceof Rows){return (Rows)s;}
		}
		throw new ExlpXpathNotFoundException("No "+Rows.class.getSimpleName()+" available");
	}
	
	public static Query getQuery(String type, Queries queries) throws ExlpXpathNotFoundException
	{
		for(Query q : queries.getQuery())
		{
			if(q.getType().equals(type)){return q;}
		}
		throw new ExlpXpathNotFoundException("No "+Query.class.getSimpleName()+" for type="+type);
	}
	
	public static Size getSize(String type, Layout layout) throws ExlpXpathNotFoundException
	{
		for(Size s : layout.getSize())
		{
			if(s.getCode().equals(type)){return s;}
		}
		throw new ExlpXpathNotFoundException("No "+Size.class.getSimpleName()+" for type="+type);
	}
	
	public static <E extends Enum<E>> Style getStyle(E type, Styles styles) throws ExlpXpathNotFoundException
	{
		for(Style s : styles.getStyle())
		{
			if(s.getType().equals(type.toString())){return s;}
		}
		throw new ExlpXpathNotFoundException("No "+Style.class.getSimpleName()+" for type="+type);
	}
	
	public static <E extends Enum<E>> Figures getFigures(E code, List<Figures> list) throws ExlpXpathNotFoundException
	{
		for(net.sf.ahtutils.xml.finance.Figures figures : list)
		{
			if(figures.getCode().equals(code.toString())){return figures;}
		}
		
		throw new ExlpXpathNotFoundException("No "+Figures.class.getSimpleName()+" for code="+code);
	}
}