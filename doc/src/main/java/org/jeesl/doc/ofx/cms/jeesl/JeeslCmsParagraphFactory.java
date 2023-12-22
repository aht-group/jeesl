package org.jeesl.doc.ofx.cms.jeesl;

import org.jeesl.doc.ofx.cms.generic.JeeslMarkupFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.openfuxml.factory.xml.ofx.content.structure.XmlSectionFactory;
import org.openfuxml.model.xml.core.ofx.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCmsParagraphFactory<E extends JeeslIoCmsElement<?,?,?,?,C,?>,
										C extends JeeslIoCmsContent<?,E,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCmsParagraphFactory.class);
	
	private final JeeslMarkupFactory ofxMarkup;
	
	public JeeslCmsParagraphFactory()
	{
		ofxMarkup = new JeeslMarkupFactory();
	}
	
	public Section build(String localeCode, E element)
	{
		logger.info("Building Paragraph "+element.toString());
		Section section = XmlSectionFactory.build(localeCode);
		
		if(element.getContent().containsKey(localeCode))
		{
			C c = element.getContent().get(localeCode);
			section.getContent().addAll(ofxMarkup.build(c.getMarkup().getCode(), element.getContent().get(localeCode).getLang()).getContent());	
		}
		return section;
	}
}