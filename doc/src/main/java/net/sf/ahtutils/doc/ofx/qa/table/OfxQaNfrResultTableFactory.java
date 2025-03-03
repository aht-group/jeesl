package net.sf.ahtutils.doc.ofx.qa.table;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.io.locale.status.Lang;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.module.survey.Answer;
import org.jeesl.model.xml.module.survey.Question;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.util.query.xpath.StatusXpath;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.factory.xml.layout.XmlFloatFactory;
import org.openfuxml.factory.xml.layout.XmlHeightFactory;
import org.openfuxml.factory.xml.ofx.content.text.XmlTitleFactory;
import org.openfuxml.factory.xml.table.XmlCellFactory;
import org.openfuxml.factory.xml.table.XmlColumnFactory;
import org.openfuxml.model.xml.core.media.Image;
import org.openfuxml.model.xml.core.media.Media;
import org.openfuxml.model.xml.core.table.Body;
import org.openfuxml.model.xml.core.table.Columns;
import org.openfuxml.model.xml.core.table.Content;
import org.openfuxml.model.xml.core.table.Head;
import org.openfuxml.model.xml.core.table.Row;
import org.openfuxml.model.xml.core.table.Specification;
import org.openfuxml.model.xml.core.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.ofx.AbstractUtilsOfxDocumentationFactory;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

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
	
	public Table build(org.jeesl.model.xml.module.survey.Section section, Map<Long,Map<Long,Answer>> mapAnswers, List<Staff> staffs) throws OfxAuthoringException
	{
		try
		{
			Table table = new Table();
			if(langs.length>1){logger.warn("Incorrect Assignment");}
			Lang lCaption = StatusXpath.getLang(translations, "auTableCaptionQaTestResults", langs[0]);
			table.setTitle(XmlTitleFactory.build(lCaption.getTranslation()+" "+section.getDescription().getValue()));
			
			table.setSpecification(createTableSpecifications(section));
			table.setContent(createTableContent(section,mapAnswers,staffs));
			JaxbUtil.trace(table);
			return table;
		}
		catch (ExlpXpathNotFoundException e) {throw new OfxAuthoringException(e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {throw new OfxAuthoringException(e.getMessage());}
	}
	
	private Specification createTableSpecifications(org.jeesl.model.xml.module.survey.Section section)
	{
		Columns cols = new Columns();
		cols.getColumn().add(XmlColumnFactory.flex(30));
		for(Question q : section.getQuestion())
		{
			JaxbUtil.trace(q);
			if(q.isVisible())
			{
				cols.getColumn().add(XmlColumnFactory.flex(10,true));
			}
		}
		
		Specification specification = new Specification();
		specification.setFloat(XmlFloatFactory.build(false));
		specification.setColumns(cols);
		
		return specification;
	}
	
	protected Row createHeaderRow(org.jeesl.model.xml.module.survey.Section section)
	{
		Row row = new Row();
//		row.getCell().add(OfxCellFactory.createParagraphCell("Date"));
		row.getCell().add(XmlCellFactory.createParagraphCell("User"));
		for(Question q : section.getQuestion())
		{
			if(q.isVisible())
			{
				row.getCell().add(XmlCellFactory.createParagraphCell(q.getPosition()));
			}
		}
		return row;
	}
	
	private Content createTableContent(org.jeesl.model.xml.module.survey.Section section, Map<Long,Map<Long,Answer>> mapAnswers, List<Staff> staffs)
	{
		Head head = new Head();
		head.getRow().add(createHeaderRow(section));
		
		Body body = new Body();

		for(Staff staff : staffs)
		{
			boolean withAnswers = hasStaffAnswers(section,mapAnswers,staff);
			boolean reportingRelevant = false;
			if(Objects.nonNull(staff.getLevel()) && Objects.nonNull(staff.getLevel().isVisible())) {reportingRelevant=staff.getLevel().isVisible();}
			
			if(withAnswers && reportingRelevant)
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
	
	private boolean hasStaffAnswers(org.jeesl.model.xml.module.survey.Section section, Map<Long,Map<Long,Answer>> mapAnswers, Staff staff)
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
	
	private Row buildRow(org.jeesl.model.xml.module.survey.Section section, Map<Long,Answer> mapAnswers, Staff staff)
	{
		JaxbUtil.trace(staff);
		Row row = new Row();
		
		row.getCell().add(XmlCellFactory.createParagraphCell(staff.getUser().getLastName()));
		for(Question q : section.getQuestion())
		{
			if(q.isVisible())
			{
				if(mapAnswers.containsKey(q.getId()))
				{
					Answer answer = mapAnswers.get(q.getId());
					
					if(Objects.nonNull(answer.isValueBoolean()))
					{
						if(answer.isValueBoolean()){row.getCell().add(XmlCellFactory.image(image("check")));}
						else{row.getCell().add(XmlCellFactory.image(image("cross")));}
					}
				}
				else
				{
					row.getCell().add(XmlCellFactory.createParagraphCell(""));
				}
			}
		}
		return row;
	}
	
	public Image image(String name)
	{
		String imageName = "icon/mark/"+name;

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