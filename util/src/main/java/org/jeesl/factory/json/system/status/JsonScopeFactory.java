package org.jeesl.factory.json.system.status;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonScopeFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonScopeFactory.class);
	
	private final String localeCode;
	private final JsonScope q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonScopeFactory<L,D,S> instance(String localeCode, JsonScope q)
	{
		return new JsonScopeFactory<>(localeCode,q);
	}
	public JsonScopeFactory(String localeCode, JsonScope q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	
	public static JsonScope build() {return new JsonScope();}
	public static JsonScope build(String code) {JsonScope json = JsonScopeFactory.build(); json.setCode(code); return json;}
	public static JsonScope build(String code, String label)
	{
		JsonScope json = JsonScopeFactory.build();
		json.setCode(code);
		json.setLabel(label);
		return json;
	}
	
	public static List<String> toCodes(List<JsonScope> list)
	{
		 return list.stream().map(JsonScope::getCode).filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
	}
	
	public JsonScope build(S ejb)
	{
		JsonScope json = JsonScopeFactory.build();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())){json.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getLabel()) && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(Objects.nonNull(q.getDescription()) && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}