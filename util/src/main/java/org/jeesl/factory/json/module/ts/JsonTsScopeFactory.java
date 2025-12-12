package org.jeesl.factory.json.module.ts;

import org.jeesl.factory.json.system.status.JsonTypeFactory;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsScope;
import org.jeesl.interfaces.model.module.ts.core.JeeslTsType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.json.module.ts.JsonTsScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonTsScopeFactory<L extends JeeslLang, D extends JeeslDescription,
								SCOPE extends JeeslTsScope<L,D,?,ST,?,?,?>,
								ST extends JeeslTsType<L,D,ST,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonTsScopeFactory.class);
	
	private final JsonTsScope q;
	
	private JsonTypeFactory<L,D,ST> jfType;
	
	public JsonTsScopeFactory(JsonTsScope q)
	{
		this.q=q;
		if(q.getType()!=null) {jfType = new JsonTypeFactory<>(q.getType());}
	}
	
	public static JsonTsScope build(){return new JsonTsScope();}
	public static <E extends Enum<E>> JsonTsScope build(E code){JsonTsScope json = build(); json.setCode(code.toString()); return json;}
	
	public JsonTsScope build(SCOPE ejb)
	{
		JsonTsScope json = build();
		if(q.getCode()!=null) {json.setCode(ejb.getCode());}
		if(q.getType()!=null) {json.setType(jfType.build(ejb.getType()));}
		return json;
	}
}