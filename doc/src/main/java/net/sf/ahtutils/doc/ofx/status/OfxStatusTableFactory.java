package net.sf.ahtutils.doc.ofx.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.StringUtil;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.doc.ofx.OfxMultiLangFactory;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlAlignmentFactory;
import org.openfuxml.factory.xml.ofx.content.XmlCommentFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.ofx.layout.XmlLayoutFactory;
import org.openfuxml.factory.xml.ofx.layout.XmlLineFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.factory.xml.text.OfxTextFactory;
import org.openfuxml.model.xml.core.layout.Layout;
import org.openfuxml.model.xml.core.ofx.Comment;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.ofx.Title;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Cell;
import org.openfuxml.model.xml.core.table.Column;
import org.openfuxml.model.xml.core.table.Columns;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Specification;
import org.openfuxml.model.xml.core.table.Table;
import org.openfuxml.util.OfxCommentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.DocumentationCommentBuilder;
import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.xml.aht.Aht;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class OfxStatusTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxStatusTableFactory.class);
	
	public static enum Code {parent,icon,code,name,description}
	
	public static final String translationKeyName = "auStatusTableName";
	public static final String translationKeyDescription = "auStatusTableDescription";
	public static final String translationKeyParent = "auStatusTableParent";
	
	private static String keyCaption = "auTableStatusCaption";
	
	private int[] colWidths;
	private boolean customColWidths;
	private int[] colWidths3 = {10,20,30};
	private int[] colWidths4 = {10,20,30};
	
	private String imagePathPrefix;
	public String getImagePathPrefix() {return imagePathPrefix;}
	public void setImagePathPrefix(String imagePathPrefix) {this.imagePathPrefix = imagePathPrefix;}
	
	private Map<Code,Boolean> renderColumn;
	private Map<Code,Column> columnDefinition;
	private Map<Code,String> headers;
	
	private Aht xmlP;

	public OfxStatusTableFactory(Configuration config, String lang, Translations translations) throws UtilsConfigurationException
	{
		this(config,new String[] {lang},translations);
	}
	public OfxStatusTableFactory(Configuration config, String[] langs, Translations translations) throws UtilsConfigurationException
	{
		super(config,langs,translations);
		
		comment = XmlCommentFactory.build();
		DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
		
		imagePathPrefix = config.getString("doc.ofx.imagePathPrefix");
		customColWidths=false;
		
		columnDefinition = new Hashtable<Code,Column>();
		renderColumn = new Hashtable<Code,Boolean>();
		renderColumn.put(Code.parent, false);
		renderColumn.put(Code.icon, false);
		renderColumn.put(Code.code, false);
		renderColumn.put(Code.name, true);
		renderColumn.put(Code.description, true);
		
		headers = new Hashtable<Code,String>();
		headers.put(Code.parent, "auStatusTableParent");
		headers.put(Code.code, "auStatusTableCode");
		headers.put(Code.name, translationKeyName);
		headers.put(Code.description, translationKeyDescription);
	}
	
	@Deprecated
	public void activateParents(Aht parents)
	{
		renderColumn.put(Code.parent, true);
		xmlP = parents;
	}
	
	@Deprecated
	public Table buildLatexTable(String id, Aht xmlStatus) throws OfxAuthoringException, UtilsConfigurationException
	{
		try
		{	
			String captionKey = buildCaptionKey(id);
			id = "table.status."+id;
			
			Comment comment = XmlCommentFactory.build();
			OfxCommentBuilder.fixedId(comment, id);
			DocumentationCommentBuilder.translationKeys(comment,config,UtilsDocumentation.keyTranslationFile);
			DocumentationCommentBuilder.tableHeaders(comment,headers);
			DocumentationCommentBuilder.tableKey(comment,captionKey,"Table Caption");
			OfxCommentBuilder.doNotModify(comment);
			
			Table table = toOfx(xmlStatus);
			table.setId(id);
			table.setComment(comment);
			
			Title title = XmlTitleFactory.build();
			for(String lang : langs)
			{
				Lang lCaption = StatusXpath.getLang(translations, captionKey, lang);
				title.getContent().add(OfxTextFactory.build(lang,lCaption.getTranslation()));
			}
			table.setTitle(title);
			
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	@Deprecated
	public Table toOfx(Aht xmlStatus) throws UtilsConfigurationException
	{
		Table table = null;
		try
		{
			table = new Table();
			table.setSpecification(createSpecifications());
			table.setContent(createContent(xmlStatus));
			table.getContent().setHead(buildHead());
		}
		catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
		catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
		
		return table;
	}
	
	@Deprecated
	private Head buildHead()
	{
		Row row = new Row();
		
		for(Code code : Code.values())
		{
			logger.trace(code+ " "+renderColumn.get(code));
			if(renderColumn.get(code))
			{
				
				if(headers.containsKey(code))
				{
					Cell cell = XmlCellFactory.build();
					for(String lang : langs)
					{
						try
						{
							Paragraph p = new Paragraph();
							p.setLang(lang);
							p.getContent().add(StatusXpath.getLang(translations, headers.get(code), lang).getTranslation());
							cell.getContent().add(p);
						}
						catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
						catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
					}
					row.getCell().add(cell);
				}
				else
				{
					row.getCell().add(XmlCellFactory.build());
				}
			}
		}
		
		Head head = new Head();
		head.getRow().add(row);
		return head;
	}
	
	@Deprecated
	private Content createContent(Aht xmlStatus) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException, UtilsConfigurationException
	{
		Body body = new Body();
		
		String previousParentString = "-1";
		boolean firstRow = true;
		for(Status status : xmlStatus.getStatus())
		{
			String parentString = null;
			if(renderColumn.get(Code.parent))
			{
				if(Objects.isNull(status.getParent())) {throw new UtilsConfigurationException("A parent is exprected for the status:"+status.getCode());}
				Status parent = StatusXpath.getStatus(xmlP.getStatus(), status.getParent().getCode());
				if(langs.length>1){logger.warn("Incorrect Assignment");}
				parentString = StatusXpath.getLang(parent.getLangs(), langs[0]).getTranslation();
				if(parentString.equals(previousParentString)){parentString="";}
				else{previousParentString=parentString;}
			}
			boolean line = !firstRow && renderColumn.get(Code.parent) && parentString.length()>0;
			body.getRow().add(createRow(status,parentString,line));
			
			firstRow=false;
		}
		
		Content content = new Content();
		content.getBody().add(body);
		
		return content;
	}
	
	@Deprecated
	private Row createRow(Status status, String parentString, boolean line) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{		
		Row row = new Row();
		
		if(line)
		{
			Layout layout = XmlLayoutFactory.build();
			layout.getLine().add(XmlLineFactory.top());
			row.setLayout(layout);
		}
		
		if(parentString!=null)
		{
			row.getCell().add(XmlCellFactory.createParagraphCell(parentString));
		}
		
		if(renderColumn.get(Code.icon))
		{
			row.getCell().add(XmlCellFactory.image(OfxStatusImageFactory.build(imagePathPrefix,status)));
		}
		
		Cell cellLabel = XmlCellFactory.build();
		Cell cellDescription = XmlCellFactory.build();
		
		for(String lang : langs)
		{
			Paragraph pLabel = new Paragraph();pLabel.setLang(lang);
			pLabel.getContent().add(StatusXpath.getLang(status.getLangs(), lang).getTranslation());
			cellLabel.getContent().add(pLabel);
			
			Paragraph pDescription = new Paragraph();pDescription.setLang(lang);
			pDescription.getContent().add(StatusXpath.getDescription(status.getDescriptions(), lang).getValue());
			cellDescription.getContent().add(pDescription);
		}
		
		row.getCell().add(cellLabel);
		row.getCell().add(cellDescription);
		
		return row;
	}
	
	@Deprecated
	private Specification createSpecifications() throws UtilsConfigurationException
	{
		logger.trace("customColWidths: "+customColWidths);
		if(!customColWidths)
		{
			if(renderColumn.get(Code.parent)){colWidths=colWidths4;}
			else{colWidths=colWidths3;}
		}
		logger.debug("colums.length: "+colWidths.length);
		if(renderColumn.get(Code.parent) && colWidths.length!=4){throw new UtilsConfigurationException("Need 4 column widths");}
		
		int flexWidth=0;
		Columns cols = new Columns();
		
		if(renderColumn.get(Code.parent))
		{
			int w=20;flexWidth=flexWidth+w;
			cols.getColumn().add(XmlColumnFactory.flex(w));
		}
		
		if(renderColumn.get(Code.icon))
		{
			XmlColumnFactory.add(cols,XmlAlignmentFactory.Horizontal.center);
		}
		
		if(renderColumn.get(Code.code))
		{
			XmlColumnFactory.add(cols,XmlAlignmentFactory.Horizontal.left);
		}
		
		Code code = Code.name;
		if(renderColumn.get(code))
		{
			if(columnDefinition.containsKey(code))
			{
				cols.getColumn().add(columnDefinition.get(code));
			}
			else
			{
				int w=30;flexWidth=flexWidth+w;
				Column c = XmlColumnFactory.flex(w);
				c.getWidth().setNarrow(true);
				cols.getColumn().add(c);
			}		
		}
		
		if(renderColumn.get(Code.description))
		{
			cols.getColumn().add(XmlColumnFactory.flex(100-flexWidth));
		}
		
		Specification specification = new Specification();
		specification.setColumns(cols);
		return specification;
	}
	
	@Deprecated public void setColWidths(int[] colWidths)
	{
		customColWidths=true;
		this.colWidths = colWidths;
	}
	
	@Deprecated public void renderColumn(Code code, boolean render){renderColumn(code,render,null);}
	@Deprecated public void renderColumn(Code code, boolean render,Column definition)
	{
		renderColumn.put(code, render);
		if(definition!=null){columnDefinition.put(code, definition);}
	}
	
	private Aht listStatus; public void setListStatus(Aht listStatus) {this.listStatus = listStatus;}
	private Aht listParent; public void setListParent(Aht listParent) {this.listParent = listParent;}
	private String parentKey; public void setParentKey(String parentKey) {this.parentKey = parentKey;}

	private List<OfxStatusTableFactory.Code> columns;
	private Comment comment;
	private Specification specs;
	private Head head;
	
	private String previousParentCode = "-1";
	private boolean firstRow = true;
	
	public Table build(String id) throws OfxAuthoringException
	{
		String captionKey = buildCaptionKey(id);
		id = "table.status."+id;
		
		DocumentationCommentBuilder.tableKey(comment,captionKey,"Table Caption");
		OfxCommentBuilder.fixedId(comment, id);
		OfxCommentBuilder.doNotModify(comment);
		
		Table table = toOfx();
		table.setId(id);
		table.setComment(comment);
		table.setTitle(OfxMultiLangFactory.title(langs, translations, captionKey));
		
		return table;
	}
	
	private Table toOfx() throws OfxAuthoringException
	{
		Table table = new Table();
		table.setSpecification(specs);
		table.setContent(createContent());
		table.getContent().setHead(head);
		
		return table;
	}
	
	private Content createContent() throws OfxAuthoringException
	{
		Body body = new Body();
		for(Status status : listStatus.getStatus())
		{
			if(BooleanComparator.active(status.isVisible()))
			{
				body.getRow().add(row(status));
				firstRow=false;
			}
		}
		
		Content content = new Content();
		content.getBody().add(body);
		
		return content;
	}
	
	private Row row(Status status) throws OfxAuthoringException
	{		
		Row row = new Row();
		
		for(OfxStatusTableFactory.Code code : columns)
		{
			if(code.equals(Code.parent))
			{
				String actualParentcode = status.getParent().getCode();
				
				logger.trace(StringUtil.stars());
				logger.trace("Previous: "+previousParentCode);
				logger.trace("Actual: "+actualParentcode);
				
				if(previousParentCode.equals(actualParentcode))
				{
					row.getCell().add(XmlCellFactory.createParagraphCell(""));
				}
				else
				{
					if(!firstRow)
					{
						row.setLayout(XmlLayoutFactory.build());
						row.getLayout().getLine().add(XmlLineFactory.top());
					}
					try
					{
						Status sParent = StatusXpath.getStatus(listParent.getStatus(),actualParentcode);
						row.getCell().add(OfxMultiLangFactory.cell(langs, sParent.getLangs()));
					}
					catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
					catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}
				}
				previousParentCode = actualParentcode;
			}
			else if(code.equals(Code.icon))
			{
				row.getCell().add(XmlCellFactory.image(OfxStatusImageFactory.build(imagePathPrefix,status)));
			}
			else if(code.equals(Code.code))
			{
				row.getCell().add(XmlCellFactory.createParagraphCell(status.getCode()));
			}
			else if(code.equals(Code.name))
			{
				row.getCell().add(OfxMultiLangFactory.cell(langs, status.getLangs()));
			}
			else if(code.equals(Code.description))
			{
				row.getCell().add(OfxMultiLangFactory.cell(langs, status.getDescriptions()));
			}
		}		
		return row;
	}
		
	public void setColumns(OfxStatusTableFactory.Code... columns) throws OfxAuthoringException
	{		
		this.columns = new ArrayList<OfxStatusTableFactory.Code>(Arrays.asList(columns));
	
		OfxStatusTableFactory.Code narrowColumn = calcNarrowColumn();
		specs = new Specification();
		specs.setColumns(new Columns());
		
		head = new Head();
		head.getRow().add(new Row());
		
		String headerKey=null;
		int headerNr=1;
		for(OfxStatusTableFactory.Code code : columns)
		{
			if(code.equals(Code.parent))
			{
				headerKey = "auStatusTableParent"+StringUtil.slash2Camel(parentKey);
				head.getRow().get(0).getCell().add(OfxMultiLangFactory.cell(langs, translations, headerKey));
				specs.getColumns().getColumn().add(XmlColumnFactory.flex(20,code.equals(narrowColumn)));
			}
			else if(code.equals(Code.icon))
			{
				headerKey = null;
				head.getRow().get(0).getCell().add(XmlCellFactory.build());
				XmlColumnFactory.add(specs.getColumns(),XmlAlignmentFactory.Horizontal.center);
			}
			else if(code.equals(Code.code))
			{
				headerKey = "auStatusTableCode";
				head.getRow().get(0).getCell().add(OfxMultiLangFactory.cell(langs, translations, headerKey));
				specs.getColumns().getColumn().add(XmlColumnFactory.flex(10,code.equals(narrowColumn)));
			}
			else if(code.equals(Code.name))
			{
				headerKey = "auStatusTableName";
				head.getRow().get(0).getCell().add(OfxMultiLangFactory.cell(langs, translations, headerKey));
				specs.getColumns().getColumn().add(XmlColumnFactory.flex(30,code.equals(narrowColumn)));
			}
			else if(code.equals(Code.description))
			{
				headerKey = "auStatusTableDescription";
				head.getRow().get(0).getCell().add(OfxMultiLangFactory.cell(langs, translations, headerKey));
				specs.getColumns().getColumn().add(XmlColumnFactory.flex(60,code.equals(narrowColumn)));
			}
			if(headerKey!=null){DocumentationCommentBuilder.tableKey(comment,headerKey,"Table Header ("+headerNr+")");}
			headerNr++;
		}
		JaxbUtil.trace(specs);
	}
	
	private OfxStatusTableFactory.Code calcNarrowColumn() throws OfxAuthoringException
	{
		boolean cParent = false;
		boolean cCode = false;
		boolean cName = false;
		boolean cDescription = false;
		for(OfxStatusTableFactory.Code code : columns)
		{
			if(code.equals(Code.parent)){cParent=true;}
			else if(code.equals(Code.code)){cCode=true;}
			else if(code.equals(Code.name)){cName=true;}
			else if(code.equals(Code.description)){cDescription=true;}
		}
		
		if     ( cParent  && !cCode && !cName && !cDescription){return Code.parent;}
		else if(              cCode &&  cName && !cDescription){return Code.code;}
		else if(             !cCode &&  cName &&  cDescription){return Code.name;}
		else {throw new OfxAuthoringException("Unknown narrow calculation");}
	}
	
	private String buildCaptionKey(String id)
	{
		String captionKey = id;
		
		while(captionKey.indexOf(".")>0)
		{
			int index = captionKey.indexOf(".");
			captionKey = captionKey.substring(0,index)+captionKey.substring(index+1, index+2).toUpperCase()+captionKey.substring(index+2, captionKey.length());
		}
		captionKey = captionKey.substring(0, 1).toUpperCase()+captionKey.substring(1, captionKey.length());
		
		if(renderColumn.get(Code.parent))
		{
			headers.put(Code.parent, headers.get(Code.parent)+""+captionKey);
		}
		return keyCaption+captionKey;
	}
}