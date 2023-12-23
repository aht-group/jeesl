package org.jeesl.util.query.xpath;

import java.util.List;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

import org.apache.commons.jxpath.JXPathContext;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.jeesl.model.xml.io.mail.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailXpath
{
	final static Logger logger = LoggerFactory.getLogger(MailXpath.class);

	public static synchronized Mail getMail(Mails mails, String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(mails);
		
		@SuppressWarnings("unchecked")
		List<Mail> listResult = (List<Mail>)context.selectNodes("mail[@code='"+code+"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Mail.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Mail.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}
	
	public static synchronized Template getTemplate(Mail mail, String lang, String type) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(mail);
		
		@SuppressWarnings("unchecked")
		List<Template> listResult = (List<Template>)context.selectNodes("template[@lang='"+lang+"' and @type='"+type+"']");
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Template.class.getSimpleName()+" for lang="+lang+" and type="+type);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Template.class.getSimpleName()+" for lang="+lang+" and type="+type);}
		return listResult.get(0);
	}
}