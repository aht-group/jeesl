package org.jeesl.interfaces.model.io.mail.template;

import java.io.Serializable;
import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public class JeeslIoTemplateEnvelope<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
								TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
								SCOPE extends JeeslStatus<L,D,SCOPE>,
								DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
								TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,?>
								>
			implements Serializable
{	
	private static final long serialVersionUID = 1;
	
	private String localeCode;
	public String getLocaleCode() {return localeCode;}
	public void setLocaleCode(String localeCode) {this.localeCode = localeCode;}

	private TEMPLATE template;
	public TEMPLATE getTemplate() {return template;}
	public void setTemplate(TEMPLATE template) {this.template = template;}
	
	private CHANNEL type;
	public CHANNEL getType() {return type;}
	public void setType(CHANNEL type) {this.type = type;}
	
	private Map<String,Object> model;
	public Map<String,Object> getModel() {return model;}
	public void setModel(Map<String,Object> model) {this.model = model;}
}