package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonDomain;
import org.jeesl.model.json.system.status.JsonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonDomainFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonDomainFactory.class);
	
	private final String localeCode;
	private final JsonDomain q;
	
	public JsonDomainFactory(JsonDomain q) {this(null,q);}
	public JsonDomainFactory(String localeCode, JsonDomain q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static JsonDomain build() {return new JsonDomain();}
	public static <E extends Enum<E>> JsonDomain JsonDomain(E code) {return build(code.toString());}
	public static JsonDomain build(String code) {JsonDomain json=build(); json.setCode(code); return json;}
	public static JsonDomain build(String code, String label) {JsonDomain json=build(code); json.setLabel(label); return json;}
		
	public JsonType build(S ejb)
	{
		JsonType json = new JsonType();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.getCode()!=null){json.setCode(ejb.getCode());}
		if(q.getLabel()!=null && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.getDescription()!=null && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}