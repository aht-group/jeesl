package org.jeesl.factory.txt.io.dms;

import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtDmsDocumentFactory <L extends JeeslLang,
									DOC extends JeeslIoDmsDocument<L,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtDmsDocumentFactory.class);
	
	private final String localeCode;
	
	
	public static <L extends JeeslLang, DOC extends JeeslIoDmsDocument<L,?,?,?>, E extends Enum<E>> TxtDmsDocumentFactory<L,DOC> instance(E localeCode) {return new TxtDmsDocumentFactory<>(localeCode.toString());}
	private TxtDmsDocumentFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String debug(DOC doc)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(doc.getId()).append("]");
		sb.append(" ").append(doc.getName().get(localeCode).getLang());
		return sb.toString();
	}
}