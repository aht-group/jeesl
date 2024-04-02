package org.jeesl.controller.io.mail;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoMailFacade;
import org.jeesl.api.facade.io.JeeslIoTemplateFacade;
import org.jeesl.controller.io.mail.freemarker.FreemarkerIoTemplateEngine;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoMailFactoryBuilder;
import org.jeesl.factory.builder.io.IoTemplateFactoryBuilder;
import org.jeesl.factory.xml.io.mail.XmlMailFactory;
import org.jeesl.factory.xml.io.mail.XmlMailsFactory;
import org.jeesl.interfaces.controller.JeeslMail;
import org.jeesl.interfaces.controller.handler.system.io.JeeslTemplateHandler;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailRetention;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateTokenType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.mail.EmailAddress;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AbstractJeeslMail<L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
								TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
								SCOPE extends JeeslStatus<L,D,SCOPE>,
								DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
								TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
								TOKENTYPE extends JeeslTemplateTokenType<L,D,TOKENTYPE,?>,
								MAILCAT extends JeeslStatus<L,D,MAILCAT>,
								MAIL extends JeeslIoMail<L,D,MAILCAT,STATUS,RETENTION,FRC>,
								STATUS extends JeeslIoMailStatus<L,D,STATUS,?>,
								RETENTION extends JeeslIoMailRetention<L,D,RETENTION,?>,
								FRC extends JeeslFileContainer<?,?>>
							implements JeeslMail<TEMPLATE>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslMail.class);
	
	private final IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate;
	private final IoMailFactoryBuilder<L,D,MAILCAT,MAIL,STATUS,RETENTION> fbMail;
	
	protected final JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate;
	protected final JeeslIoMailFacade<MAILCAT,MAIL,STATUS,RETENTION,FRC> fMail;
	
	protected JeeslTemplateHandler<LOC,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> mth;
	protected final FreemarkerIoTemplateEngine<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fmEngine;

	protected final Map<String,Template> mapTemplateHeader;
	protected final Map<String,Template> mapTemplateBody;

	protected TEMPLATE template; @Override public TEMPLATE getTemplate() {return template;}

	protected MAILCAT categoryMail;
	protected RETENTION retention;
	protected EmailAddress mailFrom;
	
	protected final Mails mails;
	
	protected String subjectPreifx;
	protected boolean developmentMode; public void activateDevelopmentMode() {developmentMode=true;}
	
	public AbstractJeeslMail(IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate,
							IoMailFactoryBuilder<L,D,MAILCAT,MAIL,STATUS,RETENTION> fbMail,
							JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate,
							JeeslIoMailFacade<MAILCAT,MAIL,STATUS,RETENTION,FRC> fMail)
	{
		this.fbTemplate=fbTemplate;
		this.fbMail=fbMail;
		this.fTemplate=fTemplate;
		this.fMail=fMail;
		
		fmEngine = new FreemarkerIoTemplateEngine<>(fbTemplate);
		
		mapTemplateHeader = new HashMap<String,Template>();
		mapTemplateBody = new HashMap<String,Template>();
		
		subjectPreifx = "";
		developmentMode = false;
		mails = XmlMailsFactory.build();
		
	}
	
	protected <E extends Enum<E>> void initIo(Class<?> c, E code)
	{
		try
		{
			categoryMail = fMail.fByCode(fbMail.getClassCategory(), code);
			
			template = fTemplate.fByCode(fbTemplate.getClassTemplate(), c.getSimpleName());
			template = fTemplate.load(template);
			fmEngine.addTemplate(template);
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
		if(mth!=null)
		{

			mth.initDefinitions(this.toDefinitions(fMail.fByEnum(fbTemplate.getClassType(), JeeslTemplateChannel.Code.email)));
		}
	}
	
	public List<DEFINITION> toDefinitions(CHANNEL channel)
	{
		List<DEFINITION> result = new ArrayList<>();
		for(DEFINITION d : template.getDefinitions())
		{
			if(d.getType().equals(channel)) {result.add(d);}
		}
		return result;
	}
	
	protected void compile() throws IOException
	{
		this.compile(mth);
	}
	protected void compile(JeeslTemplateHandler<LOC,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> handler) throws IOException
	{
		for(DEFINITION def : handler.getDefinitons())
		{
			for(LOC loc : handler.getLocales())
			{
				compile(loc.getCode(),handler.toHeader(def,loc),handler.toBody(def,loc));
			}
		}
	}
	private void compile(String localeCode, String header, String body) throws IOException
	{
		mapTemplateHeader.put(localeCode, AbstractJeeslMail.compile(header));
		mapTemplateBody.put(localeCode, AbstractJeeslMail.compile(body));
	}
	
	public static Template compile(String text) throws IOException
	{
		return new Template("name", new StringReader(text),new Configuration());
	}
	
	protected String processHeader(String localeCode, Map<String,Object> model) throws TemplateException, IOException
	{
		StringWriter swHeader = new StringWriter();
		if(mapTemplateHeader.containsKey(localeCode)) {mapTemplateHeader.get(localeCode).process(model,swHeader);}
		else {swHeader.write("No Header Template for "+localeCode);}
		swHeader.flush();
		return swHeader.toString();
	}
	
	protected String processBody(String localeCode, Map<String,Object> model) throws TemplateException, IOException
	{
		StringWriter swBody = new StringWriter();
		if(mapTemplateBody.containsKey(localeCode)) {mapTemplateBody.get(localeCode).process(model,swBody);}
		else {swBody.write("No Header Template for "+localeCode);}
		swBody.flush();
		return swBody.toString();
	}
	
	public static String preview(Map<String,Object> model, Template template)
	{
		String result;
		
		try
		{
			StringWriter swHeader = new StringWriter();
			template.process(model,swHeader);
			swHeader.flush();
			result = swHeader.toString();
		}
    	catch (IOException e) {result="Error: "+e.getMessage();}
    	catch (TemplateException e) {result="Error: "+e.getMessage();}
		
		return result;
	}
	
	public void spool(Mails mails) throws JeeslConstraintViolationException, JeeslNotFoundException
	{
		for(Mail mail : mails.getMail()) {spool(mail);}
	}
	
	public void spool(Mail mail) throws JeeslConstraintViolationException, JeeslNotFoundException
	{
		fMail.queueMail(categoryMail,null,mail);
		logger.info("Spooled");
	}
	
	@Override public void overrideRecipients(EmailAddress email)
	{
		for(Mail mail : mails.getMail())
		{
			XmlMailFactory.overwriteRecipients(mail,email);
		}
	}
	
	@Override public void spool()
	{
		for(Mail mail : mails.getMail())
		{
			try {fMail.queueMail(categoryMail,retention,mail);}
			catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		}
	}
}