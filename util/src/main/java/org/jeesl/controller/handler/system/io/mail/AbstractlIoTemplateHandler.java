package org.jeesl.controller.handler.system.io.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.io.JeeslIoTemplateFacade;
import org.jeesl.controller.io.mail.AbstractJeeslMail;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.builder.io.IoTemplateFactoryBuilder;
import org.jeesl.factory.txt.system.security.TxtUserFactory;
import org.jeesl.interfaces.bean.system.IoTemplateBean;
import org.jeesl.interfaces.controller.JeeslMail;
import org.jeesl.interfaces.controller.handler.system.io.JeeslTemplateHandler;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateTokenType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AbstractlIoTemplateHandler<L extends JeeslLang,D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
									TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
									SCOPE extends JeeslStatus<L,D,SCOPE>,
									DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
									TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,TOKENTYPE>,
									TOKENTYPE extends JeeslTemplateTokenType<L,D,TOKENTYPE,?>>
								implements JeeslTemplateHandler<LOC,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractlIoTemplateHandler.class);

	private final IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate;
	private final IoTemplateBean bean;
	
	private final JeeslMail<TEMPLATE> mail; public JeeslMail<TEMPLATE> getMail() {return mail;}
	private final List<LOC> locales; @Override public List<LOC> getLocales() {return locales;}
	private final List<CHANNEL> channels;
	private final List<DEFINITION> definitons; @Override public List<DEFINITION> getDefinitons() {return definitons;}
	private List<TOKEN> tokens; @Override public List<TOKEN> getTokens() {return tokens;}
	private final Map<CHANNEL,Map<String,String>> header; public Map<CHANNEL,Map<String, String>> getHeader() {return header;}
	private final Map<CHANNEL,Map<String,String>> body; public Map<CHANNEL,Map<String, String>> getBody() {return body;}
	
	private LOC locale; public LOC getLocale() {return locale;} public void setLocale(LOC locale) {this.locale = locale;}
	private CHANNEL channel; public CHANNEL getChannel() {return channel;} public void setChannel(CHANNEL channel) {this.channel = channel;}
	private String recipients; @Override public String getRecipients() {return recipients;} @Override public void setRecipients(String recipients) {this.recipients = recipients;} 
	private int numberRecipients; public int getNumberRecipients() {return numberRecipients;} public void setNumberRecipients(int numberRecipients) {this.numberRecipients = numberRecipients;}
	
	private String previewHeader; @Override public String getPreviewHeader() {return previewHeader;}
	private String previewBody; @Override public String getPreviewBody() {return previewBody;}
	
	
	public AbstractlIoTemplateHandler(IoTemplateFactoryBuilder<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fbTemplate,
										JeeslIoTemplateFacade<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN,TOKENTYPE> fTemplate,
										IoTemplateBean bean,
										JeeslMail<TEMPLATE> mail)
	{
		this.fbTemplate=fbTemplate;
		this.bean=bean;
		this.mail=mail;
		tokens = fTemplate.allForParent(fbTemplate.getClassToken(),mail.getTemplate());
		locales = new ArrayList<>();
		channels = new ArrayList<>();
		definitons = new ArrayList<>();
		header = new HashMap<>();
		body = new HashMap<>();
	}
	
	public void addLocale(LOC locale){this.locales.add(locale);if(this.locale==null) {this.locale=locale;}}
	public void addLocales(List<LOC> locales){this.locales.addAll(locales);}
	
	public void initDefinitions(List<DEFINITION> definitions)
	{
		this.definitons.clear();
		this.definitons.addAll(definitions);
		logger.info("adding "+definitions.size());
		for(DEFINITION d : definitions)
		{
			CHANNEL c = d.getType();
			channels.add(c);
			header.put(c,new HashMap<>());
			body.put(c,new HashMap<>());
			for(LOC loc : locales)
			{
				header.get(c).put(loc.getCode(),d.getHeader().get(loc.getCode()).getLang());
				body.get(c).put(loc.getCode(),d.getDescription().get(loc.getCode()).getLang());
			}
		}
	}
	
	public void setDefault()
	{
		locale = locales.get(0);
		channel = channels.get(0);
		try
		{
			preview();
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
	private void preview() throws IOException
	{
		Template ptHeader = AbstractJeeslMail.compile(header.get(channel).get(locale.getCode()));
		Template ptBody = AbstractJeeslMail.compile(body.get(channel).get(locale.getCode()));
		
		Map<String,Object> model = fbTemplate.txtToken().buildModel(tokens);
		previewHeader = AbstractJeeslMail.preview(model, ptHeader);
		previewBody = AbstractJeeslMail.preview(model, ptBody);
	}
	
	@Override public String toHeader(DEFINITION definition, LOC locale)
	{
		if(header.containsKey(definition.getType()) && header.get(definition.getType()).containsKey(locale.getCode())) {return header.get(definition.getType()).get(locale.getCode());}
		else {return "";}
	}
	@Override public String toBody(DEFINITION definition, LOC locale)
	{
		if(body.containsKey(definition.getType()) && body.get(definition.getType()).containsKey(locale.getCode())) {return body.get(definition.getType()).get(locale.getCode());}
		else {return "";}
	}
	
	public boolean hasOneLocale() {return locales.size()==1;}
	
	public <USER extends JeeslUser<?>> void updateRecipients(Collection<USER> users)
	{
		TxtUserFactory<USER> tfUser = new TxtUserFactory<USER>();
		recipients = tfUser.names(users);
		numberRecipients = users.size();
	}
	
	public void saveTemplate()
	{
		logger.info("saveTemplate");
		try {preview();}
		catch (IOException e) {logger.error(e.getMessage());}
	}
	
	public void sendMails()
	{
		if(bean!=null)
		{
			try {bean.sendIoMailsFromTemplateHandler();}
			catch (UtilsProcessingException | IOException | TemplateException e) {e.printStackTrace();}
		}
	}
}