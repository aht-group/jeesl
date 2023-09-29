package org.jeesl.interfaces.controller.handler.system.io;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslTemplateHandler <LOC extends JeeslLocale<?,?,LOC,?>,
										CATEGORY extends JeeslStatus<?,?,CATEGORY>,
										CHANNEL extends JeeslTemplateChannel<?,?,CHANNEL,?>,
										TEMPLATE extends JeeslIoTemplate<?,?,CATEGORY,SCOPE,DEFINITION,TOKEN>,
										SCOPE extends JeeslStatus<?,?,SCOPE>,
										DEFINITION extends JeeslIoTemplateDefinition<?,CHANNEL,TEMPLATE>,
										TOKEN extends JeeslIoTemplateToken<?,?,TEMPLATE,TOKENTYPE>,
										TOKENTYPE extends JeeslStatus<?,?,TOKENTYPE>> 
							extends Serializable
{
	
//	JeeslMail<TEMPLATE> getMail();
	
	String getRecipients();
	void setRecipients(String recipients);
	
	void addLocale(LOC locale);
	List<LOC> getLocales();
	
	List<DEFINITION> getDefinitons();
	void initDefinitions(List<DEFINITION> definitions);
	
	List<TOKEN> getTokens();
	
	String toHeader(DEFINITION definition, LOC locale);
	String toBody(DEFINITION definition, LOC locale);
	
	String getPreviewHeader();
	String getPreviewBody();
}