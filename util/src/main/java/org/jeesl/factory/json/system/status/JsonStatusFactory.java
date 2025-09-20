package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonStatusFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonStatusFactory.class);
	
	private final String localeCode;
	private final JsonStatus q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>, E extends Enum<E>> JsonStatusFactory<L,D,S>
		instance(E localeCode, JsonStatus q) {return new JsonStatusFactory<>(localeCode.toString(),q);}
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonStatusFactory<L,D,S>
		instance(String localeCode, JsonStatus q) {return new JsonStatusFactory<>(localeCode,q);}
	
	public JsonStatusFactory(String localeCode, JsonStatus q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static <E extends Enum<E>> JsonStatus build(E code) {return JsonStatusFactory.build(code.toString(),null);}
	public static JsonStatus build(String code, String label) {return build(null,code,label);}
	public static JsonStatus build(Long id, String code, String label)
	{
		JsonStatus json = new JsonStatus();
		json.setId(id);
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
	
	public JsonStatus build(S ejb)
	{
		JsonStatus json = new JsonStatus();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}