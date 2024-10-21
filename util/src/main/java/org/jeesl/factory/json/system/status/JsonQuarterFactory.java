package org.jeesl.factory.json.system.status;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonQuarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonQuarterFactory<L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonQuarterFactory.class);
	
	private final String localeCode;
	private final JsonQuarter q;
	
	public static <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>> JsonQuarterFactory<L,D,S>
		instance(String localeCode, JsonQuarter q) {return new JsonQuarterFactory<>(localeCode,q);}
	public JsonQuarterFactory(String localeCode, JsonQuarter q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public static JsonQuarter build(String code, String label)
	{
		JsonQuarter json = new JsonQuarter();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
	
	public JsonQuarter build(S ejb)
	{
		JsonQuarter json = new JsonQuarter();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(q.isSetCode()){json.setCode(ejb.getCode());}
		if(q.isSetLabel() && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(q.isSetDescription() && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}