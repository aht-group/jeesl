package org.jeesl.controller.io.mail.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JDomUtil;

import org.exlp.util.jx.JaxbUtil;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jeesl.exception.processing.JeeslDeveloperException;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.jeesl.util.query.xpath.MailXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerEngine
{
	final static Logger logger = LoggerFactory.getLogger(FreemarkerEngine.class);

	@SuppressWarnings("unused")
	private String encoding;
	private Mails mails;
	private Template ftl;
	private Configuration freemarkerConfiguration;
	private FreemarkerConfigBuilder fcb;
	
	public FreemarkerEngine(Mails mails)
	{
		this("UTF-8",mails);
	}
	
	public FreemarkerEngine(String encoding, Mails mails)
	{
		this.encoding=encoding;
		this.mails=mails;
		freemarkerConfiguration = new Configuration(Configuration.getVersion());
		freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/");
		fcb = new FreemarkerConfigBuilder(mails);
	}
	
	public void createTemplate(String id, String lang, String type)
	{
		try
		{
			initTemplate(fcb.build(id, lang, type));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void initTemplate(Mail cfgMail) throws IOException
	{
		JaxbUtil.trace(cfgMail);
		try
		{
			Mail mail = MailXpath.getMail(mails, cfgMail.getCode());

			org.jeesl.model.xml.io.mail.Template requestedTemplate =  cfgMail.getTemplate().get(0);
			org.jeesl.model.xml.io.mail.Template template = MailXpath.getTemplate(mail, requestedTemplate.getLang(), requestedTemplate.getType());
			
			StringBuffer sb = new StringBuffer();
				sb.append(mails.getDir());
				sb.append("/").append(mail.getDir()).append("/");
				sb.append(template.getLang()).append("-");
				sb.append(template.getType()).append("-");
				sb.append(template.getFile());
			
			ftl = freemarkerConfiguration.getTemplate(sb.toString(),"UTF-8");
//			ftl.setEncoding("UTF-8");
		}
		catch (ExlpXpathNotFoundException e) {logger.error("Mail.code="+cfgMail.getCode()+" "+e.getMessage());}
		catch (ExlpXpathNotUniqueException e) {logger.error("Mail.code="+cfgMail.getCode()+" "+e.getMessage());}
	}
 
	public String processXml(Object xml) throws SAXException, IOException, ParserConfigurationException, TemplateException
	{
		Document jdom = JaxbUtil.toDocument(xml);
		return process(jdom);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String process(Document jdom) throws SAXException, IOException, ParserConfigurationException, TemplateException
	{
		if(ftl==null){throw new JeeslDeveloperException("You forgot to init the template");}
		jdom=JDomUtil.unsetNameSpace(jdom);
		 
//		JDomUtil.debug(jdom);
		Map root = new HashMap();
		root.put("doc", freemarker.ext.dom.NodeModel.parse(new InputSource(JDomUtil.toInputStream(jdom, Format.getPrettyFormat()))));
	     
		StringWriter sw = new StringWriter();
		ftl.process(root, sw);
		sw.flush();
	     
		return sw.toString();
	}
	
	public boolean isAvailable(String id, String lang, String type)
	{
		try
		{
			Mail mail = MailXpath.getMail(mails, id);
			MailXpath.getTemplate(mail, lang, type);
			return true;
		}
		catch (ExlpXpathNotFoundException e) {return false;}
		catch (ExlpXpathNotUniqueException e) {return false;}
	}
}