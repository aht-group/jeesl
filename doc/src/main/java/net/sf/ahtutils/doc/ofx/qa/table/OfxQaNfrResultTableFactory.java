package net.sf.ahtutils.doc.ofx.qa.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.openfuxml.content.media.Image;
import org.openfuxml.content.media.Media;
import org.openfuxml.content.table.Body;
import org.openfuxml.content.table.Columns;
import org.openfuxml.content.table.Content;
import org.openfuxml.content.table.Head;
import org.openfuxml.content.table.Row;
import org.openfuxml.content.table.Specification;
import org.openfuxml.content.table.Table;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.layout.XmlHeightFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.OfxCellFactory;
import org.openfuxml.factory.xml.table.OfxColumnFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.ahtutils.xml.security.Staff;
import net.sf.ahtutils.xml.status.Lang;
import net.sf.ahtutils.xml.status.Translations;
import net.sf.ahtutils.xml.survey.Answer;
import net.sf.ahtutils.xml.survey.Question;
import net.sf.ahtutils.xml.xpath.StatusXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

public class OfxQaNfrResultTableFactory extends AbstractUtilsOfxDocumentationFactory
{
	final static Logger logger = LoggerFactory.getLogger(OfxQaNfrResultTableFactory.class);
	
	@SuppressWarnings("unused")
	private DateFormat df;
	
	public OfxQaNfrResultTableFactory(Configuration config, String lang, Translations translations)
	{
		this(config,new String[] {lang},translations);
	}
	public OfxQaNfrResultTableFactory(Configuration config, String[] langs, Translations translations)
	{
		super(config,langs,translations);
		imagePathPrefix = config.getString("doc.ofx.imagePathPrefixQA");
		df = SimpleDateFormat.getDateInstance();
	}
	
	public Table build(net.sf.ahtutils.xml.survey.Section section, Map<Long,Map<Long,Answer>> mapAnswers, List<Staff> staffs) throws OfxAuthoringException
	{
		try
		{
			Table table = new Table();
			if(langs.length>1){logger.warn("Incorrect Assignment");}
			Lang lCaption = StatusXpath.getLang(translations, "auTableCaptionQaTestResults", langs[0]);
			table.setTitle(XmlTitleFactory.build(lCaption.getTranslation()));
			
			table.setSpecification(createTableSpecifications(section));
			table.setContent(createTableContent(section,mapAnswers,staffs));
			JaxbUtil.trace(table);
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Specification createTableSpecifications(net.sf.ahtutils.xml.survey.Section section)
	{
		Columns cols = new Columns();
		cols.getColumn().add(OfxColumnFactory.flex(30));
		for(Question q : section.getQuestion())
		{
			JaxbUtil.trace(q);
			cols.getColumn().add(OfxColumnFactory.flex(10,true));
		}
		
		Specification specification = new Specification();
		specification.setFloat(XmlFloatFactory.build(false));
		specification.setColumns(cols);
		
		return specification;
	}
	
	protected Row createHeaderRow(net.sf.ahtutils.xml.survey.Section section)
	{
		Row row = new Row();
//		row.getCell().add(OfxCellFactory.createParagraphCell("Date"));
		row.getCell().add(OfxCellFactory.createParagraphCell("User"));
		for(Question q : section.getQuestion())
		{
			row.getCell().add(OfxCellFactory.createParagraphCell(q.getPosition()));
		}
		return row;
	}
	
	private Content createTableContent(net.sf.ahtutils.xml.survey.Section section, Map<Long,Map<Long,Answer>> mapAnswers, List<Staff> staffs)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(section));
		
		Body body = new Body();

		for(Staff staff : staffs)
		{
			if(hasStaffAnswers(section,mapAnswers,staff))
			{
				body.getRow().add(buildRow(section,mapAnswers.get(staff.getId()),staff));
			}
			else
			{
				logger.trace("No results for staff "+staff.getId()+" "+staff.getUser().getLastName());
			}
		}

		Content content = new Content();
		content.getBody().add(body);
		content.setHead(head);
		
		return content;
	}
	
	private boolean hasStaffAnswers(net.sf.ahtutils.xml.survey.Section section, Map<Long,Map<Long,Answer>> mapAnswers, Staff staff)
	{
		if(mapAnswers.containsKey(staff.getId()))
		{
			Map<Long,Answer> map = mapAnswers.get(staff.getId());
			for(Question q : section.getQuestion())
			{
				if(map.containsKey(q.getId())){return true;}
			}
		}
		return false;
	}
	
	private Row buildRow(net.sf.ahtutils.xml.survey.Section section, Map<Long,Answer> mapAnswers, Staff staff)
	{
		Row row = new Row();
		
		row.getCell().add(OfxCellFactory.createParagraphCell(staff.getUser().getLastName()));
		for(Question q : section.getQuestion())
		{
			if(mapAnswers.containsKey(q.getId()))
			{
				Answer answer = mapAnswers.get(q.getId());
				
				if(answer.isSetValueBoolean())
				{
					if(answer.isValueBoolean()){row.getCell().add(OfxCellFactory.image(image("check")));}
					else{row.getCell().add(OfxCellFactory.image(image("cross")));}
				}
			}
			else
			{
				row.getCell().add(OfxCellFactory.createParagraphCell(""));
			}
		}
		return row;
	}
	
	public Image image(String name)
	{
		String imageName = "/icon/mark/"+name;

		StringBuffer sb = new StringBuffer();
		sb.append(imagePathPrefix).append("/");
		sb.append(imageName);
		sb.append(".svg");
		logger.trace(sb.toString());
		
		Media media = new Media();
		media.setSrc(sb.toString());
		media.setDst(imageName);
		
		Image image = new Image();
		image.setMedia(media);
		image.setHeight(XmlHeightFactory.em(1));
		return image;
	}
}