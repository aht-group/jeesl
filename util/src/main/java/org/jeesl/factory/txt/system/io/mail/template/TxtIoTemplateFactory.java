package org.jeesl.factory.txt.system.io.mail.template;

import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateEnvelope;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtIoTemplateFactory<L extends JeeslLang,D extends JeeslDescription,
									CATEGORY extends JeeslStatus<L,D,CATEGORY>,
									CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
									TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
									SCOPE extends JeeslStatus<L,D,SCOPE>,
									DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
									TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(TxtIoTemplateFactory.class);
		
	public String buildCode(TEMPLATE template, DEFINITION definition, String localeCode)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(template.getCode());
		sb.append("-").append(definition.getType().getCode());
		sb.append("-").append(localeCode);
		return sb.toString();
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					CATEGORY extends JeeslStatus<L,D,CATEGORY>,
					CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
					TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
					SCOPE extends JeeslStatus<L,D,SCOPE>,
					DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
					TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,?>>
		String buildCode(DEFINITION definition, String localeCode)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(definition.getTemplate().getCode());
		sb.append("-").append(definition.getType().getCode());
		sb.append("-").append(localeCode);
		return sb.toString();
	}
	
	public String buildCode(TEMPLATE template, CHANNEL type, String localeCode)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(template.getCode());
		sb.append("-").append(type.getCode());
		sb.append("-").append(localeCode);
		return sb.toString();
	}
	
	public <TC extends Enum<TC>> String buildCode(TEMPLATE template, TC typeCode, String localeCode)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(template.getCode());
		sb.append("-").append(typeCode.toString());
		sb.append("-").append(localeCode);
		return sb.toString();
	}
	
	public static  <TC extends Enum<TC>> String buildCode(String template, TC typeCode, String localeCode)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(template);
		sb.append("-").append(typeCode.toString());
		sb.append("-").append(localeCode);
		return sb.toString();
	}
	public static <TE extends Enum<TE>, TC extends Enum<TC>> String buildCode(TE templateCode, TC typeCode, String localeCode)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(templateCode.toString());
		sb.append("-").append(typeCode.toString());
		sb.append("-").append(localeCode);
		return sb.toString();
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					CATEGORY extends JeeslStatus<L,D,CATEGORY>,
					CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
					TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
					SCOPE extends JeeslStatus<L,D,SCOPE>,
					DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
					TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,?>>
	String buildCode(JeeslIoTemplateEnvelope<L,D,CATEGORY,CHANNEL,TEMPLATE,SCOPE,DEFINITION,TOKEN> envelope)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(envelope.getTemplate().getCode());
		sb.append("-").append(envelope.getType().getCode());
		sb.append("-").append(envelope.getLocaleCode());
		return sb.toString();
	}
}