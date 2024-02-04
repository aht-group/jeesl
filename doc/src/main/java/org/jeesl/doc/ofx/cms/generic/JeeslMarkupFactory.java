package org.jeesl.doc.ofx.cms.generic;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jsoup.Jsoup;
import org.openfuxml.factory.xml.ofx.content.structure.XmlParagraphFactory;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.model.xml.core.ofx.Paragraph;
import org.openfuxml.model.xml.core.ofx.Section;
import org.openfuxml.transform.XhtmlTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMarkupFactory <M extends JeeslIoMarkup<T>,
								T extends JeeslIoMarkupType<?,?,T,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMarkupFactory.class);
	
	private boolean debug = false;
	
	private final XhtmlTransformer xhtmlTransformer;

	public JeeslMarkupFactory()
	{
		xhtmlTransformer = new XhtmlTransformer();
	}
	
	public Section build(M markup)
	{
		return build(markup.getType().getCode(),markup.getContent());
	}
	
	public Section build(String type, String content)
	{
		if(debug)
		{
			logger.info("Building Markup");
			logger.info(content);
		}
		
		Section section = XmlSectionFactory.build();

		if(type.equals(JeeslIoMarkupType.Code.text.toString()))
		{
			Paragraph p = XmlParagraphFactory.build();
			content = Jsoup.parse(content).text();
			p.getContent().add(content);
			section.getContent().add(p);
		}
		else if(type.equals(JeeslIoMarkupType.Code.xhtml.toString()))
		{
			Section xml = xhtmlTransformer.process(content);
			if(debug)
			{
				JaxbUtil.info(xml);
			}
			section.getContent().addAll(xml.getContent());
		}
		else {logger.warn("Unhandled markup Type: "+type);}
		return section;
	}
}