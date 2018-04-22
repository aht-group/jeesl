package org.jeesl.interfaces.model.system.io.mail.template;

import java.io.Serializable;
import java.util.Map;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public class JeeslIoTemplateEnvelope<L extends UtilsLang,D extends UtilsDescription,
								CATEGORY extends UtilsStatus<CATEGORY,L,D>,
								TYPE extends UtilsStatus<TYPE,L,D>,
								TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
								SCOPE extends UtilsStatus<SCOPE,L,D>,
								DEFINITION extends JeeslIoTemplateDefinition<D,TYPE,TEMPLATE>,
								TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE>
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
	
	private TYPE type;
	public TYPE getType() {return type;}
	public void setType(TYPE type) {this.type = type;}
	
	private Map<String,String> model;
	public Map<String, String> getModel() {return model;}
	public void setModel(Map<String, String> model) {this.model = model;}
}