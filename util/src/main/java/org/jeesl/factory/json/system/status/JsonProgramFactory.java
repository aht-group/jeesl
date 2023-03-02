package org.jeesl.factory.json.system.status;

import java.util.Objects;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.json.system.status.JsonProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonProgramFactory<L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonProgramFactory.class);
	
	private final String localeCode;
	private final JsonProgram q;
	
	public static <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>> JsonProgramFactory<L,D,S> instance(String localeCode, JsonProgram q)
	{
		return new JsonProgramFactory<>(localeCode,q);
	}
	public JsonProgramFactory(String localeCode, JsonProgram q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
		
	public static JsonProgram build() {return new JsonProgram();}
	public static <E extends Enum<E>> JsonProgram build(E code) {return build(code.toString());}
	public static JsonProgram build(String code) {JsonProgram json = build(); json.setCode(code); return json;}
	public static JsonProgram build(String code, String label) {JsonProgram json = build(code); json.setLabel(label); return json;}
	
	public JsonProgram build(S ejb)
	{
		JsonProgram json = new JsonProgram();
	
		if(q.getId()!=null){json.setId(ejb.getId());}
		if(Objects.nonNull(q.getCode())){json.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getLabel()) && ejb.getName().containsKey(localeCode)){json.setLabel(ejb.getName().get(localeCode).getLang());}
		if(Objects.nonNull(q.getDescription()) && ejb.getDescription().containsKey(localeCode)){json.setDescription(ejb.getDescription().get(localeCode).getLang());}
	
		return json;
	}
}